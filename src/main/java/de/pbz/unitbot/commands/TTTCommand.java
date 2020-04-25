package de.pbz.unitbot.commands;

import de.pbz.unitbot.Command;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class TTTCommand implements Command {
    private static final Logger LOG = LoggerFactory.getLogger(TTTCommand.class);

    private static final String TTT_COMMAND = "!ttt";
    private static final String TTT_ADDRESS = "`connect h2879589.stratoserver.net:27015; password <enter_password>`";

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        LOG.info("Handling !ttt message.");
        return Flux.just(event)
                .map(MessageCreateEvent::getMessage)
                .filter(m -> m.getContent().orElse("").equalsIgnoreCase(TTT_COMMAND))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(TTT_ADDRESS))
                .then();
    }
}
