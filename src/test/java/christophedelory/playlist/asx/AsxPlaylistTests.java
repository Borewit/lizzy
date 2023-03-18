package christophedelory.playlist.asx;

import christophedelory.playlist.Playlist;
import christophedelory.playlist.SpecificPlaylist;
import christophedelory.util.TestUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static christophedelory.util.TestUtil.checkPlaylistItemSource;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Advanced Stream Redirector (ASX) Playlist Tests")
public class AsxPlaylistTests
{

    @Test
    @DisplayName("Read ASX playlist file: Microsoft example")
    public void readReferenceAsx() throws IOException
    {
        Playlist playlist = TestUtil.readPlaylistFrom("asx/test02.asx");
        assertNotNull(playlist, "playlist");
        assertEquals(1, playlist.getRootSequence().getComponents().size());
        checkPlaylistItemSource(playlist, 0, "mms://windowsmediaserver/path/yourfile.asf");
    }

    @Test
    @DisplayName("Be able to read ASX playlist case insensitive")
    public void readLowerCaseAsx() throws IOException
    {
        Playlist playlist = TestUtil.readPlaylistFrom("asx/test01.asx");
        assertNotNull(playlist, "playlist");
        assertEquals(1, playlist.getRootSequence().getComponents().size());
        checkPlaylistItemSource(playlist, 0, "http://www.johnsmith.com/media/Raging_Tango.mp3");
    }

    @Test
    @DisplayName("Read ASX ANSI")
    @Disabled
    public void readLiveStreamAsx() throws IOException
    {
        Playlist playlist = TestUtil.readPlaylistFrom("asx/test03.asx");
        assertNotNull(playlist, "playlist");
        assertEquals(2, playlist.getRootSequence().getComponents().size());
        checkPlaylistItemSource(playlist, 0, "http://example.com/announcement.wma");
        checkPlaylistItemSource(playlist, 1, "http://example.com:8080");
    }

    @Test
    @DisplayName("Read ASX with ENTRYREF element")
    public void readAsxWithEntryrefElement() throws IOException
    {
        Playlist playlist = TestUtil.readPlaylistFrom("asx/test05.asx");
        assertNotNull(playlist, "playlist");
        assertEquals(1, playlist.getRootSequence().getComponents().size());
        checkPlaylistItemSource(playlist, 0, "http://sample.microsoft.com/metafile.asx");
    }

    @Test
    @DisplayName("Write to ASX playlist file")
    public void writeAsx() throws IOException
    {
        writeAsx("asx/test01.asx");
    }

    @Test
    @DisplayName("Handle a non ASX XML playlist")
    public void parseNonAsxXmlPlaylist() throws IOException
    {
        Path playlistPath = TestUtil.sampleFolderPath.resolve("xspf/test02.xspf");

        try (InputStream in = Files.newInputStream(playlistPath))
        {
            AsxProvider asxProvider = new AsxProvider();
            try
            {
                SpecificPlaylist specificPlaylist = asxProvider.readFrom(in, null);
                assertNull(specificPlaylist, "ASX provider should return null reading a different XML Type");
            }
            catch (Exception exception)
            {
                // May reject playlist by throwing an exception
            }
        }
    }

    private static void writeAsx(String testFile) throws IOException
    {
        Playlist playlist = TestUtil.readPlaylistFrom(testFile);
        AsxProvider asxProvider = new AsxProvider();
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream())
        {
            SpecificPlaylist specificPlaylist = asxProvider.toSpecificPlaylist(playlist);
            specificPlaylist.writeTo(byteArrayOutputStream, null);
        }
    }
}
