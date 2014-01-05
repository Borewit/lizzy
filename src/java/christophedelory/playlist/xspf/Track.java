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
package christophedelory.playlist.xspf;

import java.util.ArrayList;
import java.util.List;

import org.exolab.castor.types.AnyNode;

import christophedelory.lang.StringUtils;

/**
 * A XSPF track.
 * @version $Revision: 92 $
 * @author Christophe Delory
 * @castor.class xml="track" ns-uri="http://xspf.org/ns/0/"
 */
public class Track extends Attribution
{
    /**
     * A title, if any.
     */
    private String _title = null;

    /**
     * A creator, if any.
     */
    private String _creator = null;

    /**
     * An annotation, if any.
     */
    private String _annotation = null;

    /**
     * An information, if any.
     */
    private String _info = null;

    /**
     * An image, if any.
     */
    private String _image = null;

    /**
     * An album, if any.
     */
    private String _album = null;

    /**
     * A track number, if any.
     */
    private Integer _trackNum = null;

    /**
     * A duration, if any.
     */
    private Integer _duration = null;

    /**
     * A list of links.
     */
    private final List<Link> _links = new ArrayList<Link>();

    /**
     * A list of metadata.
     */
    private final List<Meta> _metas = new ArrayList<Meta>();

    /**
     * A list of extensions.
     */
    private final List<AnyNode> _extensions = new ArrayList<AnyNode>();

    /**
     * Returns a human-readable name of the track that authored the resource which defines the duration of track rendering.
     * This value is primarily for fuzzy lookups, though a user-agent may display it.
     * @return a title. May be <code>null</code>.
     * @see #setTitle
     * @castor.field
     *  get-method="getTitle"
     *  set-method="setTitle"
     * @castor.field-xml
     *  name="title"
     *  node="element"
     */
    public String getTitle()
    {
        return _title;
    }

    /**
     * Initializes a human-readable name of the track that authored the resource which defines the duration of track rendering.
     * @param title a title. May be <code>null</code>.
     * @see #getTitle
     */
    public void setTitle(final String title)
    {
        _title = StringUtils.normalize(title);
    }

    /**
     * Returns a human-readable name of the entity (author, authors, group, company, etc) that authored the resource
     * which defines the duration of track rendering.
     * This value is primarily for fuzzy lookups, though a user-agent may display it.
     * @return a creator. May be <code>null</code>.
     * @see #setCreator
     * @castor.field
     *  get-method="getCreator"
     *  set-method="setCreator"
     * @castor.field-xml
     *  name="creator"
     *  node="element"
     */
    public String getCreator()
    {
        return _creator;
    }

    /**
     * Initializes a human-readable name of the entity (author, authors, group, company, etc) that authored the resource
     * which defines the duration of track rendering.
     * @param creator a creator. May be <code>null</code>.
     * @see #getCreator
     */
    public void setCreator(final String creator)
    {
        _creator = StringUtils.normalize(creator);
    }

    /**
     * Returns a human-readable comment on the track.
     * This is character data, not HTML, and it may not contain markup.
     * @return an annotation. May be <code>null</code>.
     * @see #setAnnotation
     * @castor.field
     *  get-method="getAnnotation"
     *  set-method="setAnnotation"
     * @castor.field-xml
     *  name="annotation"
     *  node="element"
     */
    public String getAnnotation()
    {
        return _annotation;
    }

    /**
     * Initializes a human-readable comment on the track.
     * @param annotation an annotation. May be <code>null</code>.
     * @see #getAnnotation
     */
    public void setAnnotation(final String annotation)
    {
        _annotation = StringUtils.normalize(annotation);
    }

    /**
     * Returns an URI of a place where this resource can be bought or more info can be found.
     * @return an information. May be <code>null</code>.
     * @see #setInfo
     * @castor.field
     *  get-method="getInfo"
     *  set-method="setInfo"
     * @castor.field-xml
     *  name="info"
     *  node="element"
     */
    public String getInfo()
    {
        return _info;
    }

    /**
     * Initializes an URI of a place where this resource can be bought or more info can be found.
     * @param info an information. May be <code>null</code>.
     * @see #getInfo
     */
    public void setInfo(final String info)
    {
        _info = StringUtils.normalize(info);
    }

    /**
     * Returns an URI of an image to display for the duration of the track.
     * @return an image. May be <code>null</code>.
     * @see #setImage
     * @castor.field
     *  get-method="getImage"
     *  set-method="setImage"
     * @castor.field-xml
     *  name="image"
     *  node="element"
     */
    public String getImage()
    {
        return _image;
    }

    /**
     * Initializes an URI of an image to display for the duration of the track.
     * @param image an image. May be <code>null</code>.
     * @see #getImage
     */
    public void setImage(final String image)
    {
        _image = StringUtils.normalize(image);
    }

    /**
     * Returns a human-readable name of the collection from which the resource which defines the duration of track rendering comes.
     * For a song originally published as a part of a CD or LP, this would be the title of the original release.
     * This value is primarily for fuzzy lookups, though a user-agent may display it.
     * @return an album. May be <code>null</code>.
     * @see #setAlbum
     * @castor.field
     *  get-method="getAlbum"
     *  set-method="setAlbum"
     * @castor.field-xml
     *  name="album"
     *  node="element"
     */
    public String getAlbum()
    {
        return _album;
    }

    /**
     * Initializes a human-readable name of the collection from which the resource which defines the duration of track rendering comes.
     * @param album an album. May be <code>null</code>.
     * @see #getAlbum
     */
    public void setAlbum(final String album)
    {
        _album = StringUtils.normalize(album);
    }

