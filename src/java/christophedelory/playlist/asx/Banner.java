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
 * Specifies an URL for a graphic that appears in the display panel of Windows Media Player, beneath the video content.
 * If the media is audio only, the banner graphic is displayed by itself.
 * Windows Media Player reserves a space 32 pixels high by 194 pixels wide (the banner bar) for the graphic.
 * If the graphic defined in the URL is smaller than that, it displays at its original size.
 * If the graphic is larger than the reserved space, Windows Media Player will crop the image to fit the space.
 * <br>
 * A BANNER element defined for an ASX element displays while all clips in the playlist are playing.
 * A BANNER element defined in an ENTRY element displays only while that clip is playing,
 * and during that time overrides any banner defined within the parent ASX element.
 * You can specify how Windows Media Player reserves space for the banner by setting the BANNERBAR attribute of the ASX element.
 * <br>
 * Banner images are not supported with DRM files or when Windows Media Player is embedded in a Web page.
 * <br>
 * Not supported on Windows CE.
 * @version $Revision: 92 $
 * @author Christophe Delory
 * @castor.class xml="banner"
 */
public class Banner extends URLElement
{
    /**
     * The description (abstract) of the associated BANNER element.
     */
    private TextElement _abstract = null;

    /**
     * Specifies a URL for a Web site, e-mail address, or script command associated with a banner.
     */
    private MoreInfo _moreInfo = null;

    /**
     * Returns the description of the associated element.
     * Contains text that represents a description of the associated ASX or ENTRY element.
     * Only the <strike>first</strike> last ABSTRACT element within the scope of another element is used when an Advanced Stream Redirector (ASX) file is processed.
     * <strike>Subsequent</strike> Other ABSTRACT elements in that scope are ignored.
     * You can use an ABSTRACT element within the scope of the BANNER element to display text as a ToolTip when the user pauses the mouse pointer over the banner graphic.
     * @return an abstract. May be <code>null</code>.
     * @see #setAbstract
     * @castor.field
     *  get-method="getAbstract"
     *  set-method="setAbstract"
     * @castor.field-xml
     *  name="abstract"
     *  node="element"
     */
    public TextElement getAbstract()
    {
        return _abstract;
    }

    /**
     * Initializes the description of the associated element.
     * @param abstrct an abstract. May be <code>null</code>.
     * @see #getAbstract
     */
    public void setAbstract(final TextElement abstrct)
    {
        if (abstrct != null)
        {
            abstrct.setParent(this);
        }

        _abstract = abstrct;
    }

    /**
     * Returns the URL of a graphic file that will appear in the display panel of the associated element.
     * A MOREINFO element within a BANNER element defines a URL to which the user is taken when the user clicks the banner graphic
     * (the URL can be any path or protocol, such as an e-mail link, a Hypertext Transfer Protocol (HTTP) URL to a Web site,
     * or even a Microsoft JScript command).
     * When the pointer is moved over the graphic, the graphic becomes embossed and looks like a button.
     * @return supplementary information for this element. May be <code>null</code>.
     * @see #setMoreInfo
     * @castor.field
     *  get-method="getMoreInfo"
     *  set-method="setMoreInfo"
     * @castor.field-xml
     *  name="moreinfo"
     *  node="element"
     */
    public MoreInfo getMoreInfo()
    {
        return _moreInfo;
    }

    /**
     * Initializes the URL of a graphic file that will appear in the display panel of the associated element.
     * @param moreInfo supplementary information for this element. May be <code>null</code>.
     * @see #getMoreInfo
     */
    public void setMoreInfo(final MoreInfo moreInfo)
    {
        if (moreInfo != null)
        {
            moreInfo.setParent(this);
        }

        _moreInfo = moreInfo;
    }
}
