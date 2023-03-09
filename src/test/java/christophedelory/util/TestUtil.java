package christophedelory.util;
import christophedelory.playlist.Playlist;
import christophedelory.playlist.SpecificPlaylist;
import christophedelory.playlist.SpecificPlaylistFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestUtil
{
    public static Path getSampleFolderPath()
    {
        String currentDir = System.getProperty("user.dir");
        return Paths.get(currentDir, "test", "samples");
    }

    public static List<Path> getSamplePaths() throws IOException
    {
        Set<String> skipSamples = new HashSet<>(Arrays.asList(
            "test02.smil",
            "test03.smil",
            "test08.smil"
        ));
        try (Stream<Path> files = Files.list(getSampleFolderPath()))
        {
            return files
                .filter(file -> !skipSamples.contains(file.getFileName().toString()))
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
        Path playlistPath = getSampleFolderPath().resolve(filename);
        final SpecificPlaylist inputSpecificPlaylist = SpecificPlaylistFactory.getInstance().readFrom(playlistPath.toFile());
        assertNotNull(inputSpecificPlaylist, "inputSpecificPlaylist");
        return inputSpecificPlaylist.toPlaylist();
    }
}
