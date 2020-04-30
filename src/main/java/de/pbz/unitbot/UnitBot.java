package de.pbz.unitbot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer;
import de.pbz.unitbot.audio.GuildMusicManager;
import de.pbz.unitbot.audio.PlaylistHandler;
import de.pbz.unitbot.audio.SingleTrackHandler;
import de.pbz.unitbot.commands.*;
import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class UnitBot {
    private static final Logger LOG = LoggerFactory.getLogger(UnitBot.class);

    private static final Map<String, Command> commands = new HashMap<>();

    public static void main(String[] args) {
        if (args.length != 1) {
            LOG.error("Wrong parameters");
            return;
        }
        LOG.info("Starting Unit Bot...");

        initCommands();

        final DiscordClient client = DiscordClientBuilder.create(args[0]).build();

        client.getEventDispatcher().on(ReadyEvent.class)
                .subscribe(ready -> LOG.info("Logged in as " + ready.getSelf().getUsername()));

        client.getEventDispatcher().on(MessageCreateEvent.class)
                .flatMap(event -> Mono.justOrEmpty(event.getMessage().getContent())
                        .flatMap(content -> Flux.fromIterable(commands.entrySet())
                                .filter(entry -> content.startsWith('!' + entry.getKey()))
                                .flatMap(entry -> entry.getValue().execute(event))
                                .next()))
                .subscribe();

        client.login().block();
    }

    private static void initCommands() {
        commands.put("help", new HelpCommand());
        commands.put("ttt", new TTTCommand());
        commands.put("mc", new MCCommand());
        commands.put("cat", new CatCommand());
        commands.put("ssp", new RPSCommand());

        final AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
        playerManager.getConfiguration().setFrameBufferFactory(NonAllocatingAudioFrameBuffer::new);
        AudioSourceManagers.registerRemoteSources(playerManager);
        GuildMusicManager musicManager = new GuildMusicManager(playerManager);

        commands.put("join", event -> Mono.justOrEmpty(event.getMember())
                .flatMap(Member::getVoiceState)
                .flatMap(VoiceState::getChannel)
                .flatMap(channel -> channel.join(spec -> spec.setProvider(musicManager.getProvider())))
                .then());
        commands.put("play", event -> Mono.justOrEmpty(event.getMessage().getContent())
                .map(content -> Arrays.asList(content.split(" ")))
                .filter(l -> l.size() >= 2)
                .doOnNext(command -> playerManager.loadItem(command.get(1), new SingleTrackHandler(musicManager)))
                .onErrorStop()
                .then());
        commands.put("playlist", event -> Mono.justOrEmpty(event.getMessage().getContent())
                .map(content -> Arrays.asList(content.split(" ")))
                .filter(l -> l.size() >= 2)
                .doOnNext(command -> playerManager.loadItemOrdered(musicManager, command.get(1), new PlaylistHandler(musicManager)))
                .onErrorStop()
                .then());
    }
}
