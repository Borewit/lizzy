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
package io.github.borewit.lizzy.playlist.pls;

import io.github.borewit.lizzy.content.Content;
import io.github.borewit.lizzy.playlist.*;
import io.github.borewit.lizzy.playlist.m3u.Resource;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * PLS playlist format.
 * The format is essentially that of an INI file structured as follows:
 * <ul>
 * <li>Header</li>
 * <ul>
 * <li>"[playlist]": this tag indicates that it is a playlist file.</li>
 * <li>"NumberOfEntries": this variable indicates the number of tracks.</li>
 * </ul>
 * <li>Track entry (assuming track entry #X)</li>
 * <ul>
 * <li>"FileX": variable defining location of stream.</li>
 * <li>"TitleX": defines track title.</li>
 * <li>"LengthX": length in seconds of track. Value of -1 indicates indefinite.</li>
 * </ul>
 * <li>Footer</li>
 * <ul>
 * <li>"Version": playlist version. Currently only a value of 2 is valid.</li>
 * </ul>
 * </ul>
 *
 * @author Borewit
 * @author Christophe Delory
 * @version $Revision: 91 $
 */
public class PLS implements SpecificPlaylist {
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

    final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, PLSProvider.TextEncoding)); // Throws NullPointerException if out is null. May throw UnsupportedEncodingException.

    writer.write("[Playlist]"); // May throw IOException.
    writer.newLine(); // May throw IOException.

    writer.write("NumberOfEntries="); // May throw IOException.

    writer.write(Integer.toString(_resources.size()));
    writer.newLine(); // May throw IOException.

    int i = 1;

    for (Resource resource : _resources) {
      writer.write("File"); // May throw IOException.
      writer.write(Integer.toString(i)); // May throw IOException.
      writer.write("="); // May throw IOException.
      writer.write(resource.getLocation()); // May throw NullPointerException, IOException.
      writer.newLine(); // May throw IOException.

      if (resource.getName() != null) {
        writer.write("Title"); // May throw IOException.
        writer.write(Integer.toString(i)); // May throw IOException.
        writer.write("="); // May throw IOException.
        writer.write(resource.getName()); // May throw IOException.
        writer.newLine(); // May throw IOException.
      }

      if (resource.getLength() >= 0L) {
        writer.write("Length"); // May throw IOException.
        writer.write(Integer.toString(i)); // May throw IOException.
        writer.write("="); // May throw IOException.
        writer.write(Long.toString(resource.getLength())); // May throw IOException.
        writer.newLine(); // May throw IOException.
      }

      i++;
    }

    writer.write("Version=2"); // May throw IOException.
    writer.newLine(); // May throw IOException.

    writer.flush(); // May throw IOException.
  }

  @Override
  public Playlist toPlaylist() {
    final Playlist ret = new Playlist();

    for (Resource resource : _resources) {
      if (resource.getLocation() != null) {
        final Media media = new Media(); // NOPMD Avoid instantiating new objects inside loops
        final Content content = new Content(resource.getLocation()); // NOPMD Avoid instantiating new objects inside loops
        media.setSource(content);
        content.setDuration(resource.getLength() * 1000L);
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
