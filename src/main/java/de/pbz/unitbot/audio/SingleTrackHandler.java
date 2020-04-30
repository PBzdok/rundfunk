package de.pbz.unitbot.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SingleTrackHandler implements AudioLoadResultHandler {
    private static final Logger LOG = LoggerFactory.getLogger(SingleTrackHandler.class);

    private final MusicManager musicManager;

    public SingleTrackHandler(MusicManager musicManager) {
        this.musicManager = musicManager;
    }

    @Override
    public void trackLoaded(AudioTrack track) {
        LOG.info("Start track: " + track.getIdentifier());
        musicManager.getPlayer().playTrack(track);
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        // LavaPlayer found multiple AudioTracks from some playlist
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
