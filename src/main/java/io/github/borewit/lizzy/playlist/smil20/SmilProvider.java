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
package io.github.borewit.lizzy.playlist.smil20;

import io.github.borewit.lizzy.content.type.ContentType;
import io.github.borewit.lizzy.player.PlayerSupport;
import io.github.borewit.lizzy.playlist.*;
import io.github.borewit.lizzy.playlist.xml.smil20.*;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;
import java.io.IOException;
import java.io.InputStream;

/**
 * The W3C SMIL playlist XML format.
 * An XML recommendation of the World Wide Web Consortium that includes playlist features.
 *
 * @author Borewit
 * @author Christophe Delory
 */
public class SmilProvider extends JaxbPlaylistProvider<Smil>
{
  public static final String smilNamespace = "http://www.w3.org/2001/SMIL20/Language";

  /**
   * A list of compatible content types.
   */
  private static final ContentType[] FILETYPES =
    {
      new ContentType(new String[]{".smil", ".smi"},
        new String[]{"application/smil+xml", "application/smil"},
        new PlayerSupport[]
          {
            new PlayerSupport(PlayerSupport.Player.MEDIA_PLAYER_CLASSIC, false, null),
            new PlayerSupport(PlayerSupport.Player.QUICKTIME, true, null),
            new PlayerSupport(PlayerSupport.Player.REALPLAYER, false, null),
          },
        "Synchronized Multimedia Integration Language (SMIL)"),
    };

  public SmilProvider()
  {
    super(Smil.class);
  }

  @Override
  public String getId()
  {
    return "smil";
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
      final JAXBElement<Smil> smil = this.unmarshal(inputStream);
      String rootElementName = smil.getName().getLocalPart();

      return rootElementName != null && rootElementName.equalsIgnoreCase("smil") ?
        new SmilAdapter(this, smil.getValue()) : null;
    }
    catch (JAXBException |
           XMLStreamException exception)

    {
      throw new IOException(exception);
    }
  }

  @Override
  public SpecificPlaylist toSpecificPlaylist(final Playlist playlist)
  {
    final Smil smil = new Smil();
    final SmilContainerBody body = new SmilContainerBody();
    smil.setBody(body);
    final SmilSequence smilSequence = new SmilSequence();
    body.getSeqOrParOrExl().add(smilSequence);

    addToPlaylist(smilSequence, playlist.getRootSequence());

    return new SmilAdapter(this, smil);
  }

  /**
   * Adds the specified generic playlist component, and all its childs if any, to the input time container.
   *
   * @param timingElement the parent time container. Shall not be <code>null</code>.
   * @param component     the generic playlist component to handle. Shall not be <code>null</code>.
   * @throws NullPointerException if <code>timingElement</code> is <code>null</code>.
   * @throws NullPointerException if <code>component</code> is <code>null</code>.
   */
  private void addToPlaylist(final SmilTimeContainer timingElement, final AbstractPlaylistComponent component)
  {
    if (component instanceof AbstractTimeContainer)
    {
      AbstractTimeContainer timeContainer = (AbstractTimeContainer) component;
      SmilTimeContainer smilTimeContainer;
      if (component instanceof Sequence)
      {
        smilTimeContainer = new SmilSequence();
      }
      else if (component instanceof Parallel)
      {
        smilTimeContainer = new SmilParallel();
      }
      else
      {
        throw new RuntimeException("Unexpected AbstractTimeContainer instance");
      }
      smilTimeContainer.setRepeatCount(timeContainer.getRepeatCount());
      timingElement.getSeqOrParOrExl().add(smilTimeContainer);

      // Recursion
      timeContainer.getComponents().forEach(c -> addToPlaylist(smilTimeContainer, c));
    }
    else if (component instanceof Media)
    {
      final Media media = (Media) component;
      final SmilReference ref = new SmilReference();

      if (media.getSource() != null)
      {
        ref.setSrc(media.getSource().toString());
        ref.setType(media.getSource().getType()); // May be null.
      }

      ref.setDur(media.getDuration());
      ref.setRepeatCount(media.getRepeatCount());
      timingElement.getAudioOrImgOrRef().add(ref);
    }
    else
    {
      throw new RuntimeException("Unexpected component instance type");
    }
  }

  /**
   * Overrides XMLStreamReader in such a way that we can parse both qualified and unqualified SMIL
   */
  @Override
  protected XMLStreamReader getXmlStreamReader(final InputStream in, final String encoding) throws XMLStreamException
  {
    // Normalize XML tags and attributes to uppercase
    return new NormalizeNamespace(super.getXmlStreamReader(in, encoding));
  }

  /**
   * Use SMIL namespace if there is no namespace defined
   */
  private static class NormalizeNamespace extends StreamReaderDelegate
  {
    NormalizeNamespace(XMLStreamReader xsr)
    {
      super(xsr);
    }

    @Override
    public String getNamespaceURI()
    {
      final String nsUri = super.getNamespaceURI();
      return nsUri == null ? smilNamespace : nsUri;
    }
  }
}
