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
package christophedelory.playlist.smil;

import java.io.InputStream;

import christophedelory.playlist.*;
import org.apache.commons.logging.Log;

import christophedelory.content.type.ContentType;
import christophedelory.player.PlayerSupport;
import christophedelory.xml.XmlSerializer;

/**
 * The W3C SMIL playlist XML format.
 * An XML recommendation of the World Wide Web Consortium that includes playlist features.
 * @version $Revision: 90 $
 * @author Christophe Delory
 */
public class SmilProvider extends AbstractPlaylistProvider
{
    /**
     * A list of compatible content types.
     */
    private static final ContentType[] FILETYPES =
    {
        new ContentType(new String[] { ".smil", ".smi" },
                        new String[] { "application/smil+xml", "application/smil" },
                        new PlayerSupport[]
                        {
                            new PlayerSupport(PlayerSupport.Player.MEDIA_PLAYER_CLASSIC, false, null),
                            new PlayerSupport(PlayerSupport.Player.QUICKTIME, true, null),
                            new PlayerSupport(PlayerSupport.Player.REALPLAYER, false, null),
                        },
                        "Synchronized Multimedia Integration Language (SMIL)"),
    };

    public SmilProvider()
    {
        super(SmilProvider.class);
    }

    @Override
    public String getId()
    {
        return "smil";
    }

    @Override
    public ContentType[] getContentTypes()
    {
        return FILETYPES.clone();
    }

    @Override
    public SpecificPlaylist readFrom(final InputStream in, final String encoding, final Log logger) throws Exception
    {
        // Unmarshal the SMIL playlist.
        final XmlSerializer serializer = XmlSerializer.getMapping("christophedelory/playlist/smil"); // May throw Exception.
        serializer.getUnmarshaller().setIgnoreExtraElements(true); // Many SMIL elements are not implemented yet.

        final Smil ret = (Smil) serializer.unmarshal(preProcessXml(in, encoding)); // May throw Exception.
        ret.setProvider(this);

        return ret;
    }

    @Override
    public SpecificPlaylist toSpecificPlaylist(final Playlist playlist)
    {
        final Smil ret = new Smil();
        ret.setProvider(this);

        final Body body = new Body();
        body.setRepeatCount(Float.valueOf((float) playlist.getRootSequence().getRepeatCount()));
        ret.setBody(body);

        final AbstractPlaylistComponent[] components = playlist.getRootSequence().getComponents();

        for (AbstractPlaylistComponent component : components)
        {
            addToPlaylist(body, component);
        }

        return ret;
    }

    /**
     * Adds the specified generic playlist component, and all its childs if any, to the input time container.
     * @param timingElement the parent time container. Shall not be <code>null</code>.
     * @param component the generic playlist component to handle. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>timingElement</code> is <code>null</code>.
     * @throws NullPointerException if <code>component</code> is <code>null</code>.
     */
    private void addToPlaylist(final AbstractTimingElement timingElement, final AbstractPlaylistComponent component)
    {
        if (component instanceof Sequence)
        {
            final Sequence sequence = (Sequence) component;
            final SequentialTimingElement seq = new SequentialTimingElement();
            seq.setRepeatCount(Float.valueOf((float) sequence.getRepeatCount()));
            timingElement.addSmilElement(seq);

            final AbstractPlaylistComponent[] components = sequence.getComponents();

            for (AbstractPlaylistComponent c : components)
            {
                addToPlaylist(seq, c);
            }
        }
        else if (component instanceof Parallel)
        {
            final Parallel parallel = (Parallel) component;
            final ParallelTimingElement par = new ParallelTimingElement();
            par.setRepeatCount(Float.valueOf((float) parallel.getRepeatCount()));
            timingElement.addSmilElement(par);

            final AbstractPlaylistComponent[] components = parallel.getComponents();

            for (AbstractPlaylistComponent c : components)
            {
                addToPlaylist(par, c);
            }
        }
        else if (component instanceof Media)
        {
            final Media media = (Media) component;
            final Reference ref = new Reference();

            if (media.getSource() != null)
            {
                ref.setSource(media.getSource().toString());
                ref.setType(media.getSource().getType()); // May be null.
            }

            ref.setDuration(media.getDuration());
            ref.setRepeatCount(Float.valueOf((float) media.getRepeatCount()));
            timingElement.addSmilElement(ref);
        }
    }
}
