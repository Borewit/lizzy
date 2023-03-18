package christophedelory.util;

import christophedelory.playlist.*;
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
        "test03.asx",
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
        List<SpecificPlaylistProvider> playlistProviders = SpecificPlaylistFactory.getInstance().findProvidersByExtension(absPlaylistPath.toString());
        assertFalse(playlistProviders.isEmpty(), String.format("Expect to find a provider extension of path %s", absPlaylistPath));
        for (SpecificPlaylistProvider playlistProvider : playlistProviders)
        {
            try (InputStream is = Files.newInputStream(absPlaylistPath))
            {
                try
                {
                    SpecificPlaylist specificPlaylist = playlistProvider.readFrom(is, null);
                    if (specificPlaylist != null)
                        return specificPlaylist.toPlaylist();
                }
                catch (Exception e)
                {
                    throw new Exception(String.format("Failed to read from %s", absPlaylistPath.getFileName()), e);
                }
            }
        }
        fail(String.format("Failed to find provider which is able to decode playlist path \"%s\"", playlistPath));
        return null;
    }

    public static Map<String, JsonPlaylist> getPlaylistMetadata() throws IOException
    {
        TypeReference<TreeMap<String, JsonPlaylist>> typeRef = new TypeReference<TreeMap<String, JsonPlaylist>>()
        {
        };
        return new ObjectMapper().readValue(jsonTestDataPath.toFile(), typeRef);
    }

    public static void checkPlaylistItemSource(final Playlist playlist, final int itemIndex, final String expectedSource)
    {
        Object entry = playlist.getRootSequence().getComponents().get(itemIndex);
        assertNotNull(entry, "Playlist entry");
        assertTrue(entry instanceof Media, String.format("Expect entry to be Media instance, got class %s", entry.getClass()));
        Media media = (Media) entry;
        assertNotNull(media.getSource(), "Media source");
        assertEquals(expectedSource, media.getSource().toString(), "Media source URL");
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
