package christophedelory.playlist;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SpecificPlaylistFactory Tests")
public class SpecificPlaylistFactoryTests
{
  @Test
  public void testLoadPlaylistProviders()
  {
    SpecificPlaylistFactory specificPlaylistFactory = SpecificPlaylistFactory.getInstance();
    // If this tests fails, ensure resources/META-INF.services/* are included as resources
    assertFalse(specificPlaylistFactory.getProviders().isEmpty(), "Factory should have providers loaded");
  }

  @DisplayName("Serialize playlist format")
  @ParameterizedTest
  @ValueSource(strings = {"pla", "asx", "b4s", "wpl", "smil", "rss", "atom", "hypetape", "xspf", "rmp", "plist", "pls", "mpcpl", "plp", "m3u"})
  void testSerializePlaylist(String extension) throws Exception
  {
    SpecificPlaylistFactory specificPlaylistFactory = SpecificPlaylistFactory.getInstance();
    SpecificPlaylistProvider specificPlaylistProvider = specificPlaylistFactory.findProviderByExtension("playlist." + extension);
    SpecificPlaylist playlist = specificPlaylistProvider.toSpecificPlaylist(new Playlist());

    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream())
    {
      playlist.writeTo(outputStream, "UTF-8");
    }
  }
}
