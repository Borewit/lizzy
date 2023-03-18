package christophedelory.playlist.plist;

import christophedelory.playlist.Playlist;
import christophedelory.playlist.SpecificPlaylist;
import christophedelory.util.TestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;

import static christophedelory.util.TestUtil.checkPlaylistItemSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Property list (p-list) Tests")
public class PlistPlaylistTests
{

    @Test
    @DisplayName("Read from p-list playlist file")
    public void readPlist() throws Exception
    {
        Playlist playlist = TestUtil.readPlaylistFrom("plist/test01.plist");
        assertNotNull(playlist, "playlist");
        assertEquals(1, playlist.getRootSequence().getComponents().size());
        checkPlaylistItemSource(playlist, 0, "file://localhost/Users/niel/Music/iTunes/iTunes%20Music/Count%20Basie%20&%20His%20Orchestra/Prime%20Time/03%20Sweet%20Georgia%20Brown.m4p");
    }


    @Test
    @DisplayName("Write to p-list playlist file")
    public void writePlist() throws Exception
    {
        Playlist playlist = TestUtil.readPlaylistFrom("plist/test01.plist");
        PlistProvider plistProvider = new PlistProvider();
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream())
        {
            SpecificPlaylist specificPlaylist = plistProvider.toSpecificPlaylist(playlist);
            specificPlaylist.writeTo(byteArrayOutputStream, null);
        }
    }


}
