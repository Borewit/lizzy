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

import java.util.ArrayList;
import java.util.List;

import christophedelory.lang.StringUtils;

/**
 * Controls the position, size and scaling of media object elements that are placed within it rendering space.
 * @version $Revision: 92 $
 * @author Christophe Delory
 * @castor.class xml="region"
 */
public class Region extends Position
{
    /**
     * The list of sub-regions.
     */
    private final List<Region> _regions = new ArrayList<Region>();

    /**
     * The name of this region.
     */
    private String _regionName = null;

    /**
     * The background color used to fill the area of a region displaying media that is not filled by the media.
     */
    private String _backgroundColor = null;

    /**
     * Specifies an image that may be placed as the background of a region.
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
     * Allows the sound intensity of an audio object to be specified.
     */
    private String _soundLevel = "100%";

    /**
     * The stack level of the region.
     */
    private Integer _zIndex = null;

    /**
     * The default 2D placement of the audio portion of a media element assigned to play within the given region.
     */
    private String _soundAlign = null;

    /**
     * Returns the list of regions defined in this region.
     * @return a list of regions. May be empty but not <code>null</code>.
     * @see #addRegion
     * @castor.field
     *  get-method="getRegions"
     *  set-method="addRegion"
     *  type="christophedelory.playlist.smil.Region"
     *  collection="arraylist"
     * @castor.field-xml
     *  name="region"
     *  node="element"
     */
    public List<Region> getRegions()
    {
        return _regions;
    }

    /**
     * Adds a region to this region.
     * @param region a region. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>region</code> is <code>null</code>.
     * @see #getRegions
     */
    public void addRegion(final Region region)
    {
        if (region == null)
        {
            throw new NullPointerException("no region");
        }

        _regions.add(region);
    }

    /**
     * Returns the region name.
     * The attribute assigns a name to this region element that can be referred to by the region attribute of media object elements.
     * The regionName attribute is not a unique identifier; multiple region elements can share the same regionName attribute value.
     * This attribute does not have a default value.
     * @return the region name. May be <code>null</code>.
     * @see #setRegionName
     * @castor.field
     *  get-method="getRegionName"
     *  set-method="setRegionName"
     * @castor.field-xml
     *  name="regionName"
     *  node="attribute"
     */
    public String getRegionName()
    {
        return _regionName;
    }

    /**
     * Initializes the region name.
     * @param regionName a region name. May be <code>null</code>.
     * @see #getRegionName
     */
    public void setRegionName(final String regionName)
    {
        _regionName = StringUtils.normalize(regionName);
    }

    /**
     * Returns the background color used to fill the area of a region displaying media that is not filled by the media.
     * Default value is transparent.
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
     * @return the background image as a string.
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
     * Returns the height of this region as a a string.
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
     * Initializes the height of this region as a a string.
     * @param height a height. May be <code>null</code>.
     * @see #getHeightString
     * @see #setHeight
     */
    public void setHeightString(final String height)
    {
        _height = StringUtils.normalize(height);
    }

    /**
     * Initializes the height of this region as an integer.
     * @param height a height.
     * @see #setHeightString
     */
    public void setHeight(final int height)
    {
        _height = Integer.toString(height);
    }

    /**
     * Returns the width of this region as a string.
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
     * Initializes the width of this region as a string.
     * @param width a width. May be <code>null</code>.
     * @see #getWidthString
     * @see #setWidth
     */
    public void setWidthString(final String width)
    {
        _width = StringUtils.normalize(width);
    }

    /**
     * Initializes the width of this region as an integer.
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
        return (_skipContent == null) ? false : _skipContent.booleanValue();
    }

    /**
     * Checks if the content may be skipped or not.
     * The skip content controls whether the content of an element is evaluated or should be skipped.
     * @return the associated boolean. May be <code>null</code>.
     * @see #setSkipContent
     * @see #isSkipContent
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
     * Returns the region sound level as a string.
     * Allows the sound intensity of an audio object to be specified.
     * Defaults to "<code>100%</code>".
     * @return a sound level. May be <code>null</code>.
     * @see #setSoundLevelString
     * @castor.field
     *  get-method="getSoundLevelString"
     *  set-method="setSoundLevelString"
     * @castor.field-xml
     *  name="soundLevel"
     *  node="attribute"
     */
    public String getSoundLevelString()
    {
        return _soundLevel;
    }

