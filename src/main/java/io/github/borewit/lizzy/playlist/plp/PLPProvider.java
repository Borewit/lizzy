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
package io.github.borewit.lizzy.playlist.plp;

import io.github.borewit.lizzy.content.type.ContentType;
import io.github.borewit.lizzy.player.PlayerSupport;
import io.github.borewit.lizzy.playlist.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * The PLP playlist format, which lists locations of files in a standard text format.
 *
 * @author Christophe Delory
 * @version $Revision: 91 $
 * @since 0.2.0
 */
public class PLPProvider implements SpecificPlaylistProvider {
  private final Logger logger = LogManager.getLogger(PLPProvider.class);

  public static Charset TextEncoding = StandardCharsets.UTF_16LE;

  /**
   * A list of compatible content types.
   */
  private static final ContentType[] FILETYPES =
    {
      new ContentType(new String[]{".plp"},
        new String[]{"text/plain"}, // FIXME
        new PlayerSupport[]
          {
          },
        "Sansa Playlist File"),
    };

  @Override
  public String getId() {
    return "plp";
  }

  @Override
  public ContentType[] getContentTypes() {
    return FILETYPES.clone();
  }

  @Override
  public SpecificPlaylist readFrom(final InputStream inputStream) throws IOException {
    final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, TextEncoding)); // Throws NullPointerException if in is null. May throw UnsupportedEncodingException, IOException.

    PLP ret = new PLP();
    ret.setProvider(this);

    String line;
    boolean magic1Found = false;
    boolean magic2Found = false;
    String disk = null;

    while ((line = reader.readLine()) != null) // May throw IOException.
    {
      line = line.trim();

      if (line.length() > 0) {
        // First the PLP marker string.
        if (!magic1Found) {
          if (!"PLP PLAYLIST".equals(line)) // NOPMD Deeply nested if then statement
          {
            throw new IllegalArgumentException("Not a PLP playlist format");
          }

          magic1Found = true;
          continue;
        }

        // Then the version marker string.
        if (!magic2Found) {
          if (!"VERSION 1.20".equals(line)) // NOPMD Deeply nested if then statement
          {
            logger.error("Malformed PLP playlist (no version information)");
            ret = null;
            break;
          }

          magic2Found = true;
          continue;
        }

        final int idx = line.indexOf(',');

        if (idx <= 0) {
          logger.error("Malformed PLP playlist (playlist entry line format)");
          ret = null;
          break;
        }

        final String tmpDisk = line.substring(0, idx).trim(); // Shall not throw IndexOutOfBoundsException.

        if (disk == null) {
          disk = tmpDisk;
        } else if (!disk.equals(tmpDisk)) {
          logger.error("Malformed PLP playlist (inconsistent disk specifier)");
          ret = null;
          break;
        }

        ret.getFilenames().add(line.substring(idx + 1).trim()); // Shall not throw IndexOutOfBoundsException.
      }
    }

    if ((ret != null) && (disk != null)) {
      ret.setDiskSpecifier(disk);
    }

    return ret;
  }

  @Override
  public SpecificPlaylist toSpecificPlaylist(final Playlist playlist) throws IOException {
    final PLP ret = new PLP();
    ret.setProvider(this);

    addToPlaylist(ret.getFilenames(), playlist.getRootSequence()); // May throw Exception.

    return ret;
  }

  /**
   * Adds the song file names referenced in the specified generic playlist component to the input list.
   *
   * @param filenames the resulting list of file names. Shall not be <code>null</code>.
   * @param component the generic playlist component to handle. Shall not be <code>null</code>.
   */
  private void addToPlaylist(final List<String> filenames, final AbstractPlaylistComponent component) {
    if (component instanceof Sequence) {
      final Sequence seq = (Sequence) component;

      if (seq.getRepeatCount() < 0) {
        throw new IllegalArgumentException("A PLP playlist cannot handle a sequence repeated indefinitely");
      }

      for (int iter = 0; iter < seq.getRepeatCount(); iter++) {
        seq.getComponents().forEach(c -> addToPlaylist(filenames, c));
      }
    } else if (component instanceof Parallel) {
      throw new IllegalArgumentException("A parallel time container is incompatible with a PLP playlist");
    } else if (component instanceof Media) {
      final Media media = (Media) component;

      if (media.getDuration() != null) {
        throw new IllegalArgumentException("A PLP playlist cannot handle a timed media");
      }

      if (media.getRepeatCount() < 0) {
        throw new IllegalArgumentException("A PLP playlist cannot handle a media repeated indefinitely");
      }

      if (media.getSource() != null) {
        for (int iter = 0; iter < media.getRepeatCount(); iter++) {
          filenames.add(media.getSource().toString()); // Shall not throw UnsupportedOperationException, ClassCastException, NullPointerException, IllegalArgumentException.
        }
      }
    }
  }
}
