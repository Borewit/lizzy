package christophedelory.playlist;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SpecificPlaylistFactoryTests
{
  @Test
  public void testLoadPlaylistProviders()
  {
    SpecificPlaylistFactory specificPlaylistFactory = SpecificPlaylistFactory.getInstance();
    // If this tests fails, ensure resources/META-INF.services/* are included as resources
    assertFalse(specificPlaylistFactory.getProviders().isEmpty(), "Factory should have providers loaded");
  }
}
