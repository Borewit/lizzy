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
package io.github.borewit.lizzy.playlist.mpcpl;

import io.github.borewit.lizzy.content.Content;
import io.github.borewit.lizzy.playlist.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * The Media Player Classic Playlist (MPCPL) format.
 *
 * @author Christophe Delory
 * @version $Revision: 91 $
 * @since 0.3.0
 */
public class MPCPL implements SpecificPlaylist {
  /**
   * The provider of this specific playlist.
   */
  private transient SpecificPlaylistProvider _provider = null;

  /**
   * The list of child resources.
   */
  private final List<Resource> _resources = new ArrayList<Resource>();

  public void setProvider(final SpecificPlaylistProvider provider) {
    _provider = provider;
  }

  @Override
  public SpecificPlaylistProvider getProvider() {
    return _provider;
  }

  @Override
  public void writeTo(final OutputStream out) throws IOException {
    final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, MPCPLProvider.TextEncoding)); // Throws NullPointerException if out is null. May throw UnsupportedEncodingException.

    writer.write("MPCPLAYLIST"); // May throw IOException.
    writer.newLine(); // May throw IOException.

    int i = 1;

    for (Resource resource : _resources) {
      writer.write(Integer.toString(i)); // May throw IOException.
      writer.write(",type,"); // May throw IOException.
      writer.write(resource.getType()); // May throw IOException.
      writer.newLine(); // May throw IOException.

      writer.write(Integer.toString(i)); // May throw IOException.
      writer.write(",filename,"); // May throw IOException.
      writer.write(resource.getFilename()); // May throw NullPointerException, IOException.
      writer.newLine(); // May throw IOException.

      if (resource.getSubtitle() != null) {
        writer.write(Integer.toString(i)); // May throw IOException.
        writer.write(",subtitle,"); // May throw IOException.
        writer.write(resource.getSubtitle()); // May throw IOException.
        writer.newLine(); // May throw IOException.
      }

      i++;
    }

    writer.flush(); // May throw IOException.
  }

  @Override
  public Playlist toPlaylist() {
    final Playlist ret = new Playlist();

    for (Resource resource : _resources) {
      if (resource.getFilename() != null) {
        final Media media = new Media(); // NOPMD Avoid instantiating new objects inside loops
        final Content content = new Content(resource.getFilename()); // NOPMD Avoid instantiating new objects inside loops
        media.setSource(content);
        ret.getRootSequence().addComponent(media);
      }
    }

    ret.normalize();

    return ret;
  }

  /**
   * Returns the list of playlist resources.
   *
   * @return a list of child resources. May be empty but not <code>null</code>.
   */
  public List<Resource> getResources() {
    return _resources;
  }
}
