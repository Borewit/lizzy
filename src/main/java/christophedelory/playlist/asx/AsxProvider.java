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
package christophedelory.playlist.asx;

import java.io.InputStream;
import java.io.StringReader;

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
import christophedelory.xml.XmlSerializer;

/**
 * The Windows Media ASX playlist XML format.
 * An XML style playlist containing more information about the items on the playlist.
 * @version $Revision: 90 $
 * @author Christophe Delory
 */
public class AsxProvider implements SpecificPlaylistProvider
{
    /**
     * A list of compatible content types.
     */
    private static final ContentType[] FILETYPES =
    {
        new ContentType(new String[] { ".asx" },
                        new String[] { "video/x-ms-asf" },
                        new PlayerSupport[]
                        {
                            new PlayerSupport(PlayerSupport.Player.WINAMP, false, null),
                            new PlayerSupport(PlayerSupport.Player.VLC_MEDIA_PLAYER, false, null),
                            new PlayerSupport(PlayerSupport.Player.WINDOWS_MEDIA_PLAYER, true, null),
                            new PlayerSupport(PlayerSupport.Player.MEDIA_PLAYER_CLASSIC, true, null),
                            new PlayerSupport(PlayerSupport.Player.REALPLAYER, false, null),
                        },
                        "Advanced Stream Redirector (ASX)"),
        new ContentType(new String[] { ".wmx" },
                        new String[] { "video/x-ms-wvx" },
                        new PlayerSupport[]
                        {
                            new PlayerSupport(PlayerSupport.Player.REALPLAYER, false, null),
                        },
                        "Windows Media Redirector (WMX)"),
        new ContentType(new String[] { ".wvx" },
                        new String[] { "video/x-ms-wvx" },
                        new PlayerSupport[]
                        {
                            new PlayerSupport(PlayerSupport.Player.WINDOWS_MEDIA_PLAYER, false, null),
                            new PlayerSupport(PlayerSupport.Player.MEDIA_PLAYER_CLASSIC, false, null),
                            new PlayerSupport(PlayerSupport.Player.REALPLAYER, false, null),
                        },
                        "Windows Media Video Redirector (WVX)"),
        new ContentType(new String[] { ".wax" },
                        new String[] { "audio/x-ms-wax" },
                        new PlayerSupport[]
                        {
                            new PlayerSupport(PlayerSupport.Player.WINDOWS_MEDIA_PLAYER, false, null),
                            new PlayerSupport(PlayerSupport.Player.MEDIA_PLAYER_CLASSIC, false, null),
                            new PlayerSupport(PlayerSupport.Player.REALPLAYER, false, null),
                        },
                        "Windows Media Audio Redirector (WAX)"),
    };

    @Override
    public String getId()
    {
        return "asx";
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
            enc = "UTF-8"; // FIXME US-ASCII?
        }

        String str = IOUtils.toString(in, enc); // May throw IOException. Throws NullPointerException if in is null.

        // Replace all occurrences of a single '&' with "&amp;" (or leave this construct as is).
        // First replace blindly all '&' to its corresponding character reference.
        str = str.replace("&", "&amp;");
        // Then restore any existing character reference.
        str = str.replaceAll("&amp;([a-zA-Z0-9#]+;)", "&$1"); // Shall not throw PatternSyntaxException.

        // Convert all XML element/attribute names to lower case.
        final StringBuilder sb = new StringBuilder();
        final int len = str.length();
        boolean inElement = false;
        boolean inAttribute = false;
        char previousChar = ' '; // Neutral value in our case.
        char attributeSeparator = '"'; // Dummy initialization, just to make the compiler happy.

        for (int i = 0; i < len; i++)
        {
            final char c = str.charAt(i); // Shall not throw IndexOutOfBoundsException.

            switch (c)
            {
                case '<':
                    inElement = true;
                    inAttribute = true;
                    sb.append(c);
                    break;

                case '>':
                    inElement = false;
                    inAttribute = false;
                    sb.append(c);
                    break;

                case '"':
                    if (inElement && (previousChar != '\\'))
                    {
                        if (inAttribute)
                        {
                            attributeSeparator = '"';
                            inAttribute = false;
                        }
                        else if (attributeSeparator == '"')
                        {
                            inAttribute = true;
                        }
                    }

                    sb.append(c);
                    break;

                case '\'':
                    if (inElement && (previousChar != '\\'))
                    {
                        if (inAttribute)
                        {
                            attributeSeparator = '\'';
                            inAttribute = false;
                        }
                        else if (attributeSeparator == '\'')
                        {
                            inAttribute = true;
                        }
                    }

                    sb.append(c);
                    break;

                default:
                    if (inElement && inAttribute)
                    {
                        sb.append(Character.toLowerCase(c));
                    }
                    else
                    {
                        sb.append(c);
                    }
            }

            previousChar = c;
        }

