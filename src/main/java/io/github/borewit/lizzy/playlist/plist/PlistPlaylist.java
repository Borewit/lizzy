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
import io.github.borewit.lizzy.content.Content;
import io.github.borewit.lizzy.playlist.Media;
import io.github.borewit.lizzy.playlist.Playlist;
import io.github.borewit.lizzy.playlist.Sequence;
import io.github.borewit.lizzy.playlist.SpecificPlaylist;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

/**
 * The definition of an iTunes playlist.
 *
 * @author Borewit
 * @author Christophe Delory
 */
public class PlistPlaylist implements SpecificPlaylist {
  /**
   * The provider of this specific playlist.
   */
  private final PlistProvider provider;

  /**
   * The playlist itself.
   */
  private final NSDictionary plist;


  public PlistPlaylist(final PlistProvider provider, final NSDictionary plist) {
    this.provider = provider;
    this.plist = plist;
  }

  @Override
  public PlistProvider getProvider() {
    return provider;
  }

  @Override
  public void writeTo(final OutputStream out) throws IOException {
    XMLPropertyListWriter.write(plist, out);
    out.flush(); // May throw IOException.
  }

  @Override
  public Playlist toPlaylist() {
    final Playlist ret = new Playlist();

    NSDictionary tracks = null;

    final NSObject tracksObject = plist.objectForKey("Tracks");

    if (tracksObject instanceof NSDictionary) {
      tracks = (NSDictionary) tracksObject;
    }

    NSArray playlists = null;
    final NSObject playlistsObject = plist.objectForKey("Playlists");

    if (playlistsObject instanceof NSArray) {
      playlists = (NSArray) playlistsObject;
    }

    if ((tracks != null) && (playlists != null)) {
      // Iterate through the playlists.
      for (NSObject playlistObject : playlists.getArray()) {
        if (!(playlistObject instanceof NSDictionary)) continue; // NOPMD Deeply nested if then statement

        final NSDictionary playlist = (NSDictionary) playlistObject;
        final NSObject playlistItemsArrayObject = playlist.objectForKey("Playlist Items");

        if (!(playlistItemsArrayObject instanceof NSArray)) continue;

        final NSArray playlistItemsArray = (NSArray) playlistItemsArrayObject;
        final Sequence sequence;
        if (playlists.getArray().length > 1) {
          // If there are multiple playlists, assigned them to a dedicated sequence.
          sequence = new Sequence();
          ret.getRootSequence().addComponent(sequence);
        } else {
          sequence = ret.getRootSequence();
        }

        for (NSObject playlistItemsDictObject : playlistItemsArray.getArray()) {
          if (!(playlistItemsDictObject instanceof NSDictionary)) continue;

          final NSObject trackIdObject = ((NSDictionary) playlistItemsDictObject).objectForKey("Track ID");

          if (!(trackIdObject instanceof NSNumber)) continue;

          final int trackId = ((NSNumber) trackIdObject).intValue();

          // Got one track identifier!!!
          // Now find it in the track list.
          final NSObject trackObject = tracks.objectForKey(Integer.toString(trackId));

          if (!(trackObject instanceof NSDictionary)) continue;

          final NSDictionary track = (NSDictionary) trackObject;
          final NSObject locationObject = track.objectForKey("Location");

          if (!(locationObject instanceof NSString)) continue;

          final String location = ((NSString) locationObject).getContent();

          if (location == null) continue;

          // Now create the media.
          final Media media = new Media(); // NOPMD Avoid instantiating new objects inside loops
          final Content content = new Content(location); // NOPMD Avoid instantiating new objects inside loops
          media.setSource(content);

          // Try to retrieve the duration.
          final NSObject totalTimeObject = track.objectForKey("Total Time");

          if (totalTimeObject instanceof NSNumber) {
            try {
              final long totalTime = ((NSNumber) totalTimeObject).longValue();
              content.setDuration(totalTime);
            } catch (NumberFormatException ignore) // NOPMD Avoid empty catch blocks
            {
              // Ignore it.
            }

            // Try to retrieve the length.
            final NSObject sizeObject = track.objectForKey("Size");

            if (sizeObject instanceof NSNumber) {
              try {
                final long size = ((NSNumber) sizeObject).longValue();

                if (size >= 0) {
                  content.setLength(size);
                }
              } catch (NumberFormatException e) // NOPMD Avoid empty catch blocks
              {
                // Ignore it.
              }
            }

            // Try to retrieve the last modified date.
            final NSObject dateModifiedObject = track.objectForKey("Date Modified");

            if (dateModifiedObject instanceof NSDate) {
              final Date dateModified = ((NSDate) dateModifiedObject).getDate();
              if (dateModified != null) {
                content.setLastModified(dateModified.getTime());
              }
            }
          }
          sequence.addComponent(media);
        }
      }

      ret.normalize();
    }

    return ret;
  }

  /**
   * Returns the playlist serialization object itself.
   *
   * @return a plist element. Shall not be <code>null</code>.
   */
  public NSDictionary getPlist() {
    return plist;
  }
}
