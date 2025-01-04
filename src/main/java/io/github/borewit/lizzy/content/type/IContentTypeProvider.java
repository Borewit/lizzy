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
package io.github.borewit.lizzy.content.type;

/**
 * Provides if possible a content type for a given media content.
 * Concrete classes must have a zero-argument constructor so that they can be instantiated during loading.
 *
 * @author Christophe Delory
 * @version $Revision: 55 $
 */
public interface IContentTypeProvider {
  /**
   * Returns a content type representing the given content name (file or URL).
   *
   * @param contentName a content (file or URL) name. Shall not be <code>null</code>.
   * @return a content type. May be <code>null</code> if none was found.
   * @throws NullPointerException if <code>contentName</code> is <code>null</code>.
   */
  ContentType getContentType(String contentName);
}
