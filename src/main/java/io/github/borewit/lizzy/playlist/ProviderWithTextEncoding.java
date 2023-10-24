package io.github.borewit.lizzy.playlist;

import java.nio.charset.Charset;

abstract class ProviderWithTextEncoding implements SpecificPlaylistProvider
{
  protected Charset textEncoding;

  protected ProviderWithTextEncoding(Charset defaultTextEncoding) {
    this.textEncoding = defaultTextEncoding;
  }

  public void setTextEncoding(Charset textEncoding) {
    this.textEncoding = textEncoding;
  }
}
