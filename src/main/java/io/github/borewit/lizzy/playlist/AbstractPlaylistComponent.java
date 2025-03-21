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

import java.io.IOException;

/**
 * The base component of a playlist.
 *
 * @author Christophe Delory
 * @version $Revision: 92 $
 */
public abstract class AbstractPlaylistComponent {
  /**
   * The number of iterations of this playlist component.
   */
  private float _repeatCount = 1.0f;

  /**
   * The parent container of this playlist component.
   */
  private transient AbstractTimeContainer _parent = null;

  /**
   * Initializes the parent container of this playlist component.
   *
   * @param parent a container. May be <code>null</code>.
   * @see #getParent
   */
  void setParent(final AbstractTimeContainer parent) {
    _parent = parent;
  }

  /**
   * Returns the parent container of this playlist component, if any.
   *
   * @return the parent container. May be <code>null</code> if this component hasn't been added to a playlist yet.
   */
  public AbstractTimeContainer getParent() {
    return _parent;
  }

  /**
   * Returns the number of iterations of this playlist component.
   * If negative, represents an indefinite repeat count.
   * Zero (0) means never played.
   * The default value is one (1).
   *
   * @return the element's repeat count.
   * @see #setRepeatCount
   */
  public float getRepeatCount() {
    return _repeatCount;
  }

  /**
   * Initializes the number of iterations of this playlist component.
   *
   * @param repeatCount a repeat count.
   * @see #getRepeatCount
   */
  public void setRepeatCount(final Float repeatCount) {
    this._repeatCount = repeatCount == null ? 1.0f : repeatCount;
  }

  /**
   * Accepts the specified playlist visitor.
   *
   * @param visitor a visitor. Shall not be <code>null</code>.
   * @throws NullPointerException if <code>visitor</code> is <code>null</code>.
   * @throws IOException          if any error occurs during the visit.
   */
  public abstract void acceptDown(PlaylistVisitor visitor) throws IOException;

  /**
   * Accepts the specified playlist visitor.
   *
   * @param visitor a visitor. Shall not be <code>null</code>.
   * @throws NullPointerException if <code>visitor</code> is <code>null</code>.
   * @throws IOException          if any error occurs during the visit.
   */
  public void acceptUp(PlaylistVisitor visitor) throws IOException {
    if (_parent != null) {
      _parent.acceptUp(visitor); // May throw Exception. Throws NullPointerException if visitor is null.
    }
  }
}
