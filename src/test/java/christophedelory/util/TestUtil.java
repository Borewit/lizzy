package christophedelory.util;
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
}
