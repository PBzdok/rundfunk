package de.pbz.unitbot.commands;

import de.pbz.unitbot.Command;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.io.IOException;

import static org.asynchttpclient.Dsl.asyncHttpClient;

public class CatCommand implements Command {
    private static final Logger LOG = LoggerFactory.getLogger(CatCommand.class);

    private static final String CAT_API_URL = "https://api.thecatapi.com/v1/images/search";
    private static final String CAT_API_KEY = "157d7d66-6077-4d4c-80f9-b7c16527f910";

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        LOG.info("Handling !cat message.");
        return Mono.just(event)
                .map(MessageCreateEvent::getMessage)
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(getCatImageUrl()))
                .onErrorResume(e -> Mono.just(event)
                        .map(MessageCreateEvent::getMessage)
                        .flatMap(Message::getChannel)
                        .flatMap(channel -> channel.createMessage(e.getMessage()))
                )
                .then();
    }

    private String getCatImageUrl() {
        JSONArray jsonArray = new JSONArray(getCatJson());
        JSONObject entry = jsonArray.getJSONObject(0);
        return entry.getString("url").replaceAll("\"", "");
    }

    private String getCatJson() {
        try (AsyncHttpClient asyncHttpClient = asyncHttpClient()) {
            LOG.info("Getting Cat JSON...");
            return asyncHttpClient
                    .prepareGet(CAT_API_URL)
                    .addHeader("x-api-key", CAT_API_KEY)
                    .execute()
                    .toCompletableFuture()
                    .thenApply(Response::getResponseBody)
                    .join();
        } catch (IOException e) {
            LOG.error("Failed to load Cat JSON: " + e.getCause());
            throw new RuntimeException("Could not get Cat JSON", e);
        }
    }
}
