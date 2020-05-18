package de.pbz.unitbot.commands.misc;

import de.pbz.unitbot.commands.Command;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.concurrent.ThreadLocalRandom;

public class RPSCommand implements Command {
    private static final Logger LOG = LoggerFactory.getLogger(RPSCommand.class);

    private static final String[] CHOICES = {":fist_tone2:", ":v_tone2:", ":raised_hand_tone2:"};

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        LOG.info("Handling !rps message.");
        return Mono.just(event)
                .map(MessageCreateEvent::getMessage)
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(CHOICES[ThreadLocalRandom.current().nextInt(0, 2 + 1)]))
                .then();
    }
}
