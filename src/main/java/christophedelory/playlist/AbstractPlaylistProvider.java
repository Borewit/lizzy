package christophedelory.playlist;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public abstract class AbstractPlaylistProvider implements SpecificPlaylistProvider
{
  private final Logger logger = LogManager.getLogger(AbstractPlaylistProvider.class);

  public SpecificPlaylist readFrom(final InputStream in) throws Exception {
    return this.readFrom(in, null);
  }

  @Override
  public SpecificPlaylist readFrom(final InputStream in, final String encoding) throws Exception {
    return this.readFrom(in, encoding);
  }

  protected final InputStreamReader preProcessXml(final InputStream in, final String encoding) throws IOException
  {
    final Charset charset = encoding == null ? StandardCharsets.UTF_8 : Charset.forName(encoding);
    logger.debug(String.format("Decoding with charset %s", charset));
    return new InputStreamReader(in, charset);
  }

}
