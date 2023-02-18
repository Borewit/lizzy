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
package christophedelory.playlist.b4s;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import christophedelory.lang.StringUtils;

/**
 * The playlist composing a Winamp 3+ B4S file.
 * @version $Revision: 92 $
 * @author Christophe Delory
 * @castor.class xml="playlist"
 */
public class Playlist
{
    /**
     * A list of entries.
     */
    private final List<Entry> _entries = new ArrayList<Entry>();

    /**
     * The number of entries.
     */
    private int _numEntries = -1;

    /**
     * A label, if any.
     */
    private String _label = null;

    /**
     * Returns the number of entries in this playlist.
     * @return the number of entries. Positive or null.
     * @see #setNumberOfEntries
     * @castor.field
     *  get-method="getNumberOfEntries"
     *  set-method="setNumberOfEntries"
     * @castor.field-xml
     *  name="num_entries"
     *  node="attribute"
     */
    public int getNumberOfEntries()
    {
        return _entries.size();
    }

    /**
     * Initializes the number of entries in this playlist.
     * @param numEntries the number of entries. Shall not be strictly negative.
     * @throws IllegalArgumentException if <code>numEntries</code> is strictly negative.
     * @see #getNumberOfEntries
     */
    public void setNumberOfEntries(final int numEntries)
    {
        if (numEntries < 0)
        {
            throw new IllegalArgumentException("Negative number of entries");
        }

        _numEntries = numEntries;
    }

    /**
     * Returns a label for the playlist.
     * @return a label. May be <code>null</code>.
     * @see #setLabel
     * @castor.field
     *  get-method="getLabel"
     *  set-method="setLabel"
     * @castor.field-xml
     *  name="label"
     *  node="attribute"
     */
    public String getLabel()
    {
        return _label;
    }

    /**
     * Initializes a label for the playlist.
     * @param label a label. May be <code>null</code>.
     * @see #getLabel
     */
    public void setLabel(final String label)
    {
        _label = StringUtils.normalize(label);
    }

    /**
     * Adds an entry to this playlist.
     * @param entry an entry. Shall not be <code>null</code>
     * @throws NullPointerException if <code>entry</code> is <code>null</code>.
     * @see #getEntries
     */
    public void addEntry(final Entry entry)
    {
        if (entry == null)
        {
            throw new NullPointerException("no entry");
        }

        if ((_numEntries < 0) || (_entries.size() < _numEntries))
        {
            _entries.add(entry);
        }
        else
        {
            final Log logger = LogFactory.getLog(getClass()); // May throw LogConfigurationException.
            logger.warn("Ignoring extra B4S entry");
        }
    }

    /**
     * Returns the list of entries defined in this playlist.
     * @return a list of entries. May be empty but not <code>null</code>.
     * @see #addEntry
     * @castor.field
     *  get-method="getEntries"
     *  set-method="addEntry"
     *  type="christophedelory.playlist.b4s.Entry"
     *  collection="arraylist"
     * @castor.field-xml
     *  name="entry"
     *  node="element"
     */
    public List<Entry> getEntries()
    {
        // FIXME Should we also limit the number of entries here?
        return _entries;
    }
}
