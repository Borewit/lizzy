package christophedelory.playlist.b4s;

import christophedelory.playlist.Playlist;
import christophedelory.util.TestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static christophedelory.util.TestUtil.checkPlaylistItemSource;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Winamp .b4s Playlist Tests")
public class B4sPlaylistTests
{
    @Test
    @DisplayName("Read b4s playlist file")
    public void readB4s() throws Exception
    {
        Playlist playlist = TestUtil.readPlaylistFrom("b4s/test01.b4s");
        assertNotNull(playlist, "playlist");
        assertEquals(2, playlist.getRootSequence().getComponents().size());
        checkPlaylistItemSource(playlist, 0, "file:E:\\fresh dls\\Modern Rock - March 2003\\(modern rock march 2003)-08-system of a down-i-e-a-i-a-i-o.mp3");
        checkPlaylistItemSource(playlist, 1, "file:\\CARDS\\Albums\\normal\\Led Zeppelin - Houses Of The Holy\\Led Zeppelin - Houses Of The Holy - 03 - Over The Hills And Far Away.mp3");
    }
}
