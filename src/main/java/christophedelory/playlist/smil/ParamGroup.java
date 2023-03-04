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

import java.util.ArrayList;
import java.util.List;

/**
 * This element provides a convenience mechanism for defining a group of media parameters that may be used with several different media objects.
 * If present, the paramGroup element must appear in the head section of the document.
 * The content of the paramGroup element consists of zero or more param elements.
 * The paramGroup element may not contain nested paramGroup element definitions.
 * @version $Revision: 92 $
 * @author Christophe Delory
 * @castor.class xml="paramGroup"
 */
public class ParamGroup extends Core
{
    /**
     * The skip content controls whether the content of an element is evaluated or should be skipped.
     */
    private Boolean _skipContent = null;

    /**
     * A list of parameters.
     */
    private final List<Param> _params = new ArrayList<Param>();

    /**
     * Checks if the content may be skipped or not.
     * @return the associated boolean.
     * @see #getSkipContent
     */
    public boolean isSkipContent()
    {
        return _skipContent != null && _skipContent.booleanValue();
    }

    /**
     * Checks if the content may be skipped or not.
     * The skip content controls whether the content of an element is evaluated or should be skipped.
     * @return the associated boolean. May be <code>null</code>.
     * @see #setSkipContent
     * @see #isSkipContent
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

    /**
     * Returns the list of parameters defined in this group of parameters.
     * @return the list of parameters. May be empty but not <code>null</code>.
     * @see #addParam
     * @castor.field
     *  get-method="getParams"
     *  set-method="addParam"
     *  type="christophedelory.playlist.smil.Param"
     *  collection="arraylist"
     * @castor.field-xml
     *  name="param"
     *  node="element"
     */
    public List<Param> getParams()
    {
        return _params;
    }

    /**
     * Adds a parameter to this group of parameters.
     * @param param a parameter. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>param</code> is <code>null</code>.
     * @see #getParams
     */
    public void addParam(final Param param)
    {
        if (param == null)
        {
            throw new NullPointerException("no param");
        }

        _params.add(param);
    }
}
