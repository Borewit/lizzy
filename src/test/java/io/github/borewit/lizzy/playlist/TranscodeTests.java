package io.github.borewit.lizzy.playlist;

import io.github.borewit.lizzy.FetchContentMetadata;
import io.github.borewit.lizzy.util.TestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Transcode all supported playlist formats")
public class TranscodeTests
{

  @DisplayName("Transcode playlist format")
  @ParameterizedTest
  @ValueSource(strings = {"pla", "asx", "b4s", "wpl", "smil", "rss", "atom", "xspf", "rmp", "plist", "pls", "mpcpl", "plp", "m3u"})
  public void transcodePlaylist(String type) throws IOException
  {
    List<Path> samples = TestUtil.getSamplePaths().stream()
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
        throw new IOException(String.format("Transcoding of \"%s\" failed", samplePath), e);
      }
    }
  }

  private void transcode(Path samplePath) throws IOException
  {
    final String[] targetPlaylistFormats = {"pla", "asx", "b4s", "wpl", "smil", "rss", "atom", "xspf", "rmp", "plist", "pls", "mpcpl", "plp", "m3u"};

    final Playlist inputPlaylist = TestUtil.readPlaylistFrom(samplePath);
    final FetchContentMetadata metadataVisitor = new FetchContentMetadata();
    inputPlaylist.acceptDown(metadataVisitor);

    for (String targetPlaylistFormat : targetPlaylistFormats)
    {
      SpecificPlaylistProvider outputProvider = SpecificPlaylistFactory.getInstance().findProviderById(targetPlaylistFormat); // Shall not throw NullPointerException because of _type.
      assertNotNull(outputProvider, String.format("Output provider for type ID \"%s\"", samplePath));
      outputProvider.toSpecificPlaylist(inputPlaylist);
    }
  }
}