    /**
     * Returns an integer with value greater than zero giving the ordinal position of the media on the xspf:album.
     * This value is primarily for fuzzy lookups, though a user-agent may display it.
     * @return a track number. May be <code>null</code>.
     * @see #setTrackNumber
     * @castor.field
     *  get-method="getTrackNumber"
     *  set-method="setTrackNumber"
     * @castor.field-xml
     *  name="trackNum"
     *  node="element"
     */
    public Integer getTrackNumber()
    {
        return _trackNum;
    }

    /**
     * Initializes an integer with value greater than zero giving the ordinal position of the media on the xspf:album.
     * @param trackNum a track number. May be <code>null</code>.
     * @throws IllegalArgumentException if the specified track number is not stricly positive.
     * @see #getTrackNumber
     */
    public void setTrackNumber(final Integer trackNum)
    {
        if ((trackNum != null) && (trackNum.intValue() <= 0))
        {
            throw new IllegalArgumentException("Negative or null track number");
        }

        _trackNum = trackNum;
    }

    /**
     * Returns the time to render a resource, in milliseconds.
     * This value is only a hint: different XSPF generators will generate slightly different values.
     * A user-agent MUST NOT use this value to determine the rendering duration, since the data will likely be low quality.
     * @return a duration. May be <code>null</code>.
     * @see #setDuration
     * @castor.field
     *  get-method="getDuration"
     *  set-method="setDuration"
     * @castor.field-xml
     *  name="duration"
     *  node="element"
     */
    public Integer getDuration()
    {
        return _duration;
    }

    /**
     * Initializes the time to render a resource, in milliseconds.
     * @param duration a duration. May be <code>null</code>.
     * @throws IllegalArgumentException if the specified duration is not stricly positive.
     * @see #getDuration
     * @see #setDuration(int)
     */
    public void setDuration(final Integer duration)
    {
        if ((duration != null) && (duration.intValue() <= 0))
        {
            throw new IllegalArgumentException("Negative or null duration");
        }

        _duration = duration;
    }

    /**
     * Initializes the time to render a resource, in milliseconds.
     * @param duration a duration.
     * @throws IllegalArgumentException if the duration is not stricly positive.
     * @see #getDuration
     * @see #setDuration(Integer)
     */
    public void setDuration(final int duration)
    {
        if (duration <= 0)
        {
            throw new IllegalArgumentException("Negative or null duration");
        }

        _duration = Integer.valueOf(duration);
    }

    /**
     * Returns a list of track extensions.
     * This element allows non-XSPF XML to be included in XSPF documents.
     * The purpose is to allow nested XML, which the meta and link elements do not.
     * @return a list of extensions. May be empty but not <code>null</code>.
     * @see #addExtension
     * @castor.field
     *  get-method="getExtensions"
     *  set-method="addExtension"
     *  type="other"
     *  collection="arraylist"
     * @castor.field-xml
     *  name="extension"
     *  node="element"
     */
    public List<AnyNode> getExtensions()
    {
        return _extensions;
    }

    /**
     * Adds an extension to this track.
     * <br>
     * The extension element must have an "application" attribute.
     * It's the URI of a resource defining the structure and purpose of the nested XML.
     * <br>
     * The input type is an {@link Object} just because it's what the Castor framework expects.
     * @param extension an extension. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>extension</code> is <code>null</code>.
     * @throws IllegalArgumentException if <code>extension</code> is not an {@link AnyNode} instance.
     * @throws IllegalArgumentException if no "application" attribute can be found in the specified extension element.
     * @see #getExtensions
     */
    public void addExtension(final Object extension)
    {
        if (!(extension instanceof AnyNode)) // Throws NullPointerException if extension is null.
        {
            throw new IllegalArgumentException(AnyNode.class + " expected");
        }

        final AnyNode anyNode = (AnyNode) extension;
        final AnyNode attr = anyNode.getFirstAttribute();

        if (attr == null)
        {
            throw new IllegalArgumentException("No application attribute");
        }

        if (!"application".equals(attr.getLocalName()))
        {
            throw new IllegalArgumentException("Unknown attribute");
        }

        _extensions.add(anyNode);
    }

    /**
     * Adds a link to this track.
     * @param link a link. Shall not be <code>null</code>
     * @throws NullPointerException if <code>link</code> is <code>null</code>.
     * @see #getLinks
     */
    public void addLink(final Link link)
    {
        if (link == null)
        {
            throw new NullPointerException("no link");
        }

        _links.add(link);
    }

    /**
     * Returns the list of links defined in this track.
     * The link element allows XSPF to be extended without the use of XML namespaces.
     * @return a list of links. May be empty but not <code>null</code>.
     * @see #addLink
     * @castor.field
     *  get-method="getLinks"
     *  set-method="addLink"
     *  type="christophedelory.playlist.xspf.Link"
     *  collection="arraylist"
     * @castor.field-xml
     *  name="link"
     *  node="element"
     */
    public List<Link> getLinks()
    {
        return _links;
    }

    /**
     * Adds a metadata to this playlist.
     * @param meta a metadata. Shall not be <code>null</code>
     * @throws NullPointerException if <code>meta</code> is <code>null</code>.
     * @see #getMetas
     */
    public void addMeta(final Meta meta)
    {
        if (meta == null)
        {
            throw new NullPointerException("no meta");
        }

        _metas.add(meta);
    }

    /**
     * Returns the list of metadata defined in this playlist.
     * The meta element allows metadata fields to be added to xspf:track elements.
     * @return a list of metadata. May be empty but not <code>null</code>.
     * @see #addMeta
     * @castor.field
     *  get-method="getMetas"
     *  set-method="addMeta"
     *  type="christophedelory.playlist.xspf.Meta"
     *  collection="arraylist"
     * @castor.field-xml
     *  name="meta"
     *  node="element"
     */
    public List<Meta> getMetas()
    {
        return _metas;
    }
}
