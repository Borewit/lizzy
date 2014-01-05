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
package christophedelory.playlist.wpl;

import java.io.OutputStream;
import java.io.StringWriter;

import christophedelory.content.Content;
import christophedelory.playlist.Playlist;
import christophedelory.playlist.SpecificPlaylist;
import christophedelory.playlist.SpecificPlaylistProvider;
import christophedelory.xml.XmlSerializer;

/**
 * The smil element is always the top-level element in a Windows Media Playlist (WPL) file.
 * It specifies that the file uses SMIL (Synchronized Multimedia Integration Language) syntax and grammar.
 * Every Windows Media Playlist must have the smil element at its root.
 * <br>
 * Windows Media Player 9 Series or later.
 * @version $Revision: 92 $
 * @author Christophe Delory
 * @castor.class xml="smil"
 */
public class Smil implements SpecificPlaylist
{
    /**
     * The provider of this specific playlist.
     */
    private transient SpecificPlaylistProvider _provider = null;

    /**
     * The header of the SMIL presentation, if existing.
     */
    private Head _header = null;

    /**
     * The body of the SMIL presentation, if existing.
     */
    private Body _body = null;

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
        // Marshal the SMIL playlist.
        final StringWriter writer = new StringWriter();
        final XmlSerializer serializer = XmlSerializer.getMapping("christophedelory/playlist/wpl"); // May throw Exception.
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
    public Playlist toPlaylist()
    {
        final Playlist ret = new Playlist();

        if (_body != null)
        {
            for (Media wplMedia : _body.getSeq().getMedias())
            {
                if (wplMedia.getSource() != null)
                {
                    final christophedelory.playlist.Media media = new christophedelory.playlist.Media(); // NOPMD Avoid instantiating new objects inside loops
                    media.setSource(new Content(wplMedia.getSource())); // NOPMD Avoid instantiating new objects inside loops
                    ret.getRootSequence().addComponent(media);
                }
            }

            ret.normalize();
        }

        return ret;
    }

    /**
     * Returns the header of this SMIL presentation, if any.
     * @return an header element. May be <code>null</code>.
     * @see #setHeader
     * @castor.field
     *  get-method="getHeader"
     *  set-method="setHeader"
     * @castor.field-xml
     *  name="head"
     *  node="element"
     */
    public Head getHeader()
    {
        return _header;
    }

    /**
     * Initializes the header of this SMIL presentation.
     * @param header an header element. May be <code>null</code>.
     * @see #getHeader
     */
    public void setHeader(final Head header)
    {
        _header = header;
    }

    /**
     * Returns the body of this SMIL presentation, if any.
     * @return the body element. May be <code>null</code>.
     * @see #setBody
     * @castor.field
     *  get-method="getBody"
     *  set-method="setBody"
     * @castor.field-xml
     *  name="body"
     *  node="element"
     */
    public Body getBody()
    {
        return _body;
    }

    /**
     * Initializes the body of this SMIL presentation.
     * @param body a body element. May be <code>null</code>.
     * @see #getBody
     */
    public void setBody(final Body body)
    {
        _body = body;
    }
}
