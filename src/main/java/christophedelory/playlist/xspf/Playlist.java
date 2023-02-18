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

import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.exolab.castor.types.AnyNode;

import christophedelory.content.Content;
import christophedelory.lang.StringUtils;
import christophedelory.playlist.Media;
import christophedelory.playlist.SpecificPlaylist;
import christophedelory.playlist.SpecificPlaylistProvider;
import christophedelory.xml.XmlSerializer;

/**
 * XSPF, an XML format designed to enable playlist sharing.
 * @version $Revision: 91 $
 * @author Christophe Delory
 * @castor.class xml="playlist" ns-uri="http://xspf.org/ns/0/"
 */
public class Playlist implements SpecificPlaylist
{
    /**
     * The provider of this specific playlist.
     */
    private transient SpecificPlaylistProvider _provider = null;

    /**
     * The version number.
     */
    private Integer _version = Integer.valueOf(1);

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
     * A location, if any.
     */
    private String _location = null;

    /**
     * An identifier, if any.
     */
    private String _identifier = null;

    /**
     * An image, if any.
     */
    private String _image = null;

    /**
     * A date, if any.
     */
    private Date _date = null;

    /**
     * A license, if any.
     */
    private String _license = null;

    /**
     * An attribution, if any.
     */
    private Attribution _attribution = null;

    /**
     * A list of links.
     */
    private final List<Link> _links = new ArrayList<Link>();

    /**
     * A list of metadata.
     */
    private final List<Meta> _metas = new ArrayList<Meta>();

    /**
     * A list of tracks.
     */
    private final List<Track> _tracks = new ArrayList<Track>();

    /**
     * A list of extensions.
     */
    private final List<AnyNode> _extensions = new ArrayList<AnyNode>();

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
        // Marshal the playlist.
        final StringWriter writer = new StringWriter();
        final XmlSerializer serializer = XmlSerializer.getMapping("christophedelory/playlist/xspf"); // May throw Exception.
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
    public christophedelory.playlist.Playlist toPlaylist()
    {
        final christophedelory.playlist.Playlist ret = new christophedelory.playlist.Playlist();

        for (Track track : _tracks)
        {
            for (StringContainer stringContainer : track.getStringContainers())
            {
                if ((stringContainer instanceof Location) && (stringContainer.getText() != null))
                {
                    final Media media = new Media(); // NOPMD Avoid instantiating new objects inside loops
                    final Content content = new Content(stringContainer.getText()); // NOPMD Avoid instantiating new objects inside loops
                    media.setSource(content);

                    if (track.getDuration() != null)
                    {
                        content.setDuration(track.getDuration().longValue());
                    }

                    ret.getRootSequence().addComponent(media);
                }
            }
        }

        // We don't really need it.
        ret.normalize();

        return ret;
    }

    /**
     * Returns the version number.
     * Default value is 1.
     * Notice that the namespace is 0 but the version is 1.
     * This is because version 1 playlists are backwards compatible with version 0 parsers.
     * @return a version number. Shall not be <code>null</code>.
     * @see #setVersion
     * @castor.field
     *  get-method="getVersion"
     *  set-method="setVersion"
     *  required="true"
     * @castor.field-xml
     *  name="version"
     *  node="attribute"
     */
    public Integer getVersion()
    {
        return _version;
    }

    /**
     * Initializes the version number.
     * @param version a version number. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>version</code> is <code>null</code>.
     * @see #getVersion
     */
    public void setVersion(final Integer version)
    {
        if (version == null)
        {
            throw new NullPointerException("no version number");
        }

        _version = version;
    }

    /**
     * Returns a human-readable title for the playlist.
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
     * Initializes a human-readable title for the playlist.
     * @param title a title. May be <code>null</code>.
     * @see #getTitle
     */
    public void setTitle(final String title)
    {
        _title = StringUtils.normalize(title);
    }

    /**
     * Returns a human-readable name of the entity (author, authors, group, company, etc) that authored the playlist.
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
     * Initializes a human-readable name of the entity (author, authors, group, company, etc) that authored the playlist.
     * @param creator a creator. May be <code>null</code>.
     * @see #getCreator
     */
    public void setCreator(final String creator)
    {
        _creator = StringUtils.normalize(creator);
    }

    /**
     * Returns a human-readable comment on the playlist.
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
     * Initializes a human-readable comment on the playlist.
     * @param annotation an annotation. May be <code>null</code>.
     * @see #getAnnotation
     */
    public void setAnnotation(final String annotation)
    {
        _annotation = StringUtils.normalize(annotation);
    }

    /**
     * Returns an URI of a web page to find out more about this playlist.
     * Likely to be homepage of the author, and would be used to find out more about the author and to find more playlists by the author.
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
     * Initializes an URI of a web page to find out more about this playlist.
     * @param info an information. May be <code>null</code>.
     * @see #getInfo
     */
    public void setInfo(final String info)
    {
        _info = StringUtils.normalize(info);
    }

