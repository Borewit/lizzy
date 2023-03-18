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
package christophedelory.playlist.plist;

import christophedelory.content.Content;
import christophedelory.playlist.*;
import com.dd.plist.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

/**
 * The definition of an iTunes playlist.
 *
 * @author Christophe Delory
 * @version $Revision: 92 $
 */
public class PlistPlaylist implements SpecificPlaylist
{
  /**
   * The provider of this specific playlist.
   */
  private final PlistProvider _provider;

  /**
   * The playlist itself.
   */
  private NSDictionary _plist = new NSDictionary();



  public PlistPlaylist(final PlistProvider provider)
  {
    _provider = provider;
  }

  @Override
  public PlistProvider getProvider()
  {
    return _provider;
  }

  @Override
  public void writeTo(final OutputStream out, final String encoding) throws IOException
  {
    XMLPropertyListWriter.write(_plist, out);
    out.flush(); // May throw IOException.
  }

  @Override
  public Playlist toPlaylist()
  {
    final Playlist ret = new Playlist();

    NSDictionary tracks = null;

    final NSObject tracksObject = _plist.objectForKey("Tracks");

    if (tracksObject instanceof NSDictionary)
    {
      tracks = (NSDictionary) tracksObject;
    }

    NSArray playlists = null;
    final NSObject playlistsObject = _plist.objectForKey("Playlists");

    if (playlistsObject instanceof NSArray)
    {
      playlists = (NSArray) playlistsObject;
    }

    if ((tracks != null) && (playlists != null))
    {
      // Iterate through the playlists.
      for (NSObject playlistObject : playlists.getArray())
      {
        if (!(playlistObject instanceof NSDictionary)) continue; // NOPMD Deeply nested if then statement

        final NSDictionary playlist = (NSDictionary) playlistObject;
        final NSObject playlistItemsArrayObject = playlist.objectForKey("Playlist Items");

        if (!(playlistItemsArrayObject instanceof NSArray)) continue;

        final NSArray playlistItemsArray = (NSArray) playlistItemsArrayObject;
        // Each playlist is assigned to a dedicated sequence.
        final Sequence sequence = new Sequence(); // NOPMD Avoid instantiating new objects inside loops
        ret.getRootSequence().addComponent(sequence);

        for (NSObject playlistItemsDictObject : playlistItemsArray.getArray())
        {
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

          if (totalTimeObject instanceof NSNumber)
          {
            try
            {
              final long totalTime = ((NSNumber) totalTimeObject).longValue();
              content.setDuration(totalTime);
            }
            catch (NumberFormatException ignore) // NOPMD Avoid empty catch blocks
            {
              // Ignore it.
            }

            // Try to retrieve the length.
            final NSObject sizeObject = track.objectForKey("Size");

            if (sizeObject instanceof NSNumber)
            {
              try
              {
                final long size = ((NSNumber) sizeObject).longValue();

                if (size >= 0)
                {
                  content.setLength(size);
                }
              }
              catch (NumberFormatException e) // NOPMD Avoid empty catch blocks
              {
                // Ignore it.
              }
            }

            // Try to retrieve the last modified date.
            final NSObject dateModifiedObject = track.objectForKey("Date Modified");

            if (dateModifiedObject instanceof NSDate)
            {
              final Date dateModified = ((NSDate) dateModifiedObject).getDate();
              if (dateModified != null)
              {
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
   * Returns the playlist itself.
   *
   * @return a plist element. Shall not be <code>null</code>.
   * @see #setPlist
   */
  public NSDictionary getPlist()
  {
    return _plist;
  }

  /**
   * Initializes the playlist itself.
   *
   * @param plist a plist element. Shall not be <code>null</code>.
   * @throws NullPointerException if <code>plist</code> is <code>null</code>.
   * @see #getPlist
   */
  public void setPlist(final NSDictionary plist)
  {
    if (plist == null)
    {
      throw new NullPointerException("No plist");
    }

    _plist = plist;
  }
}
