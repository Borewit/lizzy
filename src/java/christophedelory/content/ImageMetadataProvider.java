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
package christophedelory.content;

import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;

/**
 * A content metadata provider based on a AWT {@link BufferedImage}.
 * GIF, PNG, JPEG, BMP, and WBMP formats are supported.
 * @version $Revision: 90 $
 * @author Christophe Delory
 * @since 1.0.0
 */
public class ImageMetadataProvider implements ContentMetadataProvider
{
    @Override
    public void fillMetadata(final Content content, final Log logger) throws Exception
    {
        final URL url = content.getURL(); // Throws NullPointerException if content is null. May throw SecurityException, IllegalArgumentException, MalformedURLException.

        final BufferedImage image = ImageIO.read(url); // Shall not throw IllegalArgumentException. May throw IOException.
        // image may be null.

        content.setWidth(image.getWidth()); // Throws NullPointerException if image is null.
        content.setHeight(image.getHeight());
        content.setDuration(0L); // FIXME We don't handle here animated GIFs.
    }
}
