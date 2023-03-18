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

import static org.junit.jupiter.api.Assertions.*;

public class TestUtil
{
    public static final Path sampleFolderPath = Paths.get(System.getProperty("user.dir"), "samples");
    public static final Path jsonTestDataPath = sampleFolderPath.resolve("playlists.json");

    private static final Set<String> skipSamples = new HashSet<>(Arrays.asList(
        "test08.smil"
    ));

    public static List<Path> getSamplePaths() throws IOException
    {
        return Files.walk(sampleFolderPath)
            .filter(file -> Files.isRegularFile(file)
                && !file.getFileName().toString().startsWith(".")
                && !skipSamples.contains(file.getFileName().toString())
                && !file.equals(jsonTestDataPath)
            )
            .collect(Collectors.toList());
    }

    public static Playlist makeAbstractPlaylist()
    {
        try
        {
            return readPlaylistFrom("m3u/test01.m3u");
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
        Path absPlaylistPath = playlistPath.isAbsolute() ? playlistPath : sampleFolderPath.resolve(playlistPath);
        SpecificPlaylistProvider specificPlaylistProvider = SpecificPlaylistFactory.getInstance().findProviderByExtension(absPlaylistPath.toString());
        assertNotNull(specificPlaylistProvider, String.format("Provider by extension %s", absPlaylistPath));
        try (InputStream is = Files.newInputStream(absPlaylistPath))
        {
            try
            {
                return specificPlaylistProvider.readFrom(is, null).toPlaylist();
            }
            catch (Exception e)
            {
                throw new Exception(String.format("Failed to read from %s", absPlaylistPath.getFileName()), e);
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
