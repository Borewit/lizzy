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
 * Defines the parameters position such as bottom, left, right and top.
 * @version $Revision: 92 $
 * @author Christophe Delory
 */
public class Position extends Core
{
    /**
     * The use and definition of this attribute are identical to the "bottom" property in the CSS2 specification.
     */
    private String _bottom = null;

    /**
     * The use and definition of this attribute are identical to the "left" property in the CSS2 specification.
     */
    private String _left = null;

    /**
     * The use and definition of this attribute are identical to the "right" property in the CSS2 specification.
     */
    private String _right = null;

    /**
     * The use and definition of this attribute are identical to the "top" property in the CSS2 specification.
     */
    private String _top = null;

    /**
     * Returns the bottom position as a string.
     * The use and definition of this attribute are identical to the "bottom" property in the CSS2 specification.
     * Default value is "auto".
     * @return a bottom position. May be <code>null</code>.
     * @see #setBottomString
     * @castor.field
     *  get-method="getBottomString"
     *  set-method="setBottomString"
     * @castor.field-xml
     *  name="bottom"
     *  node="attribute"
     */
    public String getBottomString()
    {
        return _bottom;
    }

    /**
     * Initializes the bottom position as a string.
     * @param bottom a bottom position. May be <code>null</code>.
     * @see #getBottomString
     * @see #setBottom
     */
    public void setBottomString(final String bottom)
    {
        _bottom = StringUtils.normalize(bottom);
    }

    /**
     * Initializes the bottom position as an integer.
     * @param bottom a bottom position.
     * @see #setBottomString
     */
    public void setBottom(final int bottom)
    {
        _bottom = Integer.toString(bottom);
    }

    /**
     * Returns the left position as a string.
     * The use and definition of this attribute are identical to the "left" property in the CSS2 specification.
     * Default value is "auto".
     * @return a left position. May be <code>null</code>.
     * @see #setLeftString
     * @castor.field
     *  get-method="getLeftString"
     *  set-method="setLeftString"
     * @castor.field-xml
     *  name="left"
     *  node="attribute"
     */
    public String getLeftString()
    {
        return _left;
    }

    /**
     * Initializes the left position as a string.
     * @param left a left position. May be <code>null</code>.
     * @see #getLeftString
     * @see #setLeft
     */
    public void setLeftString(final String left)
    {
        _left = StringUtils.normalize(left);
    }

    /**
     * Initializes the left position as an integer.
     * @param left a left position.
     * @see #setLeftString
     */
    public void setLeft(final int left)
    {
        _left = Integer.toString(left);
    }

    /**
     * Returns the right position as a string.
     * The use and definition of this attribute are identical to the "right" property in the CSS2 specification.
     * Default value is "auto".
     * @return a right position. May be <code>null</code>.
     * @see #setRightString
     * @castor.field
     *  get-method="getRightString"
     *  set-method="setRightString"
     * @castor.field-xml
     *  name="right"
     *  node="attribute"
     */
    public String getRightString()
    {
        return _right;
    }

    /**
     * Initializes the right position as a string.
     * @param right a right position. May be <code>null</code>.
     * @see #getRightString
     * @see #setRight
     */
    public void setRightString(final String right)
    {
        _right = StringUtils.normalize(right);
    }

    /**
     * Initializes the right position as an integer.
     * @param right a right position.
     * @see #setRightString
     */
    public void setRight(final int right)
    {
        _right = Integer.toString(right);
    }

    /**
     * Returns the top position as a string.
     * The use and definition of this attribute are identical to the "top" property in the CSS2 specification.
     * Default value is "auto".
     * @return a top position. May be <code>null</code>.
     * @see #setTopString
     * @castor.field
     *  get-method="getTopString"
     *  set-method="setTopString"
     * @castor.field-xml
     *  name="top"
     *  node="attribute"
     */
    public String getTopString()
    {
        return _top;
    }

    /**
     * Initializes the top position as a string.
     * @param top a top position. May be <code>null</code>.
     * @see #getTopString
     * @see #setTop
     */
    public void setTopString(final String top)
    {
        _top = StringUtils.normalize(top);
    }

    /**
     * Initializes the top position as an integer.
     * @param top a top position.
     * @see #setTopString
     */
    public void setTop(final int top)
    {
        _top = Integer.toString(top);
    }
}
