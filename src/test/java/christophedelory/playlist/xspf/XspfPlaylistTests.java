package christophedelory.playlist.xspf;

import christophedelory.playlist.Playlist;
    import christophedelory.playlist.SpecificPlaylist;
    import christophedelory.util.TestUtil;
    import org.junit.jupiter.api.DisplayName;
    import org.junit.jupiter.api.Test;

    import java.io.ByteArrayOutputStream;

import static christophedelory.util.TestUtil.checkPlaylistItemSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
    import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("XSPF Playlist Tests")
public class XspfPlaylistTests
{

    @Test
    @DisplayName("Read from XSPF Playlist")
    public void readPlist() throws Exception
    {

        Playlist playlist = TestUtil.readPlaylistFrom("test02.xspf");
        assertNotNull(playlist, "playlist");
        assertEquals(3, playlist.getRootSequence().getComponents().size());
        checkPlaylistItemSource(playlist, 0, "http://example.net/song_1.ogg");
        checkPlaylistItemSource(playlist, 1, "http://example.net/song_2.flac");
        checkPlaylistItemSource(playlist, 2, "http://example.com/song_3.mp3");
    }

    @Test
    @DisplayName("Write to XSPF Playlist")
    public void writePlist() throws Exception
    {
        Playlist playlist = TestUtil.makeAbstractPlaylist();
        XspfProvider plistProvider = new XspfProvider();
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream())
        {
            SpecificPlaylist specificPlaylist = plistProvider.toSpecificPlaylist(playlist);
            specificPlaylist.writeTo(byteArrayOutputStream, null);
        }
    }
}
