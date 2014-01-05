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
 * Specifies an URL for a Web site, e-mail address, or script command associated with a show, clip, or banner.
 * The user can access the target of the URL by clicking on the graphic or text associated with the MOREINFO element.
 * The details depend on the parent element of the MOREINFO element:
 * <ul>
 * <li>If the HREF parameter specifies a URL to a Web site, the browser opens and navigates to the URL.</li>
 * <li>If the HREF attribute specifies an e-mail address, the user's e-mail application starts.</li>
 * <li>If the HREF attribute specifies a script command, the script command executes in the browser.</li>
 * </ul>
 * If the MOREINFO element appears within an ASX or ENTRY element, when the mouse cursor is held over the title of the show (for an ASX element)
 * or clip (for an ENTRY element), the URL defined in the HREF attribute can be selected and accessed by Windows Media Player.
 * @version $Revision: 92 $
 * @author Christophe Delory
 * @castor.class xml="moreinfo"
 */
public class MoreInfo extends URLElement
{
    /**
     * The target frame or window.
     */
    private String _target = null;

    /**
     * Returns a value defining the frame or window in which the browser will open the page defined by the HREF attribute.
     * The values for this attribute follow standard HTML syntax and definitions.
     * In the case of a control embedded in a Web page, if no TARGET attribute is defined,
     * the URL loads the current browser window and replaces the existing page, which means the content stops playing.
     * Therefore, it is recommended that you always specify a TARGET attribute when embedding the Windows Media Player control in a Web page.
     * <br>
     * If the metafile is opened in the stand-alone Windows Media Player, the TARGET attribute is ignored, and the URL loads in an existing browser window.
     * If there is no browser window currently open, the URL loads in a new browser window.
     * <br>
     * This can be a string containing a window name, or one of the following values:
     * <dl>
     * <dt>_blank</dt><dd>The document loads in a new browser window.</dd>
     * <dt>_self</dt><dd>The document loads in the same frame as the current document containing the Windows Media Player control.</dd>
     * <dt>_parent</dt><dd>The document loads in the immediate parent frame of the current frame, or the current frame if there is no parent frame.</dd>
     * <dt>_top</dt><dd>The document loads in the full browser window, replacing all other frames or documents.</dd>
     * </dl>
     * The TARGET attribute is not supported in Windows CE.
     * This attribute is set to "" (an empty string) and any attempts to change it are ignored.
     * @return the target frame or window. May be <code>null</code>.
     * @see #setTarget
     * @castor.field
     *  get-method="getTarget"
     *  set-method="setTarget"
     * @castor.field-xml
     *  name="target"
     *  node="attribute"
     */
    public String getTarget()
    {
        return _target;
    }

    /**
     * Initializes the target frame or window.
     * @param target a target. May be <code>null</code>.
     * @see #getTarget
     */
    public void setTarget(final String target)
    {
        _target = StringUtils.normalize(target);
    }
}
