package io.github.borewit.lizzy.playlist.plist;

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

@DisplayName("Property list (p-list) Tests")
public class PlistPlaylistTests {

  public static final String test01_item0_url = "file://localhost/Users/niel/Music/iTunes/iTunes%20Music/Count%20Basie%20&%20His%20Orchestra/Prime%20Time/03%20Sweet%20Georgia%20Brown.m4p";

  @Test
  @DisplayName("Read from p-list playlist file")
  public void readPlist() throws IOException {
    Playlist playlist = TestUtil.readPlaylistFrom("plist/test01.plist");
    assertNotNull(playlist, "playlist");
    assertEquals(1, playlist.getRootSequence().getComponents().size());
    TestUtil.checkPlaylistItemSource(playlist, 0, test01_item0_url);
  }

  @Test
  @DisplayName("Read iTunesMusicLibrary.xml")
  public void readITunesMusicLibrary() throws IOException {
    Playlist playlist = TestUtil.readPlaylistFrom("plist/iTunesMusicLibrary.xml");
    assertNotNull(playlist, "playlist");
    assertEquals(4, playlist.getRootSequence().getComponents().size());
    TestUtil.checkPlaylistItemSource(playlist, 1, "file://localhost/Users/z/Music/iTunes/iTunes%20Media/TV%20Shows/Onion%20News%20Network/Season%201/101%20Interview%20With%20the%20Onion%20News%20Network%20(HD).m4v");
  }

  @Test
  @DisplayName("Write to p-list playlist file")
  public void writePlist() throws IOException {
    Playlist playlist = TestUtil.readPlaylistFrom("plist/test01.plist");
    PlistProvider plistProvider = new PlistProvider();
    assertEquals(1, playlist.getRootSequence().getComponents().size());
    TestUtil.checkPlaylistItemSource(playlist, 0, test01_item0_url);
    byte[] plistData;
    try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
      SpecificPlaylist specificPlaylist = plistProvider.toSpecificPlaylist(playlist);
      specificPlaylist.writeTo(byteArrayOutputStream);
      plistData = byteArrayOutputStream.toByteArray();
    }
    System.out.println(new String(plistData));
    try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(plistData)) {
      Playlist writtenPlaylist = plistProvider.readFrom(byteArrayInputStream).toPlaylist();
      assertEquals(1, writtenPlaylist.getRootSequence().getComponents().size());
      TestUtil.checkPlaylistItemSource(writtenPlaylist, 0, test01_item0_url);
    }
  }

}
