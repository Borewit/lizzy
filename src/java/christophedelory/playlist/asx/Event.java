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

/**
 * Defines a behavior or action taken by Windows Media Player when it receives a script command labeled as an event.
 * An event is a particular type of script command embedded in a stream sent to the WMP control that consists of a double string.
 * The first string is the word "event", and the second string is the event name.
 * The event name in the second string must match the event name defined in the ASX file (the match is not case-sensitive).
 * Events can be sent to a WMP control receiving a real-time stream,
 * or can be saved in an .asf, .wma, or .wmv file that gets delivered as an on-demand unicast stream.
 * When the WMP control receives the script command, it processes the event as defined by the EVENT element.
 * <br>
 * This element defines a scope of ENTRY or ENTRYREF elements that are processed whenever the WMP control receives the script command with the named event.
 * The ENTRYREF can be a URL that points to an ASP page.
 * With this element, you can specify a behavior for stream switching in near real-time,
 * as opposed to pre-authored stream changes using references to other pieces of content or ASX files.
 * <br>
 * When you use ASP pages to generate playlists, you must specify a value for the Response.ContentType property and the Response.expires property
 * in the ASP page because of latency issues with Windows Media Player.
 * The Response.ContentType must be a valid file name extension for Windows Media metafiles.
 * Valid types include .asf, .asx, .wma, .wax, .wmv, and .wvx.
 * <br>
 * See the Platform SDK for details about using the Response object in ASP.
 * <br>
 * This element can appear anywhere within the ASX element.
 * If multiple EVENT elements within an ASX element have identical values for their NAME attributes,
 * the WMP control uses the first occurrence within the ASX element, and ignores all others.
 * When EVENT elements have distinct NAME attributes, their order within the ASX element does not matter.
 * <br>
 * The WMP control discards events it receives while processing another event.
 * Nesting of events is not supported.
 * When Windows Media Player is in preview mode, event content is not constrained by the PREVIEWDURATION element;
 * the full length of event content can play even if the preview duration for the active ENTRY element expires prior to the end of the event.
 * @version $Revision: 92 $
 * @author Christophe Delory
 * @castor.class xml="event"
 */
public class Event extends AsxElement implements AsxElementContainer
{
    /**
     * The value of the event as created in Windows Media Author.
     */
    private String _name = "";

    /**
     * Defines what shall be done after playing the referenced content.
     */
    private String _whenDone = "RESUME";

    /**
     * A list of entries or entry references.
     */
    private final List<AsxElement> _entries = new ArrayList<AsxElement>();

    /**
     * Returns the value of the event as created in Windows Media Author.
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
     * Initializes the value of the event as created in Windows Media Author.
     * @param name a name. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>name</code> is <code>null</code>.
     * @see #getName
     */
    public void setName(final String name)
    {
        _name = name.trim(); // Throws NullPointerException if name is null.
    }

    /**
     * Returns the value that defines what occurs after the referenced event content finishes playing.
     * The following values are the possible:
     * <dl>
     * <dt>RESUME</dt><dd>The current entry (the clip interrupted by the event) resumes playing.
     *  If the content is stored content, it resumes at the same point where it stopped;
     *  if the content is broadcast, it resumes at the current position.</dd>
     * <dt>NEXT</dt><dd>The next ENTRY element plays as if the event had not occurred and we had reached the end of the current clip.</dd>
     * <dt>BREAK</dt><dd>If the current entry is within a REPEAT loop, the loop terminates as if the repeat count had been completed.
     *  Otherwise, we jump to the end of the playlist as if the final entry had completed as usual.</dd>
     * </dl>
     * Default value is "RESUME".
     * @return the associated action. Shall not be <code>null</code>.
     * @see #setWhenDone
     * @castor.field
     *  get-method="getWhenDone"
     *  set-method="setWhenDone"
     *  required="true"
     * @castor.field-xml
     *  name="whendone"
     *  node="attribute"
     */
    public String getWhenDone()
    {
        return _whenDone;
    }

    /**
     * Initializes the value that defines what shall be done after playing the referenced content.
     * @param whenDone the associated action. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>whenDone</code> is <code>null</code>.
     * @see #getWhenDone
     */
    public void setWhenDone(final String whenDone)
    {
        _whenDone = whenDone.trim().toUpperCase(); // Throws NullPointerException if whenDone is null.
    }

    @Override
    public void addAsxElement(final AsxElement asxElement)
    {
        if (!((asxElement instanceof Entry) || (asxElement instanceof Entryref))) // Throws NullPointerException if asxElement is null.
        {
            throw new IllegalStateException("Element not valid here: " + asxElement);
        }

        asxElement.setParent(this);
        _entries.add(asxElement);
    }

    /**
     * @castor.field
     *  get-method="getAsxElements"
     *  set-method="addAsxElement"
     *  type="christophedelory.playlist.asx.AsxElement"
     *  collection="arraylist"
     * @castor.field-xml
     *  auto-naming="deriveByClass"
     *  node="element"
     */
    @Override
    public List<AsxElement> getAsxElements()
    {
        return _entries;
    }
}
