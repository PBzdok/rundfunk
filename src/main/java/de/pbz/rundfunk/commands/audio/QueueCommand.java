package de.pbz.rundfunk.commands.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import de.pbz.rundfunk.audio.MusicManager;
import de.pbz.rundfunk.audio.PlaylistHandler;
import de.pbz.rundfunk.commands.Command;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.Arrays;

public class QueueCommand implements Command {
    private static final Logger LOG = LoggerFactory.getLogger(QueueCommand.class);

    private final AudioPlayerManager playerManager;
    private final MusicManager musicManager;

    public QueueCommand(AudioPlayerManager playerManager, MusicManager musicManager) {
        this.playerManager = playerManager;
        this.musicManager = musicManager;
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        LOG.info("Handling !queue message.");
        return Mono.justOrEmpty(event.getMessage().getContent())
                .map(content -> Arrays.asList(content.split(" ")))
                .filter(l -> l.size() >= 2)
                .doOnNext(command -> playerManager.loadItem(command.get(1), new PlaylistHandler(musicManager)))
                .onErrorStop()
                .dematerialize();
    }
}
