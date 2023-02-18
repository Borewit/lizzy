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
package christophedelory.playlist.rmp;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import christophedelory.lang.StringUtils;

/**
 * The server definition in a Real Metadata Package.
 * @since 0.3.0
 * @version $Revision: 92 $
 * @author Christophe Delory
 */
@XmlRootElement(name="SERVER")
public class Server
{
    /**
     * The server name.
     */
    private String _name = null;

    /**
     * The server description.
     */
    private String _desc = null;

    /**
     * The net name.
     */
    private String _netName = null;

    /**
     * The server location.
     */
    private String _location = "%f";

    /**
     * The server key.
     */
    private String _key = null;

    /**
     * Returns the server name.
     * Optional.
     * Example: "Jazz Center".
     * @return a name. May be <code>null</code>.
     * @see #setName
     */
    @XmlElement(name="NAME")
    public String getName()
    {
        return _name;
    }

    /**
     * Initializes the server name.
     * @param name a name. May be <code>null</code>.
     * @see #getName
     */
    public void setName(final String name)
    {
        _name = StringUtils.normalize(name);
    }

    /**
     * Returns the server description.
     * Optional.
     * Example: "Your Center for Jazz".
     * @return a description. May be <code>null</code>.
     * @see #setDescription
     */
    @XmlElement(name="DESC")
    public String getDescription()
    {
        return _desc;
    }

    /**
     * Initializes the description of this server.
     * @param desc a description. May be <code>null</code>.
     * @see #getDescription
     */
    public void setDescription(final String desc)
    {
        _desc = StringUtils.normalize(desc);
    }

    /**
     * Returns the server network name.
     * Basically, the main portion of the URL for where the tracks will exist, minus the http:// that you would normally place in a browser URL.
     * @return a network name. May be <code>null</code>.
     * @see #setNetName
     */
    @XmlElement(name="NETNAME")
    public String getNetName()
    {
        return _netName;
    }

    /**
     * Initializes the server network name.
     * @param netName a network name. May be <code>null</code>.
     * @see #getNetName
     */
    public void setNetName(final String netName)
    {
        _netName = StringUtils.normalize(netName);
    }

    /**
     * Returns the server location.
     * This is the remainder of the path to the music tracks on the server.
     * <br>
     * Example: "/docs/rmpdemo/%f": if the network name is "docs.real.com", includes for tracks that exist on http://docs.real.com/docs/rmpdemo/.
     * The %f tells RealJukebox to look in the tracklist for the exact filename that exists on the server.
     * <dl>
     * <dt>%f</dt><dd>File name</dd>
     * <dt>%fid</dt><dd>Track id</dd>
     * <dt>%lid</dt><dd>Tracklist ID</dd>
     * <dt>%pid</dt><dd>Package id</dd>
     * </dl>
     * Defaults to "%f".
     * @return a location. Shall not be <code>null</code>.
     * @see #setLocation
     */
    @XmlElement(name="LOCATION", required=true)
    public String getLocation()
    {
        return _location;
    }

    /**
     * Initializes the server location.
     * @param location a location. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>location</code> is <code>null</code>.
     * @see #getLocation
     */
    public void setLocation(final String location)
    {
        _location = location.trim(); // Throws NullPointerException if location is null.
    }

    /**
     * Returns the server key.
     * Currently not used.
     * @return a key. May be <code>null</code>.
     * @see #setKey
     */
    @XmlElement(name="KEY")
    public String getKey()
    {
        return _key;
    }

    /**
     * Initializes the server key.
     * @param key a key. May be <code>null</code>.
     * @see #getKey
     */
    public void setKey(final String key)
    {
        _key = StringUtils.normalize(key);
    }
}