    /**
     * Returns a source URI for this playlist.
     * @return a location. May be <code>null</code>.
     * @see #setLocation
     * @castor.field
     *  get-method="getLocation"
     *  set-method="setLocation"
     * @castor.field-xml
     *  name="location"
     *  node="element"
     */
    public String getLocation()
    {
        return _location;
    }

    /**
     * Initializes a source URI for this playlist.
     * @param location a location. May be <code>null</code>.
     * @see #getLocation
     */
    public void setLocation(final String location)
    {
        _location = StringUtils.normalize(location);
    }

    /**
     * Returns a canonical ID for this playlist.
     * @return an identifier. May be <code>null</code>.
     * @see #setIdentifier
     * @castor.field
     *  get-method="getIdentifier"
     *  set-method="setIdentifier"
     * @castor.field-xml
     *  name="identifier"
     *  node="element"
     */
    public String getIdentifier()
    {
        return _identifier;
    }

    /**
     * Initializes a canonical ID for this playlist.
     * @param identifier an identifier. May be <code>null</code>.
     * @see #getIdentifier
     */
    public void setIdentifier(final String identifier)
    {
        _identifier = StringUtils.normalize(identifier);
    }

    /**
     * Returns an URI of an image to display in the absence of a //playlist/trackList/image element.
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
     * Initializes an URI of an image to display in the absence of a //playlist/trackList/image element.
     * @param image an image. May be <code>null</code>.
     * @see #getImage
     */
    public void setImage(final String image)
    {
        _image = StringUtils.normalize(image);
    }

    /**
     * Returns a creation date (not last-modified date) of the playlist.
     * The underlying string is formatted as a XML schema dateTime.
     * A sample date is "2005-01-08T17:10:47-05:00".
     * In the absence of a timezone, the element MAY be assumed to use Coordinated Universal Time (UTC, sometimes called "Greenwich Mean Time").
     * <br>
     * Note: in version 0 of XSPF, this was specified as an ISO 8601 date.
     * xsd:dateTime is the same thing (with better documentation) for almost every date in history,
     * and there are no playlist creation dates that might be different.
     * @return a date. May be <code>null</code>.
     * @see #setDate
     * @castor.field
     *  get-method="getDate"
     *  set-method="setDate"
     * @castor.field-xml
     *  name="date"
     *  node="element"
     */
    public Date getDate()
    {
        return _date;
    }

    /**
     * Initializes a creation date (not last-modified date) of the playlist.
     * @param date a date. May be <code>null</code>.
     * @see #getDate
     */
    public void setDate(final Date date)
    {
        _date = date;
    }

    /**
     * Returns an URI of a resource that describes the license under which this playlist was released.
     * @return a license. May be <code>null</code>.
     * @see #setLicense
     * @castor.field
     *  get-method="getLicense"
     *  set-method="setLicense"
     * @castor.field-xml
     *  name="license"
     *  node="element"
     */
    public String getLicense()
    {
        return _license;
    }

    /**
     * Initializes an URI of a resource that describes the license under which this playlist was released.
     * @param license a license. May be <code>null</code>.
     * @see #getLicense
     */
    public void setLicense(final String license)
    {
        _license = StringUtils.normalize(license);
    }

    /**
     * Returns the playlist's attribution.
     * @return an attribution. May be <code>null</code>.
     * @see #setAttribution
     * @castor.field
     *  get-method="getAttribution"
     *  set-method="setAttribution"
     * @castor.field-xml
     *  name="attribution"
     *  node="element"
     */
    public Attribution getAttribution()
    {
        return _attribution;
    }

    /**
     * Initializes this playlist's attribution.
     * @param attribution an attribution. May be <code>null</code>.
     * @see #getAttribution
     */
    public void setAttribution(final Attribution attribution)
    {
        _attribution = attribution;
    }

    /**
     * Returns a list of playlist extensions.
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
     * Adds an extension to this playlist.
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
     * Adds a link to this playlist.
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
     * Returns the list of links defined in this playlist.
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
     * The meta element allows metadata fields to be added to XSPF.
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

    /**
     * Adds a track to this playlist.
     * @param track a track. Shall not be <code>null</code>
     * @throws NullPointerException if <code>track</code> is <code>null</code>.
     * @see #getTracks
     */
    public void addTrack(final Track track)
    {
        if (track == null)
        {
            throw new NullPointerException("no track");
        }

        _tracks.add(track);
    }

    /**
     * Returns an ordered list of xspf:track elements to be rendered.
     * The sequence is a hint, not a requirement; renderers are advised to play tracks from top to bottom unless there is an indication otherwise.
     * If an xspf:track element cannot be rendered, a user-agent MUST skip to the next xspf:track element and MUST NOT interrupt the sequence.
     * @return a list of tracks. May be empty but not <code>null</code>.
     * @see #addTrack
     * @castor.field
     *  get-method="getTracks"
     *  set-method="addTrack"
     *  type="christophedelory.playlist.xspf.Track"
     *  collection="arraylist"
     * @castor.field-xml
     *  name="track"
     *  location="trackList"
     *  node="element"
     */
    public List<Track> getTracks()
    {
        return _tracks;
    }
}
