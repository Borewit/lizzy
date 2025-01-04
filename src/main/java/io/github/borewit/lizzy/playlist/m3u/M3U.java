package io.github.borewit.lizzy.playlist.m3u;

import java.io.IOException;
import java.io.OutputStream;

public class M3U extends AbstractM3U {
  @Override
  public void writeTo(final OutputStream out) throws IOException {
    super.writeTo(out, M3UProvider.M3uTextEncoding);
  }

}
