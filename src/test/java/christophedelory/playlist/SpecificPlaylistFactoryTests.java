package christophedelory.playlist;

import christophedelory.test.json.playlist.JsonEntry;
import christophedelory.test.json.playlist.JsonPlaylist;
import christophedelory.util.TestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

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

    @DisplayName("Read playlist formats")
    @ParameterizedTest
    @ValueSource(strings = {"pla", "asx", "b4s", "wpl", "smil", "rss", "atom", "xspf", "rmp", "plist", "pls", "mpcpl", "plp", "m3u"})
    void testReadPlaylist(String extension) throws Exception
    {
        Map<String, JsonPlaylist> playlistMap = TestUtil.getPlaylistMetadata();

        for (String name : playlistMap.keySet())
        {
            if (name.endsWith(extension))
            {
                Playlist playlist = TestUtil.readPlaylistFrom(name);
                compare(name, playlistMap.get(name), playlist);
            }
        }
    }

    private static void compare(String name, JsonPlaylist reference, Playlist playlist)
    {
        assertNotNull(playlist, String.format("Reading playlist %s", name));

        Sequence sequence = playlist.getRootSequence();
        assertNotNull(sequence, String.format("Sequence %s", name));

        if (reference.rootSequence != null)
        {
            compare(name, reference.rootSequence, playlist.getRootSequence().getComponents());
        }
    }

    private static void compare(String name, List<JsonEntry> references, AbstractPlaylistComponent[] components)
    {
        assertNotNull(components, String.format("Components %s", name));

        assertEquals(references.size(), components.length, String.format("Number of root components in %s", name));
        int n = 0;
        for (JsonEntry jsonEntry : references)
        {
            compare(name, n, jsonEntry, components[n]);
            ++n;
        }
    }

    private static void compare(String name, int n, JsonEntry reference, AbstractPlaylistComponent component)
    {
        assertNotNull(component, String.format("Playlist component %d %s", n, name));

        if (component instanceof Media)
        {
            Media media = (Media) component;
            assertNotNull(media.getSource(), String.format("Media component source %d %s", n, name));
            assertEquals(reference.source, media.getSource().toString(), String.format("Media component source %d %s", n, name));
        }
        else if (component instanceof Sequence)
        {
            Sequence sequence = (Sequence) component;
            compare(String.format("%s/sequence[%d]", name, n), reference.sequence, sequence.getComponents());
        }
        else if (component instanceof Parallel)
        {
            Parallel sequence = (Parallel) component;
            compare(String.format("%s/parallel[%d]", name, n), reference.parallel, sequence.getComponents());
        }
    }

    @DisplayName("Serialize playlist format")
    @ParameterizedTest
    @ValueSource(strings = {"pla", "asx", "b4s", "wpl", "smil", "rss", "atom", "xspf", "rmp", "plist", "pls", "mpcpl", "plp", "m3u"})
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
