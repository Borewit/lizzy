package io.github.borewit.lizzy.playlist.m3u;

import io.github.borewit.lizzy.content.type.ContentType;
import io.github.borewit.lizzy.player.PlayerSupport;
import io.github.borewit.lizzy.playlist.Playlist;
import io.github.borewit.lizzy.playlist.SpecificPlaylist;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class M3U8Provider extends AbstractM3UProvider {

  public static final Charset M3u8TextEncoding = StandardCharsets.UTF_8;

  /**
   * A list of compatible content types.
   */
  private static final ContentType[] FILETYPES =
    {
      new ContentType(new String[]{".m3u8"},
        new String[]{"audio/x-mpegurl", "audio/mpegurl"},
        new PlayerSupport[]
          {
            new PlayerSupport(PlayerSupport.Player.WINAMP, true, null),
            new PlayerSupport(PlayerSupport.Player.FOOBAR2000, true, null),
          },
        "Winamp M3U8"),
    };

  @Override
  public String getId() {
    return "m3u8";
  }

  @Override
  public ContentType[] getContentTypes() {
    return FILETYPES.clone();
  }

  @Override
  public SpecificPlaylist toSpecificPlaylist(final Playlist playlist) {
    final M3U8 ret = new M3U8();
    ret.setProvider(this);

    super.addToPlaylist(ret.getResources(), playlist.getRootSequence()); // May throw Exception.

    return ret;
  }

  public SpecificPlaylist readFrom(final InputStream inputStream) throws IOException {
    return super.readPlaylist(new M3U8(), inputStream, M3u8TextEncoding);
  }
}
