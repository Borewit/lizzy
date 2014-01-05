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

import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Date;

import christophedelory.content.Content;
import christophedelory.playlist.Media;
import christophedelory.playlist.Playlist;
import christophedelory.playlist.Sequence;
import christophedelory.playlist.SpecificPlaylist;
import christophedelory.playlist.SpecificPlaylistProvider;
import christophedelory.plist.Array;
import christophedelory.plist.Dict;
import christophedelory.plist.Plist;
import christophedelory.plist.PlistObject;
import christophedelory.plist.PlistText;
import christophedelory.xml.XmlSerializer;

/**
 * The definition of an iTunes playlist.
 * @version $Revision: 92 $
 * @author Christophe Delory
 */
public class PlistPlaylist implements SpecificPlaylist
{
    /**
     * The provider of this specific playlist.
     */
    private transient SpecificPlaylistProvider _provider = null;

    /**
     * The playlist itself.
     */
    private Plist _plist = new Plist();

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
        // Marshal the PLIST playlist.
        final StringWriter writer = new StringWriter();
        final XmlSerializer serializer = XmlSerializer.getMapping("christophedelory/plist"); // May throw Exception.
        // Specifies whether XML documents (as generated at marshalling) should use indentation or not. Default is false.
        serializer.getMarshaller().setProperty("org.exolab.castor.indent", "true");
        serializer.marshal(_plist, writer, false); // May throw Exception.

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

        if ((_plist.getPlistObject() != null) && (_plist.getPlistObject() instanceof Dict))
        {
            final Dict rootDict = (Dict) _plist.getPlistObject();

            Dict tracks = null;
            final PlistObject tracksObject = rootDict.findObjectByKey("Tracks");

            if ((tracksObject != null) && (tracksObject instanceof Dict))
            {
                tracks = (Dict) tracksObject;
            }

            Array playlists = null;
            final PlistObject playlistsObject = rootDict.findObjectByKey("Playlists");

            if ((playlistsObject != null) && (playlistsObject instanceof Array))
            {
                playlists = (Array) playlistsObject;
            }

            if ((tracks != null) && (playlists != null))
            {
                // Iterate through the playlists.
                for (PlistObject playlistObject : playlists.getPlistObjects())
                {
                    if (playlistObject instanceof Dict) // NOPMD Deeply nested if then statement
                    {
                        final Dict playlist = (Dict) playlistObject;
                        final PlistObject playlistItemsArrayObject = playlist.findObjectByKey("Playlist Items");

                        if ((playlistItemsArrayObject != null) && (playlistItemsArrayObject instanceof Array))
                        {
                            final Array playlistItemsArray = (Array) playlistItemsArrayObject;
                            // Each playlist is assigned to a dedicated sequence.
                            final Sequence sequence = new Sequence(); // NOPMD Avoid instantiating new objects inside loops

                            for (PlistObject playlistItemsDictObject : playlistItemsArray.getPlistObjects())
                            {
                                if (playlistItemsDictObject instanceof Dict)
                                {
                                    final PlistObject trackIdObject = ((Dict) playlistItemsDictObject).findObjectByKey("Track ID");

                                    if ((trackIdObject != null) && (trackIdObject instanceof PlistText))
                                    {
                                        final String trackId = ((PlistText) trackIdObject).getValue();

                                        if (trackId != null)
                                        {
                                            // Got one track identifier!!!
                                            // Now find it in the track list.
                                            final PlistObject trackObject = tracks.findObjectByKey(trackId);

                                            if ((trackObject != null) && (trackObject instanceof Dict))
                                            {
                                                final Dict track = (Dict) trackObject;
                                                final PlistObject locationObject = track.findObjectByKey("Location");

                                                if ((locationObject != null) && (locationObject instanceof PlistText))
                                                {
                                                    final String location = ((PlistText) locationObject).getValue();

                                                    if (location != null)
                                                    {
                                                        // Now create the media.
                                                        final Media media = new Media(); // NOPMD Avoid instantiating new objects inside loops
                                                        final Content content = new Content(location); // NOPMD Avoid instantiating new objects inside loops
                                                        media.setSource(content);

                                                        // Try to retrieve the duration.
                                                        final PlistObject totalTimeObject = track.findObjectByKey("Total Time");

                                                        if ((totalTimeObject != null) && (totalTimeObject instanceof christophedelory.plist.Integer))
                                                        {
                                                            final String totalTimeString = ((christophedelory.plist.Integer) totalTimeObject).getValue();

                                                            if (totalTimeString != null)
                                                            {
                                                                try
                                                                {
                                                                    final Integer totalTime = Integer.decode(totalTimeString); // May throw NumberFormatException.
                                                                    content.setDuration(totalTime.longValue());
                                                                }
                                                                catch (NumberFormatException e) // NOPMD Avoid empty catch blocks
                                                                {
                                                                    // Ignore it.
                                                                }
                                                            }
                                                        }

                                                        // Try to retrieve the length.
                                                        final PlistObject sizeObject = track.findObjectByKey("Size");

                                                        if ((sizeObject != null) && (sizeObject instanceof christophedelory.plist.Integer))
                                                        {
                                                            final String sizeString = ((christophedelory.plist.Integer) sizeObject).getValue();

                                                            if (sizeString != null)
                                                            {
                                                                try
                                                                {
                                                                    final Integer size = Integer.decode(sizeString); // May throw NumberFormatException.

                                                                    if (size.intValue() >= 0)
                                                                    {
                                                                        content.setLength(size.longValue());
                                                                    }
                                                                }
                                                                catch (NumberFormatException e) // NOPMD Avoid empty catch blocks
                                                                {
                                                                    // Ignore it.
                                                                }
                                                            }
                                                        }

                                                        // Try to retrieve the last modified date.
                                                        final PlistObject dateModifiedObject = track.findObjectByKey("Date Modified");

                                                        if ((dateModifiedObject != null) && (dateModifiedObject instanceof christophedelory.plist.Date))
                                                        {
                                                            final Date dateModified = ((christophedelory.plist.Date) dateModifiedObject).getValue();

                                                            if (dateModified != null)
                                                            {
                                                                content.setLastModified(dateModified.getTime());
                                                            }
                                                        }

                                                        sequence.addComponent(media);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            ret.getRootSequence().addComponent(sequence);
                        }
                    }
                }

                ret.normalize();
            }
        }

        return ret;
    }

    /**
     * Returns the playlist itself.
     * @return a plist element. Shall not be <code>null</code>.
     * @see #setPlist
     */
    public Plist getPlist()
    {
        return _plist;
    }

    /**
     * Initializes the playlist itself.
     * @param plist a plist element. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>plist</code> is <code>null</code>.
     * @see #getPlist
     */
    public void setPlist(final Plist plist)
    {
        if (plist == null)
        {
            throw new NullPointerException("No plist");
        }

        _plist = plist;
    }
}
