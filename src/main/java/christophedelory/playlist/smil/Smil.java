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

import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import christophedelory.content.Content;
import christophedelory.playlist.AbstractTimeContainer;
import christophedelory.playlist.Media;
import christophedelory.playlist.Parallel;
import christophedelory.playlist.Playlist;
import christophedelory.playlist.Sequence;
import christophedelory.playlist.SpecificPlaylist;
import christophedelory.playlist.SpecificPlaylistProvider;
import christophedelory.xml.XmlSerializer;

/**
 * Acts as the root element for all SMIL Host Language conformant language profiles.
 * @version $Revision: 92 $
 * @author Christophe Delory
 * @castor.class xml="smil"
 */
public class Smil extends Core implements SpecificPlaylist
{
    /**
     * The provider of this specific playlist.
     */
    private transient SpecificPlaylistProvider _provider = null;

    /**
     * The header of the SMIL presentation, if existing.
     */
    private Head _header = null;

    /**
     * The body of the SMIL presentation, if existing.
     */
    private Body _body = null;

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
        // Marshal the SMIL playlist.
        final StringWriter writer = new StringWriter();
        final XmlSerializer serializer = XmlSerializer.getMapping("christophedelory/playlist/smil"); // May throw Exception.
        // Specifies whether XML documents (as generated at marshalling) should use indentation or not. Default is false.
        serializer.getMarshaller().setProperty("org.exolab.castor.indent", "true");
        serializer.marshal(this, writer, false); // May throw Exception.

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

        if (_body != null)
        {
            final Float repeatCount = _body.getRepeatCount();

            if (repeatCount != null)
            {
                ret.getRootSequence().setRepeatCount(repeatCount.intValue());
            }

            for (AbstractSmilElement smilElem : _body.getSmilElements())
            {
                addToContainer(smilElem, ret.getRootSequence());
            }

            ret.normalize();
        }

