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
package christophedelory.playlist.rmp;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import christophedelory.lang.StringUtils;

/**
 * A track in a Real Metadata Package.
 * @since 0.3.0
 * @version $Revision: 92 $
 * @author Christophe Delory
 */
@XmlRootElement(name="TRACK")
public class Track
{
    /**
     * The identifier.
     */
    private String _id = null;

    /**
     * The MIP URL.
     */
    private String _url = null;

    /**
     * The title.
     */
    private String _title = null;

    /**
     * The track album.
     */
    private String _album = null;

    /**
     * The track artist.
     */
    private String _artist = null;

    /**
     * The genre.
     */
    private String _genre = null;

    /**
     * The track file name.
     */
    private String _fileName = null;

    /**
     * The track size in bytes.
     */
    private Long _size = null;

    /**
     * The track format.
     */
    private String _format = null;

    /**
     * The track quality in bps.
     */
    private Integer _quality = null;

    /**
     * The number of channels in this track.
     */
    private Integer _channels = null;

    /**
     * The track duration in seconds.
     */
    private Integer _duration = null;

    /**
     * The context info URL.
     */
    private String _contextInfoUrl = null;

    /**
     * The context info width in pixels.
     */
    private Integer _contextInfoWidth = null;

    /**
     * The context info height in pixels.
     */
    private Integer _contextInfoHeight = null;

    /**
     * Specifies that this is a streaming URL that should simply be imported and not downloaded.
     */
    private boolean _isStreaming = false;

    /**
     * The parent track list.
     */
    private transient Tracklist _parent = null;

    /**
     * Initializes the parent track list.
     * @param parent the parent track list. May be <code>null</code>.
     * @see #getParent
     */
    void setParent(final Tracklist parent)
    {
        _parent = parent;
    }

    /**
     * Returns the parent track list, if any.
     * @return the parent track list. May be <code>null</code>.
     */
    public Tracklist getParent()
    {
        return _parent;
    }

    /**
     * Returns the track identifier.
     * @return an identifier. May be <code>null</code>.
     * @see #setId
     */
    @XmlElement(name="TRACKID")
    public String getId()
    {
        return _id;
    }

    /**
     * Initializes the identifier of this track.
     * @param id an identifier. May be <code>null</code>.
     * @see #getId
     */
    public void setId(final String id)
    {
        _id = StringUtils.normalize(id);
    }

    /**
     * Returns the MIP URL.
     * @return an URL. May be <code>null</code>.
     * @see #setUrlString
     */
    @XmlElement(name="URL")
    public String getUrlString()
    {
        return _url;
    }

    /**
     * Initializes the MIP URL.
     * @param url an URL. May be <code>null</code>.
     * @see #getUrlString
     */
    public void setUrlString(final String url)
    {
        _url = StringUtils.normalize(url);
    }

    /**
     * Returns the track title.
     * @return a title. May be <code>null</code>.
     * @see #setTitle
     */
    @XmlElement(name="TITLE")
    public String getTitle()
    {
        return _title;
    }

    /**
     * Initializes the track title.
     * @param title a title. May be <code>null</code>.
     * @see #getTitle
     */
    public void setTitle(final String title)
    {
        _title = StringUtils.normalize(title);
    }

    /**
     * Returns the track album.
     * @return an album. May be <code>null</code>.
     * @see #setAlbum
     */
    @XmlElement(name="ALBUM")
    public String getAlbum()
    {
        return _album;
    }

    /**
     * Initializes the track album.
     * @param album an album. May be <code>null</code>.
     * @see #getAlbum
     */
    public void setAlbum(final String album)
    {
        _album = StringUtils.normalize(album);
    }

    /**
     * Returns the track artist.
     * @return an artist. May be <code>null</code>.
     * @see #setArtist
     */
    @XmlElement(name="ARTIST")
    public String getArtist()
    {
        return _artist;
    }

    /**
     * Initializes the track artist.
     * @param artist an artist. May be <code>null</code>.
     * @see #getArtist
     */
    public void setArtist(final String artist)
    {
        _artist = StringUtils.normalize(artist);
    }

    /**
     * Returns the track genre.
     * @return a genre. May be <code>null</code>.
     * @see #setGenre
     */
    @XmlElement(name="GENRE")
    public String getGenre()
    {
        return _genre;
    }

    /**
     * Initializes the track genre.
     * @param genre a genre. May be <code>null</code>.
     * @see #getGenre
     */
    public void setGenre(final String genre)
    {
        _genre = StringUtils.normalize(genre);
    }

    /**
     * Returns the track file name.
     * This is the place that the "%f" refers to from the PACKAGE/SERVER/LOCATION tag.
     * This filename is both the filename on the server, as well as the destination filename for the user's hard drive.
     * @return a file name. May be <code>null</code>.
     * @see #setFileName
     */
    @XmlElement(name="FILENAME")
    public String getFileName()
    {
        return _fileName;
    }

