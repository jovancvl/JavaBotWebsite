package jovancvl.javabotwebsite.WebSocket;

import dev.arbjerg.lavalink.client.AbstractAudioLoadResultHandler;
import dev.arbjerg.lavalink.client.player.*;
import jovancvl.javabotwebsite.Bot.Music.GuildMusicManager;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public class WebsiteAudioLoader extends AbstractAudioLoadResultHandler {
    private final GuildMusicManager manager;
    private final long guildId;

    public WebsiteAudioLoader(GuildMusicManager manager, long guildId) {
        this.manager = manager;
        this.guildId = guildId;
    }

    @Override
    public void ontrackLoaded(@NotNull TrackLoaded trackLoaded) {
        final Track track = trackLoaded.getTrack();

        this.manager.scheduler.enqueue(track);

        WebSocketMessage response = new WebSocketMessage(track.getInfo().getTitle());
        String url = "/controls/" + this.guildId + "/addSong";

        WebSocketMessagingService.template.convertAndSend(url, response);
    }

    @Override
    public void onPlaylistLoaded(@NotNull PlaylistLoaded playlistLoaded) {
        List<Track> songList = playlistLoaded.getTracks();

        this.manager.scheduler.enqueuePlaylist(songList);

        List<String> songNamesList = new LinkedList<>(this.manager.scheduler.queue.stream().map((song) -> song.getInfo().getTitle()).toList());
        WebSocketMessage response = new WebSocketMessage("Playlist loaded", songNamesList);

        WebSocketMessagingService.template.convertAndSend("/controls/" + this.guildId + "/addPlaylist", response);
    }

    @Override
    public void onSearchResultLoaded(@NotNull SearchResult searchResult) {
        final List<Track> tracks = searchResult.getTracks();

        if (tracks.isEmpty()) {
            // send message back that nothing was found
            this.noMatches();
            return;
        }

        final Track firstTrack = tracks.get(0);

        this.manager.scheduler.enqueue(firstTrack);

        // send message back that a song was found
        WebSocketMessage response = new WebSocketMessage(firstTrack.getInfo().getTitle());
        String url = "/controls/" + this.guildId + "/addSong";

        WebSocketMessagingService.template.convertAndSend(url, response);

    }

    @Override
    public void noMatches() {
        WebSocketMessage response = new WebSocketMessage("No matches");
        String url = "/controls/" + this.guildId + "/noMatches";

        WebSocketMessagingService.template.convertAndSend(url, response);

    }

    @Override
    public void loadFailed(@NotNull LoadFailed loadFailed) {
        WebSocketMessage response = new WebSocketMessage("loadfailed");
        String url = "/controls/" + this.guildId + "/loadFailed";

        WebSocketMessagingService.template.convertAndSend(url, response);
    }
}