        return ret;
    }

    /**
     * Adds the specified SMIL element, and its childs if appropriate, to the given timing container.
     * @param smilElement the SMIL element to handle. Shall not be <code>null</code>.
     * @param currentContainer the parent timing container. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>smilElement</code> is <code>null</code>.
     * @throws NullPointerException if <code>currentContainer</code> is <code>null</code>.
     */
    private void addToContainer(final AbstractSmilElement smilElement, final AbstractTimeContainer currentContainer)
    {
        if (smilElement instanceof SequentialTimingElement)
        {
            final SequentialTimingElement smilSeq = (SequentialTimingElement) smilElement;
            final Sequence seq = new Sequence();
            final Float repeatCount = smilSeq.getRepeatCount();

            if (repeatCount != null)
            {
                seq.setRepeatCount(repeatCount.intValue());
            }

            currentContainer.addComponent(seq);

            for (AbstractSmilElement smilElem : smilSeq.getSmilElements())
            {
                addToContainer(smilElem, seq);
            }
        }
        else if (smilElement instanceof ParallelTimingElement)
        {
            final ParallelTimingElement smilPar = (ParallelTimingElement) smilElement;
            final Parallel par = new Parallel();
            final Float repeatCount = smilPar.getRepeatCount();

            if (repeatCount != null)
            {
                par.setRepeatCount(repeatCount.intValue());
            }

            currentContainer.addComponent(par);

            for (AbstractSmilElement smilElem : smilPar.getSmilElements())
            {
                addToContainer(smilElem, par);
            }
        }
        // TODO What can we do with an ExclusiveTimingElement? More complicated than at first sight...
        else if (smilElement instanceof Reference)
        {
            final Reference smilRef = (Reference) smilElement;

            if (smilRef.getSource() != null)
            {
                final Media media = new Media();
                final Content content = new Content(smilRef.getSource());
                content.setType(smilRef.getType()); // May be null.
                media.setSource(content);

                if ((smilRef.getDuration() != null) && (smilRef.getDuration().longValue() > 0L)) // NOPMD Deeply nested if..then statements are hard to read
                {
                    // FIXME We can't check here if the expressed duration is in fact the effective media duration
                    // (in which case we don't need to specify it, and we would be more compatible to simple playlist formats).
                    media.setDuration(smilRef.getDuration());
                }

                final Float repeatCount = smilRef.getRepeatCount();

                if (repeatCount != null) // NOPMD Deeply nested if..then statements are hard to read
                {
                    media.setRepeatCount(repeatCount.intValue());
                }

                currentContainer.addComponent(media);
            }
        }
    }

    /**
     * Returns the header of this SMIL presentation, if any.
     * @return an header element. May be <code>null</code>.
     * @see #setHeader
     * @castor.field
     *  get-method="getHeader"
     *  set-method="setHeader"
     * @castor.field-xml
     *  name="head"
     *  node="element"
     */
    public Head getHeader()
    {
        return _header;
    }

    /**
     * Initializes the header of this SMIL presentation.
     * @param header an header element. May be <code>null</code>.
     * @see #getHeader
     */
    public void setHeader(final Head header)
    {
        _header = header;
    }

    /**
     * Returns the body of this SMIL presentation, if any.
     * @return the body element. May be <code>null</code>.
     * @see #setBody
     * @castor.field
     *  get-method="getBody"
     *  set-method="setBody"
     * @castor.field-xml
     *  name="body"
     *  node="element"
     */
    public Body getBody()
    {
        return _body;
    }

    /**
     * Initializes the body of this SMIL presentation.
     * @param body a body element. May be <code>null</code>.
     * @see #getBody
     */
    public void setBody(final Body body)
    {
        _body = body;
    }

    /**
     * Accepts the specified SMIL playlist visitor.
     * @param visitor a SMIL playlist visitor. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>visitor</code> is <code>null</code>.
     */
    public void acceptDown(final SmilVisitor visitor)
    {
        visitor.beginVisitSmil(this); // Throws NullPointerException if visitor is null.

        // Visit the header of this SMIL element.
        if (_header != null)
        {
            _header.acceptDown(visitor);
        }

        // Visit the body of this SMIL element.
        if (_body != null)
        {
            _body.acceptDown(visitor);
        }

        visitor.endVisitSmil(this);
    }

    /**
     * Finds all SMIL references assigned to the specified region.
     * @param regionId a SMIL region identifier. Should not be <code>null</code>.
     * @return a list of references. May be empty but not <code>null</code>.
     * @throws NullPointerException if <code>regionId</code> is <code>null</code>.
     * @see Reference#getRegionString
     */
    public List<Reference> findReferencesFromRegionId(final String regionId)
    {
        final ReferenceVisitor v = new ReferenceVisitor(regionId);
        acceptDown(v); // May throw NullPointerException if regionId is null.

        return v.getReferences();
    }

    /**
     * A SMIL playlist visitor dedicated to references.
     */
    private static class ReferenceVisitor extends BaseSmilVisitor
    {
        /**
         * A list of references.
         */
        private final List<Reference> _result;

        /**
         * The SMIL region identifier to search for in a SMIL hierarchy.
         */
        private final String _regionId;

        /**
         * Builds a new SMIL visitor.
         * @param regionId a SMIL region identifier. Should not be <code>null</code>.
         */
        public ReferenceVisitor(final String regionId)
        {
            super();

            _result = new ArrayList<Reference>();
            _regionId = regionId;
        }

        @Override
        public void beginVisitReference(final Reference target)
        {
            if (_regionId.equals(target.getRegionString())) // May throw NullPointerException if target is null.
            {
                _result.add(target);
            }
        }

        /**
         * Returns the list of references built so far in this visitor.
         * @return a list of references. May be empty but not <code>null</code>.
         */
        public List<Reference> getReferences()
        {
            return _result;
        }
    }
}
