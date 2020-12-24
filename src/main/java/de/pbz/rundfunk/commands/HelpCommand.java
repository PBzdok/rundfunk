package de.pbz.rundfunk.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class HelpCommand implements Command {
    private static final Logger LOG = LoggerFactory.getLogger(HelpCommand.class);

    private static final String HELP = "`!cat` -> Just cats...\n" +
            "`!wt` -> Get WatchTogether G-Unit room url.\n" +
            "`!rps` -> Play rock-paper-scissors.\n" +
            "`!join` -> Make the bot join the voice channel you are in. Permissions still apply!\n" +
            "`!play <direct_link>` -> Play single audio (youtube, soundcloud, bandcamp, vimeo) in the voice channel the bot is in.\n" +
            "`!playlist <direct_link>` -> Play playlist (youtube, soundcloud, bandcamp, vimeo) in the voice channel the bot is in.\n" +
            "`!track` -> Show currently playing track information.\n" +
            "`!skip` -> Skip current track if there are more queued tracks.\n" +
            "`!df` -> Play epic DarkForce skit\n." +
            "`!wimbledon` -> I don't know why we have this...";

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