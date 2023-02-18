/*
 * Copyright (c) 2008-2009, Christophe Delory
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
package christophedelory.playlist.rmp;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import christophedelory.lang.StringUtils;

/**
 * The list of tracks in a Real Metadata Package.
 * @since 0.3.0
 * @version $Revision: 91 $
 * @author Christophe Delory
 */
@XmlRootElement(name="TRACKLIST")
public class Tracklist
{
    /**
     * The identifier.
     */
    private String _id = null;

    /**
     * The list of tracks.
     */
    private final List<Track> _tracks = new ArrayList<Track>();

    /**
     * The parent package.
     */
    private transient Package _parent = null;

    /**
     * Initializes the parent package.
     * @param parent the parent package. May be <code>null</code>.
     * @see #getParent
     */
    void setParent(final Package parent)
    {
        _parent = parent;
    }

    /**
     * Returns the parent package, if any.
     * @return the parent package. May be <code>null</code>.
     */
    public Package getParent()
    {
        return _parent;
    }

    /**
     * Returns the track list identifier.
     * @return an identifier. May be <code>null</code>.
     * @see #setId
     */
    @XmlElement(name="LISTID")
    public String getId()
    {
        return _id;
    }

    /**
     * Initializes the identifier of this track list.
     * @param id an identifier. May be <code>null</code>.
     * @see #getId
     */
    public void setId(final String id)
    {
        _id = StringUtils.normalize(id);
    }

    /**
     * Adds a track to this package.
     * @param track a track. Shall not be <code>null</code>
     * @throws NullPointerException if <code>track</code> is <code>null</code>.
     * @see #getTracks
     * @see #setTracks
     */
    public void addTrack(final Track track)
    {
        track.setParent(this); // Throws NullPointerException if track is null.
        _tracks.add(track);
    }

    /**
     * Returns the list of tracks defined in this package.
     * @return a list of tracks. May be empty but not <code>null</code>.
     * @see #setTracks
     * @see #addTrack
     */
    @XmlElement(name="TRACK", type=Track.class)
    public List<Track> getTracks()
    {
        return _tracks;
    }

    /**
     * Initializes the list of tracks defined in this package.
     * @param tracks a list of tracks. May be empty but not <code>null</code>.
     * @throws NullPointerException if <code>tracks</code> is <code>null</code>.
     * @see #getTracks
     * @see #addTrack
     */
    public void setTracks(final List<Track> tracks)
    {
        synchronized(_tracks)
        {
            _tracks.clear();

            for (Track track : tracks)
            {
                addTrack(track);
            }
        }
    }
}
