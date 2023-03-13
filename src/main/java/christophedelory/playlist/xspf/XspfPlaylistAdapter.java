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
package christophedelory.playlist.xspf;

import java.io.OutputStream;

import christophedelory.content.Content;
import christophedelory.playlist.JaxbPlaylistProvider;
import christophedelory.playlist.Media;
import christophedelory.playlist.SpecificPlaylist;

import io.github.borewit.playlist.xspf.ObjectFactory;
import io.github.borewit.playlist.xspf.XspfPlaylist;
import io.github.borewit.playlist.xspf.XspfTrack;


/**
 * XSPF, an XML format designed to enable playlist sharing.
 * @version $Revision: 91 $
 * @author Christophe Delory
 */
public class XspfPlaylistAdapter implements SpecificPlaylist
{

    /**
     * The provider of this specific playlist.
     */
    private final JaxbPlaylistProvider provider;

    private final XspfPlaylist xspfPlaylist;

    public XspfPlaylistAdapter(JaxbPlaylistProvider provider, XspfPlaylist xspfPlaylist) {
        this.provider = provider;
        this.xspfPlaylist = xspfPlaylist;
    }

    @Override
    public JaxbPlaylistProvider getProvider()
    {
        return provider;
    }

    @Override
    public void writeTo(final OutputStream out, final String encoding) throws Exception
    {
        this.provider.writeTo(new ObjectFactory().createPlaylist(xspfPlaylist), out, encoding);
    }

    @Override
    public christophedelory.playlist.Playlist toPlaylist()
    {
        final christophedelory.playlist.Playlist ret = new christophedelory.playlist.Playlist();

        if (xspfPlaylist.getTrackList() != null) {
            for (XspfTrack track : xspfPlaylist.getTrackList().getTrack())
            {
                for (String location : track.getLocation())
                {
                    final Media media = new Media(); // NOPMD Avoid instantiating new objects inside loops
                    final Content content = new Content(location); // NOPMD Avoid instantiating new objects inside loops
                    media.setSource(content);

                    if (track.getDuration() != null)
                    {
                        content.setDuration(track.getDuration().longValue());
                    }

                    ret.getRootSequence().addComponent(media);
                }
            }

            // We don't really need it.
            ret.normalize();
        }
        return ret;
    }

}
