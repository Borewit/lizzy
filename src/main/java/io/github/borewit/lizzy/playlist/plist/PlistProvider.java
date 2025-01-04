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
package io.github.borewit.lizzy.playlist.plist;

import com.dd.plist.*;
import io.github.borewit.lizzy.content.type.ContentType;
import io.github.borewit.lizzy.player.PlayerSupport;
import io.github.borewit.lizzy.playlist.*;
import io.github.borewit.lizzy.xml.Version;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The iTunes library format.
 *
 * @author Christophe Delory
 * @version $Revision: 90 $
 */
public class PlistProvider implements SpecificPlaylistProvider {
  /**
   * A list of compatible content types.
   */
  private static final ContentType[] FILETYPES =
    {
      new ContentType(new String[]{".plist", ".xml"},
        new String[]{"text/xml"},
        new PlayerSupport[]
          {
            new PlayerSupport(PlayerSupport.Player.ITUNES, true, null),
          },
        "iTunes Library File"),
    };

  @Override
  public String getId() {
    return "plist";
  }

  @Override
  public ContentType[] getContentTypes() {
    return FILETYPES.clone();
  }

  @Override
  public SpecificPlaylist readFrom(final InputStream inputStream) throws IOException {
    NSDictionary plist;
    try {
      plist = (NSDictionary) PropertyListParser.parse(inputStream);
    } catch (Exception e) {
      throw new IOException(e);
    }
    return new PlistPlaylist(this, plist);
  }

  @Override
  public SpecificPlaylist toSpecificPlaylist(final Playlist playlist) throws IOException {
    final NSDictionary rootDict = new NSDictionary();
    final NSDictionary tracks = new NSDictionary();
    rootDict.put("Tracks", tracks);

    // A choice has been made to generate only one playlist.
    // This choice starts from here.
    // TODO We could have generated one playlist per sequence.
    final NSDictionary playlistDict = new NSDictionary();

    ArrayList<NSDictionary> playlistEntries = new ArrayList<>();
    playlistEntries.add(playlistDict);

    rootDict.put("Playlists", playlistEntries);

    playlistDict.put("Name", new NSString("Playlist generated by Lizzy v" + Version.CURRENT));
    playlistDict.put("Playlist ID", new NSNumber(System.identityHashCode(playlist.getRootSequence())));
    //playlistDict.put("Playlist Persistent ID", new NSString(???));
    playlistDict.put("All Items", NSObject.fromJavaObject(true));
    ArrayList<NSDictionary> playlistArray = new ArrayList<>();
    addToPlaylist(tracks, playlistArray, playlist.getRootSequence()); // May throw Exception.
    playlistDict.put("Playlist Items", playlistArray);

    return new PlistPlaylist(this, rootDict);
  }

  /**
   * Adds the specified generic playlist component, and all its childs if any, to the input track list and playlist.
   *
   * @param tracks        the list of tracks. Shall not be <code>null</code>.
   * @param playlistItems the playlist. Shall not be <code>null</code>.
   * @param component     the generic playlist component to handle. Shall not be <code>null</code>.
   */
  @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
  private void addToPlaylist(final NSDictionary tracks, List<NSDictionary> playlistItems, final AbstractPlaylistComponent component) {
    if (component instanceof Sequence) {
      final Sequence sequence = (Sequence) component;

      if (sequence.getRepeatCount() < 0) {
        throw new IllegalArgumentException("A PLIST playlist cannot handle a sequence repeated indefinitely");
      }

      for (int iter = 0; iter < sequence.getRepeatCount(); iter++) {
        sequence.getComponents().forEach(c -> addToPlaylist(tracks, playlistItems, c));
      }
    } else if (component instanceof Parallel) {
      throw new IllegalArgumentException("A PLIST playlist cannot play different media at the same time");
    } else if (component instanceof Media) {
      final Media media = (Media) component;

      if (media.getDuration() != null) {
        throw new IllegalArgumentException("A PLIST playlist cannot handle a timed media");
      }

      if (media.getRepeatCount() < 0) {
        throw new IllegalArgumentException("A PLIST playlist cannot handle a media repeated indefinitely");
      }

      if (media.getSource() != null) {
        for (int iter = 0; iter < media.getRepeatCount(); iter++) {
          // Adds a playlist entry.
          final NSDictionary entry = new NSDictionary();
          entry.put("Track ID", new NSNumber(System.identityHashCode(media.getSource())));
          playlistItems.add(entry);

          // Adds a new track.
          final NSDictionary track = new NSDictionary();
          tracks.put(Integer.toString(System.identityHashCode(media.getSource())), track);

          track.put("Track ID", new NSNumber(System.identityHashCode(media.getSource())));

          if (media.getSource().getLength() >= 0L) // NOPMD Deeply nested if then statement
          {
            track.put("Size", new NSNumber((int) media.getSource().getLength()));
          }

          if (media.getSource().getDuration() >= 0L) // NOPMD Deeply nested if then statement
          {
            track.put("Total Time", new NSNumber((int) media.getSource().getDuration()));
          }

          if (media.getSource().getLastModified() > 0L) // NOPMD Deeply nested if then statement
          {
            track.put("Date Modified", new NSDate(new Date(media.getSource().getLastModified())));
          }

          track.put("Location", new NSString(media.getSource().toString()));
        }
      }
    }
  }
}
