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
package io.github.borewit.lizzy.playlist.atom;

import io.github.borewit.lizzy.content.Content;
import io.github.borewit.lizzy.playlist.Media;
import io.github.borewit.lizzy.playlist.Playlist;
import io.github.borewit.lizzy.playlist.PlaylistWithTextEncoding;
import io.github.borewit.lizzy.playlist.xml.atom.EntryType;
import io.github.borewit.lizzy.playlist.xml.atom.FeedType;
import io.github.borewit.lizzy.playlist.xml.atom.Link;
import io.github.borewit.lizzy.playlist.xml.atom.ObjectFactory;


import java.io.IOException;
import java.io.OutputStream;

/**
 * An Atom Feed document wrapper.
 *
 * @author Borewit
 * @author Christophe Delory
 */
public class AtomPlaylist extends PlaylistWithTextEncoding
{
  /**
   * The Atom playlist provider
   */
  private final AtomProvider provider;

  /**
   * The feed document itself.
   */
  private final FeedType feed;

  public AtomPlaylist(final AtomProvider provider, FeedType feed)
  {
    super(provider);
    this.provider = provider;
    this.feed = feed;
  }

  @Override
  public AtomProvider getProvider()
  {
    return provider;
  }

  @Override
  public void writeTo(final OutputStream out) throws IOException
  {
    this.provider.writeTo(new ObjectFactory().createFeed(this.feed), out);
  }

  @Override
  public Playlist toPlaylist()
  {
    final Playlist ret = new Playlist();

    for (EntryType entry : feed.getEntry())
    {
      for (Link link : entry.getLink())
      {
        if ((link.getHref() != null) && "enclosure".equals(link.getRel()))
        {
          final Media media = new Media(); // NOPMD Avoid instantiating new objects inside loops
          final Content content = new Content(link.getHref()); // NOPMD Avoid instantiating new objects inside loops
          content.setType(link.getType());

          if (link.getLength() != null)
          {
            content.setLength(link.getLength().longValue());
          }

          media.setSource(content);
          ret.getRootSequence().addComponent(media);
        }
      }
    }

    // We don't really need it.
    ret.normalize();

    return ret;
  }
}
