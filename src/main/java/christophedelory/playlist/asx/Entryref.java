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
 * Links to the ENTRY elements in an external Windows Media metafile that has an .asx, .wax, .wvx, or .wmx extension.
 * This element points the contents of an external ASX file.
 * The WMP control processes the ENTRY elements of the referenced ASX file as if they were included in the primary ASX file at the position of the ENTRYREF element.
 * However, it skips any ENTRY elements in the referenced ASX file that have the SKIPIFREF attribute set to YES.
 <br>
 * Windows Media Player 7.0, 7.1, and Windows Media Player for Windows XP ignore any ENTRYREF elements in the referenced ASX file.
 * Thus, ASX files can only be nested one level deep when using these versions of the Player.
 * The WMP control also ignores the ASX element in the referenced file along with its attributes.
 * Windows Media Player 9 Series or later supports nesting metafiles up to five levels deep.
 * <br>
 * The URL specified in the HREF attribute becomes the base URL for any relative URLs in the external metafile.
 * This URL is used when an entry from the external metafile is playing and the user adds it to the library.
 * @version $Revision: 92 $
 * @author Christophe Delory
 * @castor.class xml="entryref"
 */
public class Entryref extends URLElement
{
    /**
     * This attribute is no longer supported.
     */
    private boolean _clientBind = false;

    /**
     * This attribute is no longer supported.
     * As the Favorites list is not supported in Windows CE, this attribute is set to "NO" and any attempts to change it are ignored.
     * @see #setClientBindString
     * @see #isClientBind
     * @return the associated value. May be <code>null</code>.
     * @castor.field
     *  get-method="getClientBindString"
     *  set-method="setClientBindString"
     * @castor.field-xml
     *  name="clientbind"
     *  node="attribute"
     */
    public String getClientBindString()
    {
        String ret = null;

        if (_clientBind)
        {
            ret = "YES";
        }

        return ret;
    }

    /**
     * This attribute is no longer supported.
     * @param clientBind the value. May be <code>null</code>.
     * @see #getClientBindString
     * @see #setClientBind
     */
    public void setClientBindString(final String clientBind)
    {
        final String bind = StringUtils.normalize(clientBind);

        _clientBind = "YES".equalsIgnoreCase(bind);
    }

    /**
     * This attribute is no longer supported.
     * @return the associated boolean.
     * @see #setClientBind
     * @see #getClientBindString
     */
    public boolean isClientBind()
    {
        return _clientBind;
    }

    /**
     * This attribute is no longer supported.
     * @param clientBind the associated boolean.
     * @see #isClientBind
     * @see #setClientBindString
     */
    public void setClientBind(final boolean clientBind)
    {
        _clientBind = clientBind;
    }
}
