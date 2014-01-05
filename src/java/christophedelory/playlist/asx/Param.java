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

/**
 * Defines a custom parameter associated with a clip or show.
 * This element allows users to place additional information about each clip inside the ENTRY element that contains it.
 * A parameter can also be associated with the show rather than an individual clip, by placing this element directly after the ASX tag.
 * <br>
 * This ASX element is only available for Windows Media Player version 6.01 and later.
 * The standard installation of Microsoft Internet Explorer 5 includes a compatible version of Windows Media Player.
 * <br>
 * Windows Media Player version 7.0 or later.
 * Windows Media Player 9 Series or later is required for the predefined NAME attributes.
 * Windows Media Player 10 or later is required for the predefined names CanPause, CanSeek, CanSkipBack, and CanSkipForward.
 * @version $Revision: 92 $
 * @author Christophe Delory
 * @castor.class xml="param"
 */
public class Param extends Child
{
    /**
     * The name used to access the parameter value.
     */
    private String _name;

    /**
     * The value associated with this parameter.
     */
    private String _value;

    /**
     * Builds a new and empty param element.
     */
    public Param()
    {
        super();

        _name = "";
        _value = "";
    }

    /**
     * Builds a new param element from the specified inputs.
     * @param name the name of the parameter.
     * @param value the value of the parameter.
     * @throws NullPointerException if <code>name</code> is <code>null</code>.
     * @throws NullPointerException if <code>value</code> is <code>null</code>.
     */
    public Param(final String name, final String value)
    {
        super();

        _name = name.trim(); // Throws NullPointerException if name is null.
        _value = value.trim(); // Throws NullPointerException if value is null.
    }

    /**
     * Returns the name used to access the parameter value.
     * The name can be any valid string.
     * <br>
     * The following strings have already been defined:
     * <dl>
     * <dt>AllowShuffle</dt><dd>The VALUE attribute specifies whether the metafile playlist allows the Windows Media Player shuffle feature
     *  to play the entries in random order.
     *  The VALUE attribute can be set to "Yes" or "No".
     *  The default value is "No".</dd>
     * <dt>CanPause</dt><dd>The VALUE attribute specifies whether the user can pause playback.
     *  The VALUE attribute can be set to "Yes" or "No".
     *  The default value is "Yes".</dd>
     * <dt>CanSeek</dt><dd>The VALUE attribute specifies whether the user can change the current playback position
     *  by using the seek bar, fast forward, or fast reverse.
     *  The VALUE attribute can be set to "Yes" or "No".
     *  The default value is "Yes".</dd>
     * <dt>CanSkipBack</dt><dd>The VALUE attribute specifies whether the user can skip backward to the previous playlist item by clicking Previous.
     *  The VALUE attribute can be set to "Yes" or "No".
     *  The default value is "Yes".</dd>
     * <dt>CanSkipForward</dt><dd>The VALUE attribute specifies whether the user can skip forward to the next playlist item by clicking Next.
     *  The VALUE attribute can be set to "Yes" or "No".
     *  The default value is "Yes".</dd>
     * <dt>CPRadioID</dt><dd>The VALUE attribute specifies the ID of a radio feed provided by a type 1 online store.
     *  That is, the VALUE attribute must be equal to the RadioID field of one of the radio feeds in the online store's catalog.
     *  The parent element is the ASX element.</dd>
     * <dt>Encoding</dt><dd>The VALUE attribute is set to "utf-8" to indicate that the metafile is a Unicode (UTF-8) encoded file.</dd>
     * <dt>HtmlFlink</dt><dd>The VALUE attribute is a string provided by a type 1 online store.
     *  Windows Media Player passes the string to the IWMPContentPartner::GetItemInfo method, which is implemented by the online store's plug-in.
     *  The GetItemInfo method returns the URL of the Web page to display in the Now Playing pane of the Player.
     *  The Web page has access to all of the methods that the External object exposes to type 1 stores.
     *  For a list of those methods, see External Object for Type 1 Online Stores.</dd>
     * <dt>HTMLView</dt><dd>The VALUE attribute specifies a URL that displays in the Now Playing pane of the full mode Player
     *  for the duration of the playlist or the current entry depending on whether the parent element is the ASX element or an ENTRY element.
     *  HTMLView is not supported for the Windows Media Player control.</dd>
     * <dt>Log:FieldName[:NameSpace]</dt><dd>The VALUE attribute specifies the value that the indicated log field will be set to.
     *  The :NameSpace portion of the NAME attribute is optional.</dd>
     * <dt>Prebuffer</dt><dd>The VALUE attribute specifies whether the next playlist entry begins buffering before the end of the current entry,
     *  which enables a seamless transition.
     *  The VALUE attribute can be set to "true" or "false".</dd>
     * <dt>ShowWhileBuffering</dt><dd>The VALUE attribute specifies whether an image file referenced by the current ENTRY element
     *  continues to display past its specified duration time while the next playlist entry is buffered.
     *  The VALUE attribute can be set to "true" or "false".</dd>
     * </dl>
     * @return the parameter name. Shall not be <code>null</code>.
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
     * Initializes the name used to access the parameter value.
     * @param name the parameter name. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>name</code> is <code>null</code>.
     * @see #getName
     */
    public void setName(final String name)
    {
        _name = name.trim(); // Throws NullPointerException if name is null.
    }

    /**
     * Returns the value associated with this parameter.
     * It can be either a numeric or string value.
     * @see #setValue
     * @return the parameter value. Shall not be <code>null</code>.
     * @castor.field
     *  get-method="getValue"
     *  set-method="setValue"
     *  required="true"
     * @castor.field-xml
     *  name="value"
     *  node="attribute"
     */
    public String getValue()
    {
        return _value;
    }

    /**
     * Initializes the value associated with this parameter.
     * @param value the parameter value. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>value</code> is <code>null</code>.
     * @see #getValue
     */
    public void setValue(final String value)
    {
        _value = value.trim(); // Throws NullPointerException if value is null.
    }

    @Override
    public int hashCode()
    {
        return (_name == null) ? 0 : _name.hashCode();
    }

    @Override
    public boolean equals(final Object o)
    {
        boolean ret = false;

        if (o != null)
        {
            if (o instanceof Param)
            {
                final Param p = (Param) o;

                ret = (_name == null) ? (p._name == null) : _name.equals(p._name);
            }
            else if (o instanceof String)
            {
                final String s = (String) o;

                ret = (_name == null) ? (s == null) : _name.equals(s);
            }
        }

        return ret;
    }
}
