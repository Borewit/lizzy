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

import java.io.OutputStream;
import java.io.StringWriter;

import christophedelory.playlist.Media;
import christophedelory.playlist.Playlist;
import christophedelory.playlist.Sequence;
import christophedelory.playlist.SpecificPlaylist;
import christophedelory.playlist.SpecificPlaylistProvider;
import christophedelory.rss.Enclosure;
import christophedelory.rss.Item;
import christophedelory.rss.RSS;
import christophedelory.rss.media.Content;
import christophedelory.rss.media.Group;
import christophedelory.xml.XmlSerializer;

/**
 * A {@link RSS} document wrapper.
 * @version $Revision: 92 $
 * @author Christophe Delory
 */
public class RSSPlaylist implements SpecificPlaylist
{
    /**
     * The provider of this specific playlist.
     */
    private transient SpecificPlaylistProvider _provider = null;

    /**
     * The RSS document itself.
     */
    private RSS _rss = new RSS();

    @Override
    public void setProvider(final SpecificPlaylistProvider provider)
    {
        _provider = provider;
    }

    @Override
    public SpecificPlaylistProvider getProvider()
    {
        return _provider;
    }

    @Override
    public void writeTo(final OutputStream out, final String encoding) throws Exception
    {
        // Marshal the RSS document.
        final StringWriter writer = new StringWriter();
        final XmlSerializer serializer = XmlSerializer.getMapping("christophedelory/rss"); // May throw Exception.
        // Specifies whether XML documents (as generated at marshalling) should use indentation or not. Default is false.
        serializer.getMarshaller().setProperty("org.exolab.castor.indent", "true");
        //serializer.getMarshaller().setNamespaceMapping("", "http://purl.org/rss/1.0/modules/content/");
        serializer.getMarshaller().setNamespaceMapping("media", "http://search.yahoo.com/mrss/");
        serializer.marshal(_rss, writer, false); // May throw Exception.

        String enc = encoding;

        if (enc == null)
        {
            enc = "UTF-8";
        }

        final byte[] bytes = writer.toString().getBytes(enc); // May throw UnsupportedEncodingException.
        out.write(bytes); // Throws NullPointerException if out is null. May throw IOException.
        out.flush(); // May throw IOException.
    }

    @Override
    public Playlist toPlaylist()
    {
        final Playlist ret = new Playlist();

        for (Item item : _rss.getChannel().getItems())
        {
            final Enclosure enclosure = item.getEnclosure();

            if ((enclosure == null) || (enclosure.getURL() == null))
            {
                for (Group mediaGroup : item.getMediaGroups())
                {
                    boolean foundOne = false;

                    // First search for the default one.
                    for (Content mediaContent : mediaGroup.getMediaContents())
                    {
                        // Put only the first valid one in this case.
                        if (mediaContent.isDefault() && addMediaContent(mediaContent, ret.getRootSequence()))
                        {
                            foundOne = true;
                            break;
                        }
                    }

                    if (!foundOne)
                    {
                        for (Content mediaContent : mediaGroup.getMediaContents())
                        {
                            // Put only the first valid one.
                            if (addMediaContent(mediaContent, ret.getRootSequence()))
                            {
                                foundOne = true;
                                break;
                            }
                        }
                    }
                }

                for (Content mediaContent : item.getMediaContents())
                {
                    addMediaContent(mediaContent, ret.getRootSequence());
                }
            }
            else
            {
                final Media media = new Media(); // NOPMD Avoid instantiating new objects inside loops
                final christophedelory.content.Content content = new christophedelory.content.Content(enclosure.getURL()); // NOPMD Avoid instantiating new objects inside loops
                content.setLength(enclosure.getLength());
                content.setType(enclosure.getType());
                media.setSource(content);
                ret.getRootSequence().addComponent(media);
            }
        }

        // We don't really need it.
        ret.normalize();

        return ret;
    }

    /**
     * Adds the specified media content to the given sequence.
     * @param mediaContent the media content to add. Shall not be <code>null</code>.
     * @param sequence the parent sequence. Shall not be <code>null</code>.
     * @return <code>true</code> if a media has effectively been added to the sequence.
     * @throws NullPointerException if <code>mediaContent</code> is <code>null</code>.
     * @throws NullPointerException if <code>sequence</code> is <code>null</code>.
     */
    private boolean addMediaContent(final Content mediaContent, final Sequence sequence)
    {
        boolean ret = false;

        if (mediaContent.getURL() != null) // NOPMD Avoid if (x != y) ..; else ..;
        {
            final Media media = new Media();
            final christophedelory.content.Content content = new christophedelory.content.Content(mediaContent.getURL());
            content.setType(mediaContent.getType()); // May be null.

            if (mediaContent.getFileSize() != null)
            {
                content.setLength(mediaContent.getFileSize().longValue());
            }

            if (mediaContent.getDuration() != null)
            {
                content.setDuration(mediaContent.getDuration().longValue() * 1000L);
            }

            if (mediaContent.getWidth() != null)
            {
                content.setWidth(mediaContent.getWidth().intValue()); // Even if negative.
            }

            if (mediaContent.getHeight() != null)
            {
                content.setHeight(mediaContent.getHeight().intValue()); // Even if negative.
            }

            media.setSource(content);
            sequence.addComponent(media);

            ret = true;
        }
        else if ((mediaContent.getMediaPlayer() != null) && (mediaContent.getMediaPlayer().getURL() != null))
        {
            final Media media = new Media();
            final christophedelory.content.Content content = new christophedelory.content.Content(mediaContent.getMediaPlayer().getURL());
            content.setType(mediaContent.getType()); // May be null.

            if (mediaContent.getFileSize() != null)
            {
                content.setLength(mediaContent.getFileSize().longValue());
            }

            if (mediaContent.getDuration() != null)
            {
                content.setDuration(mediaContent.getDuration().longValue() * 1000L);
            }

            media.setSource(content);
            sequence.addComponent(media);

            ret = true;
        }

        return ret;
    }

    /**
     * Returns the RSS document itself.
     * @return a RSS element. Shall not be <code>null</code>.
     * @see #setRSS
     */
    public RSS getRSS()
    {
        return _rss;
    }

    /**
     * Initializes the RSS document itself.
     * @param rss a RSS element. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>rss</code> is <code>null</code>.
     * @see #getRSS
     */
    public void setRSS(final RSS rss)
    {
        if (rss == null)
        {
            throw new NullPointerException("No RSS document");
        }

        _rss = rss;
    }
}
