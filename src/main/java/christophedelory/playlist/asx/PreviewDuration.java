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
 * Specifies the length of time a clip is played in preview mode.
 * <br>
 * If this element appears within an ENTRY element or a REF element, it applies to the clip defined by that element.
 * If it appears within the scope of an ASX element, it applies to every clip in the metafile.
 * A PREVIEWDURATION element in a REF element takes precedence over one in an ENTRY element,
 * and either takes precedence over a PREVIEWDURATION element in an ASX element.
 * If no PREVIEWDURATION element is defined for a clip, the default preview time is 10 seconds.
 * <br>
 * If there is a STARTTIME or STARTMARKER element for the clip, Windows Media Player renders the clip starting at the point defined by one of these elements;
 * otherwise it renders from the beginning of the clip.
 * The clip stops normally if it is shorter than the time defined by the PREVIEWDURATION element.
 * <br>
 * The DURATION element overrides a PREVIEWDURATION element.
 * <br>
 * Windows Media Player version 7.0 or later.
 * Not supported on Windows CE.
 * @version $Revision: 55 $
 * @author Christophe Delory
 * @castor.class xml="previewduration"
 */
public class PreviewDuration extends Duration
{
    /**
     * Builds a new and empty preview duration description.
     */
    public PreviewDuration()
    {
        super();

        setValue(10000L); // Shall not throw IllegalArgumentException.
    }
}
