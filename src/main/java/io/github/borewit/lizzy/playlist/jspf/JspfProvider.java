/*
 * Copyright (c) 2008, Christophe Delory
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY CHRISTOPHE DELORY ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL CHRISTOPHE DELORY BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package io.github.borewit.lizzy.playlist.jspf;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.borewit.lizzy.content.type.ContentType;
import io.github.borewit.lizzy.player.PlayerSupport;
import io.github.borewit.lizzy.playlist.*;
import io.github.borewit.lizzy.playlist.jspf.json.JsonJspfPlaylist;
import io.github.borewit.lizzy.playlist.jspf.json.JsonJspfTrack;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * JSON XSPF Playlist implementation
 */
public class JspfProvider implements SpecificPlaylistProvider {
  /**
   * A list of compatible content types.
   */
  private static final ContentType[] FILETYPES =
    {
      new ContentType(new String[]{".jspf"},
        new String[]{"application/jspf+json"}, // Informal MIME type
        new PlayerSupport[]
          {
            new PlayerSupport(PlayerSupport.Player.VLC_MEDIA_PLAYER, true, null),
          },
        "XML Shareable Playlist Format (XSPF)"),
    };

  @Override
  public String getId() {
    return "jspf";
  }

  @Override
  public ContentType[] getContentTypes() {
    return FILETYPES.clone();
  }

  @Override
  public SpecificPlaylist readFrom(final InputStream inputStream) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    JsonJspfPlaylist jsonJspfPlaylist = objectMapper.readValue(inputStream, JsonJspfPlaylist.class);
    return new JspfPlaylist(this, jsonJspfPlaylist);
  }

  @Override
  public SpecificPlaylist toSpecificPlaylist(final Playlist playlist) throws IOException {
    JsonJspfPlaylist jspfPlaylist = new JsonJspfPlaylist();
    this.addToPlaylist(jspfPlaylist.getTracks(), playlist.getRootSequence());
    return new JspfPlaylist(this, jspfPlaylist);
  }

  public void writeTo(OutputStream out, JsonJspfPlaylist playlist) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.writeValue(out, playlist);
  }

  /**
   * Adds the specified generic playlist component, and all its childs if any, to the input track list.
   *
   * @param playlistTracks the parent playlist. Shall not be <code>null</code>.
   * @param component      the generic playlist component to handle. Shall not be <code>null</code>.
   */
  private void addToPlaylist(List<JsonJspfTrack> playlistTracks, final AbstractPlaylistComponent component) {
    if (component instanceof Sequence) {
      final Sequence sequence = (Sequence) component;

      if (sequence.getRepeatCount() < 0) {
        throw new IllegalArgumentException("An XSPF playlist cannot handle a sequence repeated indefinitely");
      }

      for (int iter = 0; iter < sequence.getRepeatCount(); iter++) {
        sequence.getComponents().forEach(c -> addToPlaylist(playlistTracks, c));
      }
    } else if (component instanceof Parallel) {
      throw new IllegalArgumentException("An XSPF playlist cannot play different media at the same time");
    } else if (component instanceof Media) {
      final Media media = (Media) component;

      if (media.getDuration() != null) {
        throw new IllegalArgumentException("An XSPF playlist cannot handle a timed media");
      }

      if (media.getRepeatCount() < 0) {
        throw new IllegalArgumentException("An XSPF playlist cannot handle a media repeated indefinitely");
      }

      if (media.getSource() != null) {
        for (int iter = 0; iter < media.getRepeatCount(); iter++) {
          final JsonJspfTrack track = new JsonJspfTrack(); // NOPMD Avoid instantiating new objects inside loops
          track.setLocation(media.getSource().toString());

          if (media.getSource().getDuration() > 0L) // NOPMD Deeply nested if..then statements are hard to read
          {
            track.setDuration(media.getSource().getDuration());
          }
          playlistTracks.add(track);
        }
      }
    }
  }
}
