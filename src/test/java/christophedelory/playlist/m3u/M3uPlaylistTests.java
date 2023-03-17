package christophedelory.playlist.m3u;

import christophedelory.playlist.Playlist;
import christophedelory.playlist.SpecificPlaylist;
import christophedelory.util.TestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;


import static christophedelory.util.TestUtil.checkPlaylistItemSource;
import static christophedelory.util.TestUtil.sampleFolderPath;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("M3U Playlist Tests")
public class M3uPlaylistTests
{
    @Test
    @DisplayName("Read M3U playlist file")
    public void readReferenceM3u() throws Exception
    {
        Playlist playlist = TestUtil.readPlaylistFrom("test01.m3u");
        assertNotNull(playlist, "playlist");
        assertEquals(3, playlist.getRootSequence().getComponents().size());
        checkPlaylistItemSource(playlist, 0, "c:/music/foo.mp3");
        checkPlaylistItemSource(playlist, 1, "foo/fighters.mp3");
        checkPlaylistItemSource(playlist, 2, "http://foofighters.com/somesong.mp3");
    }

    @Test
    @DisplayName("Read M3U8 playlist with BOM")
    public void readReferenceM3u8WithBom() throws Exception
    {
        assertTrue(TestUtil.hasBom(sampleFolderPath.resolve("m3u/playlist-utf8-bom.m3u")), "M3U8 is prefixed with a BOM");

        Playlist playlist = TestUtil.readPlaylistFrom("m3u/playlist-utf8-bom.m3u");
        assertNotNull(playlist, "playlist");
        assertEquals(2, playlist.getRootSequence().getComponents().size());
        checkPlaylistItemSource(playlist, 0, "C:\\Music\\SampleMusic.mp3");
        checkPlaylistItemSource(playlist, 1, "C:\\Music\\ExampleMusic.mp3");
    }

    private static void writeM3u(String testFile) throws Exception
    {
        Playlist playlist = TestUtil.readPlaylistFrom(testFile);
        M3UProvider m3uProvider = new M3UProvider();
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream())
        {
            SpecificPlaylist specificPlaylist = m3uProvider.toSpecificPlaylist(playlist);
            specificPlaylist.writeTo(byteArrayOutputStream, null);
        }
    }
}
