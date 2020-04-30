package de.pbz.unitbot.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import de.pbz.unitbot.Command;
import de.pbz.unitbot.audio.MusicManager;
import de.pbz.unitbot.audio.SingleTrackHandler;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.Arrays;

public class PlayCommand implements Command {
    private static final Logger LOG = LoggerFactory.getLogger(PlayCommand.class);

    private final AudioPlayerManager playerManager;
    private final MusicManager musicManager;

    public PlayCommand(AudioPlayerManager playerManager, MusicManager musicManager) {
        this.playerManager = playerManager;
        this.musicManager = musicManager;
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        LOG.info("Handling !play message.");
        return Mono.justOrEmpty(event.getMessage().getContent())
                .map(content -> Arrays.asList(content.split(" ")))
                .filter(l -> l.size() >= 2)
                .doOnNext(command -> playerManager.loadItem(command.get(1), new SingleTrackHandler(musicManager)))
                .onErrorStop()
                .then();
    }
}
