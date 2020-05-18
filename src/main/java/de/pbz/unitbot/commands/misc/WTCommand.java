package de.pbz.unitbot.commands.misc;

import de.pbz.unitbot.commands.Command;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class WTCommand implements Command {
    private static final Logger LOG = LoggerFactory.getLogger(WTCommand.class);

    private static final String WT_ADDRESS = "`https://www.watch2gether.com/rooms/hwe8j3o8mg52tsdjll`";

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        LOG.info("Handling !wt message.");
        return Mono.just(event)
                .map(MessageCreateEvent::getMessage)
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(WT_ADDRESS))
                .then();
    }
}