    /**
     * Initializes the sound level of the region.
     * @param soundLevel a sound level. May be <code>null</code>.
     * @see #getSoundLevelString
     */
    public void setSoundLevelString(final String soundLevel)
    {
        _soundLevel = StringUtils.normalize(soundLevel);
    }

    /**
     * Returns the stack level of the region.
     * @return a stack level. May be <code>null</code>.
     * @see #setZIndex
     * @castor.field
     *  get-method="getZIndex"
     *  set-method="setZIndex"
     * @castor.field-xml
     *  name="z-index"
     *  node="attribute"
     */
    public Integer getZIndex()
    {
        return _zIndex;
    }

    /**
     * Initializes the stack level of the region.
     * @param zIndex a stack level. May be <code>null</code>.
     * @see #getZIndex
     * @see #setZIndex(int)
     */
    public void setZIndex(final Integer zIndex)
    {
        _zIndex = zIndex;
    }

    /**
     * Initializes the stack level of the region.
     * @param zIndex a stack level.
     * @see #setZIndex(Integer)
     */
    public void setZIndex(final int zIndex)
    {
        _zIndex = Integer.valueOf(zIndex);
    }

    /**
     * Returns the default 2D placement of the audio portion of a media element assigned to play within the given region.
     * <br>
     * The following values are allowed:
     * <dl>
     * <dt>both</dt>
     * <dd>Place the audio on both channels.
     * If stereo or other dual-channel audio is used, the audio will be reproduced on both channels, implementation permitting.</dd>
     * <dt>left</dt>
     * <dd>Place the audio on the left channel only.
     * If stereo audio is used, only the left source audio will be reproduced on the left audio channel, implementation permitting.</dd>
     * <dt>right</dt>
     * <dd>Place the audio on the right channel only.
     * If stereo audio is used, only the right source audio will be reproduced on the right audio channel, implementation permitting.</dd>
     * </dl>
     * The default value is "both".
     * @return the default sound alignment. May be <code>null</code>.
     * @see #setSoundAlign
     * @see Reference#getSoundAlign
     * @castor.field
     *  get-method="getSoundAlign"
     *  set-method="setSoundAlign"
     * @castor.field-xml
     *  name="soundAlign"
     *  node="attribute"
     */
    public String getSoundAlign()
    {
        return _soundAlign;
    }

    /**
     * Initializes the default 2D placement of the audio portion of a media element assigned to play within the given region.
     * @param soundAlign the default sound alignment. May be <code>null</code>.
     * @see #getSoundAlign
     * @see Reference#setSoundAlign
     */
    public void setSoundAlign(final String soundAlign)
    {
        _soundAlign = StringUtils.normalize(soundAlign);
    }

    /**
     * Accepts the specified SMIL playlist visitor.
     * @param visitor a SMIL playlist visitor. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>visitor</code> is <code>null</code>.
     */
    public void acceptDown(final SmilVisitor visitor)
    {
        visitor.beginVisitRegion(this); // Throws NullPointerException if visitor is null.

        // First keep a local copy of the list, just in case the visitor modifies it.
        final Region[] regions = new Region[_regions.size()];
        _regions.toArray(regions); // Shall not throw NullPointerException, ArrayStoreException.

        // Then visit the sub-regions.
        for (Region region : regions)
        {
            region.acceptDown(visitor);
        }

        visitor.endVisitRegion(this);
    }

    @Override
    public int hashCode()
    {
        return (getId() == null) ? 0 : getId().hashCode();
    }

    @Override
    public boolean equals(final Object o)
    {
        boolean ret = false;

        if (o != null)
        {
            if (o instanceof Region)
            {
                final Region region = (Region) o;
                ret = (getId() == null) ? (getId() == null) : getId().equals(region.getId());
            }
            else if (o instanceof String)
            {
                final String s = (String) o;
                ret = (getId() == null) ? (s == null) : getId().equals(s);
            }
        }

        return ret;
    }
}
