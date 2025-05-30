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
 * A playlist sequence: the media contents play one after the other.
 *
 * @author Christophe Delory
 * @version $Revision: 90 $
 */
public class Sequence extends AbstractTimeContainer {
  @Override
  public void acceptDown(final PlaylistVisitor visitor) throws IOException {
    visitor.beginVisitSequence(this); // Throws NullPointerException if visitor is null. May throw Exception.

    super.acceptDown(visitor); // May throw Exception.

    visitor.endVisitSequence(this); // May throw Exception.
  }

  @Override
  public void acceptUp(final PlaylistVisitor visitor) throws IOException {
    visitor.beginVisitSequence(this); // Throws NullPointerException if visitor is null. May throw Exception.

    super.acceptUp(visitor); // May throw Exception.

    visitor.endVisitSequence(this); // May throw Exception.
  }
}
