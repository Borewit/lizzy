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
package christophedelory.playlist.plist;

import java.io.InputStream;
import java.io.StringReader;
import java.util.Date;

import org.apache.commons.logging.Log;

import christophedelory.content.type.ContentType;
import christophedelory.io.IOUtils;
import christophedelory.player.PlayerSupport;
import christophedelory.playlist.AbstractPlaylistComponent;
import christophedelory.playlist.Media;
import christophedelory.playlist.Parallel;
import christophedelory.playlist.Playlist;
import christophedelory.playlist.Sequence;
import christophedelory.playlist.SpecificPlaylist;
import christophedelory.playlist.SpecificPlaylistProvider;
import christophedelory.plist.Array;
import christophedelory.plist.Dict;
import christophedelory.plist.Plist;
import christophedelory.plist.True;
import christophedelory.xml.Version;
import christophedelory.xml.XmlSerializer;

/**
 * The iTunes library format.
 * @version $Revision: 90 $
 * @author Christophe Delory
 */
public class PlistProvider implements SpecificPlaylistProvider
{
    /**
     * A list of compatible content types.
     */
    private static final ContentType[] FILETYPES =
    {
        new ContentType(new String[] { ".plist", ".xml" },
                        new String[] { "text/xml" },
                        new PlayerSupport[]
                        {
                            new PlayerSupport(PlayerSupport.Player.ITUNES, true, null),
                        },
                        "iTunes Library File"),
    };

    @Override
    public String getId()
    {
        return "plist";
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

        // Unmarshal the playlist.
        final XmlSerializer serializer = XmlSerializer.getMapping("christophedelory/plist"); // May throw Exception.
        serializer.getUnmarshaller().setIgnoreExtraElements(false); // Force an error if unknown elements are found.

        final StringReader reader = new StringReader(str);
        final Plist plist = (Plist) serializer.unmarshal(reader); // May throw Exception.

        final PlistPlaylist ret = new PlistPlaylist();
        ret.setProvider(this);
        ret.setPlist(plist);

        return ret;
    }

    @Override
    public SpecificPlaylist toSpecificPlaylist(final Playlist playlist) throws Exception
    {
        final PlistPlaylist ret = new PlistPlaylist();
        ret.setProvider(this);

        final Dict rootDict = new Dict();
        ret.getPlist().setPlistObject(rootDict);

        final Dict tracks = new Dict();
        rootDict.put("Tracks", tracks);

        final Array playlists = new Array();
        rootDict.put("Playlists", playlists);

        // A choice has been made to generate only one playlist.
        // This choice starts from here.
        // TODO We could have generated one playlist per sequence.
        final Dict playlistDict = new Dict();
        playlists.addPlistObject(playlistDict);
        playlistDict.put("Name", new christophedelory.plist.String("Playlist generated by Lizzy v" + Version.CURRENT));
        playlistDict.put("Playlist ID", new christophedelory.plist.Integer(System.identityHashCode(playlist.getRootSequence())));
        //playlistDict.put("Playlist Persistent ID", new christophedelory.plist.String(???));
        playlistDict.put("All Items", new True());
        final Array playlistArray = new Array();
        playlistDict.put("Playlist Items", playlistArray);

        addToPlaylist(tracks, playlistArray, playlist.getRootSequence()); // May throw Exception.

        return ret;
    }

    /**
     * Adds the specified generic playlist component, and all its childs if any, to the input track list and playlist.
     * @param tracks the list of tracks. Shall not be <code>null</code>.
     * @param playlist the playlist. Shall not be <code>null</code>.
     * @param component the generic playlist component to handle. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>tracks</code> is <code>null</code>.
     * @throws NullPointerException if <code>playlist</code> is <code>null</code>.
     * @throws NullPointerException if <code>component</code> is <code>null</code>.
     * @throws Exception if this service provider is unable to represent the input playlist.
     */
    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    private void addToPlaylist(final Dict tracks, final Array playlist, final AbstractPlaylistComponent component) throws Exception
    {
        if (component instanceof Sequence)
        {
            final Sequence sequence = (Sequence) component;

            if (sequence.getRepeatCount() < 0)
            {
                throw new IllegalArgumentException("A PLIST playlist cannot handle a sequence repeated indefinitely");
            }

            final AbstractPlaylistComponent[] components = sequence.getComponents();

            for (int iter = 0; iter < sequence.getRepeatCount(); iter++)
            {
                for (AbstractPlaylistComponent c : components)
                {
                    addToPlaylist(tracks, playlist, c); // May throw Exception.
                }
            }
        }
        else if (component instanceof Parallel)
        {
            throw new IllegalArgumentException("A PLIST playlist cannot play different media at the same time");
        }
        else if (component instanceof Media)
        {
            final Media media = (Media) component;

            if (media.getDuration() != null)
            {
                throw new IllegalArgumentException("A PLIST playlist cannot handle a timed media");
            }

            if (media.getRepeatCount() < 0)
            {
                throw new IllegalArgumentException("A PLIST playlist cannot handle a media repeated indefinitely");
            }

            if (media.getSource() != null)
            {
                for (int iter = 0; iter < media.getRepeatCount(); iter++)
                {
                    // Adds a playlist entry.
                    final Dict entry = new Dict();
                    entry.put("Track ID", new christophedelory.plist.Integer(System.identityHashCode(media.getSource())));
                    playlist.addPlistObject(entry);

                    // Adds a new track.
                    final Dict track = new Dict();
                    tracks.put(Integer.toString(System.identityHashCode(media.getSource())), track);

                    track.put("Track ID", new christophedelory.plist.Integer(System.identityHashCode(media.getSource())));

                    if (media.getSource().getLength() >= 0L) // NOPMD Deeply nested if then statement
                    {
                        track.put("Size", new christophedelory.plist.Integer((int) media.getSource().getLength()));
                    }

                    if (media.getSource().getDuration() >= 0L) // NOPMD Deeply nested if then statement
                    {
                        track.put("Total Time", new christophedelory.plist.Integer((int) media.getSource().getDuration()));
                    }

                    if (media.getSource().getLastModified() > 0L) // NOPMD Deeply nested if then statement
                    {
                        track.put("Date Modified", new christophedelory.plist.Date(new Date(media.getSource().getLastModified())));
                    }

                    track.put("Location", new christophedelory.plist.String(media.getSource().toString()));
                }
            }
        }
    }
}
