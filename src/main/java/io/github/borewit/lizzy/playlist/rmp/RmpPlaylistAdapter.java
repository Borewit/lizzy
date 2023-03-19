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
package io.github.borewit.lizzy.playlist.rmp;

import io.github.borewit.lizzy.content.Content;
import io.github.borewit.lizzy.playlist.*;
import io.github.borewit.playlist.rmp.RmpPackage;

import java.io.IOException;
import java.io.OutputStream;

/**
 * A Real Metadata Package.
 *
 * @author Borewit
 * @author Christophe Delory
 * @since 0.3.0
 */
public class RmpPlaylistAdapter extends AbstractPlaylist
{
  private RmpProvider provider;
  private final RmpPackage rmpPackage;

  /**
   * Builds a new and empty Real Metadata Package.
   */
  public RmpPlaylistAdapter(RmpProvider provider, RmpPackage rmpPackage)
  {
    this.provider = provider;
    this.rmpPackage = rmpPackage;
  }


  @Override
  public JaxbPlaylistProvider getProvider()
  {
    return this.provider;
  }

  @Override
  public void writeTo(final OutputStream out, final String encoding) throws IOException
  {
    this.provider.writeTo(this.rmpPackage, out, encoding);
  }

  @Override
  public Playlist toPlaylist()
  {
    final Playlist ret = new Playlist();

    String location = rmpPackage.getSERVER().getLOCATION();
    location = location.replace("%lid", (rmpPackage.getTRACKLIST().getLISTID() == null) ? "" : rmpPackage.getTRACKLIST().getLISTID());
    location = location.replace("%pid", (rmpPackage.getTARGET() == null) ? "" : rmpPackage.getTARGET());

    for (RmpPackage.TRACKLIST.TRACK track : rmpPackage.getTRACKLIST().getTRACK())
    {
      String url = location.replace("%fid", (track.getTRACKID() == null) ? "" : track.getTRACKID());
      // AFTER %fid replacement
      url = url.replace("%f", (track.getFILENAME() == null) ? "" : track.getFILENAME());

      if (rmpPackage.getSERVER().getNETNAME() != null)
      {
        final StringBuilder sb = new StringBuilder("http://"); // NOPMD Avoid instantiating new objects inside loops
        sb.append(rmpPackage.getSERVER().getNETNAME());
        sb.append(url);
        url = sb.toString();
      }

      final Media media = new Media(); // NOPMD Avoid instantiating new objects inside loops
      final Content content = new Content(url); // NOPMD Avoid instantiating new objects inside loops

      if (track.getSIZE() != null)
      {
        content.setLength(track.getSIZE().longValue());
      }

      if (track.getDURATION() != null)
      {
        content.setDuration(track.getDURATION().longValue() * 1000L);
      }

      media.setSource(content);
      ret.getRootSequence().addComponent(media);
    }

    // We don't really need it.
    ret.normalize();

    return ret;
  }
}
