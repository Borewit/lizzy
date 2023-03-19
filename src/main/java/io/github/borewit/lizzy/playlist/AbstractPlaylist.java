package io.github.borewit.lizzy.playlist;

import java.io.IOException;
import java.io.OutputStream;

public abstract class AbstractPlaylist implements SpecificPlaylist
{
  @Override
  public void writeTo(OutputStream out) throws IOException
  {
    this.writeTo(out, null);
  }

}