    /**
     * Initializes the track file name.
     * @param fileName a file name. May be <code>null</code>.
     * @see #getFileName
     */
    public void setFileName(final String fileName)
    {
        _fileName = StringUtils.normalize(fileName);
    }

    /**
     * Returns the size of this track, in bytes.
     * @return a size. May be <code>null</code>.
     * @see #setSize
     */
    @XmlElement(name="SIZE")
    public Long getSize()
    {
        return _size;
    }

    /**
     * Initializes the size of this track, in bytes.
     * @param size a size. May be <code>null</code>.
     * @throws IllegalArgumentException if the specified value is not positive.
     * @see #getSize
     * @see #setSize(int)
     */
    public void setSize(final Long size)
    {
        if ((size != null) && (size.intValue() < 0))
        {
            throw new IllegalArgumentException("Negative size");
        }

        _size = size;
    }

    /**
     * Initializes the size of this track, in bytes.
     * @param size a size. Shall not be strictly negative.
     * @throws IllegalArgumentException if the specified size is not positive.
     * @see #getSize
     * @see #setSize(Long)
     */
    public void setSize(final int size)
    {
        if (size < 0)
        {
            throw new IllegalArgumentException("Negative size");
        }

        _size = Long.valueOf(size);
    }

    /**
     * Returns the track format.
     * @return a format. May be <code>null</code>.
     * @see #setFormat
     */
    @XmlElement(name="FORMAT")
    public String getFormat()
    {
        return _format;
    }

    /**
     * Initializes the track format.
     * @param format a format. May be <code>null</code>.
     * @see #getFormat
     */
    public void setFormat(final String format)
    {
        _format = StringUtils.normalize(format);
    }

    /**
     * Returns the quality of this track, in bps.
     * @return a quality. May be <code>null</code>.
     * @see #setQuality
     */
    @XmlElement(name="QUALITY")
    public Integer getQuality()
    {
        return _quality;
    }

    /**
     * Initializes the quality of this track, in bps.
     * @param quality a quality. May be <code>null</code>.
     * @throws IllegalArgumentException if the specified value is not positive.
     * @see #getQuality
     * @see #setQuality(int)
     */
    public void setQuality(final Integer quality)
    {
        if ((quality != null) && (quality.intValue() < 0))
        {
            throw new IllegalArgumentException("Negative quality");
        }

        _quality = quality;
    }

    /**
     * Initializes the quality of this track, in bps.
     * @param quality a quality. Shall not be strictly negative.
     * @throws IllegalArgumentException if the specified quality is not positive.
     * @see #getQuality
     * @see #setQuality(Integer)
     */
    public void setQuality(final int quality)
    {
        if (quality < 0)
        {
            throw new IllegalArgumentException("Negative quality");
        }

        _quality = Integer.valueOf(quality);
    }

    /**
     * Returns the number of channels in this track.
     * @return a number of channels. May be <code>null</code>.
     * @see #setChannels
     */
    @XmlElement(name="CHANNELS")
    public Integer getChannels()
    {
        return _channels;
    }

    /**
     * Initializes the number of channels in this track.
     * @param channels a number of channels. May be <code>null</code>.
     * @throws IllegalArgumentException if the specified value is not positive.
     * @see #getChannels
     * @see #setChannels(int)
     */
    public void setChannels(final Integer channels)
    {
        if ((channels != null) && (channels.intValue() < 0))
        {
            throw new IllegalArgumentException("Negative number of channels");
        }

        _channels = channels;
    }

    /**
     * Initializes the number of channels in this track.
     * @param channels a number of channels. Shall not be strictly negative.
     * @throws IllegalArgumentException if the specified number of channels is not positive.
     * @see #getChannels
     * @see #setChannels(Integer)
     */
    public void setChannels(final int channels)
    {
        if (channels < 0)
        {
            throw new IllegalArgumentException("Negative number of channels");
        }

        _channels = Integer.valueOf(channels);
    }

    /**
     * Returns the duration of this track, in seconds.
     * @return a duration. May be <code>null</code>.
     * @see #setDuration
     */
    @XmlElement(name="DURATION")
    public Integer getDuration()
    {
        return _duration;
    }

    /**
     * Initializes the duration of this track, in seconds.
     * @param duration a duration. May be <code>null</code>.
     * @throws IllegalArgumentException if the specified value is not positive.
     * @see #getDuration
     * @see #setDuration(int)
     */
    public void setDuration(final Integer duration)
    {
        if ((duration != null) && (duration.intValue() < 0))
        {
            throw new IllegalArgumentException("Negative duration");
        }

        _duration = duration;
    }

    /**
     * Initializes the duration of this track, in seconds.
     * @param duration a duration. Shall not be strictly negative.
     * @throws IllegalArgumentException if the specified duration is not positive.
     * @see #getDuration
     * @see #setDuration(Integer)
     */
    public void setDuration(final int duration)
    {
        if (duration < 0)
        {
            throw new IllegalArgumentException("Negative duration");
        }

        _duration = Integer.valueOf(duration);
    }

