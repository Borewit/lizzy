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
 * The element which allows a general parameter value to be sent to a media object renderer as a name/value pair.
 * This parameter is sent to the renderer at the time that the media object is processed by the scheduler.
 * Any number of param elements may appear (in any order) in the content of a media object element or in a paramGroup element.
 * If a given parameter is defined multiple times, the lexically last version of that parameter value should be used.
 * @version $Revision: 92 $
 * @author Christophe Delory
 * @castor.class xml="param"
*/
public class Param extends ParamGroup
{
    /**
     * Defines the name of a run-time parameter, assumed to be known by the inserted object.
     */
    private String _name = "";

    /**
     * Specifies the content type of the resource designated by the value attribute only in the case where valuetype is set to "ref".
     */
    private String _type = null;

    /**
     * Specifies the value of the run-time parameter specified by name.
     */
    private String _value = null;

    /**
     * Specifies the type of the value attribute.
     */
    private String _valueType = null;

    /**
     * Returns the name of the run-time parameter, assumed to be known by the inserted object.
     * Whether the property name is case-sensitive depends on the specific object implementation.
     * @return a name. Shall not be <code>null</code>.
     * @see #setName
     * @castor.field
     *  get-method="getName"
     *  set-method="setName"
     *  required="true"
     * @castor.field-xml
     *  name="name"
     *  node="attribute"
     */
    public String getName()
    {
        return _name;
    }

    /**
     * Initializes the name of the run-time parameter.
     * @param name a name. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>name</code> is <code>null</code>.
     * @see #getName
     */
    public void setName(final String name)
    {
        _name = name.trim(); // Throws NullPointerException if name is null.
    }

    /**
     * Returns the content type of the resource designated by the value attribute only in the case where valuetype is set to "ref".
     * This attribute thus specifies for the user agent, the type of values that will be found at the URI designated by value.
     * @return a content type. May be <code>null</code>.
     * @see #setType
     * @castor.field
     *  get-method="getType"
     *  set-method="setType"
     * @castor.field-xml
     *  name="type"
     *  node="attribute"
     */
    public String getType()
    {
        return _type;
    }

    /**
     * Initializes the content type of the resource designated by the value attribute only in the case where valuetype is set to "ref".
     * @param type a content type. May be <code>null</code>.
     * @see #getType
     */
    public void setType(final String type)
    {
        _type = StringUtils.normalize(type);
    }

    /**
     * Returns the value of the run-time parameter specified by name.
     * Property values have no meaning to SMIL; their meaning is determined by the object in question.
     * @return a value as a string. May be <code>null</code>.
     * @see #setValue
     * @castor.field
     *  get-method="getValue"
     *  set-method="setValue"
     * @castor.field-xml
     *  name="value"
     *  node="attribute"
     */
    public String getValue()
    {
        return _value;
    }

    /**
     * Initializes the value of the run-time parameter specified by name.
     * @param value a value. May be <code>null</code>.
     * @see #getValue
     */
    public void setValue(final String value)
    {
        _value = StringUtils.normalize(value);
    }

    /**
     * Returns the type of the value attribute.
     * Could be "data", "ref", or "object".
     * @return the type of the value. May be <code>null</code>.
     * @see #setValueType
     * @castor.field
     *  get-method="getValueType"
     *  set-method="setValueType"
     * @castor.field-xml
     *  name="valueType"
     *  node="attribute"
     */
    public String getValueType()
    {
        return _valueType;
    }

    /**
     * Initializes the type of the value attribute.
     * @param valueType the type of the value. May be <code>null</code>.
     * @see #getValueType
     */
    public void setValueType(final String valueType)
    {
        _valueType = StringUtils.normalize(valueType);
    }
}
