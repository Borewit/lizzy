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
 * The Media Player Classic Playlist (MPCPL) provider.
 *
 * @author Christophe Delory
 * @version $Revision: 91 $
 * @since 0.3.0
 */
public class MPCPLProvider implements SpecificPlaylistProvider {
  private final Logger logger = LogManager.getLogger(MPCPLProvider.class);

  public static Charset TextEncoding = StandardCharsets.UTF_8;

  /**
   * A list of compatible content types.
   */
  private static final ContentType[] FILETYPES =
    {
      new ContentType(new String[]{".mpcpl"},
        new String[]{"text/plain"}, // FIXME
        new PlayerSupport[]
          {
            new PlayerSupport(PlayerSupport.Player.MEDIA_PLAYER_CLASSIC, true, null),
          },
        "Media Player Classic Playlist"),
    };

  @Override
  public String getId() {
    return "mpcpl";
  }

  @Override
  public ContentType[] getContentTypes() {
    return FILETYPES.clone();
  }

  @Override
  public SpecificPlaylist readFrom(final InputStream inputStream) throws IOException {
    final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, TextEncoding)); // Throws NullPointerException if in is null. May throw UnsupportedEncodingException, IOException.

    MPCPL ret = new MPCPL();
    ret.setProvider(this);

    String line;
    boolean magicFound = false;

    while ((line = reader.readLine()) != null) // May throw IOException.
    {
      line = line.trim();

      if (line.length() > 0) {
        // First the MPCPL marker string.
        if (!magicFound) {
          if (!line.equalsIgnoreCase("MPCPLAYLIST")) // NOPMD Deeply nested if then statement
          {
            throw new IllegalArgumentException("Not a MPCPL playlist format");
          }

          magicFound = true;
          continue;
        }

        int idx = line.indexOf(',');

        if (idx <= 0) {
          logger.error("Malformed MPCPL playlist entry " + line);
          ret = null;
          break;
        }

        final String resourceIndexString = line.substring(0, idx).trim(); // Shall not throw IndexOutOfBoundsException.
        line = line.substring(idx + 1); // Shall not throw IndexOutOfBoundsException.

        idx = line.indexOf(',');

        if (idx <= 0) {
          logger.error("Malformed MPCPL playlist entry " + line);
          ret = null;
          break;
        }

        final String key = line.substring(0, idx).trim().toLowerCase(); // Shall not throw IndexOutOfBoundsException.
        final String value = line.substring(idx + 1).trim(); // Shall not throw IndexOutOfBoundsException.

        int resourceIndex;

        try {
          resourceIndex = Integer.parseInt(resourceIndexString) - 1; // May throw NumberFormatException.
        } catch (NumberFormatException e) {
          logger.error(e.toString());
          ret = null;
          break;
        }

        // Ensure that the resource list has enough slots.
        for (int i = ret.getResources().size(); i < (resourceIndex + 1); i++) {
          ret.getResources().add(new Resource()); // NOPMD Avoid instantiating new objects inside loops
        }

        final Resource resource = ret.getResources().get(resourceIndex); // Shall not throw ArrayIndexOutOfBoundsException.

        if ("filename".equals(key)) {
          resource.setFilename(value);
        } else if ("type".equals(key)) {
          resource.setType(value);
        } else if ("subtitle".equals(key)) {
          resource.setSubtitle(value);
        } else {
          logger.warn("Unknown MPCPL keyword " + key);
        }
      }
    }

    return ret;
  }

  @Override
  public SpecificPlaylist toSpecificPlaylist(final Playlist playlist) throws IOException {
    final MPCPL ret = new MPCPL();
    ret.setProvider(this);

    addToPlaylist(ret.getResources(), playlist.getRootSequence()); // May throw Exception.

    return ret;
  }

  /**
   * Adds the resources referenced in the specified generic playlist component to the input list.
   *
   * @param resources the resulting list of resources. Shall not be <code>null</code>.
   * @param component the generic playlist component to handle. Shall not be <code>null</code>.
   */
  private void addToPlaylist(final List<Resource> resources, final AbstractPlaylistComponent component) {
    if (component instanceof Sequence) {
      final Sequence seq = (Sequence) component;

      if (seq.getRepeatCount() < 0) {
        throw new IllegalArgumentException("A MPCPL playlist cannot handle a sequence repeated indefinitely");
      }

      for (int iter = 0; iter < seq.getRepeatCount(); iter++) {
        seq.getComponents().forEach(c -> addToPlaylist(resources, c));
      }
    } else if (component instanceof Parallel) {
      throw new IllegalArgumentException("A parallel time container is incompatible with a MPCPL playlist");
    } else if (component instanceof Media) {
      final Media media = (Media) component;

      if (media.getDuration() != null) {
        throw new IllegalArgumentException("A MPCPL playlist cannot handle a timed media");
      }

      if (media.getRepeatCount() < 0) {
        throw new IllegalArgumentException("A MPCPL playlist cannot handle a media repeated indefinitely");
      }

      if (media.getSource() != null) {
        for (int iter = 0; iter < media.getRepeatCount(); iter++) {
          final Resource resource = new Resource(); // NOPMD Avoid instantiating new objects inside loops
          resource.setFilename(media.getSource().toString());
          resources.add(resource); // Shall not throw UnsupportedOperationException, ClassCastException, NullPointerException, IllegalArgumentException.
        }
      }
    }
  }
}
