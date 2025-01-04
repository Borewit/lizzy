package io.github.borewit.lizzy.test.json.playlist;

import io.github.borewit.lizzy.playlist.Playlist;

import java.util.List;

public class JsonPlaylist {
  public List<JsonEntry> rootSequence;

  public static JsonPlaylist toJson(Playlist playlist) {
    JsonPlaylist jsonPlaylist = new JsonPlaylist();
    jsonPlaylist.rootSequence = JsonEntry.toJson(playlist.getRootSequence().getComponents());
    return jsonPlaylist;
  }
}
