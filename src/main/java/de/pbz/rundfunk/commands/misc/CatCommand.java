package de.pbz.rundfunk.commands.misc;

import de.pbz.rundfunk.commands.Command;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;


public class CatCommand implements Command {
    private static final Logger LOG = LoggerFactory.getLogger(CatCommand.class);

    private static final String CAT_API_URL = "https://api.thecatapi.com/v1/images/search";
    private static final String CAT_API_KEY = "157d7d66-6077-4d4c-80f9-b7c16527f910";

    private final HttpClient client = HttpClient.create();

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        LOG.info("Handling !cat message.");
        return Mono.just(event)
                .map(MessageCreateEvent::getMessage)
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(getCatImageUrl()))
                .then();
    }

    private String getCatImageUrl() {
        return client
                .headers(entries -> entries.set("x-api-key", CAT_API_KEY))
                .get()
                .uri(CAT_API_URL)
                .responseContent()
                .aggregate()
                .asString()
                .map(CatCommand::parseCatList)
                .onErrorStop()
                .block();
    }

    private static String parseCatList(String raw) {
        JSONArray jsonArray = new JSONArray(raw);
        JSONObject entry = jsonArray.getJSONObject(0);
        return entry.getString("url").replaceAll("\"", "");
    }
}
