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
package christophedelory.playlist.rss;

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URI;
import java.util.Date;

import org.apache.commons.logging.Log;

import christophedelory.content.type.ContentType;
import christophedelory.io.IOUtils;
import christophedelory.player.PlayerSupport;
import christophedelory.playlist.AbstractPlaylistComponent;
import christophedelory.playlist.Media;
import christophedelory.playlist.Parallel;
import christophedelory.playlist.Playlist;
import christophedelory.playlist.Sequence;
import christophedelory.playlist.SpecificPlaylist;
import christophedelory.playlist.SpecificPlaylistProvider;
import christophedelory.rss.Channel;
import christophedelory.rss.Enclosure;
import christophedelory.rss.Item;
import christophedelory.rss.RSS;
import christophedelory.rss.media.Content;
import christophedelory.xml.Version;
import christophedelory.xml.XmlSerializer;

/**
 * The RSS (XML) playlist provider.
 * @version $Revision: 92 $
 * @author Christophe Delory
 */
public class RSSProvider implements SpecificPlaylistProvider
{
    /**
     * A list of compatible content types.
     */
    private static final ContentType[] FILETYPES =
    {
        new ContentType(new String[] { ".rss", ".xml" },
                        new String[] { "application/rss+xml" },
                        new PlayerSupport[]
                        {
                        },
                        "RSS Document"),
    };

    /**
     * Specifies that the output RSS shall make use of the RSS Media extension (and not of the default enclosure capability).
     */
    private boolean _useRSSMedia = false;

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
    public SpecificPlaylist readFrom(final InputStream in, final String encoding, final Log logger) throws Exception
    {
        String enc = encoding;

        if (enc == null)
        {
            enc = "UTF-8";
        }

        String str = IOUtils.toString(in, enc); // May throw IOException. Throws NullPointerException if in is null.

        // Replace all occurrences of a single '&' with "&amp;" (or leave this construct as is).
        // First replace blindly all '&' to its corresponding character reference.
        str = str.replace("&", "&amp;");
        // Then restore any existing character reference.
        str = str.replaceAll("&amp;([a-zA-Z0-9#]+;)", "&$1"); // Shall not throw PatternSyntaxException.

        // Unmarshal the SMIL playlist.
        final XmlSerializer serializer = XmlSerializer.getMapping("christophedelory/rss"); // May throw Exception.
        serializer.getUnmarshaller().setIgnoreExtraElements(true);

        final StringReader reader = new StringReader(str);
        final RSS rss = (RSS) serializer.unmarshal(reader); // May throw Exception.

        final RSSPlaylist ret = new RSSPlaylist();
        ret.setProvider(this);
        ret.setRSS(rss);

        return ret;
    }

    @Override
    public SpecificPlaylist toSpecificPlaylist(final Playlist playlist) throws Exception
    {
        final RSSPlaylist ret = new RSSPlaylist();
        ret.setProvider(this);

        final Channel channel = ret.getRSS().getChannel();

        channel.setTitle("Lizzy v" + Version.CURRENT + " RSS playlist");
        channel.setDescription("A list of media contents");
        channel.setLinkString("http://sourceforge.net/projects/lizzy/"); // May throw URISyntaxException.
        channel.setLanguage("en");
        channel.setCopyright("Copyright (c) 2008-2009, Christophe Delory");
        channel.setPubDate(new Date());
        channel.setLastBuildDate(new Date());
        channel.setGenerator("Lizzy v" + Version.CURRENT);

        addToPlaylist(channel, playlist.getRootSequence()); // May throw Exception

        return ret;
    }

    /**
     * Adds the specified generic playlist component, and all its childs if any, to the input RSS channel.
     * @param channel the parent RSS channel. Shall not be <code>null</code>.
     * @param component the generic playlist component to handle. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>channel</code> is <code>null</code>.
     * @throws NullPointerException if <code>component</code> is <code>null</code>.
     * @throws Exception if this service provider is unable to represent the input playlist.
     */
    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    private void addToPlaylist(final Channel channel, final AbstractPlaylistComponent component) throws Exception
    {
        if (component instanceof Sequence)
        {
            final Sequence sequence = (Sequence) component;

            if (sequence.getRepeatCount() < 0)
            {
                throw new IllegalArgumentException("A RSS playlist cannot handle a sequence repeated indefinitely");
            }

            final AbstractPlaylistComponent[] components = sequence.getComponents();

            for (int iter = 0; iter < sequence.getRepeatCount(); iter++)
            {
                for (AbstractPlaylistComponent c : components)
                {
                    addToPlaylist(channel, c); // May throw Exception.
                }
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
                    URI url;

                    if (_useRSSMedia)
                    {
                        final Content content = new Content();
                        content.setURL(media.getSource().getURI()); // May throw SecurityException, URISyntaxException.
                        url = content.getURL();
                        content.setFileSize(Long.valueOf(media.getSource().getLength()));
                        content.setType(media.getSource().getType());
                        content.setDefault(true);

                        if (media.getSource().getDuration() >= 0L) // NOPMD Deeply nested if..then statements are hard to read
                        {
                            content.setDuration((int)((media.getSource().getDuration() + 999L) / 1000L));
                        }

                        if (media.getSource().getWidth() >= 0) // NOPMD Deeply nested if..then statements are hard to read
                        {
                            content.setWidth(Integer.valueOf(media.getSource().getWidth()));
                        }

                        if (media.getSource().getHeight() >= 0) // NOPMD Deeply nested if..then statements are hard to read
                        {
                            content.setHeight(Integer.valueOf(media.getSource().getHeight()));
                        }

                        item.addMediaContent(content);
                    }
                    else
                    {
                        final Enclosure enclosure = new Enclosure();
                        enclosure.setURL(media.getSource().getURI()); // May throw SecurityException, URISyntaxException.
                        url = enclosure.getURL();
                        enclosure.setLength(media.getSource().getLength());

                        if (media.getSource().getType() != null) // NOPMD Deeply nested if..then statements are hard to read
                        {
                            enclosure.setType(media.getSource().getType());
                        }

                        item.setEnclosure(enclosure);
                    }

                    if (url.getPath() == null)
                    {
                        item.setTitle(media.getSource().toString());
                    }
                    else
                    {
                        final File path = new File(url.getPath());
                        item.setTitle(path.getName());
                    }

                    channel.addItem(item);
                }
            }
        }
    }

    /**
     * Specifies that the output RSS shall make use of the RSS Media extension, or not.
     * The default case is to use the enclosure capability of standard RSS.
     * @param useRSSMedia the associated boolean.
     * @see #toSpecificPlaylist
     */
    public void setUseRSSMedia(final boolean useRSSMedia)
    {
        _useRSSMedia = useRSSMedia;
    }
}
