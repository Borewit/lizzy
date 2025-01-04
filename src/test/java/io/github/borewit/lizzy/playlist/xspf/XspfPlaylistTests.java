package io.github.borewit.lizzy.playlist.xspf;

import io.github.borewit.lizzy.playlist.Playlist;
import io.github.borewit.lizzy.playlist.SpecificPlaylist;
import io.github.borewit.lizzy.util.TestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("XSPF Playlist Tests")
public class XspfPlaylistTests {

  @Test
  @DisplayName("Read from XSPF Playlist")
  public void readPlist() throws IOException {

    Playlist playlist = TestUtil.readPlaylistFrom("xspf/test02.xspf");
    assertNotNull(playlist, "playlist");
    assertEquals(3, playlist.getRootSequence().getComponents().size());
    TestUtil.checkPlaylistItemSource(playlist, 0, "http://example.net/song_1.ogg");
    TestUtil.checkPlaylistItemSource(playlist, 1, "http://example.net/song_2.flac");
    TestUtil.checkPlaylistItemSource(playlist, 2, "http://example.com/song_3.mp3");
  }

  @Test
  @DisplayName("Write to XSPF Playlist")
  public void writePlist() throws IOException {
    Playlist playlist = TestUtil.makeAbstractPlaylist();
    XspfProvider plistProvider = new XspfProvider();
    try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
      SpecificPlaylist specificPlaylist = plistProvider.toSpecificPlaylist(playlist);
      specificPlaylist.writeTo(byteArrayOutputStream);
    }
  }
}
