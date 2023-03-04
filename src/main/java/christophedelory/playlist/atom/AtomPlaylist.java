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
package christophedelory.playlist.atom;

import christophedelory.atom.Entry;
import christophedelory.atom.Feed;
import christophedelory.atom.Link;
import christophedelory.content.Content;
import christophedelory.playlist.Media;
import christophedelory.playlist.Playlist;
import christophedelory.playlist.SpecificPlaylist;
import christophedelory.playlist.SpecificPlaylistProvider;
import christophedelory.xml.XmlSerializer;
import java.io.OutputStream;
import java.io.StringWriter;

/**
 * An Atom {@link Feed} document wrapper.
 * @version $Revision: 92 $
 * @author Christophe Delory
 */
public class AtomPlaylist implements SpecificPlaylist
{
    /**
     * The provider of this specific playlist.
     */
    private transient SpecificPlaylistProvider _provider = null;

    /**
     * The feed document itself.
     */
    private Feed _feed = new Feed();

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
        // Marshal the document.
        final StringWriter writer = new StringWriter();
        final XmlSerializer serializer = XmlSerializer.getMapping("christophedelory/atom"); // May throw Exception.
        // Specifies whether XML documents (as generated at marshalling) should use indentation or not. Default is false.
        serializer.getMarshaller().setProperty("org.exolab.castor.indent", "true");
        //serializer.getMarshaller().setNamespaceMapping("", "http://www.w3.org/2005/Atom");
        serializer.marshal(_feed, writer, false); // May throw Exception.

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

        for (Entry entry : _feed.getEntries())
        {
            for (Link link : entry.getLinks())
            {
                if ((link.getHref() != null) && link.getRel().equals("enclosure"))
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

    /**
     * Returns the Atom feed document itself.
     * @return a feed element. Shall not be <code>null</code>.
     * @see #setFeed
     */
    public Feed getFeed()
    {
        return _feed;
    }

    /**
     * Initializes the Atom feed document itself.
     * @param feed a feed element. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>feed</code> is <code>null</code>.
     * @see #getFeed
     */
    public void setFeed(final Feed feed)
    {
        if (feed == null)
        {
            throw new NullPointerException("No Atom Feed Document");
        }

        _feed = feed;
    }
}
