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

import java.util.Locale;

import christophedelory.playlist.SpecificPlaylistFactory;
import christophedelory.playlist.SpecificPlaylistProvider;

/**
 * A content type provider taking its input from the {@link SpecificPlaylistProvider specific playlist providers}.
 * @version $Revision: 90 $
 * @author Christophe Delory
 */
public class SpecificPlaylistTypeProvider implements IContentTypeProvider
{
    @Override
    public ContentType getContentType(final String contentName)
    {
        final String name = contentName.toLowerCase(Locale.ENGLISH); // Throws NullPointerException if contentName is null.

        ContentType ret = null;
        final SpecificPlaylistProvider provider = SpecificPlaylistFactory.getInstance().findProviderByExtension(name);

        if (provider != null)
        {
            // If a provider is returned, one of its content types must match.
            final ContentType[] contentTypes = provider.getContentTypes();

            for (ContentType contentType : contentTypes)
            {
                if (contentType.matchExtension(name))
                {
                    ret = contentType;
                    break;
                }
            }
        }

        return ret;
    }
}
