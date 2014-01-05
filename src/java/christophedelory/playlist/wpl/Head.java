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

import java.util.ArrayList;
import java.util.List;

import christophedelory.lang.StringUtils;

/**
 * Contains metadata that applies to the entire playlist.
 * Typically the head element contains a title element and one or more meta elements that define global characteristics of the playlist.
 * <br>
 * Windows Media Player 9 Series or later.
 * @version $Revision: 92 $
 * @author Christophe Delory
 * @castor.class xml="head"
 */
public class Head
{
    /**
     * The list of metadata.
     */
    private final List<Meta> _metas = new ArrayList<Meta>();

    /**
     * Specifies a title for the playlist.
     */
    private String _title = null;

    /**
     * Specifies the author of the playlist.
     */
    private String _author = null;

    /**
     * Returns the playlist's title.
     * When choosing a title for a Windows Media Playlist (WPL), consider that the content of the playlist can be dynamic.
     * A good approach is to base the title on the logic in the argument elements because this is what defines the content of the playlist.
     * Examples of this are "My Favorite Rock Songs from 1966" or "Dance Songs That I Haven't Played Recently".
     * @return a title. May be <code>null</code>.
     * @see #setTitle
     * @castor.field
     *  get-method="getTitle"
     *  set-method="setTitle"
     * @castor.field-xml
     *  name="title"
     *  node="element"
     */
    public String getTitle()
    {
        return _title;
    }

    /**
     * Initializes the playlist's title.
     * @param title a title. May be <code>null</code>.
     * @see #getTitle
     */
    public void setTitle(final String title)
    {
        _title = StringUtils.normalize(title);
    }

    /**
     * Returns the playlist's author.
     * @return an author. May be <code>null</code>.
     * @see #setAuthor
     * @castor.field
     *  get-method="getAuthor"
     *  set-method="setAuthor"
     * @castor.field-xml
     *  name="author"
     *  node="element"
     */
    public String getAuthor()
    {
        return _author;
    }

    /**
     * Initializes the playlist's author.
     * @param author an author. May be <code>null</code>.
     * @see #getAuthor
     */
    public void setAuthor(final String author)
    {
        _author = StringUtils.normalize(author);
    }

    /**
     * Adds the specified metadata to this SMIL header.
     * @param meta a metadata. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>meta</code> is <code>null</code>.
     * @see #getMetas
     */
    public void addMeta(final Meta meta)
    {
        if (meta == null)
        {
            throw new NullPointerException("no meta");
        }

        _metas.add(meta);
    }

    /**
     * Returns the list of metadata defined in this SMIL header.
     * @return a list of metadata. May be empty but not <code>null</code>.
     * @see #addMeta
     * @castor.field
     *  get-method="getMetas"
     *  set-method="addMeta"
     *  type="christophedelory.playlist.wpl.Meta"
     *  collection="arraylist"
     * @castor.field-xml
     *  name="meta"
     *  node="element"
     */
    public List<Meta> getMetas()
    {
        return _metas;
    }

    /**
     * Returns the first metadata found with the specified property name.
     * @param name the property name. May be <code>null</code>.
     * @return a metadata instance, or <code>null</code> if none is found.
     * @see Meta#getName
     * @see #getMetas
     */
    public Meta findMetaByName(final String name)
    {
        Meta ret = null;

        for (Meta meta : _metas)
        {
            if (meta.getName().equals(name)) // name may be null.
            {
                ret = meta;
                break;
            }
        }

        return ret;
    }

    /**
     * Removes all metadata found with the specified property name from the underlying list of metadata.
     * @param name the property name. May be <code>null</code>.
     * @return <code>true</code> if at least one metadata has been effectively removed, <code>false</code> otherwise.
     * @see Meta#getName
     * @see #getMetas
     * @see #findMetaByName
     */
    public boolean removeMetaByName(final String name)
    {
        boolean ret = false;
        final java.util.Iterator<Meta> iter = _metas.iterator();

        while (iter.hasNext())
        {
            final Meta meta = iter.next(); // Should not throw NoSuchElementException.

            if (meta.getName().equals(name)) // name may be null.
            {
                iter.remove(); // Should not throw UnsupportedOperationException, IllegalStateException.
                ret = true;
            }
        }

        return ret;
    }
}
