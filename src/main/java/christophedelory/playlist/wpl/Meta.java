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
package christophedelory.playlist.wpl;

import christophedelory.lang.StringUtils;

/**
 * Specifies metadata that applies to the entire playlist.
 * <br>
 * Windows Media Player 9 Series or later.
 * @version $Revision: 92 $
 * @author Christophe Delory
 * @castor.class xml="meta"
 */
public class Meta
{
    /**
     * The value of an item of metadata.
     */
    private String _content = null;

    /**
     * The name of an item of metadata.
     */
    private String _name = "";

    /**
     * Returns the name of an item of metadata.
     * <br>
     * The creator of a Windows Media Playlist can set the name attribute of a meta element to any string.
     * The following list shows some typical name attributes that are found in Windows Media Playlists
     * created by Windows Media Player and other Microsoft components.
     * <ul>
     * <li>Author</li>
     * <li>Category</li>
     * <li>Genre</li>
     * <li>UserName</li>
     * <li>UserRating</li>
     * <li>Generator</li>
     * </ul>
     * @return a metadata name. Shall not be <code>null</code>.
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
     * Initializes the name of an item of metadata.
     * @param name a metadata name. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>name</code> is <code>null</code>.
     * @see #getName
     */
    public void setName(final String name)
    {
        _name = name.trim(); // Throws NullPointerException if name is null.
    }

    /**
     * Returns the value of an item of metadata.
     * For example, if the name attribute is "Genre" the content attribute could be "Rock".
     * @return a metadata value. May be <code>null</code>.
     * @see #setContent
     * @castor.field
     *  get-method="getContent"
     *  set-method="setContent"
     * @castor.field-xml
     *  name="content"
     *  node="attribute"
     */
    public String getContent()
    {
        return _content;
    }

    /**
     * Initializes the value of an item of metadata.
     * @param content a metadata value. May be <code>null</code>.
     * @see #getContent
     */
    public void setContent(final String content)
    {
        _content = StringUtils.normalize(content);
    }
}
