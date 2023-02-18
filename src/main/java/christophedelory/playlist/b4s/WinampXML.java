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
package christophedelory.playlist.b4s;

import java.io.OutputStream;
import java.io.StringWriter;

import christophedelory.content.Content;
import christophedelory.playlist.Media;
import christophedelory.playlist.SpecificPlaylist;
import christophedelory.playlist.SpecificPlaylistProvider;
import christophedelory.xml.XmlSerializer;

/**
 * The Winamp 3+ B4S playlist.
 * @version $Revision: 92 $
 * @author Christophe Delory
 * @castor.class xml="WinampXML"
 */
public class WinampXML implements SpecificPlaylist
{
    /**
     * The provider of this specific playlist.
     */
    private transient SpecificPlaylistProvider _provider = null;

    /**
     * The playlist itself.
     */
    private Playlist _playlist = new Playlist();

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
        // Marshal the B4S playlist.
        final StringWriter writer = new StringWriter();
        final XmlSerializer serializer = XmlSerializer.getMapping("christophedelory/playlist/b4s"); // May throw Exception.
        // Specifies whether XML documents (as generated at marshalling) should use indentation or not. Default is false.
        serializer.getMarshaller().setProperty("org.exolab.castor.indent", "true");
        serializer.marshal(this, writer, false); // May throw Exception.

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
    public christophedelory.playlist.Playlist toPlaylist()
    {
        final christophedelory.playlist.Playlist ret = new christophedelory.playlist.Playlist();

        for (Entry entry : _playlist.getEntries())
        {
            if (entry.getPlaystring() != null)
            {
                final Media media = new Media(); // NOPMD Avoid instantiating new objects inside loops
                final Content content = new Content(entry.getPlaystring()); // NOPMD Avoid instantiating new objects inside loops

                if (entry.getLength() != null)
                {
                    content.setLength(entry.getLength().longValue());
                }

                media.setSource(content);
                ret.getRootSequence().addComponent(media);
            }
        }

        // We don't really need it.
        ret.normalize();

        return ret;
    }

    /**
     * Returns the playlist itself.
     * @return a playlist. Shall not be <code>null</code>.
     * @see #setPlaylist
     * @castor.field
     *  get-method="getPlaylist"
     *  set-method="setPlaylist"
     *  required="true"
     * @castor.field-xml
     *  name="playlist"
     *  node="element"
     */
    public Playlist getPlaylist()
    {
        return _playlist;
    }

    /**
     * Initializes the playlist itself.
     * @param playlist a playlist. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>playlist</code> is <code>null</code>.
     * @see #getPlaylist
     */
    public void setPlaylist(final Playlist playlist)
    {
        if (playlist == null)
        {
            throw new NullPointerException("no playlist");
        }

        _playlist = playlist;
    }
}
