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

import java.util.ArrayList;
import java.util.List;

import christophedelory.lang.StringUtils;

/**
 * Defines a media clip.
 * This element is the fundamental construct in an ASX file.
 * The ENTRY element and its associated attributes define meta-information for a single, logical piece of content, called a clip.
 * Child elements within the scope of an ENTRY element define media content for the WMP control to open (REF),
 * information about the clip that the WMP control will display as text (AUTHOR, COPYRIGHT, TITLE), and other settings related to the clip.
 * The REF child element links the content to be streamed for the parent ENTRY element.
 * Though the script will not break, the ENTRY is meaningless without a REF child.
 * @version $Revision: 92 $
 * @author Christophe Delory
 * @castor.class xml="entry"
 */
public class Entry extends AsxOrEntryElement
{
    /**
     * The value indicating whether the user can skip forward past the clip.
     */
    private boolean _clientSkip = true;

    /**
     * The value indicating whether the Windows Media Player control should skip this clip when the ENTRY element is included in a second ASX file through the use of an ENTRYREF element.
     */
    private boolean _skipIfRef = false;

    /**
     * The length of time the WMP control will render a stream.
     */
    private Duration _duration = null;

    /**
     * The marker at which Windows Media Player ends rendering the stream.
     */
    private Marker _endMarker = null;

    /**
     * The marker at which Windows Media Player starts rendering the stream.
     */
    private Marker _startMarker = null;

    /**
     * The time index from which Windows Media Player will start rendering the stream.
     */
    private Duration _startTime = null;

    /**
     * The list of REF elements which are associated to this ENTRY.
     */
    private final List<Reference> _references = new ArrayList<Reference>();

    /**
     * Returns a value indicating whether the user can skip forward past the clip.
     * If the value of the CLIENTSKIP attribute is NO, the user cannot skip forward past the piece of content defined by the ENTRY element.
     * This does not work if the child REF element references another metafile.
     * Nested metafiles should be referenced using the ENTRYREF element.
     * <br>
     * Possible values include the following:
     * <dl>
     * <dt>YES</dt><dd>User can skip forward past the clip.</dd>
     * <dt>NO</dt><dd>User cannot skip forward past the clip.</dd>
     * </dl>
     * The default value is "YES".
     * @return the associated value as a string. May be <code>null</code>.
     * @see #setClientSkipString
     * @see #isClientSkip
     * @castor.field
     *  get-method="getClientSkipString"
     *  set-method="setClientSkipString"
     * @castor.field-xml
     *  name="clientskip"
     *  node="attribute"
     */
    public String getClientSkipString()
    {
        String ret = null;

        if (!_clientSkip)
        {
            ret = "NO";
        }
        
        return ret;
    }

    /**
     * Initializes the value indicating whether the user can skip forward past the clip.
     * @param clientSkip the value. May be <code>null</code>.
     * @see #getClientSkipString
     * @see #setClientSkip
     */
    public void setClientSkipString(final String clientSkip)
    {
        final String skip = StringUtils.normalize(clientSkip);

        _clientSkip = !("NO".equalsIgnoreCase(skip));
    }

    /**
     * Indicates whether the user can skip forward past the clip.
     * @return the associated boolean.
     * @see #setClientSkip
     * @see #getClientSkipString
     */
    public boolean isClientSkip()
    {
        return _clientSkip;
    }

    /**
     * Indicates whether the user can skip forward past the clip.
     * @param clientSkip the associated boolean.
     * @see #isClientSkip
     * @see #setClientSkipString
     */
    public void setClientSkip(final boolean clientSkip)
    {
        _clientSkip = clientSkip;
    }

    /**
     * Returns the value indicating whether the Windows Media Player control should skip this clip when the ENTRY element is included in a second ASX file through the use of an ENTRYREF element.
     * The SKIPIFREF attribute pertains only to ENTRY elements that are included in a second ASX file through the use of an ENTRYREF element.
     * The ENTRYREF element references another ASX file for logical inclusion in the current file.
     * If the value of the SKIPIFREF attribute for an ENTRY element from the referenced ASX file is YES,
     * the WMP control ignores this pulled-in entry, and moves on to the next ENTRY element, if any.
     * The next ENTRY element can be the next entry in the original file, or the next entry in the ASX file referenced in the ENTRYREF element.
     * <br>
     * Possible values include the following:
     * <dl>
     * <dt>YES</dt><dd>The WMP control will ignore this entry, if referenced through an ENTRYREF element.</dd>
     * <dt>NO</dt><dd>The WMP control will not ignore this entry.</dd>
     * </dl>
     * The default value is "NO".
     * @return the associated value. May be <code>null</code>.
     * @see #setSkipIfRefString
     * @see #isSkipIfRef
     * @castor.field
     *  get-method="getSkipIfRefString"
     *  set-method="setSkipIfRefString"
     * @castor.field-xml
     *  name="skipifref"
     *  node="attribute"
     */
    public String getSkipIfRefString()
    {
        String ret = null;

        if (_skipIfRef)
        {
            ret = "YES";
        }
        
        return ret;
    }

    /**
     * Initializes the value indicating whether the Windows Media Player control should skip this clip when the ENTRY element is included in a second ASX file through the use of an ENTRYREF element.
     * @param skipIfRef a value. May be <code>null</code>.
     * @see #getSkipIfRefString
     * @see #setSkipIfRef
     */
    public void setSkipIfRefString(final String skipIfRef)
    {
        final String skip = StringUtils.normalize(skipIfRef);

        _skipIfRef = "YES".equalsIgnoreCase(skip);
    }

    /**
     * Indicates whether the Windows Media Player control should skip this clip when the ENTRY element is included in a second ASX file through the use of an ENTRYREF element.
     * @return the associated boolean.
     * @see #setSkipIfRef
     * @see #getSkipIfRefString
     */
    public boolean isSkipIfRef()
    {
        return _skipIfRef;
    }

    /**
     * Indicates whether the Windows Media Player control should skip this clip when the ENTRY element is included in a second ASX file through the use of an ENTRYREF element.
     * @param skipIfRef the associated boolean.
     * @see #isSkipIfRef
     * @see #setSkipIfRefString
     */
    public void setSkipIfRef(final boolean skipIfRef)
    {
        _skipIfRef = skipIfRef;
    }

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
     * Initializes the length of time the WMP control will render a stream.
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
     * Returns the start time of this ENTRY.
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
     * Initializes the start time of this ENTRY.
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

    /**
     * Adds a reference to this ASX.
     * @param ref an ASX reference. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>ref</code> is <code>null</code>.
     * @see #getReferences
     */
    public void addReference(final Reference ref)
    {
        ref.setParent(this); // Throws NullPointerException if ref is null.
        _references.add(ref);
    }

    /**
     * Returns the list of references defined for this ASX.
     * @return a list of references. May be empty but not <code>null</code>.
     * @see #addReference
     * @castor.field
     *  get-method="getReferences"
     *  set-method="addReference"
     *  type="christophedelory.playlist.asx.Reference"
     *  collection="arraylist"
     * @castor.field-xml
     *  name="ref"
     *  node="element"
     */
    public List<Reference> getReferences()
    {
        return _references;
    }
}
