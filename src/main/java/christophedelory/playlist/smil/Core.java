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
 * The base class which contains all common attributes of a SMIL object.
 * @version $Revision: 92 $
 * @author Christophe Delory
 */
public class Core
{
    /**
     * Defines the class of the SMIL object.
     */
    private String _className = null;

    /**
     * The textual identifier of this SMIL object.
     */
    private String _id = null;

    /**
     * Represents the language associated to this SMIL object.
     */
    private String _lang = null;

    /**
     * Represents the title of the SMIL object.
     */
    private String _title = null;

    /**
     * Returns the class name as a string.
     * @return a class name. May be <code>null</code>.
     * @see #setClassName
     * @castor.field
     *  get-method="getClassName"
     *  set-method="setClassName"
     * @castor.field-xml
     *  name="class"
     *  node="attribute"
     */
    public String getClassName()
    {
        return _className;
    }

    /**
     * Initializes the class name as a string.
     * @param className a class name. May be <code>null</code>.
     * @see #getClassName
     */
    public void setClassName(final String className)
    {
        _className = StringUtils.normalize(className);
    }

    /**
     * Returns the textual identifier of this SMIL core object.
     * @return a textual identifier. May be <code>null</code>.
     * @see #setId
     * @castor.field
     *  get-method="getId"
     *  set-method="setId"
     * @castor.field-xml
     *  name="id"
     *  node="attribute"
     */
    public String getId()
    {
        return _id;
    }

    /**
     * Initializes the textual identifier of this SMIL core object.
     * @param id a textual identifier. May be <code>null</code>.
     * @see #getId
     */
    public void setId(final String id)
    {
        _id = StringUtils.normalize(id);
    }

    /**
     * Returns the language associated to this SMIL object as a string.
     * @return a language. May be <code>null</code>.
     * @see #setLang
     * @castor.field
     *  get-method="getLang"
     *  set-method="setLang"
     * @castor.field-xml
     *  name="xml:lang"
     *  node="attribute"
     */
    public String getLang()
    {
        return _lang;
    }

    /**
     * Initializes the language associated to this SMIL object as a string.
     * @param lang a language. May be <code>null</code>.
     * @see #getLang
     */
    public void setLang(final String lang)
    {
        _lang = StringUtils.normalize(lang);
    }

    /**
     * Returns the title of the SMIL object.
     * @return a title. May be <code>null</code>.
     * @see #setTitle
     * @castor.field
     *  get-method="getTitle"
     *  set-method="setTitle"
     * @castor.field-xml
     *  name="title"
     *  node="attribute"
     */
    public String getTitle()
    {
        return _title;
    }

    /**
     * Initializes the title of the SMIL object.
     * @param title a title. May be <code>null</code>.
     * @see #getTitle
     */
    public void setTitle(final String title)
    {
        _title = StringUtils.normalize(title);
    }
}
