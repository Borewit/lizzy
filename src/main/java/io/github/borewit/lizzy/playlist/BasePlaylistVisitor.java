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
 * A starting point for a {@link Playlist playlist} visitor.
 *
 * @author Christophe Delory
 * @version $Revision: 90 $
 */
public class BasePlaylistVisitor implements PlaylistVisitor {
  @Override
  public void beginVisitPlaylist(final Playlist target) {
    // No-op.
  }

  @Override
  public void endVisitPlaylist(final Playlist target) {
    // No-op.
  }

  @Override
  public void beginVisitParallel(final Parallel target) {
    // No-op.
  }

  @Override
  public void endVisitParallel(final Parallel target) {
    // No-op.
  }

  @Override
  public void beginVisitSequence(final Sequence target) {
    // No-op.
  }

  @Override
  public void endVisitSequence(final Sequence target) {
    // No-op.
  }

  @Override
  public void beginVisitMedia(final Media target) {
    // No-op.
  }

  @Override
  public void endVisitMedia(final Media target) {
    // No-op.
  }
}
