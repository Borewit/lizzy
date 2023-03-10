package christophedelory.playlist;

import christophedelory.lizzy.FetchContentMetadata;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static christophedelory.util.TestUtil.getSamplePaths;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

@DisplayName("Transcode all supported playlist formats")
public class TranscodeTests
{

    @DisplayName("Transcode playlist format")
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
                fail(String.format("Transconding of \"%s\" failed", samplePath), e);
            }
        }
    }

    private void transcode(Path samplePath) throws Exception
    {
        final String[] targetPlaylistFormats = {"pla", "asx", "b4s", "wpl", "smil", "rss", "atom", "hypetape", "xspf", "rmp", "plist", "pls", "mpcpl", "plp", "m3u"};

        SpecificPlaylistProvider inputProvider = SpecificPlaylistFactory.getInstance().findProviderByExtension(samplePath.toString()); // Shall not throw NullPointerException because of _type.
        assertNotNull(inputProvider, String.format("Input provider for sample \"%s\"", samplePath));

        final SpecificPlaylist inputSpecificPlaylist = SpecificPlaylistFactory.getInstance().readFrom(samplePath.toFile());
        assertNotNull(inputSpecificPlaylist, String.format("Convert input playlist to abstract playlist", samplePath));

        final FetchContentMetadata metadataVisitor = new FetchContentMetadata();
        final Playlist inputPlaylist = inputSpecificPlaylist.toPlaylist();
        inputPlaylist.acceptDown(metadataVisitor);

        for (String targetPlaylistFormat : targetPlaylistFormats)
        {
            SpecificPlaylistProvider outputProvider = SpecificPlaylistFactory.getInstance().findProviderById(targetPlaylistFormat); // Shall not throw NullPointerException because of _type.
            assertNotNull(outputProvider, String.format("Output provider for type ID \"%s\"", samplePath));
            outputProvider.toSpecificPlaylist(inputPlaylist);
        }
    }

}
