package de.pbz.unitbot.commands;

import de.pbz.unitbot.Command;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class MCCommand implements Command {
    private static final Logger LOG = LoggerFactory.getLogger(MCCommand.class);

    private static final String MC_ADDRESS = "`bte.mcs.lol`";

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        LOG.info("Handling !mc message.");
        return Mono.just(event)
                .map(MessageCreateEvent::getMessage)
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(MC_ADDRESS))
                .then();
    }
}
