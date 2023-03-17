package christophedelory.util;

import christophedelory.playlist.Media;
import christophedelory.playlist.Playlist;
import christophedelory.playlist.SpecificPlaylistFactory;
import christophedelory.playlist.SpecificPlaylistProvider;
import christophedelory.test.json.playlist.JsonPlaylist;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.input.BOMInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class TestUtil
{
    public static final Path testFolderPath = Paths.get(System.getProperty("user.dir"), "test");
    public static final Path sampleFolderPath = testFolderPath.resolve("samples");
    public static final Path jsonTestDataPath = testFolderPath.resolve("playlists.json");

    public static List<Path> getSamplePaths() throws IOException
    {
        Set<String> skipSamples = new HashSet<>(Arrays.asList(
            "test02.smil",
            "test03.smil",
            "test08.smil",
            "repeat.asx"
        ));
        try (Stream<Path> files = Files.list(sampleFolderPath))
        {
            return files
                .filter(file -> Files.isRegularFile(file) && !skipSamples.contains(file.getFileName().toString()))
                .collect(Collectors.toList());
        }
    }

    public static Playlist makeAbstractPlaylist()
    {
        try
        {
            return readPlaylistFrom("test01.m3u");
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public static Playlist readPlaylistFrom(String filename) throws Exception
    {
        Path playlistPath = sampleFolderPath.resolve(filename);
        return readPlaylistFrom(playlistPath);
    }

    public static Playlist readPlaylistFrom(Path playlistPath) throws Exception
    {
        SpecificPlaylistProvider specificPlaylistProvider = SpecificPlaylistFactory.getInstance().findProviderByExtension(playlistPath.toString());
        assertNotNull(specificPlaylistProvider, String.format("Provider by extension %s", playlistPath));
        try (InputStream is = Files.newInputStream(playlistPath))
        {
            try
            {
                return specificPlaylistProvider.readFrom(is, null).toPlaylist();
            }
            catch (Exception e)
            {
                throw new Exception(String.format("Failed to read from %s", playlistPath.getFileName()), e);
            }
        }
    }

    public static Map<String, JsonPlaylist> getPlaylistMetadata() throws IOException
    {
        TypeReference<TreeMap<String, JsonPlaylist>> typeRef = new TypeReference<TreeMap<String, JsonPlaylist>>()
        {
        };
        return new ObjectMapper().readValue(jsonTestDataPath.toFile(), typeRef);
    }

    public static void checkPlaylistItemSource(final Playlist playlist, final int itemIndex, final String expectedUri)
    {
        Object entry = playlist.getRootSequence().getComponents().get(itemIndex);
        assertTrue(entry instanceof Media, "Expect playlist media entry");
        Media media = (Media) entry;
        assertNotNull(media.getSource(), "Media source");
        assertEquals(expectedUri, media.getSource().toString(), "Media source URL");
    }

    public static boolean hasBom(Path path) throws IOException
    {
        try (InputStream fis = Files.newInputStream(path))
        {
            try (BOMInputStream bomIn = new BOMInputStream(fis))
            {
                // has a UTF-8 BOM
                return bomIn.hasBOM();
            }
        }
    }

}
