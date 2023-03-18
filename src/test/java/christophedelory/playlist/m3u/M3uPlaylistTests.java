package christophedelory.playlist.m3u;

import christophedelory.playlist.Playlist;
import christophedelory.playlist.SpecificPlaylist;
import christophedelory.util.TestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static christophedelory.util.TestUtil.checkPlaylistItemSource;
import static christophedelory.util.TestUtil.sampleFolderPath;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("M3U Playlist Tests")
public class M3uPlaylistTests
{
    @Test
    @DisplayName("Read M3U playlist file")
    public void readReferenceM3u() throws IOException
    {
        Playlist playlist = TestUtil.readPlaylistFrom("m3u/test01.m3u");
        assertNotNull(playlist, "playlist");
        assertEquals(3, playlist.getRootSequence().getComponents().size());
        checkPlaylistItemSource(playlist, 0, "c:/music/foo.mp3");
        checkPlaylistItemSource(playlist, 1, "foo/fighters.mp3");
        checkPlaylistItemSource(playlist, 2, "http://foofighters.com/somesong.mp3");
    }

    @Test
    @DisplayName("Read M3U8 playlist with BOM")
    public void readReferenceM3u8WithBom() throws IOException
    {
        assertTrue(TestUtil.hasBom(sampleFolderPath.resolve("m3u/playlist-utf8-bom.m3u")), "M3U8 is prefixed with a BOM");

        Playlist playlist = TestUtil.readPlaylistFrom("m3u/playlist-utf8-bom.m3u");
        assertNotNull(playlist, "playlist");
        assertEquals(2, playlist.getRootSequence().getComponents().size());
        checkPlaylistItemSource(playlist, 0, "C:\\Music\\SampleMusic.mp3");
        checkPlaylistItemSource(playlist, 1, "C:\\Music\\ExampleMusic.mp3");
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
            specificPlaylist.writeTo(byteArrayOutputStream, null);
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
        checkPlaylistItemSource(checkWrittenPlaylist, 0, "http://www.johnsmith.com/media/Raging_Tango.mp3");
    }

    @Test
    @DisplayName("Read MP3U Playlist UTF-8 no BOM")
    public void readPlaylistM3uUtf8NoBom() throws IOException
    {
        Playlist m3uPlaylist = TestUtil.readPlaylistFrom("m3u/playlist-utf8.m3u");
        assertNotNull(m3uPlaylist, "PlaylistFactory should read and construct M3U playlist");
        assertEquals(2, m3uPlaylist.getRootSequence().getComponents().size(), "M3U playlist contains 2 tracks");
        checkPlaylistItemSource(m3uPlaylist, 0, "C:\\Music\\SampleMusic.mp3");
        checkPlaylistItemSource(m3uPlaylist, 1, "C:\\Music\\ExampleMusic.mp3");
    }

    @Test
    @DisplayName("Read MP3U Playlist UTF-16-BE with BOM")
    public void readPlaylistM3uUtf16BeWithBom() throws IOException
    {
        Playlist m3uPlaylist = TestUtil.readPlaylistFrom("m3u/playlist-utf16be-bom.m3u");
        assertNotNull(m3uPlaylist, "PlaylistFactory should read and construct M3U playlist");
        assertEquals(2, m3uPlaylist.getRootSequence().getComponents().size(), "M3U playlist contains 2 tracks");
        checkPlaylistItemSource(m3uPlaylist, 0, "C:\\Music\\SampleMusic.mp3");
        checkPlaylistItemSource(m3uPlaylist, 1, "C:\\Music\\ExampleMusic.mp3");
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
