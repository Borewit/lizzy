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
 * Represents a generic media reference which allows to define the media objects elements.
 * @version $Revision: 92 $
 * @author Christophe Delory
 * @castor.class xml="ref"
 */
public class Reference extends AbstractSmilElement
{
    /**
     * The URI of the media element used for locating and fetching the associated media.
     */
    private String _source = null;

    /**
     * Content type of the media object referenced by the src attribute.
     */
    private String _type = null;

    /**
     * The general parameters which are associated to a media object of a SMIL Presentation.
     */
    private final List<Param> _listParams = new ArrayList<Param>();

    /**
     * The parameter group.
     */
    private String _paramGroup = null;

    /**
     * The fill parameter of this reference.
     */
    private String _fill = null;

    /**
     * The coarse 2D placement of the audio portion of a media element assigned to play within a given region.
     */
    private String _soundAlign = null;

    /**
     * Returns the URI (source) of the media element used for locating and fetching the associated media, as a string.
     * A media object with no src attribute has an intrinsic duration of zero, and participates in timing just as any other media element.
     * No media will be fetched by the SMIL implementation for a media element without a src attribute.
     * @return an URI. May be <code>null</code>.
     * @see #setSource
     * @castor.field
     *  get-method="getSource"
     *  set-method="setSource"
     * @castor.field-xml
     *  name="src"
     *  node="attribute"
     */
    public String getSource()
    {
        return _source;
    }

    /**
     * Initializes the URI (source) of the media element from the specified string.
     * @param source an URI. May be <code>null</code>.
     * @see #getSource
     */
    public void setSource(final String source)
    {
        if (source == null)
        {
            _source = null;
        }
        else
        {
            _source = source.trim().replace('\\', '/');
        }
    }

    /**
     * Returns the content type of the media object referenced by the src attribute.
     * The usage of this attribute depends on the protocol of the src attribute.
     * Different protocols: RTSP, HTTP, FTP.
     * @return the content type of the media object. May be <code>null</code>.
     * @see #setType
     * @castor.field
     *  get-method="getType"
     *  set-method="setType"
     * @castor.field-xml
     *  name="type"
     *  node="attribute"
     */
    public String getType()
    {
        return _type;
    }

    /**
     * Initializes the content type of the media object.
     * @param type the media object content type. May be <code>null</code>.
     * @see #getType
     */
    public void setType(final String type)
    {
        _type = StringUtils.normalize(type);
    }

    /**
     * Adds a general parameter to this SMIL media object.
     * @param param a media object parameter. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>param</code> is <code>null</code>.
     * @see #getParams
     */
    public void addParam(final Param param)
    {
        if (param == null)
        {
            throw new NullPointerException("no param");
        }

        _listParams.add(param);
    }

    /**
     * Returns the list of general parameters which are associated to a media object of a SMIL presentation.
     * @return the list of parameters. May be empty but not <code>null</code>.
     * @see #addParam
     * @castor.field
     *  get-method="getParams"
     *  set-method="addParam"
     *  type="christophedelory.playlist.smil.Param"
     *  collection="arraylist"
     * @castor.field-xml
     *  name="param"
     *  node="element"
     */
    public List<Param> getParams()
    {
        return _listParams;
    }

    /**
     * Returns the fill parameter as a string.
     * The fill attribute allows an author to specify that an element should be extended beyond the active duration
     * by freezing the final state of the element.
     * The fill attribute is also used to determine the behavior when the active duration is less than the duration specified in the min attribute.
     * For this reason, rather than referring to the end of the active duration, this description refers to the "last instance of the simple duration".
     * Could be equal to "remove", "freeze", "hold", "transition",  "auto", "default".
     * @return the fill parameter. May be <code>null</code>.
     * @see #setFill
     * @castor.field
     *  get-method="getFill"
     *  set-method="setFill"
     * @castor.field-xml
     *  name="fill"
     *  node="attribute"
     */
    public String getFill()
    {
        return _fill;
    }

    /**
     * Initializes the fill parameter as a string.
     * @param fill the fill parameter. May be <code>null</code>.
     * @see #getFill
     */
    public void setFill(final String fill)
    {
        _fill = StringUtils.normalize(fill);
    }

    /**
     * Returns the parameter group associated to this reference.
     * @return the parameter group as a string. May be <code>null</code>.
     * @see #setParamGroupString
     * @castor.field
     *  get-method="getParamGroupString"
     *  set-method="setParamGroupString"
     * @castor.field-xml
     *  name="paramGroup"
     *  node="attribute"
     */
    public String getParamGroupString()
    {
        return _paramGroup;
    }

    /**
     * Initializes the parameter group associated to this reference.
     * @param paramGroup the parameter group. May be <code>null</code>.
     * @see #getParamGroupString
     */
    public void setParamGroupString(final String paramGroup)
    {
        _paramGroup = StringUtils.normalize(paramGroup);
    }

    /**
     * Returns the coarse 2D placement of the audio portion of a media element assigned to play within a given region.
     * The default value is inherited from the region.
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
     * @return the sound alignment. May be <code>null</code>.
     * @see #setSoundAlign
     * @see Region#getSoundAlign
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
     * Initializes the coarse 2D placement of the audio portion of a media element assigned to play within a given region.
     * @param soundAlign the sound alignment. May be <code>null</code>.
     * @see #getSoundAlign
     * @see Region#setSoundAlign
     */
    public void setSoundAlign(final String soundAlign)
    {
        _soundAlign = StringUtils.normalize(soundAlign);
    }

    @Override
    public void acceptDown(final SmilVisitor visitor)
    {
        visitor.beginVisitReference(this); // Throws NullPointerException if visitor is null.

        visitor.endVisitReference(this);
    }
}
