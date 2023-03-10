package christophedelory;

import christophedelory.lizzy.Transcode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static christophedelory.util.TestUtil.getSamplePaths;
import static org.junit.jupiter.api.Assertions.fail;

@DisplayName("Command Line Application Tests")
public class CommandLineTranscodeTests
{

    private static Path getSampleFolderPath()
    {
        String currentDir = System.getProperty("user.dir");
        return Paths.get(currentDir, "test", "samples");
    }

    @DisplayName("Transcode playlist via command line")
    @ParameterizedTest
    @ValueSource(strings = {"pla", "asx", "b4s", "wpl", "smil", "rss", "atom", "hypetape", "xspf", "rmp", "plist", "pls", "mpcpl", "plp", "m3u"})
    public void transcodePlaylist(String type) throws IOException
    {
        List<Path> samples = getSamplePaths().stream()
            .filter(sample -> sample.toString().endsWith(type))
            .collect(Collectors.toList());

        for (Path samplePath : samples)
        {
            try
            {
                transcode(samplePath);
            }
            catch (Exception e)
            {
                fail(String.format("Transconding of \"%s\" failed", samplePath));

            }
        }
    }

    private void transcode(Path samplePath) throws Exception
    {
        final String[] targetPlaylistFormats = {"pla", "asx", "b4s", "wpl", "smil", "rss", "atom", "hypetape", "xspf", "rmp", "plist", "pls", "mpcpl", "plp", "m3u"};

        for (String targetPlaylistFormat : targetPlaylistFormats)
        {

            final String sourcePath = samplePath.toString();

            Transcode.main(new String[]{"-t", targetPlaylistFormat, sourcePath});

            switch (targetPlaylistFormat)
            {
                case "plp":
                    Transcode.main(new String[]{"-t", targetPlaylistFormat, "-plp:disk", "HD", sourcePath});
                    break;
                case "m3u":
                    Transcode.main(new String[]{"-t", targetPlaylistFormat, "-m3u:ext", sourcePath});
                    break;
                case "rss":
                    Transcode.main(new String[]{"-t", targetPlaylistFormat, "-rss:media", sourcePath});
                    break;
            }
        }
    }

}
