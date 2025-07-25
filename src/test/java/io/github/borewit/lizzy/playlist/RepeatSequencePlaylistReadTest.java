package io.github.borewit.lizzy.playlist;

import io.github.borewit.lizzy.util.TestUtil;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RepeatSequencePlaylistReadTest {

    @Test
    @DisplayName("Playlists with multiple sequences of repeats should read correctly")
    public void ensureCorrectReadOfPlaylistsWithRepeats() throws IOException {
        // predefine list of paths
        List<String> paths = getPaths();
        List<String> paths2 = new ArrayList<>();

        // create tmp playlist file
        File playlistFile = createFile(paths);
        try {
            // read from file
            Playlist playlist = TestUtil.readPlaylistFrom(playlistFile.toPath());

            assertNotNull(playlist, "Playlist couldn't be created");

            // flatten playlist media to one array
            flattenPlaylistComponent(playlist.getRootSequence(), paths2);
        } finally {
            Files.deleteIfExists(playlistFile.toPath());
        }

        assertEquals(paths.size(), paths2.size(), "Size should be equal for the same playlist.");

        for (int i = 0; i < paths.size(); i++) {
            assertEquals(paths.get(i), paths2.get(i), String.format("Media index (starting from 0): %d", i));
        }
    }

    private static List<String> getPaths() {
        String basePath = "path";
        List<String> fileNames = List.of(
                "file_1.mp3",
                "file_2-3.mp3",
                "file_2-3.mp3",
                "file_4.mp3",
                "file_5.mp3",
                "file_6-7-8.mp3",
                "file_6-7-8.mp3",
                "file_6-7-8.mp3",
                "file_9.mp3",
                "file_10.mp3",
                "file_11.mp3",
                "file_12.mp3"
        );

        List<String> paths = new ArrayList<>(fileNames.size());
        for (String fileName : fileNames) {
            paths.add(basePath + File.separator + fileName);
        }

        return paths;
    }

    private static File createFile(List<String> content) throws IOException {
        // create tmp file
        File playlistFile = File.createTempFile("test_list", ".m3u");

        // write paths to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(playlistFile))) {
            for (String path : content) {
                writer.append(path);
                writer.newLine();
            }
        }

        return playlistFile;
    }

    private static void flattenPlaylistComponent(AbstractPlaylistComponent component, final List<String> destination) {
        if (component instanceof Media) {
            destination.add(((Media) component).getSource().toString());
        } else if (component instanceof Sequence) {
            for (AbstractPlaylistComponent c : ((Sequence) component).getComponents()) {
                flattenPlaylistComponent(c, destination);
            }
        } else {
            // not sure what are the other AbstractPlaylistComponent implementations
            throw new UnsupportedOperationException();
        }
    }

}
