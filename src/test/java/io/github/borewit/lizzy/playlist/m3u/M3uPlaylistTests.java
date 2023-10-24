package io.github.borewit.lizzy.playlist.m3u;

import io.github.borewit.lizzy.playlist.Playlist;
import io.github.borewit.lizzy.playlist.SpecificPlaylist;
import io.github.borewit.lizzy.util.TestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("M3U Playlist Tests")
public class M3uPlaylistTests
{
  public static final String m3uSpecialCharPath = "C:\\Music\\ÀÁÂÃäA Ç ÈÉÊË ÌÍÎÏ N ÒÓÔÕö  ÙÚÛü àáâãäa ç èéêë ìíîï n òóôõöo ùúûü.mp3";

  @Test
  @DisplayName("Read M3U playlist file")
  public void readReferenceM3u() throws IOException
  {
    Playlist playlist = TestUtil.readPlaylistFrom("m3u/test01.m3u");
    assertNotNull(playlist, "playlist");
    assertEquals(3, playlist.getRootSequence().getComponents().size());
    TestUtil.checkPlaylistItemSource(playlist, 0, "c:/music/foo.mp3");
    TestUtil.checkPlaylistItemSource(playlist, 1, "foo/fighters.mp3");
    TestUtil.checkPlaylistItemSource(playlist, 2, "http://foofighters.com/somesong.mp3");
  }

  @Test
  @DisplayName("Read M3U (ISO-8859-1 encoded) playlist with special character")
  public void readM3uWithSpecialCharacters() throws IOException
  {
    Playlist playlist = TestUtil.readPlaylistFrom("m3u/playlist-iso-8859-1.m3u");
    assertNotNull(playlist, "playlist");
    assertEquals(2, playlist.getRootSequence().getComponents().size());
    TestUtil.checkPlaylistItemSource(playlist, 0, "C:\\Music\\SampleMusic.mp3");
    TestUtil.checkPlaylistItemSource(playlist, 1, m3uSpecialCharPath);
  }

  @Test
  @DisplayName("Read M3U8 playlist with BOM")
  public void readReferenceM3u8WithBom() throws IOException
  {
    assertTrue(TestUtil.hasBom(TestUtil.sampleFolderPath.resolve("m3u/playlist-utf8-bom.m3u")), "M3U8 is prefixed with a BOM");

    Playlist playlist = TestUtil.readPlaylistFrom("m3u/playlist-utf8-bom.m3u");
    assertNotNull(playlist, "playlist");
    assertEquals(2, playlist.getRootSequence().getComponents().size());
    TestUtil.checkPlaylistItemSource(playlist, 0, "C:\\Music\\SampleMusic.mp3");
    TestUtil.checkPlaylistItemSource(playlist, 1, m3uSpecialCharPath);
  }

  @Test
  @DisplayName("Write as M3U playlist file")
  public void writeM3u() throws IOException
  {
    writeM3u("asx/test01.asx");
  }

  private static void writeM3u(String testFile) throws IOException
  {
    Playlist playlist = TestUtil.readPlaylistFrom(testFile);
    M3UProvider m3uProvider = new M3UProvider();
    byte[] serializedData;
    try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream())
    {
      SpecificPlaylist specificPlaylist = m3uProvider.toSpecificPlaylist(playlist);
      specificPlaylist.writeTo(byteArrayOutputStream);
      serializedData = byteArrayOutputStream.toByteArray();
    }
    Playlist checkWrittenPlaylist;
    try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedData))
    {
      SpecificPlaylist m3uPlaylist = m3uProvider.readFrom(byteArrayInputStream);
      assertNotNull(m3uPlaylist, "Read written M3U playlist");
      checkWrittenPlaylist = m3uPlaylist.toPlaylist();
    }
    assertEquals(1, checkWrittenPlaylist.getRootSequence().getComponents().size());
    TestUtil.checkPlaylistItemSource(checkWrittenPlaylist, 0, "http://www.johnsmith.com/media/Raging_Tango.mp3");
  }

  @Test
  @DisplayName("Read MP3U8 Playlist UTF-8 no BOM")
  public void readPlaylistM3uUtf8NoBom() throws IOException
  {
    Playlist m3uPlaylist = TestUtil.readPlaylistFrom("m3u/playlist-utf8.m3u8");
    assertNotNull(m3uPlaylist, "PlaylistFactory should read and construct M3U playlist");
    assertEquals(2, m3uPlaylist.getRootSequence().getComponents().size(), "M3U playlist contains 2 tracks");
    TestUtil.checkPlaylistItemSource(m3uPlaylist, 0, "C:\\Music\\SampleMusic.mp3");
    TestUtil.checkPlaylistItemSource(m3uPlaylist, 1, m3uSpecialCharPath);
  }

  @Test
  @DisplayName("Read MP3U Playlist UTF-16-BE with BOM")
  public void readPlaylistM3uUtf16BeWithBom() throws IOException
  {
    Playlist m3uPlaylist = TestUtil.readPlaylistFrom("m3u/playlist-utf16be-bom.m3u");
    assertNotNull(m3uPlaylist, "PlaylistFactory should read and construct M3U playlist");
    assertEquals(2, m3uPlaylist.getRootSequence().getComponents().size(), "M3U playlist contains 2 tracks");
    TestUtil.checkPlaylistItemSource(m3uPlaylist, 0, "C:\\Music\\SampleMusic.mp3");
    TestUtil.checkPlaylistItemSource(m3uPlaylist, 1, m3uSpecialCharPath);
  }

  @Test
  @DisplayName("Read MP3U Playlist UTF-16-LE with BOM")
  public void readPlaylistM3uUtf16LeWithBom() throws IOException
  {
    Playlist m3uPlaylist = TestUtil.readPlaylistFrom("m3u/playlist-utf16le-bom.m3u");
    assertNotNull(m3uPlaylist, "PlaylistFactory should read and construct M3U playlist");
    assertEquals(2, m3uPlaylist.getRootSequence().getComponents().size(), "M3U playlist contains 2 tracks");
  }
}