    /**
     * Returns the context info URL.
     * A URL to be stored in the track's metadata as "Playback Specifications:Context Info URL".
     * @return an URL. May be <code>null</code>.
     * @see #setContextInfoUrlString
     */
    @XmlElement(name="CONTEXTINFO_URL")
    public String getContextInfoUrlString()
    {
        return _contextInfoUrl;
    }

    /**
     * Initializes the context info URL.
     * @param url an URL. May be <code>null</code>.
     * @see #getContextInfoUrlString
     */
    public void setContextInfoUrlString(final String url)
    {
        _contextInfoUrl = StringUtils.normalize(url);
    }

    /**
     * Returns the context info width, in pixels.
     * The desired width of the context window.
     * @return a width. May be <code>null</code>.
     * @see #setContextInfoWidth
     */
    @XmlElement(name="CONTEXTINFO_WIDTH")
    public Integer getContextInfoWidth()
    {
        return _contextInfoWidth;
    }

    /**
     * Initializes the context info width, in pixels.
     * @param width a width. May be <code>null</code>.
     * @throws IllegalArgumentException if the specified value is not positive.
     * @see #getContextInfoWidth
     * @see #setContextInfoWidth(int)
     */
    public void setContextInfoWidth(final Integer width)
    {
        if ((width != null) && (width.intValue() < 0))
        {
            throw new IllegalArgumentException("Negative context info width");
        }

        _contextInfoWidth = width;
    }

    /**
     * Initializes the context info width, in pixels.
     * @param width a width. Shall not be strictly negative.
     * @throws IllegalArgumentException if the specified width is not positive.
     * @see #getContextInfoWidth
     * @see #setContextInfoWidth(Integer)
     */
    public void setContextInfoWidth(final int width)
    {
        if (width < 0)
        {
            throw new IllegalArgumentException("Negative context info width");
        }

        _contextInfoWidth = Integer.valueOf(width);
    }

    /**
     * Returns the context info height, in pixels.
     * The desired height of the context window.
     * @return an height. May be <code>null</code>.
     * @see #setContextInfoHeight
     */
    @XmlElement(name="CONTEXTINFO_HEIGHT")
    public Integer getContextInfoHeight()
    {
        return _contextInfoHeight;
    }

    /**
     * Initializes the context info height, in pixels.
     * @param height an height. May be <code>null</code>.
     * @throws IllegalArgumentException if the specified value is not positive.
     * @see #getContextInfoHeight
     * @see #setContextInfoHeight(int)
     */
    public void setContextInfoHeight(final Integer height)
    {
        if ((height != null) && (height.intValue() < 0))
        {
            throw new IllegalArgumentException("Negative context info height");
        }

        _contextInfoHeight = height;
    }

    /**
     * Initializes the context info height, in pixels.
     * @param height an height. Shall not be strictly negative.
     * @throws IllegalArgumentException if the specified height is not positive.
     * @see #getContextInfoHeight
     * @see #setContextInfoHeight(Integer)
     */
    public void setContextInfoHeight(final int height)
    {
        if (height < 0)
        {
            throw new IllegalArgumentException("Negative context info height");
        }

        _contextInfoHeight = Integer.valueOf(height);
    }

    /**
     * Specifies that this is a streaming URL that should simply be imported and not downloaded.
     * In this case, the returned string is "1".
     * Otherwise it is <code>null</code>.
     * <br>
     * Note: The PACKAGE/ACTION tag must also be set to "import-copy"
     * and the PACKAGE/SERVER/URL tag must contain the URL associated with the streaming content.
     * The PACKAGE/TRACKLIST/TRACK/FILENAME tag is considered irrelevant in this situation.
     * @return the streaming URL indicator. May be <code>null</code>.
     * @see #setIsStreamingString
     * @see #isStreaming
     */
    @XmlElement(name="IS_STREAMING")
    public String getIsStreamingString()
    {
        String ret = null;

        if (_isStreaming)
        {
            ret = "1";
        }

        return ret;
    }

    /**
     * Specifies that this is a streaming URL that should simply be imported and not downloaded.
     * @param isStreaming the streaming URL indicator. May be <code>null</code>.
     * @see #getIsStreamingString
     * @see #setIsStreaming
     */
    public void setIsStreamingString(final String isStreaming)
    {
        _isStreaming = "1".equals(isStreaming); // isStreaming may be null.
    }

    /**
     * Specifies that this is a streaming URL that should simply be imported and not downloaded.
     * @return the streaming URL indicator.
     * @see #setIsStreaming
     * @see #getIsStreamingString
     */
    @XmlTransient
    public boolean isStreaming()
    {
        return _isStreaming;
    }

    /**
     * Specifies that this is a streaming URL that should simply be imported and not downloaded.
     * @param isStreaming the streaming URL indicator.
     * @see #isStreaming
     * @see #setIsStreamingString
     */
    public void setIsStreaming(final boolean isStreaming)
    {
        _isStreaming = isStreaming;
    }
}
