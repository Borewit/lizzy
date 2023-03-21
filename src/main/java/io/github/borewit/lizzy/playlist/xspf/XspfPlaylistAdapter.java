/*
 * Copyright (c) 2008-2009, Christophe Delory
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
package io.github.borewit.lizzy.playlist.xspf;

import io.github.borewit.lizzy.content.Content;
import io.github.borewit.lizzy.playlist.*;
import io.github.borewit.lizzy.playlist.xml.xspf.ObjectFactory;
import io.github.borewit.lizzy.playlist.xml.xspf.XspfPlaylist;
import io.github.borewit.lizzy.playlist.xml.xspf.XspfTrack;

import java.io.IOException;
import java.io.OutputStream;


/**
 * XSPF, an XML format designed to enable playlist sharing.
 *
 * @author Borewit
 * @author Christophe Delory
 */
public class XspfPlaylistAdapter extends AbstractPlaylist
{

  /**
   * The provider of this specific playlist.
   */
  private final JaxbPlaylistProvider provider;

  private final XspfPlaylist xspfPlaylist;

  public XspfPlaylistAdapter(JaxbPlaylistProvider provider, XspfPlaylist xspfPlaylist)
  {
    this.provider = provider;
    this.xspfPlaylist = xspfPlaylist;
  }

  @Override
  public JaxbPlaylistProvider getProvider()
  {
    return provider;
  }

  @Override
  public void writeTo(final OutputStream out, final String encoding) throws IOException
  {
    this.provider.writeTo(new ObjectFactory().createPlaylist(xspfPlaylist), out, encoding);
  }

  @Override
  public Playlist toPlaylist()
  {
    final Playlist ret = new Playlist();

    if (xspfPlaylist.getTrackList() != null)
    {
      for (XspfTrack track : xspfPlaylist.getTrackList().getTrack())
      {
        for (String location : track.getLocation())
        {
          final Media media = new Media(); // NOPMD Avoid instantiating new objects inside loops
          final Content content = new Content(location); // NOPMD Avoid instantiating new objects inside loops
          media.setSource(content);

          if (track.getDuration() != null)
          {
            content.setDuration(track.getDuration().longValue());
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
