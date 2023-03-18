package io.github.borewit.lizzy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static io.github.borewit.lizzy.util.TestUtil.getSamplePaths;

@DisplayName("Command Line Application Tests")
public class CommandLineTranscodeTests
{
  @DisplayName("Transcode playlist via command line")
  @ParameterizedTest
  @ValueSource(strings = {"pla", "asx", "b4s", "wpl", "smil", "rss", "atom", "xspf", "rmp", "plist", "pls", "mpcpl", "plp", "m3u"})
  public void transcodePlaylist(String type) throws Exception
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
        throw new Exception(String.format("Transcoding of \"%s\" failed", samplePath.getFileName()), e);
      }
    }
  }

  private void transcode(Path samplePath) throws Exception
  {
    final String[] targetPlaylistFormats = {"pla", "asx", "b4s", "wpl", "smil", "rss", "atom", "xspf", "rmp", "plist", "pls", "mpcpl", "plp", "m3u"};

    for (String targetPlaylistFormat : targetPlaylistFormats)
    {

      final String sourcePath = samplePath.toString();

      try
      {
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
      catch (Exception e)
      {
        throw new Exception(String.format("Transcoding of \"%s\" to \"%s\" failed", samplePath.getFileName(), targetPlaylistFormat), e);
      }
    }
  }

}
