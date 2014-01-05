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

import christophedelory.lang.StringUtils;

/**
 * Determines the (x, y) coordinates of a point relative to a region upper-left corner
 * for use in aligning elements in the document's body on regions within a visual rendering surface.
 * A regPoint may be defined using absolute (pixel) or relative (percentage) based values.
 * @version $Revision: 92 $
 * @author Christophe Delory
 * @castor.class xml="regPoint"
 */
public class RegistrationPoint extends Position
{
    /**
     * Uniquely identifies the registration algorithm to be used for the regPoint defined for the object,
     * and overrides any regAlign attribute on the referenced regPoint element.
     */
    private String _regAlign = null;

    /**
     * The skip content controls whether the content of an element is evaluated or should be skipped.
     */
    private Boolean _skipContent = null;

    /**
     * Returns the registration algorithm.
     * The regAlign value uniquely identifies the registration algorithm to be used for the regPoint defined for the object,
     * and overrides any regAlign attribute on the referenced regPoint element.
     * @return the registration algorithm. May be <code>null</code>.
     * @see #setRegAlign
     * @castor.field
     *  get-method="getRegAlign"
     *  set-method="setRegAlign"
     * @castor.field-xml
     *  name="regAlign"
     *  node="attribute"
     */
    public String getRegAlign()
    {
        return _regAlign;
    }

    /**
     * Initializes the registration algorithm.
     * @param regAlign the registration algorithm. May be <code>null</code>.
     * @see #getRegAlign
     */
    public void setRegAlign(final String regAlign)
    {
        _regAlign = StringUtils.normalize(regAlign);
    }

    /**
     * Checks if the content may be skipped or not.
     * @return the associated boolean.
     * @see #getSkipContent
     */
    public boolean isSkipContent()
    {
        return (_skipContent == null) ? false : _skipContent.booleanValue();
    }

    /**
     * Checks if the content may be skipped or not.
     * The skip content controls whether the content of an element is evaluated or should be skipped.
     * @return the associated boolean. May be <code>null</code>.
     * @see #setSkipContent
     * @castor.field
     *  get-method="getSkipContent"
     *  set-method="setSkipContent"
     * @castor.field-xml
     *  name="skip-content"
     *  node="attribute"
     */
    public Boolean getSkipContent()
    {
        return _skipContent;
    }

    /**
     * Specifies if the content may be skipped or not.
     * @param skipContent the associated boolean. May be <code>null</code>.
     * @see #getSkipContent
     */
    public void setSkipContent(final Boolean skipContent)
    {
        _skipContent = skipContent;
    }
}
