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
package io.github.borewit.lizzy.content;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ServiceLoader;

/**
 * Fills the metadata of a given {@link Content content} through {@link ContentMetadataProvider metadata providers}.
 * These metadata should be:
 * <ul>
 * <li>The content {@link Content#setDuration duration}</li>
 * <li>The content {@link Content#setWidth width} and {@link Content#setHeight height}</li>
 * </ul>
 *
 * @author Christophe Delory
 * @version $Revision: 92 $
 * @since 1.0.0
 */
public final class ContentMetadataCenter
{
  /**
   * The singleton instance.
   */
  private static ContentMetadataCenter _instance = null;

  /**
   * Returns the unique class instance.
   *
   * @return an instance of this class. Shall not be <code>null</code>.
   */
  public static ContentMetadataCenter getInstance()
  {
    synchronized (ContentMetadataCenter.class)
    {
      if (_instance == null)
      {
        _instance = new ContentMetadataCenter();
      }
    }

    return _instance;
  }

  /**
   * The associated service provider loader.
   */
  private final ServiceLoader<ContentMetadataProvider> _serviceLoader;

  /**
   * The logger attached to this instance.
   */
  private final Logger logger = LogManager.getLogger(ContentMetadataCenter.class);

  /**
   * Builds a new content metadata center.
   */
  private ContentMetadataCenter()
  {
    _serviceLoader = ServiceLoader.load(ContentMetadataProvider.class);
  }

  /**
   * Refreshes the list of providers managed by this center.
   * If new providers are added after the instantiation of this class, you will need to call this method manually.
   */
  public void reloadProviders()
  {
    _serviceLoader.reload();
  }

  /**
   * Fills if possible the metadata of the specified content.
   *
   * @param content a content. Shall not be <code>null</code>.
   * @return <code>true</code> if a provider recognized the format, <code>false</code> otherwise.
   * @throws NullPointerException if <code>content</code> is <code>null</code>.
   * @see ContentMetadataProvider#fillMetadata
   */
  public boolean fillMetadata(final Content content)
  {
    boolean ret = false;

    for (ContentMetadataProvider service : _serviceLoader)
    {
      try
      {
        service.fillMetadata(content); // Throws NullPointerException if content is null. May throw Exception.

        // If we reached this point, it's OK.
        ret = true;
        break;
      }
      catch (Throwable e)
      {
        // Ignore it.
        if (logger.isTraceEnabled())
        {
          logger.trace("Metadata provider " + service + " cannot handle content <" + content + ">", e);
        }
        else if (logger.isDebugEnabled())
        {
          logger.debug("Metadata provider " + service + " cannot handle content <" + content + ">: " + e);
        }
      }
    }

    return ret;
  }
}
