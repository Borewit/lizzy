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
 * Determines the size of the window in which the SMIL presentation is rendered,
 * as well as serving as a top level window in which to place child region elements.
 * @version $Revision: 92 $
 * @author Christophe Delory
 * @castor.class xml="topLayout"
 */
public class TopLayout extends RootLayout
{
    /**
     * Specifies when the top level window should be closed.
     */
    private String _close = null;

    /**
     * Specifies when the top level window should be opened.
     */
    private String _open = null;

    /**
     * The list of regions which are associated to this top layout.
     */
    private final List<Region> _regions =  new ArrayList<Region>();

    /**
     * Specifies when the top level window should be closed.
     * Could be equal to onRequest, whenActive.
     * @return the close condition as a string. May be <code>null</code>.
     * @see #setClose
     * @castor.field
     *  get-method="getClose"
     *  set-method="setClose"
     * @castor.field-xml
     *  name="close"
     *  node="attribute"
     */
    public String getClose()
    {
        return _close;
    }

    /**
     * Specifies when the top level window should be closed.
     * @param close the close condition. May be <code>null</code>.
     * @see #getClose
     */
    public void setClose(final String close)
    {
        _close = StringUtils.normalize(close);
    }

    /**
     * Specifies when the top level window should be opened.
     * Could be equal to onStart, whenActive.
     * @return the open condition as a string. May be <code>null</code>.
     * @see #setOpen
     * @castor.field
     *  get-method="getOpen"
     *  set-method="setOpen"
     * @castor.field-xml
     *  name="open"
     *  node="attribute"
     */
    public String getOpen()
    {
        return _open;
    }

    /**
     * Specifies when the top level window should be opened.
     * @param open the open condition as a string. May be <code>null</code>.
     * @see #getOpen
     */
    public void setOpen(final String open)
    {
        _open = StringUtils.normalize(open);
    }

    /**
     * Returns the list of regions defined in this top layout.
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
     * Adds a region to this top layout.
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
}
