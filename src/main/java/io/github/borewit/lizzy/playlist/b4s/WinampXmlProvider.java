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
package io.github.borewit.lizzy.playlist.b4s;

import io.github.borewit.lizzy.content.type.ContentType;
import io.github.borewit.lizzy.player.PlayerSupport;
import io.github.borewit.lizzy.playlist.*;
import io.github.borewit.lizzy.playlist.xml.b4s.WinampXML;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * A proprietary XML-based format introduced in Winamp version 3.
 * Resembles iTunes library XML in many ways.
 *
 * @author Borewit
 * @author Christophe Delory
 */
public class WinampXmlProvider extends JaxbPlaylistProvider<WinampXML>
{
  /**
   * A list of compatible content types.
   */
  private static final ContentType[] FILETYPES =
    {
      new ContentType(new String[]{".b4s", ".bpl"},
        new String[]{"text/xml"}, // FIXME Something better?
        new PlayerSupport[]
          {
            new PlayerSupport(PlayerSupport.Player.WINAMP, false, null),
            new PlayerSupport(PlayerSupport.Player.VLC_MEDIA_PLAYER, false, null),
          },
        "Winamp 3+ Playlist"),
    };

  public WinampXmlProvider()
  {
    super(WinampXML.class);
  }

  @Override
  public String getId()
  {
    return "b4s";
  }

  @Override
  public ContentType[] getContentTypes()
  {
    return FILETYPES.clone();
  }

  @Override
  public SpecificPlaylist readFrom(final InputStream inputStream) throws IOException
  {
    try
    {
      final JAXBElement<WinampXML> winampXMLJAXBElement = this.unmarshal(inputStream);
      String rootElementName = winampXMLJAXBElement.getName().getLocalPart();

      return rootElementName != null && rootElementName.equalsIgnoreCase("WinampXML") ?
        new WinampXmlAdapter(this, winampXMLJAXBElement.getValue()) : null;
    }
    catch (XMLStreamException | JAXBException exception)
    {
      throw new IOException(exception);
    }

  }

  @Override
  public SpecificPlaylist toSpecificPlaylist(final Playlist playlist)
  {
    final WinampXML winampXML = new WinampXML();
    final WinampXML.Playlist xmlPlaylist = new WinampXML.Playlist();
    winampXML.setPlaylist(xmlPlaylist);

    addToPlaylist(xmlPlaylist.getEntry(), playlist.getRootSequence()); // May throw Exception.

    return new WinampXmlAdapter(this, winampXML);
  }

  /**
   * Adds the specified generic playlist component, and all its childs if any, to the input playlist.
   *
   * @param playlist  the parent playlist. Shall not be <code>null</code>.
   * @param component the generic playlist component to handle. Shall not be <code>null</code>.
   */
  private void addToPlaylist(final List<WinampXML.Playlist.Entry> playlist, final AbstractPlaylistComponent component)
  {
    if (component instanceof Sequence)
    {
      final Sequence sequence = (Sequence) component;

      if (sequence.getRepeatCount() < 0)
      {
        throw new IllegalArgumentException("A B4S playlist cannot handle a sequence repeated indefinitely");
      }

      for (int iter = 0; iter < sequence.getRepeatCount(); iter++)
      {
        sequence.getComponents().forEach(c -> addToPlaylist(playlist, c));
      }
    }
    else if (component instanceof Parallel)
    {
      throw new IllegalArgumentException("A B4S playlist cannot play different media at the same time");
    }
    else if (component instanceof Media)
    {
      final Media media = (Media) component;

      if (media.getDuration() != null)
      {
        throw new IllegalArgumentException("A B4S playlist cannot handle a timed media");
      }

      if (media.getRepeatCount() < 0)
      {
        throw new IllegalArgumentException("A B4S playlist cannot handle a media repeated indefinitely");
      }

      if (media.getSource() != null)
      {
        for (int iter = 0; iter < media.getRepeatCount(); iter++)
        {
          final WinampXML.Playlist.Entry entry = new WinampXML.Playlist.Entry(); // NOPMD Avoid instantiating new objects inside loops
          entry.setPlaystring(media.getSource().toString());

          if (media.getSource().getLength() >= 0L) // NOPMD Deeply nested if..then statements are hard to read
          {
            entry.setLength(media.getSource().getLength()); // Shall not throw IllegalArgumentException.
          }

          playlist.add(entry);
        }
      }
    }
  }
}
