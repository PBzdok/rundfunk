package de.pbz.unitbot.commands.game;

import de.pbz.unitbot.commands.Command;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class PHCommand implements Command {
    private static final Logger LOG = LoggerFactory.getLogger(PHCommand.class);

    private static final String PH_ADDRESS = "`connect h2879589.stratoserver.net:27115; password <enter_password>`";

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        LOG.info("Handling !ph message.");
        return Mono.just(event)
                .map(MessageCreateEvent::getMessage)
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(PH_ADDRESS))
                .then();
    }
}
