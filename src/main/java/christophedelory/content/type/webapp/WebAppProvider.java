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
package christophedelory.content.type.webapp;

import java.net.URL;

import christophedelory.content.type.ContentType;
import christophedelory.content.type.ContentTypeProvider;
import christophedelory.xml.XmlSerializer;

/**
 * A content type provider based on the Apache Tomcat servlet web app main configuration.
 * @version $Revision: 91 $
 * @author Christophe Delory
 */
public class WebAppProvider implements ContentTypeProvider
{
    /**
     * The MIME type mappings container.
     */
    private WebApp _webApp;

    /**
     * Builds a new content type provider.
     */
    public WebAppProvider()
    {
        try
        {
            final URL url = getClass().getClassLoader().getResource("christophedelory/content/type/webapp/web.xml"); // May throw SecurityException.

            if (url != null)
            {
                _webApp = (WebApp) XmlSerializer.getMapping("christophedelory/content/type/webapp").unmarshal(url); // May throw Exception.
            }
        }
        catch (Exception e)
        {
            _webApp = new WebApp();
        }
    }

    @Override
    public ContentType getContentType(final String contentName)
    {
        ContentType ret = null;
        final int idx = contentName.lastIndexOf('.');

        if (idx >= 0)
        {
            final String ext = contentName.substring(idx); // Shall not throw IndexOutOfBoundsException.
            final String pattern = contentName.substring(idx + 1).toLowerCase(); // Shall not throw IndexOutOfBoundsException.

            for (MimeMapping mapping : _webApp.getMimeMappings())
            {
                if (pattern.equals(mapping.getExtension()) && (mapping.getMimeType() != null))
                {
                    ret = new ContentType(new String[] { ext }, new String[] { mapping.getMimeType() }, null, null); // NOPMD Avoid instantiating new objects inside loops
                    break;
                }
            }
        }

        return ret;
    }
}
