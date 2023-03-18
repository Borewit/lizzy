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

import java.io.IOException;
import java.io.OutputStream;

import christophedelory.content.Content;
import christophedelory.playlist.Media;
import christophedelory.playlist.Playlist;
import christophedelory.playlist.SpecificPlaylist;
import christophedelory.playlist.SpecificPlaylistProvider;
import io.github.borewit.playlist.b4s.WinampXML;

/**
 * The Winamp 3+ B4S playlist.
 * @author Borewit
 * @author Christophe Delory
 */
public class WinampXmlAdapter implements SpecificPlaylist
{
    private final WinampXML winampXML;

    /**
     * The provider of this specific playlist.
     */
    private final WinampXmlProvider provider;

    public WinampXmlAdapter(WinampXmlProvider provider, WinampXML winampXML) {
        this.provider = provider;
        this.winampXML = winampXML;
    }

    @Override
    public SpecificPlaylistProvider getProvider()
    {
        return provider;
    }

    @Override
    public void writeTo(final OutputStream out, final String encoding) throws IOException
    {
        this.provider.writeTo(this.winampXML, out, encoding);
    }

    @Override
    public Playlist toPlaylist()
    {
        final christophedelory.playlist.Playlist ret = new christophedelory.playlist.Playlist();

        if (this.winampXML.getPlaylist() != null)
        {
            for (WinampXML.Playlist.Entry entry : this.winampXML.getPlaylist().getEntry())
            {
                if (entry.getPlaystring() != null)
                {
                    final Media media = new Media(); // NOPMD Avoid instantiating new objects inside loops
                    final Content content = new Content(entry.getPlaystring()); // NOPMD Avoid instantiating new objects inside loops

                    if (entry.getLength() != null)
                    {
                        content.setLength(entry.getLength());
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
}
