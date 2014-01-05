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

import christophedelory.lang.StringUtils;

/**
 * The marker at which Windows Media Player starts or ends rendering the stream.
 * This element should have a value for either the NUMBER or NAME attribute, but not both.
 * <br>
 * Windows Media Player version 7.0 or later.
 * @version $Revision: 92 $
 * @author Christophe Delory
 * @castor.class xml="marker"
 */
public class Marker extends Child
{
    /**
     * The name of a named marker in the index.
     */
    private String _name = null;

    /**
     * The number of a numeric marker in the index.
     */
    private Integer _number = null;

    /**
     * Returns the name of a named marker in the index.
     * The default value is:
     * <ul>
     * <li>the beginning of the on-demand content, or the current position in a real-time stream.</li>
     * <li>the end of the content.</li>
     * </ul>
     * @return the name of a named marker in the index. May be <code>null</code>.
     * @see #setName
     * @castor.field
     *  get-method="getName"
     *  set-method="setName"
     * @castor.field-xml
     *  name="name"
     *  node="attribute"
     */
    public String getName()
    {
        return _name;
    }

    /**
     * Initializes the name of a named marker in the index.
     * @param name the name to a named marker in the index. May be <code>null</code>.
     * @throws IllegalStateException if a number has already been specified.
     * @see #getName
     */
    public void setName(final String name)
    {
        final String n = StringUtils.normalize(name);

        if ((n != null) && (_number != null))
        {
            throw new IllegalStateException("Use a marker with either a number or a name, but not both");
        }

        _name = n;
    }

    /**
     * Returns the number of a numeric marker in the index.
     * The default value is:
     * <ul>
     * <li>the beginning of the on-demand content, or the current position in a real-time stream.</li>
     * <li>the end of the content.</li>
     * </ul>
     * @return the number of a numeric marker in the index. May be <code>null</code>.
     * @see #setNumber
     * @castor.field
     *  get-method="getNumber"
     *  set-method="setNumber"
     * @castor.field-xml
     *  name="number"
     *  node="attribute"
     */
    public Integer getNumber()
    {
        return _number;
    }

    /**
     * Initializes the number of a numeric marker in the index.
     * @param number the number of a numeric marker in the index. May be <code>null</code>.
     * @throws IllegalStateException if a name has already been specified.
     * @see #getNumber
     */
    public void setNumber(final Integer number)
    {
        if ((number != null) && (_name != null))
        {
            throw new IllegalStateException("Use a marker with either a number or a name, but not both");
        }

        _number = number;
    }
}
