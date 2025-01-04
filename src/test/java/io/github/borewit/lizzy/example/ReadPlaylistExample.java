package io.github.borewit.lizzy.example;

import io.github.borewit.lizzy.playlist.Media;
import io.github.borewit.lizzy.playlist.SpecificPlaylist;
import io.github.borewit.lizzy.playlist.SpecificPlaylistFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ReadPlaylistExample {
  public static void main(String[] args) throws IOException {
    Path playlistPath = Paths.get(System.getProperty("user.dir"), "samples", "asx", "test01.asx");
    SpecificPlaylist specificPlaylist = SpecificPlaylistFactory.getInstance().readFrom(playlistPath);
    if (specificPlaylist == null) {
      System.exit(-1);
    }
    specificPlaylist.toPlaylist().getRootSequence().getComponents().forEach(component -> {
      if (component instanceof Media) {
        Media media = (Media) component;
        System.out.printf("Media with content-source=%s\n", media.getSource().toString());
      }
    });
  }
}
