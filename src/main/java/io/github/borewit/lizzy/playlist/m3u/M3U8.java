package io.github.borewit.lizzy.playlist.m3u;


import java.io.IOException;
import java.io.OutputStream;

import static io.github.borewit.lizzy.playlist.m3u.M3U8Provider.M3u8TextEncoding;

public class M3U8 extends AbstractM3U {
  @Override
  public void writeTo(final OutputStream out) throws IOException {
    super.writeTo(out, M3u8TextEncoding);
  }

}
