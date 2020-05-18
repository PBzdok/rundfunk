package de.pbz.unitbot.commands.audio;

import de.pbz.unitbot.commands.Command;
import de.pbz.unitbot.audio.MusicManager;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class JoinCommand implements Command {
    private static final Logger LOG = LoggerFactory.getLogger(JoinCommand.class);

    private final MusicManager musicManager;

    public JoinCommand(MusicManager musicManager) {
        this.musicManager = musicManager;
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        LOG.info("Handling !join message.");
        return Mono.justOrEmpty(event.getMember())
                .flatMap(Member::getVoiceState)
                .flatMap(VoiceState::getChannel)
                .flatMap(channel -> channel.join(spec -> spec.setProvider(musicManager.getProvider())))
                .then();
    }
}
