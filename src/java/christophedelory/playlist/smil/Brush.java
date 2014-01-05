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
 * Represents a lightweight media object element which allows an author to paint a solid color or other pattern in place of a media object.
 * Thus, all attributes associated with media objects may also be applied to brush.
 * Since all information about the media object is specified in the attributes of the element itself,
 * the src attribute is ignored, and thus is not required.
 * @version $Revision: 92 $
 * @author Christophe Delory
 * @castor.class xml="brush"
 */
public class Brush extends AbstractSmilElement
{
    /**
     * The color to use for brushing media object element.
     */
    private String _color = null;

    /**
     * Returns the color to use for brushing media object element.
     * The use and definition of this attribute are identical to the "background-color" property in the CSS2 specification.
     * @return the color to use for brushing media object element. May be <code>null</code>.
     * @see #setColor
     * @castor.field
     *  get-method="getColor"
     *  set-method="setColor"
     * @castor.field-xml
     *  name="color"
     *  node="attribute"
     */
    public String getColor()
    {
        return _color;
    }

    /**
     * Initializes the color of the brush element.
     * @param color the color as a string. May be <code>null</code>.
     * @see #getColor
     */
    public void setColor(final String color)
    {
        _color = StringUtils.normalize(color);
    }

    @Override
    public void acceptDown(final SmilVisitor visitor)
    {
        // No-op.
    }
}
