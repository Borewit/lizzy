/*
 * Copyright (c) 2008-2009, Christophe Delory
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
package christophedelory.playlist.hypetape;

import java.io.InputStream;
import java.io.StringReader;

import christophedelory.playlist.*;
import christophedelory.playlist.b4s.B4sProvider;
import org.apache.commons.logging.Log;

import christophedelory.content.type.ContentType;
import christophedelory.io.IOUtils;
import christophedelory.player.PlayerSupport;
import christophedelory.xml.XmlSerializer;
import org.apache.commons.logging.LogFactory;

/**
 * The Hypetape XML playlist format.
 * @version $Revision: 91 $
 * @author Christophe Delory
 */
public class HypetapeProvider extends AbstractPlaylistProvider
{
    /**
     * A list of compatible content types.
     */
    private static final ContentType[] FILETYPES =
    {
        new ContentType(new String[] { ".hypetape", ".xml" },
                        new String[] { "text/xml" },
                        new PlayerSupport[]
                        {
                        },
                        "Hypetape XML Playlist Format"),
    };

    public HypetapeProvider()
    {
        super(HypetapeProvider.class);
    }

    @Override
    public String getId()
    {
        return "hypetape";
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

        // Unmarshal the WPL playlist.
        final XmlSerializer serializer = XmlSerializer.getMapping("christophedelory/playlist/hypetape"); // May throw Exception.
        serializer.getUnmarshaller().setIgnoreExtraElements(false);

        final StringReader reader = new StringReader(str);
        final SpecificPlaylist ret = (SpecificPlaylist) serializer.unmarshal(reader); // May throw Exception.
        ret.setProvider(this);

        return ret;
    }

    @Override
    public SpecificPlaylist toSpecificPlaylist(final christophedelory.playlist.Playlist playlist) throws Exception
    {
        final Playlist ret = new Playlist();
        ret.setProvider(this);

        addToPlaylist(ret, playlist.getRootSequence()); // May throw Exception.

        return ret;
    }

    /**
     * Adds the specified generic playlist component, and all its childs if any, to the input track list.
     * @param playlist the parent playlist. Shall not be <code>null</code>.
     * @param component the generic playlist component to handle. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>playlist</code> is <code>null</code>.
     * @throws NullPointerException if <code>component</code> is <code>null</code>.
     * @throws Exception if this service provider is unable to represent the input playlist.
     */
    private void addToPlaylist(final Playlist playlist, final AbstractPlaylistComponent component) throws Exception
    {
        if (component instanceof Sequence)
        {
            final Sequence sequence = (Sequence) component;

            if (sequence.getRepeatCount() < 0)
            {
                throw new IllegalArgumentException("A Hypetape playlist cannot handle a sequence repeated indefinitely");
            }

            final AbstractPlaylistComponent[] components = sequence.getComponents();

            for (int iter = 0; iter < sequence.getRepeatCount(); iter++)
            {
                for (AbstractPlaylistComponent c : components)
                {
                    addToPlaylist(playlist, c); // May throw Exception.
                }
            }
        }
        else if (component instanceof Parallel)
        {
            throw new IllegalArgumentException("A Hypetape playlist cannot play different media at the same time");
        }
        else if (component instanceof Media)
        {
            final Media media = (Media) component;

            if (media.getDuration() != null)
            {
                throw new IllegalArgumentException("A Hypetape playlist cannot handle a timed media");
            }

            if (media.getRepeatCount() < 0)
            {
                throw new IllegalArgumentException("A Hypetape playlist cannot handle a media repeated indefinitely");
            }

            if (media.getSource() != null)
            {
                for (int iter = 0; iter < media.getRepeatCount(); iter++)
                {
                    final Track track = new Track(); // NOPMD Avoid instantiating new objects inside loops
                    track.setId(Integer.toString(System.identityHashCode(media.getSource())));
                    track.setName(media.getSource().toString());
                    track.setMP3(media.getSource().toString());
                    playlist.addTrack(track);
                }
            }
        }
    }
}
