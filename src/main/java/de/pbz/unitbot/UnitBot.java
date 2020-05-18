package de.pbz.unitbot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer;
import de.pbz.unitbot.audio.MusicManager;
import de.pbz.unitbot.commands.Command;
import de.pbz.unitbot.commands.HelpCommand;
import de.pbz.unitbot.commands.audio.*;
import de.pbz.unitbot.commands.game.MCCommand;
import de.pbz.unitbot.commands.game.PHCommand;
import de.pbz.unitbot.commands.game.SLCommand;
import de.pbz.unitbot.commands.game.TTTCommand;
import de.pbz.unitbot.commands.misc.CatCommand;
import de.pbz.unitbot.commands.misc.RPSCommand;
import de.pbz.unitbot.commands.misc.WTCommand;
import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
        commands.put("ph", new PHCommand());
        commands.put("sl", new SLCommand());
        commands.put("mc", new MCCommand());
        commands.put("cat", new CatCommand());
        commands.put("rps", new RPSCommand());
        commands.put("wt", new WTCommand());

        final AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
        playerManager.getConfiguration().setFrameBufferFactory(NonAllocatingAudioFrameBuffer::new);
        AudioSourceManagers.registerRemoteSources(playerManager);
        MusicManager musicManager = new MusicManager(playerManager);

        commands.put("join", new JoinCommand(musicManager));
        commands.put("play", new PlayCommand(playerManager, musicManager));
        commands.put("playlist", new PlaylistCommand(playerManager, musicManager));
        commands.put("track", new TrackCommand(musicManager));
        commands.put("skip", new SkipCommand(musicManager));
        commands.put("df", new DfCommand(playerManager, musicManager));
    }
}
