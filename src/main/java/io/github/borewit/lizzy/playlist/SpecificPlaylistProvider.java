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
package io.github.borewit.lizzy.playlist;

import io.github.borewit.lizzy.content.type.ContentType;

import java.io.IOException;
import java.io.InputStream;

/**
 * A specific playlist provider manages a given type of playlist (for example ASX or M3U).
 * Concrete classes must have a zero-argument constructor so that they can be instantiated during loading.
 *
 * @author Christophe Delory
 * @version $Revision: 92 $
 */
public interface SpecificPlaylistProvider {
  /**
   * Returns a unique string identifying the type of specific playlists handled by this provider.
   *
   * @return a playlist identifier. Shall not be <code>null</code>.
   */
  String getId();

  /**
   * Returns a list of one or more content types representing the playlists compatible with this provider.
   * The list should be ordered from the most used / recommended to the least one.
   *
   * @return a list of content types. Shall not be <code>null</code> nor empty.
   */
  ContentType[] getContentTypes();

  /**
   * Reads a playlist from the specified input stream.
   * When done, the stream remains open.
   *
   * @param inputStream an input stream. Shall not be <code>null</code>.
   * @return a new playlist instance, or <code>null</code> if the format has been recognized, but the playlist is malformed.
   * @throws NullPointerException if <code>in</code> is <code>null</code>.
   * @throws NullPointerException if <code>logger</code> is <code>null</code>.
   * @throws IOException          if any error occurs during the unmarshalling process.
   * @see SpecificPlaylistFactory#readFrom
   * @see SpecificPlaylist#writeTo
   */
  SpecificPlaylist readFrom(InputStream inputStream) throws IOException;

  /**
   * Builds a specific representation of the given generic playlist.
   *
   * @param playlist a generic playlist. Shall not be <code>null</code>.
   * @return a specific service playlist. Shall not be <code>null</code>.
   * @throws NullPointerException if <code>playlist</code> is <code>null</code>.
   * @throws IOException          if this service provider is unable to represent the input playlist.
   * @see SpecificPlaylist#toPlaylist
   */
  SpecificPlaylist toSpecificPlaylist(Playlist playlist) throws IOException;
}
