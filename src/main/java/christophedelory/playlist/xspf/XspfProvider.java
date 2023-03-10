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
package christophedelory.playlist.xspf;

import java.io.InputStream;
import java.io.StringReader;

import christophedelory.playlist.*;
import org.apache.commons.logging.Log;

import christophedelory.content.type.ContentType;
import christophedelory.player.PlayerSupport;
import christophedelory.xml.XmlSerializer;

/**
 * XML Shareable Playlist Format (XSPF), pronounced spiff, is an XML-based playlist format for digital media, sponsored by the Xiph.Org Foundation.
 * Lucas Gonze of Yahoo.com/Webjay.org originated the format in 2004.
 * XSPF is a data format for sharing the kind of playlist that can be played on a personal computer or portable device.
 * In the same way that any user on any computer can open any web page, XSPF is intended to provide portability for playlists.
 * @version $Revision: 91 $
 * @author Christophe Delory
 */
public class XspfProvider extends AbstractPlaylistProvider
{
    /**
     * A list of compatible content types.
     */
    private static final ContentType[] FILETYPES =
    {
        new ContentType(new String[] { ".xspf" },
                        new String[] { "application/xspf+xml" },
                        new PlayerSupport[]
                        {
                            new PlayerSupport(PlayerSupport.Player.VLC_MEDIA_PLAYER, true, null),
                        },
                        "XML Shareable Playlist Format (XSPF)"),
    };

    public XspfProvider()
    {
        super(XspfProvider.class);
    }

    @Override
    public String getId()
    {
        return "xspf";
    }

    @Override
    public ContentType[] getContentTypes()
    {
        return FILETYPES.clone();
    }

    @Override
    public SpecificPlaylist readFrom(final InputStream in, final String encoding, final Log logger) throws Exception
    {
        // Unmarshal the WPL playlist.
        final XmlSerializer serializer = XmlSerializer.getMapping("christophedelory/playlist/xspf"); // May throw Exception.
        serializer.getUnmarshaller().setIgnoreExtraElements(true);

        final StringReader reader = new StringReader(preProcessXml(in, encoding));
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
     */
    private void addToPlaylist(final Playlist playlist, final AbstractPlaylistComponent component)
    {
        if (component instanceof Sequence)
        {
            final Sequence sequence = (Sequence) component;

            if (sequence.getRepeatCount() < 0)
            {
                throw new IllegalArgumentException("An XSPF playlist cannot handle a sequence repeated indefinitely");
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
            throw new IllegalArgumentException("An XSPF playlist cannot play different media at the same time");
        }
        else if (component instanceof Media)
        {
            final Media media = (Media) component;

            if (media.getDuration() != null)
            {
                throw new IllegalArgumentException("An XSPF playlist cannot handle a timed media");
            }

            if (media.getRepeatCount() < 0)
            {
                throw new IllegalArgumentException("An XSPF playlist cannot handle a media repeated indefinitely");
            }

            if (media.getSource() != null)
            {
                for (int iter = 0; iter < media.getRepeatCount(); iter++)
                {
                    final Track track = new Track(); // NOPMD Avoid instantiating new objects inside loops
                    final Location location = new Location(); // NOPMD Avoid instantiating new objects inside loops
                    location.setText(media.getSource().toString());
                    track.addStringContainer(location);

                    if (media.getSource().getDuration() > 0L) // NOPMD Deeply nested if..then statements are hard to read
                    {
                        track.setDuration((int) media.getSource().getDuration());
                    }

                    playlist.addTrack(track);
                }
            }
        }
    }
}
