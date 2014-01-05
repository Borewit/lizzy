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
package christophedelory.playlist.wpl;

/**
 * Contains elements that define which media items will be selected from the library.
 * The querySet element specifies which media items should be selected from the library, without regard for the size of the result set.
 * <br>
 * Windows Media Player 9 Series or later.
 * @version $Revision: 92 $
 * @author Christophe Delory
 * @castor.class xml="querySet"
 */
public class QuerySet
{
    /**
     * A source filter element.
     */
    private Filter _sourceFilter = new Filter();

    /**
     * Returns the source filter.
     * Contains elements that select items from a library.
     * The sourceFilter element selects items from the library without regard for the size of the result set.
     * The filter element, on the other hand, limits the size of the result set.
     * <br>
     * Windows Media Player 9 Series or later.
     * @return a source filter element. Shall not be <code>null</code>.
     * @see #setSourceFilter
     * @castor.field
     *  get-method="getSourceFilter"
     *  set-method="setSourceFilter"
     *  required="true"
     * @castor.field-xml
     *  name="sourceFilter"
     *  node="element"
     */
    public Filter getSourceFilter()
    {
        return _sourceFilter;
    }

    /**
     * Initializes the source filter.
     * @param sourceFilter a source filter element. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>sourceFilter</code> is <code>null</code>.
     * @see #getSourceFilter
     */
    public void setSourceFilter(final Filter sourceFilter)
    {
        if (sourceFilter == null)
        {
            throw new NullPointerException("No source filter");
        }

        _sourceFilter = sourceFilter;
    }
}
