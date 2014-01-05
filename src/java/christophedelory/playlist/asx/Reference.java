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
 * Specifies an URL for a piece of digital media content.
 * The URL can point to any media type supported, using any protocol supported by the WMP control.
 * <br>
 * The media types supported include still images such as .gif and .jpg images and Flash files with an .swf file name extension.
 * These media types are useful for including advertising content within a playlist.
 * With image files and Flash files that play in a loop,
 * you must also specify the amount of time to display the media item by including a DURATION element within the REF element.
 * If you want an image to continue displaying while the next entry in the playlist is buffered, include a PARAM element within the ENTRY element,
 * set its name attribute to ShowWhileBuffering, and set its value attribute to true.
 * <br>
 * To reference content on a CD or a DVD that allows it, the wmpcd and wmpdvd protocols are provided.
 * For example, setting the HREF attribute to "wmpdvd://f/5/3" will play chapter 3 of title 5 on a DVD, but only if the DVD has been authored to allow it.
 * <br>
 * Applications that open digital media from behind a firewall will have better performance when opening the media items
 * if the address is specified using the domain name server (DNS) name instead of the IP address.
 * <br>
 * The most common use of this element is for URL rollover.
 * If the WMP control is unable to open a piece of media defined in a REF element, it tries the URL in the next REF element.
 * Once the WMP control opens media content from a URL defined within the scope of one ENTRY element,
 * it ignores subsequent REF tags within that ENTRY element.
 * After the piece of content is done playing, the WMP control moves on to the next ENTRY element, if any.
 * <br>
 * <b>Important</b>: once the WMP control establishes a connection to a referenced piece of content, it ignores all other REF elements in that ENTRY,
 * whether the connection terminates normally or abnormally.
 * <br>
 * If the media item referenced is an image file, the DURATION element must be used to specify the display time for the image.
 * <br>
 * Note: attempting to play Flash media that includes sound with the first frame may yield unexpected results.
 * You should author Flash content to play sound starting no earlier than the second frame.
 * <br>
 * Windows Media Player version 7.0 or later.
 * @version $Revision: 92 $
 * @author Christophe Delory
 * @castor.class xml="ref"
 */
public class Reference extends URLElement
{
    /**
     * The length of time the WMP control will render a stream.
     */
    private Duration _duration = null;

    /**
     * The marker at which Windows Media Player ends rendering the stream.
     */
    private Marker _endMarker = null;

    /**
     * The length of time a clip is played in preview mode.
     */
    private PreviewDuration _previewDuration = null;

    /**
     * The marker at which Windows Media Player starts rendering the stream.
     */
    private Marker _startMarker = null;

    /**
     * The time index from which Windows Media Player will start rendering the stream.
     */
    private Duration _startTime = null;

    /**
     * Returns the length of time the WMP control will render a stream.
     * A DURATION element defined within a REF element overrides one that appears within the REF element's parent ENTRY element.
     * @return a duration. May be <code>null</code>.
     * @see #setDuration
     * @castor.field
     *  get-method="getDuration"
     *  set-method="setDuration"
     * @castor.field-xml
     *  name="duration"
     *  node="element"
     */
    public Duration getDuration()
    {
        return _duration;
    }

    /**
     * Initializes the length of time the stream will be rendered.
     * @param duration a duration. May be <code>null</code>.
     * @see #getDuration
     */
    public void setDuration(final Duration duration)
    {
        if (duration != null)
        {
            duration.setParent(this);
        }

        _duration = duration;
    }

    /**
     * Returns the preview duration.
     * @return a preview duration. May be <code>null</code>.
     * @see #setPreviewDuration
     * @castor.field
     *  get-method="getPreviewDuration"
     *  set-method="setPreviewDuration"
     * @castor.field-xml
     *  name="previewduration"
     *  node="element"
     */
    public PreviewDuration getPreviewDuration()
    {
        return _previewDuration;
    }

    /**
     * Initializes the preview duration.
     * @param previewDuration a preview duration. May be <code>null</code>.
     * @see #getPreviewDuration
     */
    public void setPreviewDuration(final PreviewDuration previewDuration)
    {
        if (previewDuration != null)
        {
            previewDuration.setParent(this);
        }

        _previewDuration = previewDuration;
    }

    /**
     * Returns the start time of this reference.
     * Defines a time index into the content from which the stream must play.
     * This element can be used only with stored, on-demand content that has been indexed.
     * @return a start time. May be <code>null</code>.
     * @see #setStartTime
     * @castor.field
     *  get-method="getStartTime"
     *  set-method="setStartTime"
     * @castor.field-xml
     *  name="starttime"
     *  node="element"
     */
    public Duration getStartTime()
    {
        return _startTime;
    }

    /**
     * Initializes the start time of this reference.
     * @param startTime a start time. May be <code>null</code>.
     * @see #getStartTime
     */
    public void setStartTime(final Duration startTime)
    {
        if (startTime != null)
        {
            startTime.setParent(this);
        }

        _startTime = startTime;
    }

    /**
     * Returns the marker at which Windows Media Player starts rendering the stream.
     * A STARTMARKER element defined within a REF element takes precedence over a STARTMARKER element defined within the REF element's parent ENTRY element.
     * A STARTMARKER element also takes precedence over a STARTTIME element.
     * If the marker specified by a STARTMARKER element occurs later in the stream than the marker defined by an ENDMARKER element,
     * no content plays, but no error is generated.
     * @return a start marker. May be <code>null</code>.
     * @see #setStartMarker
     * @castor.field
     *  get-method="getStartMarker"
     *  set-method="setStartMarker"
     * @castor.field-xml
     *  name="startmarker"
     *  node="element"
     */
    public Marker getStartMarker()
    {
        return _startMarker;
    }

    /**
     * Initializes the marker at which Windows Media Player starts rendering the stream.
     * @param startMarker a start marker. May be <code>null</code>.
     * @see #getStartMarker
     */
    public void setStartMarker(final Marker startMarker)
    {
        if (startMarker != null)
        {
            startMarker.setParent(this);
        }

        _startMarker = startMarker;
    }

    /**
     * Returns the marker at which Windows Media Player ends rendering the stream.
     * In preview mode, reaching an end marker stops the preview, even if the time specified by the PREVIEWDURATION element has not elapsed.
     * The ENDMARKER element takes precedence over the DURATION element.
     * If the ENDMARKER is hit before the time specified by DURATION has elapsed, playback will end.
     * An ENDMARKER element defined within a REF element takes precedence over an ENDMARKER defined within the REF element's parent ENTRY element.
     * If the marker specified by an END MARKER element occurs earlier in the stream than the marker defined by a STARTMARKER element,
     * no content plays, but no error is generated.
     * @return an end marker. May be <code>null</code>.
     * @see #setEndMarker
     * @castor.field
     *  get-method="getEndMarker"
     *  set-method="setEndMarker"
     * @castor.field-xml
     *  name="endmarker"
     *  node="element"
     */
    public Marker getEndMarker()
    {
        return _endMarker;
    }

    /**
     * Initializes the marker at which Windows Media Player ends rendering the stream.
     * @param endMarker an end marker. May be <code>null</code>.
     * @see #getEndMarker
     */
    public void setEndMarker(final Marker endMarker)
    {
        if (endMarker != null)
        {
            endMarker.setParent(this);
        }

        _endMarker = endMarker;
    }
}
