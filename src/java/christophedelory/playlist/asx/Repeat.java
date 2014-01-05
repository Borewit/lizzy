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
import java.util.List;

/**
 * Defines the number of times Windows Media Player repeats, or loops through, the clips defined by one or more ENTRY or ENTRYREF elements.
 * Only the first REPEAT element in an ASX file is valid; subsequent REPEAT elements are ignored.
 * <br>
 * Windows Media Player version 7.0 or later.
 * @version $Revision: 92 $
 * @author Christophe Delory
 * @castor.class xml="repeat"
 */
public class Repeat extends AsxElement implements AsxElementContainer
{
    /**
     * The repeat count number of the playlist.
     */
    private Integer _count = null;

    /**
     * A list of entries or entry references elements.
     */
    private final List<AsxElement> _entries = new ArrayList<AsxElement>();

    /**
     * Returns the number of times Windows Media Player repeats the ENTRY and ENTRYREF elements within this element's scope.
     * If no COUNT parameter is defined, the content in the associated ENTRY and ENTRYREF elements repeats an infinite number of times.
     * A value of zero causes the WMP control to ignore the REPEAT element and play the content once.
     * @see #setCount
     * @return an integer. May be <code>null</code>.
     * @castor.field
     *  get-method="getCount"
     *  set-method="setCount"
     * @castor.field-xml
     *  name="count"
     *  node="attribute"
     */
    public Integer getCount()
    {
        return _count;
    }

    /**
     * Initializes the number of times Windows Media Player repeats the ENTRY and ENTRYREF elements within this element's scope.
     * If the specified count value is negative, the repeat count is assumed to be indefinite.
     * @param count an integer. May be <code>null</code>.
     * @see #getCount
     */
    public void setCount(final Integer count)
    {
        if ((count != null) && (count.intValue() < 0))
        {
            _count = null;
        }
        else
        {
            _count = count;
        }
    }

    @Override
    public void addAsxElement(final AsxElement asxElement)
    {
        if (!((asxElement instanceof Entry) || (asxElement instanceof Entryref)))  // Throws NullPointerException if asxElement is null.
        {
            throw new IllegalStateException("Element not valid here: " + asxElement);
        }

        asxElement.setParent(this);
        _entries.add(asxElement);
    }

    /**
     * @castor.field
     *  get-method="getAsxElements"
     *  set-method="addAsxElement"
     *  type="christophedelory.playlist.asx.AsxElement"
     *  collection="arraylist"
     * @castor.field-xml
     *  auto-naming="deriveByClass"
     *  node="element"
     */
    @Override
    public List<AsxElement> getAsxElements()
    {
        return _entries;
    }
}
