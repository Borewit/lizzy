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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ServiceLoader;

/**
 * A {@link ContentType content type} factory.
 * @version $Revision: 92 $
 * @author Christophe Delory
 */
public final class ContentTypeFactory
{
    /**
     * The singleton instance.
     */
    private static ContentTypeFactory _instance = null;

    /**
     * Returns the unique class instance.
     * @return an instance of this class. Shall not be <code>null</code>.
     */
    public static ContentTypeFactory getInstance()
    {
        synchronized(ContentTypeFactory.class)
        {
            if (_instance == null)
            {
                _instance = new ContentTypeFactory();
            }
        }

        return _instance;
    }

    /**
     * The associated service providers loader.
     */
    private final ServiceLoader<ContentTypeProvider> _serviceLoader;

    /**
     * Builds a new content type factory.
     */
    private ContentTypeFactory()
    {
        _serviceLoader = ServiceLoader.load(ContentTypeProvider.class);
    }

    /**
     * Refreshes the list of providers managed by this factory.
     * If new providers are added after the instantiation of this factory, you will need to call this method manually.
     */
    public void reloadProviders()
    {
        _serviceLoader.reload();
    }

    /**
     * Returns a content type representing the given content name (file or URL).
     * @param contentName a content (file or URL) name. Shall not be <code>null</code>.
     * @return a content type. May be <code>null</code> if none was found.
     * @throws NullPointerException if <code>contentName</code> is <code>null</code>.
     * @throws SecurityException if a required system property value cannot be accessed.
     * @see #getContentType
     */
    public ContentType getContentType(final String contentName)
    {
        final String name = contentName.trim().replace('\\', '/'); // Throws NullPointerException if contentName is null.
        URI uri;

        try
        {
            uri = new URI(name); // May throw URISyntaxException.
        }
        catch (URISyntaxException e)
        {
            uri = new File(name).toURI(); // May throw SecurityException.
        }

        return getContentType(uri);
    }

    /**
     * Returns a content type representing the given content URI.
     * @param uri a content URI. Shall not be <code>null</code>.
     * @return a content type. May be <code>null</code> if none was found.
     * @throws NullPointerException if <code>uri</code> is <code>null</code>.
     * @see ContentTypeProvider#getContentType
     */
    public ContentType getContentType(final URI uri)
    {
        ContentType ret = null;
        final String path = uri.getPath(); // Throws NullPointerException if uri is null.

        if (path != null)
        {
            for (ContentTypeProvider service : _serviceLoader)
            {
                ret = service.getContentType(path);

                if (ret != null)
                {
                    break;
                }
            }
        }

        return ret;
    }
}
