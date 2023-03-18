package io.github.borewit.lizzy.playlist.b4s;

import io.github.borewit.lizzy.playlist.Playlist;
import io.github.borewit.lizzy.util.TestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Winamp .b4s Playlist Tests")
public class B4sPlaylistTests
{
  @Test
  @DisplayName("Read b4s playlist file")
  public void readB4s() throws IOException
  {
    Playlist playlist = TestUtil.readPlaylistFrom("b4s/test01.b4s");
    assertNotNull(playlist, "playlist");
    assertEquals(2, playlist.getRootSequence().getComponents().size());
    TestUtil.checkPlaylistItemSource(playlist, 0, "file:E:\\fresh dls\\Modern Rock - March 2003\\(modern rock march 2003)-08-system of a down-i-e-a-i-a-i-o.mp3");
    TestUtil.checkPlaylistItemSource(playlist, 1, "file:\\CARDS\\Albums\\normal\\Led Zeppelin - Houses Of The Holy\\Led Zeppelin - Houses Of The Holy - 03 - Over The Hills And Far Away.mp3");
  }
}
