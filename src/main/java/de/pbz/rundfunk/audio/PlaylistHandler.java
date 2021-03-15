package de.pbz.rundfunk.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;

public class PlaylistHandler implements AudioLoadResultHandler {
    private static final Logger LOG = LoggerFactory.getLogger(PlaylistHandler.class);

    private final MusicManager musicManager;

    public PlaylistHandler(MusicManager musicManager) {
        this.musicManager = musicManager;
    }

    @Override
    public void trackLoaded(AudioTrack track) {
        LOG.info("Queue track: " + track.getIdentifier());
        musicManager.getScheduler().queue(track);
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        LOG.info("Queue playlist: " + playlist.getName());
        musicManager.getScheduler().clearQueue();
        Collections.shuffle(playlist.getTracks());
        playlist.getTracks().forEach(track -> musicManager.getScheduler().queue(track));
    }

    @Override
    public void noMatches() {
        LOG.warn("Could not find source!");
    }

    @Override
    public void loadFailed(FriendlyException exception) {
        LOG.warn("Could not load track!");
        musicManager.getScheduler().nextTrack();
    }
}
