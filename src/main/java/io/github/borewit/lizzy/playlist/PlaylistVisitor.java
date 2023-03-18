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

/**
 * A {@link Playlist playlist} hierarchy visitor.
 *
 * @author Christophe Delory
 * @version $Revision: 92 $
 * @see Playlist#acceptDown
 * @see Playlist#acceptUp
 * @see Parallel#acceptDown
 * @see Parallel#acceptUp
 * @see Sequence#acceptDown
 * @see Sequence#acceptUp
 * @see Media#acceptDown
 * @see Media#acceptUp
 */
public interface PlaylistVisitor
{
  /**
   * Starts the visit of the specified playlist.
   *
   * @param target the element being visited. Shall not be <code>null</code>.
   * @see #endVisitPlaylist
   */
  void beginVisitPlaylist(Playlist target);

  /**
   * Finishes the visit of the specified playlist.
   *
   * @param target the element being visited. Shall not be <code>null</code>.
   * @see #beginVisitPlaylist
   */
  void endVisitPlaylist(Playlist target);

  /**
   * Starts the visit of the specified parallel timing container.
   *
   * @param target the element being visited. Shall not be <code>null</code>.
   * @see #endVisitParallel
   */
  void beginVisitParallel(Parallel target);

  /**
   * Finishes the visit of the specified parallel timing container.
   *
   * @param target the element being visited. Shall not be <code>null</code>.
   * @see #beginVisitParallel
   */
  void endVisitParallel(Parallel target);

  /**
   * Starts the visit of the specified sequence.
   *
   * @param target the element being visited. Shall not be <code>null</code>.
   * @see #endVisitSequence
   */
  void beginVisitSequence(Sequence target);

  /**
   * Finishes the visit of the specified sequence.
   *
   * @param target the element being visited. Shall not be <code>null</code>.
   * @see #beginVisitSequence
   */
  void endVisitSequence(Sequence target);

  /**
   * Starts the visit of the specified media.
   *
   * @param target the element being visited. Shall not be <code>null</code>.
   * @see #endVisitMedia
   */
  void beginVisitMedia(Media target);

  /**
   * Finishes the visit of the specified media.
   *
   * @param target the element being visited. Shall not be <code>null</code>.
   * @see #beginVisitMedia
   */
  void endVisitMedia(Media target);
}
