package io.github.borewit.lizzy.playlist;

import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.input.BOMInputStream;

import java.io.IOException;
import java.io.InputStream;

public abstract class AbstractPlaylistProvider implements SpecificPlaylistProvider
{
  @Override
  public SpecificPlaylist readFrom(final InputStream inputStream) throws IOException
  {
    return this.readFrom(inputStream);
  }

  public static BOMInputStream wrapInBomStream(InputStream inputStream)
  {
    return new BOMInputStream(inputStream,
      ByteOrderMark.UTF_8,
      ByteOrderMark.UTF_16LE,
      ByteOrderMark.UTF_16BE,
      ByteOrderMark.UTF_32LE,
      ByteOrderMark.UTF_32BE);
  }

}
