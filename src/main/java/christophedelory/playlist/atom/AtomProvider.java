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
package christophedelory.playlist.atom;

import christophedelory.content.type.ContentType;
import christophedelory.player.PlayerSupport;
import christophedelory.playlist.*;
import christophedelory.xml.Version;
import io.github.borewit.playlist.atom.*;

import jakarta.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * The Atom playlist provider.
 *
 * @author Christophe Delory
 */
public class AtomProvider extends JaxbPlaylistProvider<FeedType>
{
    /**
     * A list of compatible content types.
     */
    private static final ContentType[] FILETYPES =
        {
            new ContentType(new String[]{".atom", ".xml"},
                new String[]{"application/atom+xml"},
                new PlayerSupport[]
                    {
                    },
                "Atom Document"),
        };

    public AtomProvider()
    {
        super(FeedType.class);
    }

    @Override
    public String getId()
    {
        return "atom";
    }

    @Override
    public ContentType[] getContentTypes()
    {
        return FILETYPES.clone();
    }

    @Override
    public SpecificPlaylist readFrom(final InputStream in, final String encoding) throws Exception
    {
        final JAXBElement<FeedType> feed = this.unmarshal(in, encoding);
        String rootElementName = feed.getName().getLocalPart();

        return rootElementName != null && rootElementName.equalsIgnoreCase("Feed") ?
            new AtomPlaylist(this, feed.getValue()) : null;
    }

    @Override
    public SpecificPlaylist toSpecificPlaylist(final Playlist playlist) throws Exception
    {
        final FeedType feed = new FeedType();

        feed.setTitle(toAtomText("Lizzy v" + Version.CURRENT + " Atom playlist"));
        feed.setUpdated(toAtomDate(new Date()));

        final Id id = new Id();
        final StringBuilder sb = new StringBuilder();
        sb.append("urn:uuid:");
        final String tmpId = Integer.toHexString(System.identityHashCode(feed));
        for (int i = tmpId.length(); i < 8; i++)
        {
            sb.append('0');
        }
        sb.append(tmpId);
        sb.append("-d399-11d9-b93C-0003939e0af6");
        id.setContent(sb.toString());
        feed.setId(id);

        final Generator generator = new Generator();
        generator.setContent("Lizzy");
        generator.setVersion(Version.CURRENT.toString());
        generator.setUri("http://sourceforge.net/projects/lizzy/");
        feed.setGenerator(generator);


        addToPlaylist(feed, playlist.getRootSequence()); // May throw Exception

        return new AtomPlaylist(this, feed);
    }

    private static AtomTextConstruct toAtomText(String title)
    {
        AtomTextConstruct atomTextConstruct = new AtomTextConstruct();
        atomTextConstruct.setValue(title);
        return atomTextConstruct;
    }

    private static AtomDateConstruct toAtomDate(long time)
    {
        return toAtomDate(new Date(time));
    }

    private static AtomDateConstruct toAtomDate(Date date)
    {
        AtomDateConstruct atomDateConstruct = new AtomDateConstruct();
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(date);
        try
        {
            atomDateConstruct.setValue(DatatypeFactory.newInstance().newXMLGregorianCalendar(c));
        }
        catch (DatatypeConfigurationException e)
        {
            throw new RuntimeException(e);
        }
        return new AtomDateConstruct();
    }

    /**
     * Adds the specified generic playlist component, and all its childs if any, to the input Atom feed.
     *
     * @param feed      the parent Atom feed. Shall not be <code>null</code>.
     * @param component the generic playlist component to handle. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>feed</code> is <code>null</code>.
     * @throws NullPointerException if <code>component</code> is <code>null</code>.
     * @throws Exception            if this service provider is unable to represent the input playlist.
     */
    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    private void addToPlaylist(final FeedType feed, final AbstractPlaylistComponent component) throws Exception
    {
        if (component instanceof Sequence)
        {
            final Sequence sequence = (Sequence) component;

            if (sequence.getRepeatCount() < 0)
            {
                throw new IllegalArgumentException("An Atom playlist cannot handle a sequence repeated indefinitely");
            }

            final AbstractPlaylistComponent[] components = sequence.getComponents();

            for (int iter = 0; iter < sequence.getRepeatCount(); iter++)
            {
                for (AbstractPlaylistComponent c : components)
                {
                    addToPlaylist(feed, c); // May throw Exception.
                }
            }
        }
        else if (component instanceof Parallel)
        {
            throw new IllegalArgumentException("An Atom playlist doesn't support concurrent media");
        }
        else if (component instanceof Media)
        {
            final Media media = (Media) component;
            final Date now = new Date();

            if (media.getDuration() != null)
            {
                throw new IllegalArgumentException("An Atom playlist cannot handle a timed media");
            }

            if (media.getRepeatCount() < 0)
            {
                throw new IllegalArgumentException("An Atom playlist cannot handle a media repeated indefinitely");
            }

            if (media.getSource() != null)
            {
                for (int iter = 0; iter < media.getRepeatCount(); iter++)
                {
                    final EntryType entry = new EntryType();
                    final Link link = new Link();
                    final URI uri = media.getSource().getURI(); // May throw SecurityException, URISyntaxException.
                    link.setHref(uri.toString());
                    link.setRel("enclosure");
                    link.setType(media.getSource().getType());

                    if (media.getSource().getLength() >= 0L) // NOPMD Deeply nested if..then statements are hard to read
                    {
                        link.setLength(media.getSource().getLength());
                    }

                    entry.getLink().add(link);

                    final AtomTextConstruct title = new AtomTextConstruct();

                    if (uri.getPath() == null)
                    {
                        title.setValue(media.getSource().toString());
                    }
                    else
                    {
                        final File path = new File(uri.getPath());
                        title.setValue(path.getName());
                    }

                    entry.setTitle(title);

                    AtomDateConstruct atomDate = toAtomDate(now);

                    if (media.getSource().getLastModified() > 0L)
                    {
                        entry.setUpdated(toAtomDate(media.getSource().getLastModified()));
                    }
                    else
                    {
                        entry.setUpdated(atomDate);
                    }

                    entry.setPublished(atomDate);

                    final Id id = new Id();
                    final StringBuilder sb = new StringBuilder();
                    sb.append("urn:uuid:");
                    final String tmpId = Integer.toHexString(System.identityHashCode(entry));
                    for (int i = tmpId.length(); i < 8; i++)
                    {
                        sb.append('0');
                    }
                    sb.append(tmpId);
                    sb.append("-d399-11d9-b93C-0003939e0af6");
                    id.setContent(sb.toString());
                    entry.setId(id);

                    feed.getEntry().add(entry);
                }
            }
        }
    }
}
