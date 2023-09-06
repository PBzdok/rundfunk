package de.pbz.rundfunk.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

public class MusicManager {

    private final AudioPlayer player;
    private final TrackScheduler scheduler;
    private final LavaPlayerAudioProvider provider;

    public MusicManager(AudioPlayerManager manager) {
        player = manager.createPlayer();
        scheduler = new TrackScheduler(player);
        player.addListener(scheduler);
        provider = new LavaPlayerAudioProvider(player);
    }

    public AudioPlayer getPlayer() {
        return player;
    }

    public TrackScheduler getScheduler() {
        return scheduler;
    }

    public LavaPlayerAudioProvider getProvider() {
        return provider;
    }
}
