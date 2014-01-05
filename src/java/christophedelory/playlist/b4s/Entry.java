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
package christophedelory.playlist.b4s;

import christophedelory.lang.StringUtils;

/**
 * A B4S playlist entry.
 * @version $Revision: 92 $
 * @author Christophe Delory
 * @castor.class xml="entry"
 */
public class Entry
{
    /**
     * The play string.
     */
    private String _playstring = null;

    /**
     * A name, if any.
     */
    private String _name = null;

    /**
     * A length, if any.
     */
    private Integer _length = null;

    /**
     * Returns the play string attached to this entry.
     * @return a play string. May be <code>null</code> if not yet initialized.
     * @see #setPlaystring
     * @castor.field
     *  get-method="getPlaystring"
     *  set-method="setPlaystring"
     *  required="true"
     * @castor.field-xml
     *  name="Playstring"
     *  node="attribute"
     */
    public String getPlaystring()
    {
        return _playstring;
    }

    /**
     * Initializes the play string attached to this entry.
     * @param playstring a play string. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>playstring</code> is <code>null</code>.
     * @see #getPlaystring
     */
    public void setPlaystring(final String playstring)
    {
        _playstring = playstring.trim().replace('\\', '/'); // Throws NullPointerException if playstring is null.
    }

    /**
     * Returns the name of this entry.
     * @return a name. May be <code>null</code>.
     * @see #setName
     * @castor.field
     *  get-method="getName"
     *  set-method="setName"
     * @castor.field-xml
     *  name="Name"
     *  node="element"
     */
    public String getName()
    {
        return _name;
    }

    /**
     * Initializes the name of this entry.
     * @param name a name. May be <code>null</code>.
     * @see #getName
     */
    public void setName(final String name)
    {
        _name = StringUtils.normalize(name);
    }

    /**
     * Returns the length of this entry, in bytes.
     * @return a length. May be <code>null</code>.
     * @see #setLength
     * @castor.field
     *  get-method="getLength"
     *  set-method="setLength"
     * @castor.field-xml
     *  name="Length"
     *  node="element"
     */
    public Integer getLength()
    {
        return _length;
    }

    /**
     * Initializes the length of this entry, in bytes.
     * @param length a length. May be <code>null</code>.
     * @throws IllegalArgumentException if the specified length is not positive.
     * @see #getLength
     * @see #setLength(int)
     */
    public void setLength(final Integer length)
    {
        if ((length != null) && (length.intValue() < 0))
        {
            throw new IllegalArgumentException("Negative length");
        }

        _length = length;
    }

    /**
     * Initializes the length of this entry, in bytes.
     * @param length a length. Shall not be strictly negative.
     * @throws IllegalArgumentException if the specified length is not positive.
     * @see #getLength
     * @see #setLength(Integer)
     */
    public void setLength(final int length)
    {
        if (length < 0)
        {
            throw new IllegalArgumentException("Negative length");
        }

        _length = Integer.valueOf(length);
    }
}
