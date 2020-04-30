package de.pbz.unitbot.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import lombok.Getter;


public class GuildMusicManager {

    @Getter
    private final AudioPlayer player;
    @Getter
    private final TrackScheduler scheduler;
    @Getter
    private final LavaPlayerAudioProvider provider;

    public GuildMusicManager(AudioPlayerManager manager) {
        player = manager.createPlayer();
        scheduler = new TrackScheduler(player);
        player.addListener(scheduler);
        provider = new LavaPlayerAudioProvider(player);
    }
}
