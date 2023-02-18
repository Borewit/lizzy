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
import java.util.Hashtable;
import java.util.List;

/**
 * The super class of the media elements ASX and ENTRY.
 * @version $Revision: 92 $
 * @author Christophe Delory
 * @castor.class xml="asxOrEntryElement"
 */
public class AsxOrEntryElement extends AsxElement
{
    /**
     * The description of the associated element.
     */
    private TextElement _abstract = null;

    /**
     * The author of a media clip or a Windows Media metafile.
     */
    private TextElement _author = null;

    /**
     * The URL of a graphic file that will appear in the display panel of the associated element.
     */
    private Banner _banner = null;

    /**
     * The URL string appended to the front of URLs sent to the WMP control.
     */
    private Base _base = null;

    /**
     * The copyright information for this element.
     */
    private TextElement _copyright = null;

    /**
     * Allows instructing Windows Media Player to submit any log data to the specified URL for the associated element.
     */
    private final List<LogURL> _logURLs = new ArrayList<LogURL>();

    /**
     * The URL of a graphic file that will appear in the display panel of the associated element.
     */
    private MoreInfo _moreInfo = null;

    /**
     * The length of time a clip is played in preview mode.
     */
    private PreviewDuration _previewDuration = null;

    /**
     * The title of the element.
     */
    private TextElement _title = null;

    /**
     * A list of parameters.
     */
    private final Hashtable<Param,Param> _params = new Hashtable<Param,Param>();

    /**
     * Returns the URL string appended to the front of URLs sent to the WMP control.
     * @return a base. May be <code>null</code>.
     * @see #setBase
     * @castor.field
     *  get-method="getBase"
     *  set-method="setBase"
     * @castor.field-xml
     *  name="base"
     *  node="element"
     */
    public Base getBase()
    {
        return _base;
    }

    /**
     * Initializes the URL string appended to the front of URLs sent to the WMP control.
     * @param base a base. May be <code>null</code>.
     * @see #getBase
     */
    public void setBase(final Base base)
    {
        if (base != null)
        {
            base.setParent(this);
        }

        _base = base;
    }

    /**
     * Returns the title associated to this element.
     * A text string specifying the title for an ASX or ENTRY element.
     * Each parent ASX and ENTRY element should contain at most one child TITLE element.
     * Multiple TITLE elements <strike>after the first</strike> before the last will be ignored and will not display.
     * @return a title. May be <code>null</code>.
     * @see #setTitle
     * @castor.field
     *  get-method="getTitle"
     *  set-method="setTitle"
     * @castor.field-xml
     *  name="title"
     *  node="element"
     */
    public TextElement getTitle()
    {
        return _title;
    }

    /**
     * Initializes the title of this element.
     * @param title a title. May be <code>null</code>.
     * @see #getTitle
     */
    public void setTitle(final TextElement title)
    {
        if (title != null)
        {
            title.setParent(this);
        }

        _title = title;
    }

    /**
     * Returns the description of the associated element.
     * Contains text that represents a description of the associated ASX or ENTRY element.
     * Only the <strike>first</strike> last ABSTRACT element within the scope of another element is used when an Advanced Stream Redirector (ASX) file is processed.
     * <strike>Subsequent</strike> Other ABSTRACT elements in that scope are ignored.
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
     * Returns the author of a media clip or a Windows Media metafile.
     * Contains a text string representing the name of the author of an ASX file or media clip.
     * You can use the AUTHOR element within the ASX element and within ENTRY elements.
     * Each parent ASX and ENTRY element should contain at most one child AUTHOR element.
     * Multiple AUTHOR elements <strike>after the first</strike> before the last will be ignored and will not display.
     * @return an author. May be <code>null</code>.
     * @see #setAuthor
     * @castor.field
     *  get-method="getAuthor"
     *  set-method="setAuthor"
     * @castor.field-xml
     *  name="author"
     *  node="element"
     */
    public TextElement getAuthor()
    {
        return _author;
    }

    /**
     * Initializes the author of a media clip or a Windows Media metafile.
     * @param author an author. May be <code>null</code>.
     * @see #getAuthor
     */
    public void setAuthor(final TextElement author)
    {
        if (author != null)
        {
            author.setParent(this);
        }

        _author = author;
    }

