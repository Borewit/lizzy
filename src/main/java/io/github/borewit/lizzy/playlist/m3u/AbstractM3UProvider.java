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
package io.github.borewit.lizzy.playlist.m3u;

import io.github.borewit.lizzy.playlist.*;
import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.input.BOMInputStream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;

import static io.github.borewit.lizzy.playlist.AbstractPlaylistProvider.wrapInBomStream;

/**
 * A simple text-based list of the locations of the items, with each item on a new line.
 *
 * @author Christophe Delory
 * @version $Revision: 91 $
 */
abstract class AbstractM3UProvider implements SpecificPlaylistProvider {

  protected SpecificPlaylist readPlaylist(final AbstractM3U m3uPlaylist, final InputStream inputStream, Charset encoding) throws IOException {

    BOMInputStream bomInputStream = wrapInBomStream(inputStream);

    if (bomInputStream.hasBOM()) {
      ByteOrderMark bom = bomInputStream.getBOM();
      encoding = Charset.forName(bom.getCharsetName());
    }
    final BufferedReader reader = new BufferedReader(new InputStreamReader(bomInputStream, encoding)); // Throws NullPointerException if in is null. May throw UnsupportedEncodingException, IOException.

    m3uPlaylist.setProvider(this);

    String line;
    String songName = null;
    String songLength = null;

    while ((line = reader.readLine()) != null) // May throw IOException.
    {
      line = line.trim();

      if (line.length() > 0) {
        final char firstChar = line.charAt(0); // Shall not throw IndexOutOfBoundsException.

        // Exclude what looks like an XML file, or a Windows .ini file.
        // Files or URLs "usually" don't begin with such characters.
        if ((firstChar == '<') || (firstChar == '[')) {
          throw new IllegalArgumentException("Doesn't seem to be a M3U playlist (and related ones)");
        } else if (firstChar == '#') {
          if (line.toUpperCase(Locale.ENGLISH).startsWith("#EXTINF")) {
            final int indA = line.indexOf(',', 0);

            if (indA >= 0) // NOPMD Deeply nested if then statement
            {
              songName = line.substring(indA + 1, line.length());
            }

            final int indB = line.indexOf(':', 0);

            if ((indB >= 0) && (indB < indA)) // NOPMD Deeply nested if then statement
            {
              songLength = line.substring(indB + 1, indA).trim();
            }
          }
          // Otherwise ignore the comment.
          // In particular VLC directives "EXTVLCOPT:<param>=<value>" are ignored.
          // The same applies to #EXTART for album artist and #EXTALB for album title.
        } else {
          final Resource resource = new Resource(); // NOPMD Avoid instantiating new objects inside loops
          resource.setLocation(line);
          resource.setName(songName); // songName may be null.

          if (songLength != null) {
            resource.setLength(Long.parseLong(songLength)); // May throw NumberFormatException.
          }

          m3uPlaylist.getResources().add(resource);

          songName = null;
          songLength = null;
        }
      }
    }

    return m3uPlaylist;
  }


  /**
   * Adds the resources referenced in the specified generic playlist component to the input list.
   *
   * @param resources the resulting list of resources. Shall not be <code>null</code>.
   * @param component the generic playlist component to handle. Shall not be <code>null</code>.
   */
  protected void addToPlaylist(final List<Resource> resources, final AbstractPlaylistComponent component) {
    if (component instanceof Sequence) {
      final Sequence seq = (Sequence) component;

      if (seq.getRepeatCount() < 0) {
        throw new IllegalArgumentException("A M3U playlist cannot handle a sequence repeated indefinitely");
      }

      for (int iter = 0; iter < seq.getRepeatCount(); iter++) {
        seq.getComponents().forEach(c -> addToPlaylist(resources, c));
      }
    } else if (component instanceof Parallel) {
      throw new IllegalArgumentException("A parallel time container is incompatible with a M3U playlist");
    } else if (component instanceof Media) {
      final Media media = (Media) component;

      if (media.getDuration() != null) {
        throw new IllegalArgumentException("A M3U playlist cannot handle a timed media");
      }

      if (media.getRepeatCount() < 0) {
        throw new IllegalArgumentException("A M3U playlist cannot handle a media repeated indefinitely");
      }

      if (media.getSource() != null) {
        for (int iter = 0; iter < media.getRepeatCount(); iter++) {
          final Resource resource = new Resource(); // NOPMD Avoid instantiating new objects inside loops
          resource.setLocation(media.getSource().toString());

          if (media.getSource().getDuration() >= 0L) {
            resource.setLength((media.getSource().getDuration() + 999L) / 1000L);
          }

          resources.add(resource); // Shall not throw UnsupportedOperationException, ClassCastException, NullPointerException, IllegalArgumentException.
        }
      }
    }
  }
}
