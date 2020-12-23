package de.pbz.unitbot.commands.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import de.pbz.unitbot.audio.MusicManager;
import de.pbz.unitbot.audio.SingleTrackHandler;
import de.pbz.unitbot.commands.Command;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class WimbledonCommand implements Command {
    private static final Logger LOG = LoggerFactory.getLogger(WimbledonCommand.class);

    private final String ytLink = "https://www.youtube.com/watch?v=Y5M1aR5pCgA";

    private final AudioPlayerManager playerManager;
    private final MusicManager musicManager;

    public WimbledonCommand(AudioPlayerManager playerManager, MusicManager musicManager) {
        this.playerManager = playerManager;
        this.musicManager = musicManager;
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        LOG.info("Handling !wimbeldon message.");
        return Mono.justOrEmpty(event.getMessage())
                .doOnNext(command -> playerManager.loadItem(ytLink, new SingleTrackHandler(musicManager)))
                .onErrorStop()
                .then();
    }
}
