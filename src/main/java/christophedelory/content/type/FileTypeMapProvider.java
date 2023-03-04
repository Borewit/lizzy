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

import javax.activation.FileTypeMap;

/**
 * A content type provider based on {@link FileTypeMap}.
 * @version $Revision: 90 $
 * @author Christophe Delory
 */
public class FileTypeMapProvider implements ContentTypeProvider
{
    @Override
    public ContentType getContentType(final String contentName)
    {
        ContentType ret = null;
        final int idx = contentName.lastIndexOf('.');

        if (idx >= 0)
        {
            final String ext = contentName.substring(idx); // Shall not throw IndexOutOfBoundsException.

            final FileTypeMap map = FileTypeMap.getDefaultFileTypeMap();
            final String contentType = map.getContentType(contentName);

            // Test the default map.
            if (!contentType.equals("application/octet-stream"))
            {
                ret = new ContentType(new String[] { ext }, new String[] { contentType }, null, null);
            }
        }

        return ret;
    }
}
