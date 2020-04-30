package de.pbz.unitbot.commands;

import de.pbz.unitbot.Command;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class HelpCommand implements Command {
    private static final Logger LOG = LoggerFactory.getLogger(HelpCommand.class);

    private static final String HELP = "`!cat` -> Just cats...\n" +
            "`!ttt` -> Get the G-Unit TTT server address.\n" +
            "`!mc` -> Get the G-Unit MC server address.\n" +
            "`!rps` -> Play rock-paper-scissors.\n" +
            "`!join` -> Make the bot join the voice channel you are in. Permissions still apply!\n" +
            "`!play <youtube_direct_link>` -> Play single youtube video in `Volksradio` channel\n" +
            "`!playlist <youtube_direct_link>` -> Play youtube playlist in `Volksradio` channel";

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        LOG.info("Handling !help message.");
        return Mono.just(event)
                .map(MessageCreateEvent::getMessage)
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(HELP))
                .then();
    }
}
