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
 * A Real Metadata Package provider.
 * @since 0.3.0
 * @version $Revision: 92 $
 * @author Christophe Delory
 */
@XmlRootElement(name="PROVIDER")
public class Provider
{
    /**
     * The author.
     */
    private String _author = null;

    /**
     * The provider name.
     */
    private String _name = null;

    /**
     * The URL.
     */
    private String _url = null;

    /**
     * The copyright information.
     */
    private String _copyright = null;

    /**
     * The provider contact.
     */
    private String _contact = null;

    /**
     * Returns the provider author.
     * This is typically the artist name.
     * @return an author. May be <code>null</code>.
     * @see #setAuthor
     */
    @XmlElement(name="AUTHOR")
    public String getAuthor()
    {
        return _author;
    }

    /**
     * Initializes the author of this provider.
     * @param author an author. May be <code>null</code>.
     * @see #getAuthor
     */
    public void setAuthor(final String author)
    {
        _author = StringUtils.normalize(author);
    }

    /**
     * Returns the provider name.
     * This is typically the record company name.
     * @return a name. May be <code>null</code>.
     * @see #setName
     */
    @XmlElement(name="NAME")
    public String getName()
    {
        return _name;
    }

    /**
     * Initializes the provider name.
     * @param name a name. May be <code>null</code>.
     * @see #getName
     */
    public void setName(final String name)
    {
        _name = StringUtils.normalize(name);
    }

    /**
     * Returns the URL that users will access in order to get this music.
     * @return an URL. May be <code>null</code>.
     * @see #setUrlString
     */
    @XmlElement(name="URL")
    public String getUrlString()
    {
        return _url;
    }

    /**
     * Initializes the URL that users will access in order to get this music.
     * @param url an URL. May be <code>null</code>.
     * @see #getUrlString
     */
    public void setUrlString(final String url)
    {
        _url = StringUtils.normalize(url);
    }

    /**
     * Returns the provider copyright information.
     * Typically a record company copyright.
     * @return a copyright information. May be <code>null</code>.
     * @see #setCopyright
     */
    @XmlElement(name="COPYRIGHT")
    public String getCopyright()
    {
        return _copyright;
    }

    /**
     * Initializes the provider copyright information.
     * @param copyright a copyright information. May be <code>null</code>.
     * @see #getCopyright
     */
    public void setCopyright(final String copyright)
    {
        _copyright = StringUtils.normalize(copyright);
    }

    /**
     * Returns the provider contact.
     * An email for the user to contact if they are having trouble with the download.
     * @return a contact. May be <code>null</code>.
     * @see #setContact
     */
    @XmlElement(name="CONTACT")
    public String getContact()
    {
        return _contact;
    }

    /**
     * Initializes the provider contact.
     * @param contact a contact. May be <code>null</code>.
     * @see #getContact
     */
    public void setContact(final String contact)
    {
        _contact = StringUtils.normalize(contact);
    }
}
