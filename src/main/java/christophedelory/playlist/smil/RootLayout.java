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
package christophedelory.playlist.smil;

import christophedelory.lang.StringUtils;

/**
 * The root layout determines the value of the layout properties of the root element,
 * which in turn determines the size of the window in which the SMIL presentation is rendered.
 * @version $Revision: 92 $
 * @author Christophe Delory
 * @castor.class xml="root-layout"
 */
public class RootLayout extends Core
{
    /**
     * Specifies the background color used to fill the area of a region displaying media that is not filled by the media.
     */
    private String _backgroundColor = null;

    /**
     * The attribute may be used to specify an image that may be placed as the background of a region.
     */
    private String _backgroundImage = null;

    /**
     * The use and definition of this attribute are identical to the "height" property in the CSS2 specification.
     */
    private String _height = null;

    /**
     * The use and definition of this attribute are identical to the "width" property in the CSS2 specification.
     */
    private String _width = null;

    /**
     * The skip content controls whether the content of an element is evaluated or should be skipped.
     */
    private Boolean _skipContent = null;

    /**
     * Returns the background color used to fill the area of a region displaying media that is not filled by the media.
     * @return the background color. May be <code>null</code>.
     * @see #setBackgroundColor
     * @castor.field
     *  get-method="getBackgroundColor"
     *  set-method="setBackgroundColor"
     * @castor.field-xml
     *  name="backgroundColor"
     *  node="attribute"
     */
    public String getBackgroundColor()
    {
        return _backgroundColor;
    }

    /**
     * Initializes the background color.
     * @param color the background color. May be <code>null</code>.
     * @see #getBackgroundColor
     */
    public void setBackgroundColor(final String color)
    {
        _backgroundColor = StringUtils.normalize(color);
    }

    /**
     * Returns the background image as a string.
     * The attribute may be used to specify an image that may be placed as the background of a region.
     * Default value is none.
     * @return the background image as a string. May be <code>null</code>.
     * @see #setBackgroundImage
     * @castor.field
     *  get-method="getBackgroundImage"
     *  set-method="setBackgroundImage"
     * @castor.field-xml
     *  name="backgroundImage"
     *  node="attribute"
     */
    public String getBackgroundImage()
    {
        return _backgroundImage;
    }

    /**
     * Initializes the background image as a string.
     * @param image the background image. May be <code>null</code>.
     * @see #getBackgroundImage
     */
    public void setBackgroundImage(final String image)
    {
        _backgroundImage = StringUtils.normalize(image);
    }

    /**
     * Returns the height of this root layout as a string.
     * The use and definition of this attribute are identical to the "height" property in the CSS2 specification.
     * @return a height. May be <code>null</code>.
     * @see #setHeightString
     * @castor.field
     *  get-method="getHeightString"
     *  set-method="setHeightString"
     * @castor.field-xml
     *  name="height"
     *  node="attribute"
     */
    public String getHeightString()
    {
        return _height;
    }

    /**
     * Initializes the height of this root layout as a string.
     * @param height a height. May be <code>null</code>.
     * @see #getHeightString
     * @see #setHeight
     */
    public void setHeightString(final String height)
    {
        _height = StringUtils.normalize(height);
    }

    /**
     * Initializes the height of this root layout as an integer.
     * @param height a height.
     * @see #setHeightString
     */
    public void setHeight(final int height)
    {
        _height = Integer.toString(height);
    }

    /**
     * Returns the width of this root layout as a string.
     * The use and definition of this attribute are identical to the "width" property in the CSS2 specification.
     * @return a width. May be <code>null</code>.
     * @see #setWidthString
     * @castor.field
     *  get-method="getWidthString"
     *  set-method="setWidthString"
     * @castor.field-xml
     *  name="width"
     *  node="attribute"
     */
    public String getWidthString()
    {
        return _width;
    }

    /**
     * Initializes the width of this root layout as a string.
     * @param width a width. May be <code>null</code>.
     * @see #getWidthString
     * @see #setWidth
     */
    public void setWidthString(final String width)
    {
        _width = StringUtils.normalize(width);
    }

    /**
     * Initializes the width of this root layout as an integer.
     * @param width a width.
     * @see #setWidthString
     */
    public void setWidth(final int width)
    {
        _width = Integer.toString(width);
    }

    /**
     * Checks if the content may be skipped or not.
     * @return the associated boolean.
     * @see #getSkipContent
     */
    public boolean isSkipContent()
    {
        return _skipContent != null && _skipContent.booleanValue();
    }

    /**
     * Checks if the content may be skipped or not.
     * The skip content controls whether the content of an element is evaluated or should be skipped.
     * @return the associated boolean. May be <code>null</code>.
     * @see #setSkipContent
     * @castor.field
     *  get-method="getSkipContent"
     *  set-method="setSkipContent"
     * @castor.field-xml
     *  name="skip-content"
     *  node="attribute"
     */
    public Boolean getSkipContent()
    {
        return _skipContent;
    }

    /**
     * Specifies if the content may be skipped or not.
     * @param skipContent the associated boolean. May be <code>null</code>.
     * @see #getSkipContent
     */
    public void setSkipContent(final Boolean skipContent)
    {
        _skipContent = skipContent;
    }

    /**
     * Accepts the specified SMIL playlist visitor.
     * @param visitor a SMIL playlist visitor. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>visitor</code> is <code>null</code>.
     */
    public void acceptDown(final SmilVisitor visitor)
    {
        visitor.beginVisitRootLayout(this); // Throws NullPointerException if visitor is null.

        visitor.endVisitRootLayout(this);
    }
}
