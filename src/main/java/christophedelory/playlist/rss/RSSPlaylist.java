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

import christophedelory.playlist.*;
import io.github.borewit.playlist.rss20.Enclosure;
import io.github.borewit.playlist.rss20.Item;
import io.github.borewit.playlist.rss20.Rss;
import io.github.borewit.playlist.rss20.media.Group;
import io.github.borewit.playlist.rss20.media.MediaContent;
import io.github.borewit.playlist.rss20.ObjectFactory;

/**
 * Playlist to {@link Rss} JAXB object adapter.
 * @author Borewit
 * @author Christophe Delory
 */
public class RSSPlaylist implements SpecificPlaylist
{
    /**
     * The provider of this specific playlist.
     */
    private transient RSSProvider provider = null;

    /**
     * The RSS document itself.
     */
    private final Rss rss;

    public RSSPlaylist(final RSSProvider provider, Rss rss) {
        this.provider = provider;
        this.rss = rss;
    }

    @Override
    public RSSProvider getProvider()
    {
        return provider;
    }

    @Override
    public void writeTo(final OutputStream out, final String encoding) throws Exception
    {
        this.provider.writeTo(new ObjectFactory().createRss(this.rss), out, encoding);
    }

    @Override
    public Playlist toPlaylist()
    {
        final Playlist ret = new Playlist();

        for (Item item : rss.getChannel().getItem())
        {
            final Enclosure enclosure = item.getEnclosure();

            if ((enclosure == null) || (enclosure.getUrl() == null))
            {
                for (Group mediaGroup : item.getGroup())
                {
                    boolean foundOne = false;

                    // First search for the default one.
                    for (MediaContent mediaContent : mediaGroup.getContent())
                    {
                        // Put only the first valid one in this case.
                        if (mediaContent.isIsDefault() && addMediaContent(mediaContent, ret.getRootSequence()))
                        {
                            foundOne = true;
                            break;
                        }
                    }

                    if (!foundOne)
                    {
                        for (MediaContent mediaContent : mediaGroup.getContent())
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

                for (MediaContent mediaContent : item.getContent())
                {
                    addMediaContent(mediaContent, ret.getRootSequence());
                }
            }
            else
            {
                final Media media = new Media(); // NOPMD Avoid instantiating new objects inside loops
                final christophedelory.content.Content content = new christophedelory.content.Content(enclosure.getUrl()); // NOPMD Avoid instantiating new objects inside loops
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
    private boolean addMediaContent(final MediaContent mediaContent, final Sequence sequence)
    {
        boolean ret = false;

        if (mediaContent.getUrl() != null) // NOPMD Avoid if (x != y) ..; else ..;
        {
            final Media media = new Media();
            final christophedelory.content.Content content = new christophedelory.content.Content(mediaContent.getUrl());
            content.setType(mediaContent.getType()); // May be null.

            if (mediaContent.getFileSize() != null)
            {
                content.setLength(mediaContent.getFileSize());
            }

            if (mediaContent.getDuration() != null)
            {
                content.setDuration(mediaContent.getDuration() * 1000L);
            }

            if (mediaContent.getWidth() != null)
            {
                content.setWidth(mediaContent.getWidth()); // Even if negative.
            }

            if (mediaContent.getHeight() != null)
            {
                content.setHeight(mediaContent.getHeight()); // Even if negative.
            }

            media.setSource(content);
            sequence.addComponent(media);

            ret = true;
        }
        else if ((mediaContent.getPlayer() != null) && (mediaContent.getPlayer().getUrl() != null))
        {
            final Media media = new Media();
            final christophedelory.content.Content content = new christophedelory.content.Content(mediaContent.getPlayer().getUrl());
            content.setType(mediaContent.getType()); // May be null.

            if (mediaContent.getFileSize() != null)
            {
                content.setLength(mediaContent.getFileSize());
            }

            if (mediaContent.getDuration() != null)
            {
                content.setDuration(mediaContent.getDuration() * 1000L);
            }

            media.setSource(content);
            sequence.addComponent(media);

            ret = true;
        }

        return ret;
    }
}
