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
 * Specifies one of the media items in a playlist.
 * <br>
 * Windows Media Player 9 Series or later.
 * @version $Revision: 92 $
 * @author Christophe Delory
 * @castor.class xml="media"
 */
public class Media
{
    /**
     * The URL of a media item.
     */
    private String _source = null;

    /**
     * The content ID that is unique to this media item.
     */
    private String _cid = null;

    /**
     * The tracking ID that can be used to track the File System Object for this media item.
     */
    private String _tid = null;

    /**
     * Returns the URL of this media item, as a string.
     * @return an URL. May be <code>null</code> if not yet initialized.
     * @see #setSource
     * @castor.field
     *  get-method="getSource"
     *  set-method="setSource"
     *  required="true"
     * @castor.field-xml
     *  name="src"
     *  node="attribute"
     */
    public String getSource()
    {
        return _source;
    }

    /**
     * Initializes the URL of this media element from the specified string.
     * @param source an URL. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>source</code> is <code>null</code>.
     * @see #getSource
     */
    public void setSource(final String source)
    {
        _source = source.trim().replace('\\', '/'); // Throws NullPointerException if source is null.
    }

    /**
     * Returns the content ID (WPL GUID) that is unique to this media item.
     * The cid attribute (the content ID) is populated by the Windows Media Player as a way to uniquely identify a piece of media content
     * even if its metadata attributes have been changed.
     * This allows the sharing of playlists across computers, because the content can be identified on another computer,
     * and the path to it can be "auto-repaired" by the Windows Media Playlist.
     * @return the content ID. May be <code>null</code>.
     * @see #setContentId
     * @castor.field
     *  get-method="getContentId"
     *  set-method="setContentId"
     * @castor.field-xml
     *  name="cid"
     *  node="attribute"
     */
    public String getContentId()
    {
        return _cid;
    }

    /**
     * Initializes the content ID that is unique to this media item.
     * @param cid the content ID. May be <code>null</code>.
     * @see #getContentId
     */
    public void setContentId(final String cid)
    {
        _cid = StringUtils.normalize(cid);
    }

    /**
     * Returns the tracking ID (WPL GUID) that can be used to track the File System Object for this media item.
     * The tid attribute (the tracking ID) uses the Windows file system to auto-repair the path to the media if the name or location of the file is changed.
     * @return the tracking ID. May be <code>null</code>.
     * @see #setTrackingId
     * @castor.field
     *  get-method="getTrackingId"
     *  set-method="setTrackingId"
     * @castor.field-xml
     *  name="tid"
     *  node="attribute"
     */
    public String getTrackingId()
    {
        return _tid;
    }

    /**
     * Initializes the tracking ID that can be used to track the File System Object for this media item.
     * @param tid the tracking ID. May be <code>null</code>.
     * @see #getTrackingId
     */
    public void setTrackingId(final String tid)
    {
        _tid = StringUtils.normalize(tid);
    }
}
