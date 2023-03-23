package io.github.borewit.lizzy.util;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.borewit.lizzy.playlist.*;
import io.github.borewit.lizzy.test.json.playlist.JsonPlaylist;
import org.apache.commons.io.input.BOMInputStream;
import org.junit.jupiter.api.Assertions;

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

  public static Playlist readPlaylistFrom(String filename) throws IOException
  {
    Path playlistPath = sampleFolderPath.resolve(filename);
    return readPlaylistFrom(playlistPath);
  }

  public static Playlist readPlaylistFrom(Path playlistPath) throws IOException
  {
    Path absPlaylistPath = playlistPath.isAbsolute() ? playlistPath : sampleFolderPath.resolve(playlistPath);
    SpecificPlaylist specificPlaylist = SpecificPlaylistFactory.getInstance().readFrom(absPlaylistPath);
    assertNotNull(specificPlaylist, String.format("Reading playlist %s", absPlaylistPath.getFileName()));
    return specificPlaylist.toPlaylist();
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
    Assertions.assertNotNull(media.getSource(), "Media source");
    Assertions.assertEquals(expectedSource, media.getSource().toString(), "Media source URL");
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
