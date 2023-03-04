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
package christophedelory.playlist.rmp;

import java.io.InputStream;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import christophedelory.playlist.*;
import org.apache.commons.logging.Log;

import christophedelory.content.type.ContentType;
import christophedelory.io.IOUtils;
import christophedelory.player.PlayerSupport;
import christophedelory.xml.Version;

/**
 * The Real Metadata Package playlist file.
 * @version $Revision: 91 $
 * @author Christophe Delory
 * @since 0.3.0
 */
public class RmpProvider extends AbstractPlaylistProvider
{
    /**
     * A list of compatible content types.
     */
    private static final ContentType[] FILETYPES =
    {
        new ContentType(new String[] { ".rmp" },
                        new String[] { "application/vnd.rn-rn_music_package" },
                        new PlayerSupport[]
                        {
                            new PlayerSupport(PlayerSupport.Player.REALPLAYER, true, null),
                        },
                        "Real Metadata Package (RMP)"),
    };

    public RmpProvider()
    {
        super(RmpProvider.class);
    }

    @Override
    public String getId()
    {
        return "rmp";
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

        // Unmarshal the RMP playlist.
        final StringReader reader = new StringReader(str);
        final JAXBContext jc = JAXBContext.newInstance("christophedelory.playlist.rmp"); // May throw JAXBException.
        final Unmarshaller unmarshaller = jc.createUnmarshaller(); // May throw JAXBException.
        final SpecificPlaylist ret = (SpecificPlaylist) unmarshaller.unmarshal(reader); // May throw JAXBException, UnmarshalException. Shall not throw IllegalArgumentException.
        ret.setProvider(this);

        return ret;
    }

    @Override
    public SpecificPlaylist toSpecificPlaylist(final Playlist playlist) throws Exception
    {
        final Package ret = new Package();
        ret.setProvider(this);

        ret.setTitle("Lizzy v" + Version.CURRENT + " RMP playlist");
        ret.setAction("import,replace");
        ret.setTarget(Integer.toString(System.identityHashCode(ret)));
        ret.getTracklist().setId(Integer.toString(System.identityHashCode(ret.getTracklist())));

        final Provider provider = new Provider();
        provider.setAuthor("Christophe Delory");
        provider.setName("Lizzy v" + Version.CURRENT);
        provider.setUrlString("http://sourceforge.net/projects/lizzy/");
        provider.setCopyright("Copyright (c) 2008-2009, Christophe Delory");
        provider.setContact("cdelory@users.sourceforge.net");
        ret.setPackageProvider(provider);

        // TODO setSignature()?

        addToPlaylist(ret.getTracklist(), playlist.getRootSequence()); // May throw Exception.

        return ret;
    }

    /**
     * Adds the specified generic playlist component, and all its childs if any, to the input track list.
     * @param trackList the parent track list. Shall not be <code>null</code>.
     * @param component the generic playlist component to handle. Shall not be <code>null</code>.
     */
    private void addToPlaylist(final Tracklist trackList, final AbstractPlaylistComponent component)
    {
        if (component instanceof Sequence)
        {
            final Sequence sequence = (Sequence) component;

            if (sequence.getRepeatCount() < 0)
            {
                throw new IllegalArgumentException("A RMP playlist cannot handle a sequence repeated indefinitely");
            }

            final AbstractPlaylistComponent[] components = sequence.getComponents();

            for (int iter = 0; iter < sequence.getRepeatCount(); iter++)
            {
                for (AbstractPlaylistComponent c : components)
                {
                    addToPlaylist(trackList, c); // May throw Exception.
                }
            }
        }
        else if (component instanceof Parallel)
        {
            throw new IllegalArgumentException("A RMP playlist cannot play different media at the same time");
        }
        else if (component instanceof Media)
        {
            final Media media = (Media) component;

            if (media.getDuration() != null)
            {
                throw new IllegalArgumentException("A RMP playlist cannot handle a timed media");
            }

            if (media.getRepeatCount() < 0)
            {
                throw new IllegalArgumentException("A RMP playlist cannot handle a media repeated indefinitely");
            }

            if (media.getSource() != null)
            {
                for (int iter = 0; iter < media.getRepeatCount(); iter++)
                {
                    final Track track = new Track(); // NOPMD Avoid instantiating new objects inside loops
                    track.setId(Integer.toString(System.identityHashCode(track))); // FIXME Why not media.getSource() as id?
                    track.setTitle(media.getSource().toString());
                    track.setFileName(media.getSource().toString());

                    if (media.getSource().getLength() >= 0L) // NOPMD Deeply nested if..then statements are hard to read
                    {
                        track.setSize(media.getSource().getLength()); // Shall not throw IllegalArgumentException.
                    }

                    if (media.getSource().getDuration() >= 0L) // NOPMD Deeply nested if..then statements are hard to read
                    {
                        track.setDuration((int)(media.getSource().getDuration() / 1000L)); // Shall not throw IllegalArgumentException.
                    }

                    // TODO setFormat()?

                    trackList.addTrack(track);
                }
            }
        }
    }
}
