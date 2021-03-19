package de.pbz.rundfunk.commands.audio;

import de.pbz.rundfunk.audio.MusicManager;
import de.pbz.rundfunk.commands.Command;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class TrackCommand implements Command {
    private static final Logger LOG = LoggerFactory.getLogger(TrackCommand.class);

    private final MusicManager musicManager;

    public TrackCommand(MusicManager musicManager) {
        this.musicManager = musicManager;
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        LOG.info("Handling !track message.");
        return Mono.just(event)
                .map(MessageCreateEvent::getMessage)
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(getTrackInfo()))
                .then();
    }

    private String getTrackInfo() {
        try {
            var author = musicManager.getPlayer().getPlayingTrack().getInfo().author;
            var title = musicManager.getPlayer().getPlayingTrack().getInfo().title;
            var uri = musicManager.getPlayer().getPlayingTrack().getInfo().uri;
            return "Current track:" + "\n" + title + " by " + author + "\n" + uri + "\n\n"
                    + "Upcoming tracks:" + "\n" + musicManager.getScheduler().getQueueContents();
        } catch (NullPointerException e) {
            return "Track data could not be fetched.";
        }
    }
}
