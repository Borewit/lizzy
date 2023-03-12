package christophedelory.playlist.b4s;

import christophedelory.playlist.Playlist;
import christophedelory.util.TestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Winamp .b4s Playlist Tests")
public class B4sPlaylistTests
{
    @Test
    @DisplayName("Read b4s playlist file")
    public void readB4s() throws Exception
    {
        Playlist playlist = TestUtil.readPlaylistFrom("test01.b4s");
        assertNotNull(playlist, "playlist");
        assertEquals(2, playlist.getRootSequence().getComponents().length);
    }
}
