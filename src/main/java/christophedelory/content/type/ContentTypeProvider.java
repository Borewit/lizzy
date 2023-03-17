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

import jakarta.activation.FileTypeMap;
import jakarta.activation.MimetypesFileTypeMap;
import java.net.URI;

/**
 * A {@link IContentTypeProvider}.
 * @version $Revision: 92 $
 * @author Christophe Delory
 */
public final class ContentTypeProvider implements IContentTypeProvider
{
    /**
     * The singleton instance.
     */
    private static ContentTypeProvider _instance = null;
    private final FileTypeMap fileTypeMap = new MimetypesFileTypeMap();

    /**
     * Returns the unique class instance.
     * @return an instance of this class. Shall not be <code>null</code>.
     */
    public static ContentTypeProvider getInstance()
    {
        synchronized(ContentTypeProvider.class)
        {
            if (_instance == null)
            {
                _instance = new ContentTypeProvider();
            }
        }

        return _instance;
    }

    /**
     * Returns a content type representing the given content file name
     * @param contentName a content file name. Shall not be <code>null</code>.
     * @return a content type. May be <code>null</code> if none was found.
     * @throws NullPointerException if <code>contentName</code> is <code>null</code>.
     * @throws SecurityException if a required system property value cannot be accessed.
     * @see #getContentType
     */
    public ContentType getContentType(final String contentName)
    {
        ContentType ret = null;
        final int idx = contentName.lastIndexOf('.');

        if (idx >= 0)
        {
            final String ext = contentName.substring(idx); // Shall not throw IndexOutOfBoundsException.

            final FileTypeMap map = fileTypeMap;
            final String contentType = map.getContentType(contentName);

            if (contentType != null)
            {
                ret = new ContentType(new String[] { ext }, new String[] { contentType }, null, null);
            }
        }
        return ret;
    }

    /**
     * Returns a content type representing the given content URI.
     * @param uri a content URI. Shall not be <code>null</code>.
     * @return a content type. May be <code>null</code> if none was found.
     * @throws NullPointerException if <code>uri</code> is <code>null</code>.
     * @see IContentTypeProvider#getContentType
     */
    public ContentType getContentType(final URI uri)
    {
        final String path = uri.getPath(); // Throws NullPointerException if uri is null.
        return path == null ? null :  getContentType(path);
    }
}