        str = sb.toString();

        // Unmarshal the ASX playlist.
        final XmlSerializer serializer = XmlSerializer.getMapping("christophedelory/playlist/asx"); // May throw Exception.
        serializer.getUnmarshaller().setIgnoreExtraElements(false); // Force an error if unknown elements are found.

        final StringReader reader = new StringReader(str);
        final SpecificPlaylist ret = (SpecificPlaylist) serializer.unmarshal(reader); // May throw Exception.
        ret.setProvider(this);

        return ret;
    }

    @Override
    public SpecificPlaylist toSpecificPlaylist(final Playlist playlist) throws Exception
    {
        final Asx ret = new Asx();
        ret.setProvider(this);

        addToPlaylist(ret, playlist.getRootSequence()); // May throw Exception.

        return ret;
    }

    /**
     * Adds the specified generic playlist component, and all its childs if any, to the input ASX elements container.
     * @param container the parent ASX element. Shall not be <code>null</code>.
     * @param component the generic playlist component to handle. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>container</code> is <code>null</code>.
     * @throws NullPointerException if <code>component</code> is <code>null</code>.
     * @throws Exception if this service provider is unable to represent the input playlist.
     */
    private void addToPlaylist(final AsxElementContainer container, final AbstractPlaylistComponent component) throws Exception
    {
        if (component instanceof Sequence)
        {
            final Sequence sequence = (Sequence) component;

            if (sequence.getRepeatCount() != 0) // Ignore "empty" sequences.
            {
                AsxElementContainer newContainer = container;

                // Do we need a repeat element?
                if (sequence.getRepeatCount() < 0)
                {
                    final Repeat repeat = new Repeat();
                    container.addAsxElement(repeat);
                    newContainer = repeat;
                }
                else if (sequence.getRepeatCount() > 1)
                {
                    final Repeat repeat = new Repeat();
                    repeat.setCount(Integer.valueOf(sequence.getRepeatCount() - 1));
                    container.addAsxElement(repeat);
                    newContainer = repeat;
                }

                final AbstractPlaylistComponent[] components = sequence.getComponents();

                for (AbstractPlaylistComponent c : components)
                {
                    addToPlaylist(newContainer, c);
                }
            }
        }
        else if (component instanceof Parallel)
        {
            throw new IllegalArgumentException("A parallel time container is incompatible with an ASX playlist");
        }
        else if (component instanceof Media)
        {
            final Media media = (Media) component;

            if ((media.getRepeatCount() != 0) && (media.getSource() != null)) // Ignore "empty" media.
            {
                AsxElementContainer newContainer = container;

                // Do we need a repeat element?
                if (media.getRepeatCount() < 0)
                {
                    final Repeat repeat = new Repeat();
                    container.addAsxElement(repeat);
                    newContainer = repeat;
                }
                else if (media.getRepeatCount() > 1)
                {
                    final Repeat repeat = new Repeat();
                    repeat.setCount(Integer.valueOf(media.getRepeatCount() - 1));
                    container.addAsxElement(repeat);
                    newContainer = repeat;
                }

                boolean isPlaylist = false;
                final String path = media.getSource().toString().toLowerCase();

                for (ContentType type : FILETYPES)
                {
                    for (String extension : type.getExtensions())
                    {
                        isPlaylist = isPlaylist || path.endsWith(extension);
                    }
                }

                if (isPlaylist)
                {
                    if (media.getDuration() != null)
                    {
                        throw new IllegalArgumentException("An ASX playlist referenced in another ASX playlist cannot be timed");
                    }

                    final Entryref entryRef = new Entryref();
                    entryRef.setHref(media.getSource().toString());
                    newContainer.addAsxElement(entryRef);
                }
                else
                {
                    final Entry entry = new Entry();
                    final Reference reference = new Reference();
                    reference.setHref(media.getSource().toString());

                    if (media.getDuration() != null)
                    {
                        final Duration duration = new Duration();
                        duration.setValue(media.getDuration().longValue());
                        reference.setDuration(duration);
                    }

                    entry.addReference(reference);
                    newContainer.addAsxElement(entry);
                }
            }
        }
    }
}
