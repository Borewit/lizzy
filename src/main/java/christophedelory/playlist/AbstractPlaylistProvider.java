package christophedelory.playlist;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public abstract class AbstractPlaylistProvider implements SpecificPlaylistProvider
{
  protected final Log logger;

  public AbstractPlaylistProvider(Class clazz) {
    this.logger = LogFactory.getLog(clazz);
  }

  public SpecificPlaylist readFrom(final InputStream in) throws Exception {
    return this.readFrom(in, null, logger);
  }

  @Override
  public SpecificPlaylist readFrom(final InputStream in, final String encoding) throws Exception {
    return this.readFrom(in, encoding, logger);
  }

  protected final InputStreamReader preProcessXml(final InputStream in, final String encoding) throws IOException
  {
    final Charset charset = encoding == null ? StandardCharsets.UTF_8 : Charset.forName(encoding);
    logger.debug(String.format("Decoding with charset %s", charset));
    return new InputStreamReader(in, charset);
  }

}
