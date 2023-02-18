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
 * Contains the dynamically defined portion of a playlist.
 * A smartPlaylist element typically contains a querySet element and can also contain a filter element.
 * The querySet element specifies which media items should be selected from the library, without regard for the size of the result set.
 * The filter element, on the other hand, limits the size of the result set.
 * <br>
 * Windows Media Player 9 Series or later.
 * @version $Revision: 92 $
 * @author Christophe Delory
 * @castor.class xml="smartPlaylist"
 */
public class SmartPlaylist
{
    /**
     * The decimal number representing the version number of the smart playlist schema.
     */
    private String _version = "1.0.0.0"; // NOPMD Do not hard code IPv4 or IPv6 addresses, even 127.0.0.1 !

    /**
     * A query set element.
     */
    private QuerySet _querySet = null;

    /**
     * A filter element.
     */
    private Filter _filter = null;

    /**
     * Returns the decimal number representing the version number of the smart playlist schema.
     * Set to "1.0.0.0".
     * @return a version string. Shall not be <code>null</code>.
     * @see #setVersion
     * @castor.field
     *  get-method="getVersion"
     *  set-method="setVersion"
     *  required="true"
     * @castor.field-xml
     *  name="version"
     *  node="attribute"
     */
    public String getVersion()
    {
        return _version;
    }

    /**
     * Initializes the decimal number representing the version number of the smart playlist schema.
     * @param version a version string. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>version</code> is <code>null</code>.
     * @see #getVersion
     */
    public void setVersion(final String version)
    {
        _version = version.trim(); // Throws NullPointerException if version is null.
    }

    /**
     * Returns the query set.
     * @return a query set element. May be <code>null</code> if not yet initialized.
     * @see #setQuerySet
     * @castor.field
     *  get-method="getQuerySet"
     *  set-method="setQuerySet"
     *  required="true"
     * @castor.field-xml
     *  name="querySet"
     *  node="element"
     */
    public QuerySet getQuerySet()
    {
        return _querySet;
    }

    /**
     * Initializes the query set.
     * @param querySet a query set element. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>querySet</code> is <code>null</code>.
     * @see #getQuerySet
     */
    public void setQuerySet(final QuerySet querySet)
    {
        if (querySet == null)
        {
            throw new NullPointerException("No query set");
        }

        _querySet = querySet;
    }

    /**
     * Returns the filter, if any.
     * @return a filter element. May be <code>null</code>.
     * @see #setFilter
     * @castor.field
     *  get-method="getFilter"
     *  set-method="setFilter"
     * @castor.field-xml
     *  name="filter"
     *  node="element"
     */
    public Filter getFilter()
    {
        return _filter;
    }

    /**
     * Initializes the filter.
     * @param filter a filter element. May be <code>null</code>.
     * @see #getFilter
     */
    public void setFilter(final Filter filter)
    {
        _filter = filter;
    }
}
