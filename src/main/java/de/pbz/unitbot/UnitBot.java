package de.pbz.unitbot;

import de.pbz.unitbot.commands.MCCommand;
import de.pbz.unitbot.commands.TTTCommand;
import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

public class UnitBot {
    private static final Logger LOG = LoggerFactory.getLogger(UnitBot.class);

    private static final Map<String, Command> commands = new HashMap<>();

    static {
        commands.put("ttt", new TTTCommand());
        commands.put("mc", new MCCommand());
    }

    public static void main(String[] args) {
        LOG.info("Starting Unit Bot...");

        final DiscordClient client = DiscordClientBuilder.create("NjM1MjEwMjM3NDc2OTk1MDcy.XqQfCw.JUMPFqHeGDSnc9WqfhzjxbetPZU").build();

        client.getEventDispatcher().on(ReadyEvent.class)
                .subscribe(ready -> LOG.info("Logged in as " + ready.getSelf().getUsername()));

        client.getEventDispatcher().on(MessageCreateEvent.class)
                .flatMap(event -> Mono.justOrEmpty(event.getMessage().getContent())
                        .flatMap(content -> Flux.fromIterable(commands.entrySet())
                                .filter(entry -> content.startsWith('!' + entry.getKey()))
                                .flatMap(entry -> entry.getValue().execute(event))
                                .next()))
                .subscribe();

        client.login().block();
    }
}