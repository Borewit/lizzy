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

import christophedelory.content.type.ContentType;
import christophedelory.player.PlayerSupport;
import christophedelory.playlist.*;
import io.github.borewit.playlist.xspf.XspfPlaylist;
import io.github.borewit.playlist.xspf.XspfTrack;
import io.github.borewit.playlist.xspf.XspfTrackList;

import jakarta.xml.bind.JAXBElement;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.List;

/**
 * XML Shareable Playlist Format (XSPF), pronounced spiff, is an XML-based playlist format for digital media, sponsored by the Xiph.Org Foundation.
 * Lucas Gonze of Yahoo.com/Webjay.org originated the format in 2004.
 * XSPF is a data format for sharing the kind of playlist that can be played on a personal computer or portable device.
 * In the same way that any user on any computer can open any web page, XSPF is intended to provide portability for playlists.
 *
 * @author Christophe Delory
 * @version $Revision: 91 $
 */
public class XspfProvider extends JaxbPlaylistProvider<XspfPlaylist>
{
    /**
     * A list of compatible content types.
     */
    private static final ContentType[] FILETYPES =
        {
            new ContentType(new String[]{".xspf"},
                new String[]{"application/xspf+xml"},
                new PlayerSupport[]
                    {
                        new PlayerSupport(PlayerSupport.Player.VLC_MEDIA_PLAYER, true, null),
                    },
                "XML Shareable Playlist Format (XSPF)"),
        };

    public XspfProvider()
    {
        super(XspfPlaylist.class);
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
    public SpecificPlaylist readFrom(final InputStream in, final String encoding) throws Exception
    {
        final JAXBElement<XspfPlaylist> xspfPlaylist = this.unmarshal(in, encoding);
        String rootElementName = xspfPlaylist.getName().getLocalPart();
        return rootElementName != null && rootElementName.equals("playlist") ?
            new XspfPlaylistAdapter(this, xspfPlaylist.getValue()) : null;
    }

    @Override
    public SpecificPlaylist toSpecificPlaylist(final christophedelory.playlist.Playlist playlist) throws Exception
    {
        XspfTrackList xspfTrackList = new XspfTrackList();
        addToPlaylist(xspfTrackList.getTrack(), playlist.getRootSequence()); // May throw Exception.
        XspfPlaylist xspfPlaylist = new XspfPlaylist();
        xspfPlaylist.setTrackList(xspfTrackList);

        return new XspfPlaylistAdapter(this, xspfPlaylist);
    }

    /**
     * Adds the specified generic playlist component, and all its childs if any, to the input track list.
     *
     * @param xspfTrackList the parent playlist. Shall not be <code>null</code>.
     * @param component     the generic playlist component to handle. Shall not be <code>null</code>.
     */
    private void addToPlaylist(List<XspfTrack> xspfTrackList, final AbstractPlaylistComponent component)
    {
        if (component instanceof Sequence)
        {
            final Sequence sequence = (Sequence) component;

            if (sequence.getRepeatCount() < 0)
            {
                throw new IllegalArgumentException("An XSPF playlist cannot handle a sequence repeated indefinitely");
            }

            for (int iter = 0; iter < sequence.getRepeatCount(); iter++)
            {
                sequence.getComponents().forEach(c -> addToPlaylist(xspfTrackList, c));
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
                    final XspfTrack track = new XspfTrack(); // NOPMD Avoid instantiating new objects inside loops
                    track.getLocation().add(media.getSource().toString());

                    if (media.getSource().getDuration() > 0L) // NOPMD Deeply nested if..then statements are hard to read
                    {
                        track.setDuration(BigInteger.valueOf(media.getSource().getDuration()));
                    }
                    xspfTrackList.add(track);
                }
            }
        }
    }
}
