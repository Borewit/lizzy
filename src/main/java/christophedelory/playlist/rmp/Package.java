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

import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import christophedelory.content.Content;
import christophedelory.lang.StringUtils;
import christophedelory.playlist.Media;
import christophedelory.playlist.Playlist;
import christophedelory.playlist.SpecificPlaylist;
import christophedelory.playlist.SpecificPlaylistProvider;

/**
 * A Real Metadata Package.
 * @since 0.3.0
 * @version $Revision: 92 $
 * @author Christophe Delory
 */
@XmlRootElement(name="PACKAGE")
public class Package implements SpecificPlaylist
{
    /**
     * The internal date and time format.
     */
    private static final ThreadLocal<DateFormat> datetimeFormat = ThreadLocal.withInitial(() -> new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.US)); // Should not throw NullPointerException, IllegalArgumentException.

    /**
     * The provider of this specific playlist.
     */
    private transient SpecificPlaylistProvider _provider = null;

    /**
     * The title.
     */
    private String _title = null;

    /**
     * The action.
     */
    private String _action = null;

    /**
     * The target.
     */
    private String _target = null;

    /**
     * The expiration date.
     */
    private Date _expirationDate = null;

    /**
     * The package provider.
     */
    private Provider _packageProvider = null;

    /**
     * The package server.
     */
    private Server _server = new Server();

    /**
     * The track list.
     */
    private Tracklist _trackList = new Tracklist();

    /**
     * The signature.
     */
    private String _signature = null;

    /**
     * Builds a new and empty Real Metadata Package.
     */
    public Package()
    {
        _trackList.setParent(this);
    }

    @Override
    public void setProvider(final SpecificPlaylistProvider provider)
    {
        _provider = provider;
    }

    @XmlTransient
    @Override
    public SpecificPlaylistProvider getProvider()
    {
        return _provider;
    }

    @Override
    public void writeTo(final OutputStream out, final String encoding) throws Exception
    {
        String enc = encoding;

        if (enc == null)
        {
            enc = "UTF-8";
        }

        // Marshal the playlist.
        final JAXBContext jc = JAXBContext.newInstance("christophedelory.playlist.rmp"); // May throw JAXBException.
        final Marshaller marshaller = jc.createMarshaller(); // May throw JAXBException.
        marshaller.setProperty(Marshaller.JAXB_ENCODING, enc); // May throw PropertyException. Shall not throw IllegalArgumentException.
        // Specifies whether XML documents (as generated at marshalling) should use indentation or not. Default is false.
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); // Shall not throw PropertyException, IllegalArgumentException.
        marshaller.marshal(this, out); // May throw JAXBException, MarshalException. Shall not throw IllegalArgumentException.
        out.flush(); // May throw IOException.
    }

    @Override
    public Playlist toPlaylist()
    {
        final Playlist ret = new Playlist();

        String location = _server.getLocation();
        location = location.replace("%lid", (_trackList.getId() == null) ? "" : _trackList.getId());
        location = location.replace("%pid", (_target == null) ? "" : _target);

        for (Track track : _trackList.getTracks())
        {
            String url = location.replace("%fid", (track.getId() == null) ? "" : track.getId());
            // AFTER %fid replacement
            url = url.replace("%f", (track.getFileName() == null) ? "" : track.getFileName());

            if (_server.getNetName() != null)
            {
                final StringBuilder sb = new StringBuilder("http://"); // NOPMD Avoid instantiating new objects inside loops
                sb.append(_server.getNetName());
                sb.append(url);
                url = sb.toString();
            }

            final Media media = new Media(); // NOPMD Avoid instantiating new objects inside loops
            final Content content = new Content(url); // NOPMD Avoid instantiating new objects inside loops

            if (track.getSize() != null)
            {
                content.setLength(track.getSize().longValue());
            }

            if (track.getDuration() != null)
            {
                content.setDuration(track.getDuration().longValue() * 1000L);
            }

            media.setSource(content);
            ret.getRootSequence().addComponent(media);
        }

        // We don't really need it.
        ret.normalize();

        return ret;
    }

    /**
     * Returns the package title.
     * @return a title. May be <code>null</code>.
     * @see #setTitle
     */
    @XmlElement(name="TITLE")
    public String getTitle()
    {
        return _title;
    }

    /**
     * Initializes the package title.
     * @param title a title. May be <code>null</code>.
     * @see #getTitle
     */
    public void setTitle(final String title)
    {
        _title = StringUtils.normalize(title);
    }

    /**
     * Returns the package action.
     * @return an action. May be <code>null</code>.
     * @see #setAction
     */
    @XmlElement(name="ACTION")
    public String getAction()
    {
        return _action;
    }

    /**
     * Initializes the package action.
     * @param action an action. May be <code>null</code>.
     * @see #getAction
     */
    public void setAction(final String action)
    {
        _action = StringUtils.normalize(action);
    }

    /**
     * Returns the package target.
     * @return a target. May be <code>null</code>.
     * @see #setTarget
     */
    @XmlElement(name="TARGET")
    public String getTarget()
    {
        return _target;
    }

    /**
     * Initializes the package target.
     * @param target a target. May be <code>null</code>.
     * @see #getTarget
     */
    public void setTarget(final String target)
    {
        _target = StringUtils.normalize(target);
    }

    /**
     * Returns the package expiration date.
     * @return a date. May be <code>null</code>.
     * @see #setExpirationDateString
     * @see #getExpirationDate
     */
    @XmlElement(name="EXP_DATE")
    public String getExpirationDateString()
    {
        String ret = null;

        if (_expirationDate != null)
        {
            synchronized(datetimeFormat.get())
            {
                ret = datetimeFormat.get().format(_expirationDate); // Should not throw NullPointerException because of _expirationDate.
            }
        }

        return ret;
    }

    /**
     * Initializes the package expiration date.
     * @param expirationDate a date. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>expirationDate</code> is <code>null</code>.
     * @throws ParseException if the beginning of the specified string cannot be parsed.
     * @see #getExpirationDateString
     * @see #setExpirationDate
     */
    public void setExpirationDateString(final String expirationDate) throws ParseException
    {
        synchronized(datetimeFormat.get())
        {
            _expirationDate = datetimeFormat.get().parse(expirationDate); // May throw ParseException. Throws NullPointerException if expirationDate is null.
        }
    }

    /**
     * Returns the package expiration date.
     * @return a date. May be <code>null</code>.
     * @see #setExpirationDate
     * @see #getExpirationDateString
     */
    @XmlTransient
    public Date getExpirationDate()
    {
        return _expirationDate;
    }

    /**
     * Initializes the package expiration date.
     * @param expirationDate a date. May be <code>null</code>.
     * @see #getExpirationDate
     * @see #setExpirationDateString
     */
    public void setExpirationDate(final Date expirationDate)
    {
        _expirationDate = expirationDate;
    }

    /**
     * Returns the package provider.
     * @return a package provider. May be <code>null</code>.
     * @see #setPackageProvider
     */
    @XmlElement(name="PROVIDER")
    public Provider getPackageProvider()
    {
        return _packageProvider;
    }

    /**
     * Initializes the package provider.
     * @param provider a provider. May be <code>null</code>.
     * @see #getPackageProvider
     */
    public void setPackageProvider(final Provider provider)
    {
        _packageProvider = provider;
    }

    /**
     * Returns the package server.
     * @return a package server. Shall not be <code>null</code>.
     * @see #setServer
     */
    @XmlElement(name="SERVER", required=true)
    public Server getServer()
    {
        return _server;
    }

    /**
     * Initializes the package server.
     * @param server a server. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>server</code> is <code>null</code>.
     * @see #getServer
     */
    public void setServer(final Server server)
    {
        if (server == null)
        {
            throw new NullPointerException("no server");
        }

        _server = server;
    }

    /**
     * Returns the track list.
     * @return a track list. Shall not be <code>null</code>.
     * @see #setTracklist
     */
    @XmlElement(name="TRACKLIST", required=true)
    public Tracklist getTracklist()
    {
        return _trackList;
    }

    /**
     * Initializes the track list.
     * @param trackList a track list. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>trackList</code> is <code>null</code>.
     * @see #getTracklist
     */
    public void setTracklist(final Tracklist trackList)
    {
        trackList.setParent(this); // Throws NullPointerException if trackList is null.
        _trackList = trackList;
    }

    /**
     * Returns the package signature.
     * @return a signature. May be <code>null</code>.
     * @see #setSignature
     */
    @XmlElement(name="SIG")
    public String getSignature()
    {
        return _signature;
    }

    /**
     * Initializes the package signature.
     * @param signature a signature. May be <code>null</code>.
     * @see #getSignature
     */
    public void setSignature(final String signature)
    {
        _signature = StringUtils.normalize(signature);
    }
}
