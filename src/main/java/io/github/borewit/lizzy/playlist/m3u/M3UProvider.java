package io.github.borewit.lizzy.playlist.m3u;

import io.github.borewit.lizzy.content.type.ContentType;
import io.github.borewit.lizzy.player.PlayerSupport;
import io.github.borewit.lizzy.playlist.Playlist;
import io.github.borewit.lizzy.playlist.SpecificPlaylist;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class M3UProvider extends AbstractM3UProvider
{

  public static final Charset M3uTextEncoding = StandardCharsets.ISO_8859_1;

  /**
   * A list of compatible content types.
   */
  private static final ContentType[] FILETYPES =
    {
      new ContentType(new String[]{".m3u"},
        new String[]{"audio/x-mpegurl", "audio/mpegurl"},
        new PlayerSupport[]
          {
            new PlayerSupport(PlayerSupport.Player.WINAMP, true, null),
            new PlayerSupport(PlayerSupport.Player.VLC_MEDIA_PLAYER, true, null),
            new PlayerSupport(PlayerSupport.Player.WINDOWS_MEDIA_PLAYER, true, null),
            new PlayerSupport(PlayerSupport.Player.MEDIA_PLAYER_CLASSIC, true, null),
            new PlayerSupport(PlayerSupport.Player.FOOBAR2000, true, null),
            new PlayerSupport(PlayerSupport.Player.MPLAYER, true, null),
            new PlayerSupport(PlayerSupport.Player.QUICKTIME, true, null),
            new PlayerSupport(PlayerSupport.Player.ITUNES, true, null),
            new PlayerSupport(PlayerSupport.Player.REALPLAYER, false, null),
          },
        "Winamp M3U"),
      new ContentType(new String[]{".m4u"},
        new String[]{"video/x-mpegurl"},
        new PlayerSupport[]
          {
          },
        "M4U Playlist"),
      new ContentType(new String[]{".ram"},
        new String[]{"audio/vnd.rn-realaudio", "audio/x-pn-realaudio"},
        new PlayerSupport[]
          {
            new PlayerSupport(PlayerSupport.Player.MEDIA_PLAYER_CLASSIC, false, null),
            new PlayerSupport(PlayerSupport.Player.REALPLAYER, false, null),
          },
        "Real Audio Metadata (RAM)"),
    };

  @Override
  public String getId()
  {
    return "m3u";
  }

  @Override
  public ContentType[] getContentTypes()
  {
    return FILETYPES.clone();
  }

  @Override
  public SpecificPlaylist toSpecificPlaylist(final Playlist playlist)
  {
    final M3U ret = new M3U();
    ret.setProvider(this);

    super.addToPlaylist(ret.getResources(), playlist.getRootSequence()); // May throw Exception.

    return ret;
  }

  public SpecificPlaylist readFrom(final InputStream inputStream) throws IOException
  {
    return super.readPlaylist(new M3U(), inputStream, M3uTextEncoding);
  }

}
