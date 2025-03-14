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
package io.github.borewit.lizzy.playlist.asx;

import io.github.borewit.lizzy.content.type.ContentType;
import io.github.borewit.lizzy.player.PlayerSupport;
import io.github.borewit.lizzy.playlist.*;
import io.github.borewit.lizzy.playlist.xml.asx.*;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * The Windows Media ASX playlist XML format.
 * An XML style playlist containing more information about the items on the playlist.
 *
 * @author Christophe Delory
 * @version $Revision: 90 $
 */
public class AsxProvider extends JaxbPlaylistProvider<Asx> {
  /**
   * A list of compatible content types.
   */
  private static final ContentType[] FILETYPES =
    {
      new ContentType(new String[]{".asx"},
        new String[]{"video/x-ms-asf"},
        new PlayerSupport[]
          {
            new PlayerSupport(PlayerSupport.Player.WINAMP, false, null),
            new PlayerSupport(PlayerSupport.Player.VLC_MEDIA_PLAYER, false, null),
            new PlayerSupport(PlayerSupport.Player.WINDOWS_MEDIA_PLAYER, true, null),
            new PlayerSupport(PlayerSupport.Player.MEDIA_PLAYER_CLASSIC, true, null),
            new PlayerSupport(PlayerSupport.Player.REALPLAYER, false, null),
          },
        "Advanced Stream Redirector (ASX)"),
      new ContentType(new String[]{".wmx"},
        new String[]{"video/x-ms-wvx"},
        new PlayerSupport[]
          {
            new PlayerSupport(PlayerSupport.Player.REALPLAYER, false, null),
          },
        "Windows Media Redirector (WMX)"),
      new ContentType(new String[]{".wvx"},
        new String[]{"video/x-ms-wvx"},
        new PlayerSupport[]
          {
            new PlayerSupport(PlayerSupport.Player.WINDOWS_MEDIA_PLAYER, false, null),
            new PlayerSupport(PlayerSupport.Player.MEDIA_PLAYER_CLASSIC, false, null),
            new PlayerSupport(PlayerSupport.Player.REALPLAYER, false, null),
          },
        "Windows Media Video Redirector (WVX)"),
      new ContentType(new String[]{".wax"},
        new String[]{"audio/x-ms-wax"},
        new PlayerSupport[]
          {
            new PlayerSupport(PlayerSupport.Player.WINDOWS_MEDIA_PLAYER, false, null),
            new PlayerSupport(PlayerSupport.Player.MEDIA_PLAYER_CLASSIC, false, null),
            new PlayerSupport(PlayerSupport.Player.REALPLAYER, false, null),
          },
        "Windows Media Audio Redirector (WAX)"),
    };

  public AsxProvider() {
    super(Asx.class);
    super.setTextEncoding(StandardCharsets.US_ASCII); // Tested with Windows media player
  }

  @Override
  public String getId() {
    return "asx";
  }

  @Override
  public ContentType[] getContentTypes() {
    return FILETYPES.clone();
  }

  @Override
  public SpecificPlaylist readFrom(final InputStream inputStream) throws IOException {
    try {
      final JAXBElement<Asx> asx = this.unmarshal(inputStream);
      String rootElementName = asx.getName().getLocalPart();

      return rootElementName != null && rootElementName.equalsIgnoreCase("ASX") ?
        new AsxPlaylistAdapter(this, asx.getValue()) : null;
    } catch (JAXBException | XMLStreamException exception) {
      throw new IOException(exception.getMessage(), exception);
    }
  }

  protected Marshaller makeMarshaller(String encoding) throws JAXBException {
    Marshaller marshaller = super.makeMarshaller(encoding);
    // First line should be: "<ASX VERSION="3.0">"
    marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
    return marshaller;
  }

  @Override
  public SpecificPlaylist toSpecificPlaylist(final Playlist playlist) throws IOException {
    final AsxPlaylistAdapter asxPlaylist = new AsxPlaylistAdapter(this);

    addToPlaylist(asxPlaylist.getAsx(), playlist.getRootSequence()); // May throw Exception.

    return asxPlaylist;
  }

  /**
   * Adds the specified generic playlist component, and all its childs if any, to the input ASX elements container.
   *
   * @param asxContainer ASX Container element (ASX or REPEAT element). Shall not be <code>null</code>.
   * @param component    the generic playlist component to handle. Shall not be <code>null</code>.
   */
  private void addToPlaylist(final ContainerElement asxContainer, final AbstractPlaylistComponent component) {
    if (component instanceof Sequence) {
      final Sequence sequence = (Sequence) component;

      // EntryElement newContainer = new EntryElement();

      // Do we need a repeat element?
      if (sequence.getRepeatCount() > 1 && asxContainer instanceof Asx) {
        Asx asx = (Asx) asxContainer;
        final REPEAT repeat = new REPEAT();
        repeat.setCOUNT((int) (sequence.getRepeatCount() - 1));
        asx.getREPEAT().add(repeat);
        sequence.getComponents().forEach(child -> addToPlaylist(repeat, child));
      } else {
        // We cannot handle nested repeats in ASX
        sequence.getComponents().forEach(child -> addToPlaylist(asxContainer, child));
      }
    } else if (component instanceof Parallel) {
      throw new IllegalArgumentException("A parallel time container is incompatible with an ASX playlist");
    } else if (component instanceof Media) {
      final Media media = (Media) component;

      if (media.getSource() != null) // Ignore "empty" media.
      {
        // Do we need a repeat element?
        if (media.getRepeatCount() > 1 && asxContainer instanceof Asx) {
          Asx asx = (Asx) asxContainer;
          final REPEAT repeat = new REPEAT();
          repeat.setCOUNT((int) (media.getRepeatCount() - 1));
          asx.getREPEAT().add(repeat);
          addToPlaylist(repeat, media);
        } else {
          boolean isPlaylist = false;
          final String path = media.getSource().toString().toLowerCase();

          for (ContentType type : FILETYPES) {
            for (String extension : type.getExtensions()) {
              isPlaylist = isPlaylist || path.endsWith(extension);
            }
          }

          if (isPlaylist) {
            if (media.getDuration() != null) {
              throw new IllegalArgumentException("An ASX playlist referenced in another ASX playlist cannot be timed");
            }
            final RefElement entryRef = new RefElement();
            entryRef.setHREF(media.getSource().toString());
            asxContainer.getENTRYOrENTRYREF().add(entryRef);
          } else {
            final ENTRY entry = new ENTRY();
            final RefElement reference = new RefElement();
            reference.setHREF(media.getSource().toString());

            if (media.getDuration() != null) {
              final DurationElement duration = new DurationElement();
              // ToDo
              // duration.setValue(media.getDuration().longValue());
              // reference.setDuration(duration);
            }

            entry.setREF(reference);
            asxContainer.getENTRYOrENTRYREF().add(entry);
          }
        }
      }
    }
  }

  @Override
  protected XMLStreamReader getXmlStreamReader(final InputStream in, final String encoding) throws XMLStreamException {
    // Normalize XML tags and attributes to uppercase
    return new AsxStreamReaderDelegate(super.getXmlStreamReader(in, encoding));
  }

  /**
   * Override the XML reader, having effectively a case-insensitive XML reader
   */
  private static class AsxStreamReaderDelegate extends StreamReaderDelegate {

    AsxStreamReaderDelegate(XMLStreamReader xsr) {
      super(xsr);
    }

    @Override
    public String getAttributeLocalName(int index) {
      return super.getAttributeLocalName(index).toUpperCase();
    }

    @Override
    public String getLocalName() {
      return super.getLocalName().toUpperCase();
    }
  }
}
