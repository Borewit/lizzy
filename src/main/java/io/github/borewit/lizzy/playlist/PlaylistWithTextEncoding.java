package io.github.borewit.lizzy.playlist;

import java.nio.charset.Charset;

public abstract class PlaylistWithTextEncoding implements SpecificPlaylist
{
  protected final ProviderWithTextEncoding providerWithTextEncoding;

  protected PlaylistWithTextEncoding(ProviderWithTextEncoding providerWithTextEncoding) {
    this.providerWithTextEncoding = providerWithTextEncoding;
  }

  public void setTextEncoding(Charset textEncoding) {
    this.providerWithTextEncoding.setTextEncoding(textEncoding);
  }

}
