package christophedelory.playlist.smil;

import christophedelory.playlist.Playlist;

import christophedelory.playlist.SpecificPlaylist;

import christophedelory.playlist.smil20.SmilProvider;
import christophedelory.util.TestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static christophedelory.util.TestUtil.checkPlaylistItemSource;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SMIL Playlist Tests")
public class SmilPlaylistTests
{

    @Test
    @DisplayName("Read SMIL playlist file")
    public void readSmilPlaylist() throws IOException
    {
        Playlist playlist = TestUtil.readPlaylistFrom("smil/test01.smil");
        assertNotNull(playlist, "playlist");
        assertEquals(2, playlist.getRootSequence().getComponents().size());
        checkPlaylistItemSource(playlist, 0, "http://example.com/foo.mp3");
        checkPlaylistItemSource(playlist, 1, "http://example.com/bar.mp3");
    }

    @Test
    @DisplayName("Read SMIL playlist with \"indefinite\" repeat count")
    public void readSmilPlaylistWithIndefiniteRepeatValue() throws IOException
    {
        Playlist playlist = TestUtil.readPlaylistFrom("smil/test02.smil");
        assertNotNull(playlist, "playlist");
        assertEquals(7, playlist.getRootSequence().getComponents().size());
        checkPlaylistItemSource(playlist, 0, "image1.jpg");
        checkPlaylistItemSource(playlist, 1, "image2.jpg");
        checkPlaylistItemSource(playlist, 2, "http://example.com/foo.mp3");
        checkPlaylistItemSource(playlist, 3, "http://example.com/bar.mp3");
        checkPlaylistItemSource(playlist, 4, "liar.wav");
    }

    @Test
    @DisplayName("Read SMIL video playlist file")
    public void readVideoSmilPlaylist() throws IOException
    {
        Playlist playlist = TestUtil.readPlaylistFrom("smil/test06.smil");
        assertNotNull(playlist, "playlist");
        assertEquals(1, playlist.getRootSequence().getComponents().size());
        checkPlaylistItemSource(playlist, 0, "myvideo.flv");
    }

    @Test
    @DisplayName("Read SMIL 2.0 playlist qualified XML")
    public void readQualifiedSmil20Playlist() throws IOException
    {
        Playlist playlist = TestUtil.readPlaylistFrom("smil/smil-2.0-example.smil");
        assertNotNull(playlist, "playlist");
        assertEquals(4, playlist.getRootSequence().getComponents().size());
        checkPlaylistItemSource(playlist, 0, "http://storage/ads/origin/origin08_x264.mp4");
        checkPlaylistItemSource(playlist, 1, "http://storage/main/sintel/sintel_dref.mp4");
        checkPlaylistItemSource(playlist, 2, "http://storage/ads/capture/capture10_x264.mp4");
        checkPlaylistItemSource(playlist, 3, "http://storage/main/sintel/sintel_dref.mp4");
    }

    @Test
    @DisplayName("Write to SMIL playlist file")
    public void writeSmil() throws IOException
    {
        writeSmil("asx/test01.asx");
    }

    private static void writeSmil(String testFile) throws IOException
    {
        Playlist playlist = TestUtil.readPlaylistFrom(testFile);
        final SmilProvider smilProvider = new SmilProvider();
        final byte[] smilData;
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream())
        {
            SpecificPlaylist specificPlaylist = smilProvider.toSpecificPlaylist(playlist);
            specificPlaylist.writeTo(byteArrayOutputStream, null);
            smilData = byteArrayOutputStream.toByteArray();
        }
        try(ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(smilData)) {
            Playlist writtenPlaylist = smilProvider.readFrom(byteArrayInputStream).toPlaylist();
            assertEquals(1, writtenPlaylist.getRootSequence().getComponents().size());
            checkPlaylistItemSource(writtenPlaylist, 0, "http://www.johnsmith.com/media/Raging_Tango.mp3");
        }
    }
}
