package christophedelory.playlist.plist;

import christophedelory.playlist.Playlist;
import christophedelory.playlist.SpecificPlaylist;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static christophedelory.util.TestUtil.getSampleFolderPath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PlistPlaylistTests
{

    @Test
    @DisplayName("Read Property list (p-list, .plist)")
    public void readPlist() throws Exception
    {

        Playlist playlist = this.readPlaylistFrom("test01.plist");
        assertNotNull(playlist, "playlist");
        assertEquals(1, playlist.getRootSequence().getComponents().length);
    }

    private Playlist readPlaylistFrom(String filename) throws Exception
    {

        Path plistPath = getSampleFolderPath().resolve(filename);
        PlistProvider plistProvider = new PlistProvider();
        SpecificPlaylist specificPlaylist;
        InputStream inputStream = Files.newInputStream(plistPath);
        try
        {
            specificPlaylist = plistProvider.readFrom(inputStream, filename);
        }
        finally
        {
            inputStream.close();
        }
        assertNotNull(specificPlaylist, "specificPlaylist");
        return specificPlaylist.toPlaylist();
    }

    @Test
    @DisplayName("Write Property list (p-list, .plist)")
    public void writePlist() throws Exception
    {
        Playlist playlist = this.readPlaylistFrom("test01.plist");
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
