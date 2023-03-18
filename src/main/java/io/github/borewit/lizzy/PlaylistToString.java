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

import io.github.borewit.lizzy.content.Content;
import io.github.borewit.lizzy.playlist.*;

/**
 * Builds a string representation of a given {@link Playlist playlist}.
 *
 * @author Christophe Delory
 * @version $Revision: 92 $
 */
public class PlaylistToString extends BasePlaylistVisitor
{
  /**
   * The string in construction.
   */
  private final StringBuilder _sb;

  /**
   * The level of indentation.
   */
  private int _indent;

  /**
   * The debug mode.
   */
  private boolean _debug;

  /**
   * Builds a new playlist visitor.
   */
  public PlaylistToString()
  {
    _sb = new StringBuilder();
    _indent = 0;
    _debug = false;
  }

  /**
   * Enables or disables the debug mode.
   * Extra information is added in debug mode.
   * Defaults to disabled.
   *
   * @param debug the debug mode.
   */
  public void setDebug(final boolean debug)
  {
    _debug = debug;
  }

  @Override
  public String toString()
  {
    return _sb.toString();
  }

  @Override
  public void beginVisitParallel(final Parallel target)
  {
    addIndent();
    _sb.append("PARALLEL(x");
    _sb.append(target.getRepeatCount());

    if (_debug)
    {
      _sb.append(", parent=");
      _sb.append(target.getParent() != null);
    }

    _sb.append(")\n");

    _indent += 2;
  }

  @Override
  public void endVisitParallel(final Parallel target)
  {
    _indent -= 2;
  }

  @Override
  public void beginVisitSequence(final Sequence target)
  {
    addIndent();
    _sb.append("SEQUENCE(x");
    _sb.append(target.getRepeatCount());

    if (_debug)
    {
      _sb.append(", parent=");
      _sb.append(target.getParent() != null);
    }

    _sb.append(")\n");

    _indent += 2;
  }

  @Override
  public void endVisitSequence(final Sequence target)
  {
    _indent -= 2;
  }

  @Override
  public void beginVisitMedia(final Media target)
  {
    addIndent();
    _sb.append("MEDIA(x");
    _sb.append(target.getRepeatCount());

    if (target.getDuration() != null)
    {
      _sb.append(", ");
      _sb.append(target.getDuration());
      _sb.append("ms");
    }

    if (_debug)
    {
      _sb.append(", parent=");
      _sb.append(target.getParent() != null);
    }

    _sb.append(')');

    final Content content = target.getSource();

    if (content != null)
    {
      _sb.append(": ");
      _sb.append(content);
      _sb.append(" [length=");
      _sb.append(content.getLength());
      _sb.append(" bytes");

      if (content.getDuration() >= 0L)
      {
        _sb.append(", duration=");
        _sb.append(content.getDuration());
        _sb.append("ms");
      }

      if ((content.getWidth() >= 0) && (content.getHeight() >= 0))
      {
        _sb.append(", ");
        _sb.append(content.getWidth());
        _sb.append('x');
        _sb.append(content.getHeight());
      }

      if (content.getType() != null)
      {
        _sb.append(", type=");
        _sb.append(content.getType());
      }

      if (content.getEncoding() != null)
      {
        _sb.append(", encoding=");
        _sb.append(content.getEncoding());
      }

      _sb.append(']');
    }

    _sb.append('\n');
  }

  /**
   * Adds the necessary number of spaces in the underlying <code>StringBuilder</code>.
   *
   * @see #_indent
   * @see #_sb
   */
  private void addIndent()
  {
    for (int i = 0; i < _indent; i++)
    {
      _sb.append(' ');
    }
  }
}
