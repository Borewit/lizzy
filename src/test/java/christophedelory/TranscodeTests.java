package christophedelory;

import christophedelory.lizzy.Transcode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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

import static org.junit.jupiter.api.Assertions.fail;

public class TranscodeTests
{

  private static Path getSampleFolderPath()
  {
    String currentDir = System.getProperty("user.dir");
    return Paths.get(currentDir, "test", "samples");
  }

  @ParameterizedTest
  @ValueSource(strings = {"pla", "asx", "b4s", "wpl", "smil", "rss", "atom", "hypetape", "xspf", "rmp", "plist", "kpl", "pls", "mpcpl", "plp", "m3u"})
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

  @Test
  public void transcodeAtomPlaylist() throws Exception
  {
    List<Path> samples = getSamplePaths().stream()
      .filter(sample -> sample.toString().endsWith("test02.atom"))
      .collect(Collectors.toList());

    for (Path samplePath : samples)
    {
      transcode(samplePath);
    }
  }

  private void transcode(Path samplePath) throws Exception
  {
    final String[] targetPlaylistFormats = {"pla", "asx", "b4s", "wpl", "smil", "rss", "atom", "hypetape", "xspf", "rmp", "plist", "kpl", "pls", "mpcpl", "plp", "m3u"};

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

  private static List<Path> getSamplePaths() throws IOException
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
