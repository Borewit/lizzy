package io.github.borewit.lizzy.playlist.jspf;

import io.github.borewit.lizzy.playlist.Playlist;
import io.github.borewit.lizzy.playlist.SpecificPlaylist;
import io.github.borewit.lizzy.util.TestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("JSPF Playlist Tests")
public class JspfPlaylistTests {

  @Test
  @DisplayName("Read JSPF playlist file")
  public void readReferenceJspf() throws IOException {
    Playlist playlist = TestUtil.readPlaylistFrom("jspf/playlist.jspf");
    assertNotNull(playlist, "playlist");
    assertEquals(2, playlist.getRootSequence().getComponents().size());
    TestUtil.checkPlaylistItemSource(playlist, 0, "http://example.com/track1");
    TestUtil.checkPlaylistItemSource(playlist, 1, "http://example.com/track2");
  }

  @Test
  @DisplayName("Write JSPF playlist file")
  public void writeJSPF() throws IOException {
    writeJSPF("jspf/playlist.jspf");
  }

  private static void writeJSPF(String testFile) throws IOException {
    Playlist playlist = TestUtil.readPlaylistFrom(testFile);
    JspfProvider jspfProvider = new JspfProvider();
    byte[] serializedData;
    try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
      SpecificPlaylist specificPlaylist = jspfProvider.toSpecificPlaylist(playlist);
      specificPlaylist.writeTo(byteArrayOutputStream);
      serializedData = byteArrayOutputStream.toByteArray();
    }
    Playlist checkWrittenPlaylist;
    try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedData)) {
      SpecificPlaylist m3uPlaylist = jspfProvider.readFrom(byteArrayInputStream);
      assertNotNull(m3uPlaylist, "Read written JSPF playlist");
      checkWrittenPlaylist = m3uPlaylist.toPlaylist();
    }
    assertEquals(2, checkWrittenPlaylist.getRootSequence().getComponents().size());
    TestUtil.checkPlaylistItemSource(checkWrittenPlaylist, 0, "http://example.com/track1");
  }
}
