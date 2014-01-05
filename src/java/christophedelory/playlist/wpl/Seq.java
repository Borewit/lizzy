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

import java.util.ArrayList;
import java.util.List;

/**
 * Contains elements that define a static playlist or elements that define a smart playlist.
 * When a seq element only contains media elements that reference specific media items, the playlist is considered to be static.
 * When a seq element contains a smartPlaylist element, it is considered to be a dynamic "smart" playlist.
 * <br>
 * Windows Media Player 9 Series or later.
 * @version $Revision: 92 $
 * @author Christophe Delory
 * @castor.class xml="seq"
 */
public class Seq
{
    /**
     * A list of media elements.
     */
    private final List<Media> _medias = new ArrayList<Media>();

    /**
     * A smart playlist element.
     */
    private SmartPlaylist _smartPlaylist = null;

    /**
     * Adds a media element to this sequence.
     * @param media a media element. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>media</code> is <code>null</code>.
     * @throws IllegalStateException if a smart playlist has already been specified.
     * @see #getMedias
     */
    public void addMedia(final Media media)
    {
        if (_smartPlaylist != null)
        {
            throw new IllegalStateException("Define a static or dynamic playlist, but not both");
        }

        if (media == null)
        {
            throw new NullPointerException("no media");
        }

        _medias.add(media);
    }

    /**
     * Returns the list of media elements.
     * @return a list of media elements. May be empty but not <code>null</code>.
     * @see #addMedia
     * @castor.field
     *  get-method="getMedias"
     *  set-method="addMedia"
     *  type="christophedelory.playlist.wpl.Media"
     *  collection="arraylist"
     * @castor.field-xml
     *  name="media"
     *  node="element"
     */
    public List<Media> getMedias()
    {
        return _medias;
    }

    /**
     * Returns the smart playlist, if any.
     * @return a smart playlist element. May be <code>null</code>.
     * @see #setSmartPlaylist
     * @castor.field
     *  get-method="getSmartPlaylist"
     *  set-method="setSmartPlaylist"
     * @castor.field-xml
     *  name="smartPlaylist"
     *  node="element"
     */
    public SmartPlaylist getSmartPlaylist()
    {
        return _smartPlaylist;
    }

    /**
     * Initializes the smart playlist.
     * @param smartPlaylist a smart playlist element. May be <code>null</code>.
     * @throws IllegalStateException if at least one media has already been added in this sequence.
     * @see #getSmartPlaylist
     */
    public void setSmartPlaylist(final SmartPlaylist smartPlaylist)
    {
        if (!_medias.isEmpty())
        {
            throw new IllegalStateException("Define a static or dynamic playlist, but not both");
        }

        _smartPlaylist = smartPlaylist;
    }
}
