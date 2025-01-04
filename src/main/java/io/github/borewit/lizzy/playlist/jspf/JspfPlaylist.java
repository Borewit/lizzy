package io.github.borewit.lizzy.playlist.jspf;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.borewit.lizzy.content.Content;
import io.github.borewit.lizzy.playlist.Media;
import io.github.borewit.lizzy.playlist.Playlist;
import io.github.borewit.lizzy.playlist.SpecificPlaylist;
import io.github.borewit.lizzy.playlist.SpecificPlaylistProvider;
import io.github.borewit.lizzy.playlist.jspf.json.JsonJspfPlaylist;
import io.github.borewit.lizzy.playlist.jspf.json.JsonJspfTrack;

import java.io.IOException;
import java.io.OutputStream;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JspfPlaylist implements SpecificPlaylist {

  private final JspfProvider jspfProvider;
  private final JsonJspfPlaylist jsonPlaylist;

  public JspfPlaylist(final JspfProvider jspfProvider, final JsonJspfPlaylist jspfPlaylist) {
    this.jspfProvider = jspfProvider;
    this.jsonPlaylist = jspfPlaylist;
  }

  @Override
  public SpecificPlaylistProvider getProvider() {
    return jspfProvider;
  }

  @Override
  public void writeTo(OutputStream out) throws IOException {
    jspfProvider.writeTo(out, jsonPlaylist);
  }

  @Override
  public Playlist toPlaylist() {
    final Playlist ret = new Playlist();

    if (this.jsonPlaylist.getTracks() != null) {
      for (JsonJspfTrack track : this.jsonPlaylist.getTracks()) {
        if (track.getLocation() != null) {
          final Media media = new Media(); // NOPMD Avoid instantiating new objects inside loops
          final Content content = new Content(track.getLocation()); // NOPMD Avoid instantiating new objects inside loops
          media.setSource(content);

          if (track.getDuration() != null) {
            content.setDuration(track.getDuration());
          }

          ret.getRootSequence().addComponent(media);
        }
      }

      // We don't really need it.
      ret.normalize();
    }
    return ret;
  }
}

