package de.pbz.rundfunk.commands.misc;

import de.pbz.rundfunk.commands.Command;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CatCommand implements Command {
    private static final Logger LOG = LoggerFactory.getLogger(CatCommand.class);

    private static final String CAT_API_URL = "https://api.thecatapi.com/v1/images/search";
    private static final String CAT_API_KEY = "157d7d66-6077-4d4c-80f9-b7c16527f910";

    final HttpClient client = HttpClient.newHttpClient();

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
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(CAT_API_URL))
                .header("x-api-key", CAT_API_KEY)
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException e) {
            return "Error while communicating with the cat api: " + e.getLocalizedMessage();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();  // set interrupt flag
            return "Error while communicating with the cat api: " + e.getLocalizedMessage();
        }
    }
}
