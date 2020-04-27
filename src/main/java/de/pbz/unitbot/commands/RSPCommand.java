package de.pbz.unitbot.commands;

import de.pbz.unitbot.Command;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.reaction.ReactionEmoji;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.concurrent.ThreadLocalRandom;

public class RSPCommand implements Command {
    private static final Logger LOG = LoggerFactory.getLogger(RSPCommand.class);

    private static final String[] CHOICES = {"rock", "scissors", "paper"};

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        LOG.info("Handling !rsp message.");
        return Mono.just(event)
                .map(MessageCreateEvent::getMessage)
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(CHOICES[ThreadLocalRandom.current().nextInt(0, 2 + 1)]))
                .then();
    }
}
