package io.github.borewit.lizzy.playlist;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlaylistFormatTests
{
  @Test
  @DisplayName("All playlist-provider ID's should be enumerated")
  public void ensureAllProviderIdsAreRepresented()
  {
    SpecificPlaylistFactory.getInstance().getProviders().stream()
      .map(SpecificPlaylistProvider::getId)
      .forEach(id -> {
        try
        {
          PlaylistFormat.valueOf(id);
        }
        catch (IllegalArgumentException exception)
        {
          fail(String.format("playlist-provider-id \"%s\" not enumerated", id));
        }
      });
  }
}
