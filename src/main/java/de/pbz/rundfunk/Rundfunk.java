package de.pbz.rundfunk;

import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer;
import de.pbz.rundfunk.audio.MusicManager;
import de.pbz.rundfunk.commands.Command;
import de.pbz.rundfunk.commands.audio.*;
import de.pbz.rundfunk.commands.misc.CatCommand;
import de.pbz.rundfunk.commands.misc.HelpCommand;
import de.pbz.rundfunk.commands.misc.RPSCommand;
import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

public class Rundfunk {
    private static final Logger LOG = LoggerFactory.getLogger(Rundfunk.class);

    private static final Map<String, Command> commands = generateDefaults();

    public static void main(String[] args) {
        if (args.length != 1) {
            LOG.error("Wrong parameters");
            return;
        }
        LOG.info("Starting Rundfunk Bot...");

        final DiscordClient client = DiscordClientBuilder.create(args[0]).build();

        client.withGateway(gateway -> {
            final Publisher<?> bot = gateway.on(MessageCreateEvent.class)
                    .flatMap(event -> Mono.justOrEmpty(event.getMessage().getContent())
                            .map(content -> content.split(" "))
                            .filter(contents -> contents.length > 0)
                            .flatMap(content -> Flux.fromIterable(commands.entrySet())
                                    .filter(entry -> content[0].equals('!' + entry.getKey()))
                                    .flatMap(entry -> entry.getValue().execute(event))
                                    .next()));

            final Publisher<?> onConnect = gateway.on(ReadyEvent.class)
                    .doOnNext(readyEvent -> LOG.info("Logged in as " + readyEvent.getSelf().getUsername()));

            final Publisher<?> onDisconnect = gateway.onDisconnect()
                    .doOnTerminate(() -> LOG.info("Disconnected!"));

            return Mono.when(bot, onConnect, onDisconnect);
        }).block();
    }

    private static HashMap<String, Command> generateDefaults() {
        var commands = new HashMap<String, Command>();

        commands.put("help", new HelpCommand());
        commands.put("cat", new CatCommand());
        commands.put("rps", new RPSCommand());

        var playerManager = new DefaultAudioPlayerManager();
        playerManager
                .getConfiguration()
                .setFrameBufferFactory(NonAllocatingAudioFrameBuffer::new);
        AudioSourceManagers.registerRemoteSources(playerManager);
        var musicManager = new MusicManager(playerManager);

        commands.put("join", new JoinCommand(musicManager));
        commands.put("play", new PlayCommand(playerManager, musicManager));
        commands.put("clear", new ClearCommand(musicManager));
        commands.put("playlist", new PlaylistCommand(playerManager, musicManager));
        commands.put("q", new QueueCommand(playerManager, musicManager));
        commands.put("queue", new QueueCommand(playerManager, musicManager));
        commands.put("track", new TrackCommand(musicManager));
        commands.put("skip", new SkipCommand(musicManager));
        commands.put("df", new DfCommand(playerManager, musicManager));
        commands.put("wimbledon", new WimbledonCommand(playerManager, musicManager));
        commands.put("geld", new GeldCommand(playerManager, musicManager));

        return commands;
    }
}
