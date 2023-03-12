package christophedelory.playlist;

import christophedelory.test.json.playlist.*;
import christophedelory.util.TestUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

@DisplayName("Utility to write playlist sample data to JSON")
public class WriteToJson
{
    @DisplayName("Write playlists to JSON")
    @Test
    @Disabled // Only used to seed the test reference data
    void writeJson() throws Exception
    {
        final Map<String, JsonPlaylist> playlistMap = new TreeMap<>();

       for (Path sample : TestUtil.getSamplePaths())
        {
            Playlist playlist = TestUtil.readPlaylistFrom(sample.toString());
            playlistMap.put(sample.getFileName().toString(), JsonPlaylist.toJson(playlist));
        }

        final File jsonFile = TestUtil.jsonTestDataPath.toFile();

        new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
            .writerWithDefaultPrettyPrinter()
            .writeValue(jsonFile, playlistMap);
    }
}

