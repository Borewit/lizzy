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
package io.github.borewit.lizzy;

import io.github.borewit.lizzy.content.ContentMetadataCenter;
import io.github.borewit.lizzy.content.type.ContentType;
import io.github.borewit.lizzy.content.type.ContentTypeProvider;
import io.github.borewit.lizzy.playlist.BasePlaylistVisitor;
import io.github.borewit.lizzy.playlist.Media;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Fetches if possible the metadata of all media contents.
 *
 * @author Christophe Delory
 * @version $Revision: 90 $
 */
public class FetchContentMetadata extends BasePlaylistVisitor
{
  /**
   * The logger.
   */
  private final Logger logger = LogManager.getLogger(FetchContentMetadata.class);

  /**
   * Specifies if we must connect to the media content URL in order to fetch additional metadata.
   */
  private boolean _connect = false;

  /**
   * Specifies if we must connect to the media content URL in order to fetch additional metadata.
   * Defaults to <code>false</code>.
   *
   * @param connect the corresponding boolean.
   */
  public void setConnect(final boolean connect)
  {
    _connect = connect;
  }

  @Override
  public void beginVisitMedia(final Media target)
  {
    if (target.getSource() != null)
    {
      if (_connect)
      {
        try
        {
          target.getSource().connect(); // May throw IOException, SecurityException, IllegalArgumentException.
        }
        catch (Exception e)
        {
          logger.warn("Cannot access media content " + target.getSource(), e);
        }

        ContentMetadataCenter.getInstance().fillMetadata(target.getSource());
      }

      // Override the type, if we can find a valid one.
      try
      {
        final ContentType contentType = ContentTypeProvider.getInstance().getContentType(target.getSource().getURI()); // May throw SecurityException, URISyntaxException.

        if (contentType != null)
        {
          target.getSource().setType(contentType.getMimeTypes()[0]);
        }
      }
      catch (Exception e)
      {
        logger.warn("Cannot build URI for media content " + target.getSource(), e);
      }
    }
  }
}
