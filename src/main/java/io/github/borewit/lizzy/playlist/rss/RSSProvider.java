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
package io.github.borewit.lizzy.playlist.rss;

import io.github.borewit.lizzy.content.type.ContentType;
import io.github.borewit.lizzy.player.PlayerSupport;
import io.github.borewit.lizzy.playlist.*;
import io.github.borewit.lizzy.xml.Version;
import io.github.borewit.playlist.rss20.Channel;
import io.github.borewit.playlist.rss20.Enclosure;
import io.github.borewit.playlist.rss20.Item;
import io.github.borewit.playlist.rss20.Rss;
import io.github.borewit.playlist.rss20.media.MediaContent;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Date;

/**
 * The RSS (XML) playlist provider.
 *
 * @author Christophe Delory
 * @version $Revision: 92 $
 */
public class RSSProvider extends JaxbPlaylistProvider<Rss>
{
  /**
   * A list of compatible content types.
   */
  private static final ContentType[] FILETYPES =
    {
      new ContentType(new String[]{".rss", ".xml"},
        new String[]{"application/rss+xml"},
        new PlayerSupport[]
          {
          },
        "RSS Document"),
    };

  /**
   * Specifies that the output RSS shall make use of the RSS Media extension (and not of the default enclosure capability).
   */
  private boolean _useRSSMedia = false;

  public RSSProvider()
  {
    super(Rss.class);
  }

  @Override
  public String getId()
  {
    return "rss";
  }

  @Override
  public ContentType[] getContentTypes()
  {
    return FILETYPES.clone();
  }

  @Override
  public SpecificPlaylist readFrom(final InputStream in, final String encoding) throws IOException
  {
    try
    {
      final JAXBElement<Rss> rssJAXBElement = this.unmarshal(in, encoding);
      String rootElementName = rssJAXBElement.getName().getLocalPart();

      return rootElementName != null && rootElementName.equalsIgnoreCase("RSS") ?
        new RSSPlaylist(this, rssJAXBElement.getValue()) : null;
    }
    catch (JAXBException | XMLStreamException exception)
    {
      throw new IOException(exception);
    }
  }

  @Override
  public SpecificPlaylist toSpecificPlaylist(final Playlist playlist) throws IOException
  {
    final Rss rss = new Rss();

    final Channel channel = new Channel();
    rss.setChannel(channel);

    channel.setTitle("Lizzy v" + Version.CURRENT + " RSS playlist");
    channel.setDescription("A list of media contents");
    channel.setLink("http://sourceforge.net/projects/lizzy/"); // May throw URISyntaxException.
    channel.setLanguage("en");
    channel.setCopyright("Copyright (c) 2008-2009, Christophe Delory");
    final String rfc822date = RFC822.toString(new Date());
    channel.setPubDate(rfc822date);
    channel.setLastBuildDate(rfc822date);
    channel.setGenerator("Lizzy v" + Version.CURRENT);

    addToPlaylist(channel, playlist.getRootSequence()); // May throw Exception

    return new RSSPlaylist(this, rss);
  }

  /**
   * Adds the specified generic playlist component, and all its childs if any, to the input RSS channel.
   *
   * @param channel   the parent RSS channel. Shall not be <code>null</code>.
   * @param component the generic playlist component to handle. Shall not be <code>null</code>.
   * @throws NullPointerException if <code>channel</code> is <code>null</code>.
   * @throws NullPointerException if <code>component</code> is <code>null</code>.
   * @throws Exception            if this service provider is unable to represent the input playlist.
   */
  @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
  private void addToPlaylist(final Channel channel, final AbstractPlaylistComponent component)
  {
    if (component instanceof Sequence)
    {
      final Sequence sequence = (Sequence) component;

      if (sequence.getRepeatCount() < 0)
      {
        throw new IllegalArgumentException("A RSS playlist cannot handle a sequence repeated indefinitely");
      }

      for (int iter = 0; iter < sequence.getRepeatCount(); iter++)
      {
        sequence.getComponents().forEach(c -> addToPlaylist(channel, c));
      }
    }
    else if (component instanceof Parallel)
    {
      throw new IllegalArgumentException("A RSS playlist doesn't support concurrent media");
    }
    else if (component instanceof Media)
    {
      final Media media = (Media) component;

      if (media.getDuration() != null)
      {
        throw new IllegalArgumentException("A RSS playlist cannot handle a timed media");
      }

      if (media.getRepeatCount() < 0)
      {
        throw new IllegalArgumentException("A RSS playlist cannot handle a media repeated indefinitely");
      }

      if (media.getSource() != null)
      {
        for (int iter = 0; iter < media.getRepeatCount(); iter++)
        {
          final Item item = new Item();
          String url;

          if (_useRSSMedia)
          {
            final MediaContent content = new MediaContent();
            try
            {
              content.setUrl(media.getSource().getURL().toString()); // May throw SecurityException, URISyntaxException.
            }
            catch (MalformedURLException e)
            {
              throw new RuntimeException(e);
            }
            url = content.getUrl();
            content.setFileSize(media.getSource().getLength());
            content.setType(media.getSource().getType());
            content.setIsDefault(true);

            if (media.getSource().getDuration() >= 0L) // NOPMD Deeply nested if..then statements are hard to read
            {
              content.setDuration((media.getSource().getDuration() + 999L) / 1000L);
            }

            if (media.getSource().getWidth() >= 0) // NOPMD Deeply nested if..then statements are hard to read
            {
              content.setWidth(media.getSource().getWidth());
            }

            if (media.getSource().getHeight() >= 0) // NOPMD Deeply nested if..then statements are hard to read
            {
              content.setHeight(media.getSource().getHeight());
            }

            item.getContent().add(content);
          }
          else
          {
            final Enclosure enclosure = new Enclosure();
            try
            {
              enclosure.setUrl(media.getSource().getURI().toString()); // May throw SecurityException, URISyntaxException.
            }
            catch (URISyntaxException e)
            {
              throw new RuntimeException(e);
            }
            url = enclosure.getUrl();
            enclosure.setLength(media.getSource().getLength());

            if (media.getSource().getType() != null) // NOPMD Deeply nested if..then statements are hard to read
            {
              enclosure.setType(media.getSource().getType());
            }

            item.setEnclosure(enclosure);
          }

          item.setTitle(url);

          channel.getItem().add(item);
        }
      }
    }
  }

  /**
   * Specifies that the output RSS shall make use of the RSS Media extension, or not.
   * The default case is to use the enclosure capability of standard RSS.
   *
   * @param useRSSMedia the associated boolean.
   * @see #toSpecificPlaylist
   */
  public void setUseRSSMedia(final boolean useRSSMedia)
  {
    _useRSSMedia = useRSSMedia;
  }
}
