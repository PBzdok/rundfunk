package de.pbz.unitbot.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlaylistHandler implements AudioLoadResultHandler {
    private static final Logger LOG = LoggerFactory.getLogger(PlaylistHandler.class);

    private final GuildMusicManager musicManager;

    public PlaylistHandler(GuildMusicManager musicManager) {
        this.musicManager = musicManager;
    }

    @Override
    public void trackLoaded(AudioTrack track) {
        LOG.info("Queue track: " + track.getIdentifier());
        musicManager.getScheduler().queue(track);
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        LOG.info("Load playlist track: " + playlist.getName());
        AudioTrack firstTrack = playlist.getSelectedTrack();

        if (firstTrack == null) {
            firstTrack = playlist.getTracks().get(0);
        }

        musicManager.getScheduler().queue(firstTrack);
    }

    @Override
    public void noMatches() {
        // LavaPlayer did not find any audio to extract
    }

    @Override
    public void loadFailed(FriendlyException exception) {
        // LavaPlayer could not parse an audio source for some reason
    }
}
