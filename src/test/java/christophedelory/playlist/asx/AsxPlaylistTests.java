package christophedelory.playlist.asx;

import christophedelory.playlist.Media;
import christophedelory.playlist.Playlist;
import christophedelory.playlist.SpecificPlaylist;
import christophedelory.util.TestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.file.Path;

import static christophedelory.util.TestUtil.checkPlaylistItemSource;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Advanced Stream Redirector (ASX) Playlist Tests")
public class AsxPlaylistTests
{

    @Test
    @DisplayName("Read ASX playlist file: Microsoft example")
    public void readReferenceAsx() throws Exception
    {
        Playlist playlist = TestUtil.readPlaylistFrom("test02.asx");
        assertNotNull(playlist, "playlist");
        assertEquals(1, playlist.getRootSequence().getComponents().length);
        Object entry = playlist.getRootSequence().getComponents()[0];
        assertTrue(entry instanceof Media, "Expect playlist media entry");
        Media media = (Media) entry;
        assertNotNull(media.getSource(), "Media source");
        assertNotNull(media.getSource().getURL(), "Media source URL");
        assertEquals("mms://windowsmediaserver/path/yourfile.asf", media.getSource().toString(), "Media source URL");
    }

    @Test
    @DisplayName("Be able to read ASX playlist case insensitive")
    public void readLowerCaseAsx() throws Exception
    {
        Playlist playlist = TestUtil.readPlaylistFrom("test01.asx");
        assertNotNull(playlist, "playlist");
        assertEquals(1, playlist.getRootSequence().getComponents().length);
        checkPlaylistItemSource(playlist, 0, "http://www.johnsmith.com/media/Raging_Tango.mp3");
    }

    @Test
    @DisplayName("Read ASX ANSI")
    public void readLiveStreamASx() throws Exception
    {
        Playlist playlist = TestUtil.readPlaylistFrom("test03.asx");
        assertNotNull(playlist, "playlist");
        assertEquals(2, playlist.getRootSequence().getComponents().length);
        checkPlaylistItemSource(playlist, 0, "http://example.com/announcement.wma");
        checkPlaylistItemSource(playlist, 1, "http://example.com:8080");
    }

    @Test
    @DisplayName("Read ASX with ENTRYREF element")
    public void readAsxWithEntryrefElement() throws Exception
    {
        Playlist playlist = TestUtil.readPlaylistFrom("test05.asx");
        assertNotNull(playlist, "playlist");
        assertEquals(1, playlist.getRootSequence().getComponents().length);
        checkPlaylistItemSource(playlist, 0, "http://sample.microsoft.com/metafile.asx");
    }

    @Test
    @DisplayName("Write to ASX playlist file")
    public void writeAsx() throws Exception
    {
        writeAsx("test01.asx");
    }

    @Test
    @DisplayName("Handle a non ASX XML playlist")
    public void parseNonAsxXmlPlaylist() throws Exception
    {
        Path playlistPath = TestUtil.sampleFolderPath.resolve("test02.xspf");

        try (InputStream in = playlistPath.toFile().toURL().openConnection().getInputStream())
        {
            AsxProvider asxProvider = new AsxProvider();
            try
            {
                SpecificPlaylist specificPlaylist = asxProvider.readFrom(in, null);
                assertNull(specificPlaylist, "ASX provider should return null reading a different XML Type");
            }
            catch (Exception exception) {
                // May reject playlist by throwing an exception
            }
        }
    }

    private static void writeAsx(String testFile) throws Exception
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
