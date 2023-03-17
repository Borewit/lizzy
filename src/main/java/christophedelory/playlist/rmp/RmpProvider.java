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

import christophedelory.content.type.ContentType;
import christophedelory.player.PlayerSupport;
import christophedelory.playlist.*;
import christophedelory.xml.Version;
import io.github.borewit.playlist.rmp.RmpPackage;

import jakarta.xml.bind.JAXBElement;
import java.io.InputStream;
import java.util.List;

/**
 * The Real Metadata Package playlist file.
 *
 * @author Borewit
 * @author Christophe Delory
 * @since 0.3.0
 */
public class RmpProvider extends JaxbPlaylistProvider<RmpPackage>
{
    /**
     * A list of compatible content types.
     */
    private static final ContentType[] FILETYPES =
        {
            new ContentType(new String[]{".rmp"},
                new String[]{"application/vnd.rn-rn_music_package"},
                new PlayerSupport[]
                    {
                        new PlayerSupport(PlayerSupport.Player.REALPLAYER, true, null),
                    },
                "Real Metadata Package (RMP)"),
        };

    public RmpProvider()
    {
        super(RmpPackage.class);
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
    public SpecificPlaylist readFrom(final InputStream in, final String encoding) throws Exception
    {
        final JAXBElement<RmpPackage> rmp = this.unmarshal(in, encoding);
        String rootElementName = rmp.getName().getLocalPart();

        return rootElementName != null && rootElementName.equalsIgnoreCase("PACKAGE") ?
            new RmpPlaylistAdapter(this, rmp.getValue()) : null;
    }

    @Override
    public SpecificPlaylist toSpecificPlaylist(final Playlist playlist) throws Exception
    {
        final RmpPackage rmpPackage = new RmpPackage();

        rmpPackage.setTITLE("Lizzy v" + Version.CURRENT + " RMP playlist");
        rmpPackage.setACTION("import,replace");
        rmpPackage.setTARGET(Integer.toString(System.identityHashCode(rmpPackage)));

        RmpPackage.TRACKLIST tracklist = new RmpPackage.TRACKLIST();
        tracklist.setLISTID(Integer.toString(System.identityHashCode(rmpPackage.getTRACKLIST())));
        rmpPackage.setTRACKLIST(tracklist);

        final RmpPackage.PROVIDER provider = new RmpPackage.PROVIDER();
        provider.setAUTHOR("Christophe Delory");
        provider.setNAME("Lizzy v" + Version.CURRENT);
        provider.setURL("http://sourceforge.net/projects/lizzy/");
        provider.setCOPYRIGHT("Copyright (c) 2008-2009, Christophe Delory");
        provider.setCONTACT("cdelory@users.sourceforge.net");
        rmpPackage.setPROVIDER(provider);

        // TODO setSignature()?

        addToPlaylist(tracklist.getTRACK(), playlist.getRootSequence()); // May throw Exception.

        return new RmpPlaylistAdapter(this, rmpPackage);
    }

    /**
     * Adds the specified generic playlist component, and all its childs if any, to the input track list.
     *
     * @param trackList the parent track list. Shall not be <code>null</code>.
     * @param component the generic playlist component to handle. Shall not be <code>null</code>.
     */
    private void addToPlaylist(final List<RmpPackage.TRACKLIST.TRACK> trackList, final AbstractPlaylistComponent component)
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
                    final RmpPackage.TRACKLIST.TRACK track = new RmpPackage.TRACKLIST.TRACK(); // NOPMD Avoid instantiating new objects inside loops
                    track.setTRACKID(Integer.toString(System.identityHashCode(track))); // FIXME Why not media.getSource() as id?
                    track.setTITLE(media.getSource().toString());
                    track.setFILENAME(media.getSource().toString());

                    if (media.getSource().getLength() >= 0L) // NOPMD Deeply nested if..then statements are hard to read
                    {
                        track.setSIZE(media.getSource().getLength()); // Shall not throw IllegalArgumentException.
                    }

                    if (media.getSource().getDuration() >= 0L) // NOPMD Deeply nested if..then statements are hard to read
                    {
                        track.setDURATION((int) (media.getSource().getDuration() / 1000L)); // Shall not throw IllegalArgumentException.
                    }

                    // TODO setFormat()?

                    trackList.add(track);
                }
            }
        }
    }
}
