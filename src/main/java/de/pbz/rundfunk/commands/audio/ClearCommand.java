package de.pbz.rundfunk.commands.audio;

import de.pbz.rundfunk.audio.MusicManager;
import de.pbz.rundfunk.commands.Command;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class ClearCommand implements Command {
    private static final Logger LOG = LoggerFactory.getLogger(ClearCommand.class);

    private final MusicManager musicManager;

    public ClearCommand(MusicManager musicManager) {
        this.musicManager = musicManager;
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        LOG.info("Handling !clear message.");
        return Mono.justOrEmpty(event.getMessage())
                .flatMap(Message::getChannel)
                .flatMap(channel -> {
                    musicManager.getScheduler().clearQueue();
                    return channel.createMessage("I erased that shiii...");
                })
                .onErrorStop()
                .then();
    }
}
