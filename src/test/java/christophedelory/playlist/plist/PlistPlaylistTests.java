package christophedelory.playlist.plist;

import christophedelory.playlist.Playlist;
import christophedelory.playlist.SpecificPlaylist;
import christophedelory.util.TestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Property list (p-list) Tests")
public class PlistPlaylistTests
{

    @Test
    @DisplayName("Read from p-list playlist file")
    public void readPlist() throws Exception
    {
        Playlist playlist = TestUtil.readPlaylistFrom("test01.plist");
        assertNotNull(playlist, "playlist");
        assertEquals(1, playlist.getRootSequence().getComponents().length);
    }


    @Test
    @DisplayName("Write to p-list playlist file")
    public void writePlist() throws Exception
    {
        Playlist playlist = TestUtil.readPlaylistFrom("test01.plist");
        PlistProvider plistProvider = new PlistProvider();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try
        {
            SpecificPlaylist specificPlaylist = plistProvider.toSpecificPlaylist(playlist);
            specificPlaylist.writeTo(byteArrayOutputStream, null);
        }
        finally
        {
            byteArrayOutputStream.close();
        }
    }


}
