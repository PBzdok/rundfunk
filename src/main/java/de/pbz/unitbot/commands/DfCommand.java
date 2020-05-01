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

public class DfCommand implements Command {
    private static final Logger LOG = LoggerFactory.getLogger(DfCommand.class);

    private final String ytLink = "https://www.youtube.com/watch?v=_00-nVogGhI";

    private final AudioPlayerManager playerManager;
    private final MusicManager musicManager;

    public DfCommand(AudioPlayerManager playerManager, MusicManager musicManager) {
        this.playerManager = playerManager;
        this.musicManager = musicManager;
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        LOG.info("Handling !df message.");
        return Mono.justOrEmpty(event.getMessage())
                .doOnNext(command -> playerManager.loadItem(ytLink, new SingleTrackHandler(musicManager)))
                .onErrorStop()
                .then();
    }
}