    /**
     * Returns the copyright information for this element.
     * Defines a text string specifying the copyright information for an ASX or ENTRY element.
     * The characters for copyright or trademark registration symbols (&copy; or &reg;) do not display properly with all international character sets.
     * To display either of these symbols properly for all users, you should use the ASCII equivalents (c) or (r) instead.
     * If the metafile is encoded using UTF-8, copyright and trademark symbols will display correctly.
     * Each parent ASX and ENTRY element should contain at most one child COPYRIGHT element.
     * Multiple COPYRIGHT elements <strike>after the first</strike> before the last will be ignored and will not display.
     * @return a copyright. May be <code>null</code>.
     * @see #setCopyright
     * @castor.field
     *  get-method="getCopyright"
     *  set-method="setCopyright"
     * @castor.field-xml
     *  name="copyright"
     *  node="element"
     */
    public TextElement getCopyright()
    {
        return _copyright;
    }

    /**
     * Initializes the copyright information for this element.
     * @param copyright a copyright. May be <code>null</code>.
     * @see #getCopyright
     */
    public void setCopyright(final TextElement copyright)
    {
        if (copyright != null)
        {
            copyright.setParent(this);
        }

        _copyright = copyright;
    }

    /**
     * Returns the URL of a graphic file that will appear in the display panel of the associated element.
     * @return a banner. May be <code>null</code>.
     * @see #setBanner
     * @castor.field
     *  get-method="getBanner"
     *  set-method="setBanner"
     * @castor.field-xml
     *  name="banner"
     *  node="element"
     */
    public Banner getBanner()
    {
        return _banner;
    }

    /**
     * Initializes the URL of a graphic file that will appear in the display panel of the associated element.
     * @param banner a banner. May be <code>null</code>.
     * @see #getBanner
     */
    public void setBanner(final Banner banner)
    {
        if (banner != null)
        {
            banner.setParent(this);
        }

        _banner = banner;
    }

    /**
     * Returns the URL of a graphic file that will appear in the display panel of the associated element.
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

    /**
     * Returns the length of time a clip is played in preview mode.
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
     * Adds a parameter to this element.
     * @param param a param. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>param</code> is <code>null</code>.
     * @see #getParams
     */
    public void addParam(final Param param)
    {
        param.setParent(this); // Throws NullPointerException if param is null.
        _params.put(param, param);
    }

    /**
     * Returns all parameters defined in this element.
     * @return a list of parameters. May be empty but not <code>null</code>.
     * @see #addParam
     * @castor.field
     *  get-method="getParams"
     *  set-method="addParam"
     *  type="christophedelory.playlist.asx.Param"
     *  collection="hashtable"
     * @castor.field-xml
     *  name="param"
     *  node="element"
     */
    public Hashtable<Param,Param> getParams()
    {
        return _params;
    }

    /**
     * Searches for the parameter named from <code>name</code>.
     * @param name the parameter name. Shall not be <code>null</code>.
     * @return the parameter value, or <code>null</code> if no such param could be found, or the parameter value itself is <code>null</code>.
     * @throws NullPointerException if <code>name</code> is <code>null</code>.
     */
    public String findParamValue(final String name)
    {
        String ret = null;
        final Param p = _params.get(name); // Throws NullPointerException if name is null.

        if (p != null)
        {
            ret = p.getValue();
        }
        
        return ret;
    }

    /**
     * Lists all log URLs associated to this element.
     * Allows instructing Windows Media Player to submit any log data to the specified URL for the associated element.
     * @return a list of URLs. May be empty but not <code>null</code>.
     * @see #addLogURL
     * @castor.field
     *  get-method="getLogURLs"
     *  set-method="addLogURL"
     *  type="christophedelory.playlist.asx.LogURL"
     *  collection="arraylist"
     * @castor.field-xml
     *  name="logurl"
     *  node="element"
     */
    public List<LogURL> getLogURLs()
    {
        return _logURLs;
    }

    /**
     * Adds a log URL of this element.
     * @param logURL a log URL. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>logURL</code> is <code>null</code>.
     * @see #getLogURLs
     */
    public void addLogURL(final LogURL logURL)
    {
        logURL.setParent(this); // Throws NullPointerException if logURL is null.
        _logURLs.add(logURL);
    }
}
