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

import java.io.InputStream;

import christophedelory.playlist.*;
import org.apache.commons.logging.Log;

import christophedelory.content.type.ContentType;
import christophedelory.player.PlayerSupport;
import christophedelory.xml.XmlSerializer;

/**
 * WPL (Windows Media Player Playlist) is a proprietary XML file format used in Microsoft Windows Media Player versions 9-11.
 * @version $Revision: 91 $
 * @author Christophe Delory
 */
public class WplProvider extends AbstractPlaylistProvider
{
    /**
     * A list of compatible content types.
     */
    private static final ContentType[] FILETYPES =
    {
        new ContentType(new String[] { ".wpl" },
                        new String[] { "application/vnd.ms-wpl" },
                        new PlayerSupport[]
                        {
                            new PlayerSupport(PlayerSupport.Player.WINAMP, false, null),
                            new PlayerSupport(PlayerSupport.Player.WINDOWS_MEDIA_PLAYER, true, null),
                        },
                        "Windows Media Player Playlist (WPL)"),
    };

    public WplProvider()
    {
        super(WplProvider.class);
    }

    @Override
    public String getId()
    {
        return "wpl";
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
        final XmlSerializer serializer = XmlSerializer.getMapping("christophedelory/playlist/wpl"); // May throw Exception.
        serializer.getUnmarshaller().setIgnoreExtraElements(false); // Force an error if unknown elements are found.

        final Smil ret = (Smil) serializer.unmarshal(preProcessXml(in, encoding)); // May throw Exception.
        ret.setProvider(this);

        return ret;
    }

    @Override
    public SpecificPlaylist toSpecificPlaylist(final Playlist playlist) throws Exception
    {
        final Smil ret = new Smil();
        ret.setProvider(this);

        final Body body = new Body();
        ret.setBody(body);

        addToPlaylist(body.getSeq(), playlist.getRootSequence()); // May throw Exception.

        final Head header = new Head();
        ret.setHeader(header);
        final Meta meta = new Meta();
        meta.setName("ItemCount");
        meta.setContent(Integer.toString(body.getSeq().getMedias().size()));
        header.addMeta(meta);

        return ret;
    }

    /**
     * Adds the specified generic playlist component, and all its childs if any, to the input sequence.
     * @param wplSeq the parent sequence. Shall not be <code>null</code>.
     * @param component the generic playlist component to handle. Shall not be <code>null</code>.
     */
    private void addToPlaylist(final Seq wplSeq, final AbstractPlaylistComponent component)
    {
        if (component instanceof Sequence)
        {
            final Sequence sequence = (Sequence) component;

            if (sequence.getRepeatCount() < 0)
            {
                throw new IllegalArgumentException("A WPL playlist cannot handle a sequence repeated indefinitely");
            }

            final AbstractPlaylistComponent[] components = sequence.getComponents();

            for (int iter = 0; iter < sequence.getRepeatCount(); iter++)
            {
                for (AbstractPlaylistComponent c : components)
                {
                    addToPlaylist(wplSeq, c); // May throw Exception.
                }
            }
        }
        else if (component instanceof Parallel)
        {
            throw new IllegalArgumentException("A WPL playlist cannot play media at the same time");
        }
        else if (component instanceof christophedelory.playlist.Media)
        {
            final christophedelory.playlist.Media media = (christophedelory.playlist.Media) component;

            if (media.getDuration() != null)
            {
                throw new IllegalArgumentException("A WPL playlist cannot handle a timed media");
            }

            if (media.getRepeatCount() < 0)
            {
                throw new IllegalArgumentException("A WPL playlist cannot handle a media repeated indefinitely");
            }

            if (media.getSource() != null)
            {
                for (int iter = 0; iter < media.getRepeatCount(); iter++)
                {
                    final Media wplMedia = new Media(); // NOPMD Avoid instantiating new objects inside loops
                    wplMedia.setSource(media.getSource().toString());
                    wplSeq.addMedia(wplMedia);
                }
            }
        }
    }
}
