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

/**
 * Represents the element which contains information that is not related to the temporal behavior of the presentation.
 * Three types of information may be contained by head.
 * These are meta information, layout information, and author-defined content control.
 * @version $Revision: 92 $
 * @author Christophe Delory
 * @castor.class xml="head"
 */
public class Head extends Core
{
    /**
     * The list of metadata.
     */
    private final List<Meta> _metas = new ArrayList<Meta>();

    /**
     * The header of a SMIL presentation may contain a layout.
     */
    private Layout _layout = null;

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
     *  type="christophedelory.playlist.smil.Meta"
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

    /**
     * Returns the layout associated to this SMIL header.
     * @return a layout. May be <code>null</code>.
     * @see #setLayout
     * @castor.field
     *  get-method="getLayout"
     *  set-method="setLayout"
     * @castor.field-xml
     *  name="layout"
     *  node="element"
     */
    public Layout getLayout()
    {
        return _layout;
    }

    /**
     * Initializes the layout associated to this SMIL header.
     * @param layout a layout. May be <code>null</code>.
     * @see #getLayout
     */
    public void setLayout(final Layout layout)
    {
        _layout = layout;
    }

    /**
     * Accepts the specified SMIL playlist visitor.
     * @param visitor a SMIL playlist visitor. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>visitor</code> is <code>null</code>.
     */
    public void acceptDown(final SmilVisitor visitor)
    {
        visitor.beginVisitHead(this); // Throws NullPointerException if visitor is null.

        // Visit the layout defined for this header.
        if (_layout != null)
        {
            _layout.acceptDown(visitor);
        }

        visitor.endVisitHead(this);
    }
}
