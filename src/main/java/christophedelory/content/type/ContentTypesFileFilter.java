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
package christophedelory.content.type;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.filechooser.FileFilter;

import christophedelory.playlist.SpecificPlaylistProvider;

/**
 * A global file filter, based on a list of zero, one or more {@link ContentType content types}.
 * @since 0.2.0
 * @version $Revision: 92 $
 * @author Christophe Delory
 */
public class ContentTypesFileFilter extends FileFilter
{
    /**
     * A list of content types.
     */
    private final List<ContentType> _types = new ArrayList<ContentType>();

    /**
     * The filter title.
     */
    private final String _title;

    /**
     * Specifies if the list of associated file extensions must be included in the filter description.
     */
    private final boolean _includeExtensionsInDescription;

    /**
     * An optionally associated specific playlist provider.
     */
    private SpecificPlaylistProvider _provider = null;

    /**
     * Builds a new file filter, with an empty list of content types.
     * @param title the (friendly) title of this filter. Used in the filter description. Shall not be <code>null</code>.
     * @param includeExtensionsInDescription specifies if the list of associated file extensions must be included in the filter description.
     * @throws NullPointerException if <code>title</code> is <code>null</code>.
     * @see #getDescription
     */
    public ContentTypesFileFilter(final String title, final boolean includeExtensionsInDescription)
    {
        super();

        _title = title.trim(); // Throws NullPointerException if title is null.
        _includeExtensionsInDescription = includeExtensionsInDescription;
    }

    /**
     * Adds a content type to this file filter.
     * @param contentType a content type. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>contentType</code> is <code>null</code>.
     * @see #getContentTypes
     */
    public void addContentType(final ContentType contentType)
    {
        _types.add(contentType); // Throws NullPointerException if contentType is null.
    }

    /**
     * Returns the list of currently defined content types.
     * @return a list of content types. May be empty but not <code>null</code>.
     * @see #addContentType
     */
    public List<ContentType> getContentTypes()
    {
        return _types;
    }

    /**
     * Returns the description of this file filter.
     * <br>
     * Typical format: "<code>&lt;initial-description&gt; [(ext1, ext2)]</code>".
     * @return a description. Shall not be <code>null</code>.
     * @see FileFilter#getDescription
     */
    @Override
    public String getDescription()
    {
        final StringBuilder sb = new StringBuilder(_title);

        if (_includeExtensionsInDescription)
        {
            boolean first = true;

            for (ContentType type : _types)
            {
                for (String ext : type.getExtensions())
                {
                    if (first)
                    {
                        sb.append(" (");
                    }
                    else
                    {
                        sb.append(", ");
                    }

                    first = false;
                    sb.append(ext);
                }
            }

            if (!first)
            {
                sb.append(')');
            }
        }

        return sb.toString();
    }

    /**
     * Tests whether the name of the given file matches this file filter.
     * <br>
     * <u>CAUTION</u>: this method accepts also all directories, in order to be properly used with a {@link javax.swing.JFileChooser}.
     * @param f the file to test. Shall not be <code>null</code>.
     * @return <code>true</code> if the file extension matches this content type.
     * @throws NullPointerException if <code>f</code> is <code>null</code>.
     */
    @Override
    public boolean accept(final File f)
    {
        boolean ret = false;

        for (ContentType type : _types)
        {
            ret = ret || type.accept(f); // Throws NullPointerException if f is null.
        }

        return ret;
    }

    /**
     * Returns the specific playlist provider optionally attached to this filter.
     * @return a playlist provider. May be <code>null</code>.
     * @since 0.3.0
     * @see #setProvider
     */
    public SpecificPlaylistProvider getProvider()
    {
        return _provider;
    }

    /**
     * Initializes the specific playlist provider attached to this filter.
     * @param provider a playlist provider. May be <code>null</code>.
     * @since 0.3.0
     * @see #getProvider
     */
    public void setProvider(final SpecificPlaylistProvider provider)
    {
        _provider = provider;
    }
}
