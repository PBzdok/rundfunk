package de.pbz.rundfunk.commands.misc;

import de.pbz.rundfunk.commands.Command;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.rest.util.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class HelpCommand implements Command {
    private static final Logger LOG = LoggerFactory.getLogger(HelpCommand.class);

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        LOG.info("Handling !help message.");
        return Mono.just(event)
                .map(MessageCreateEvent::getMessage)
                .flatMap(Message::getChannel)
                .flatMap(channel ->
                        channel.createEmbed(spec ->
                                spec.setColor(Color.DEEP_SEA)
                                        .setTitle("Ein Songwunsch gefaellig?")
                                        .addField("`!cat`", "Just cats...", false)
                                        .addField("`!rps`", "Play Rock-Paper-Scissors.", false)
                                        .addField("`!join`", "Let the bot join your voice channel.", false)
                                        .addField("`!play <direct_link>`", "Play single audio (youtube, soundcloud, bandcamp, vimeo).", false)
                                        .addField("`!playlist <direct_link>`", "Play playlist (youtube, soundcloud, bandcamp, vimeo).", false)
                                        .addField("`!q <direct_link>`", "Queue track or playlist (youtube, soundcloud, bandcamp, vimeo).", false)
                                        .addField("`!queue <direct_link>`", "Queue track or playlist (youtube, soundcloud, bandcamp, vimeo).", false)
                                        .addField("`!track`", "Show currently playing track information.", false)
                                        .addField("`!skip`", "Skip current track if more are queued", false)
                                        .addField("`!clear`", "Clear Audio Track Queue", false)
                                        .addField("`!df`", "Play DarkForce skit", false)
                                        .addField("`!wimbledon`", "Don't ask....", false)
                        ))
                .then();
    }
}
