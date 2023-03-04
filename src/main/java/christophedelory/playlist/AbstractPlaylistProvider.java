package christophedelory.playlist;

import christophedelory.playlist.plp.PLPProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.InputStream;

public abstract class AbstractPlaylistProvider implements SpecificPlaylistProvider
{
  protected final Log logger;

  public AbstractPlaylistProvider(Class clazz) {
    this.logger = LogFactory.getLog(clazz);
  }

  @Override
  public SpecificPlaylist readFrom(final InputStream in, final String encoding) throws Exception {
    return this.readFrom(in, encoding, logger);
  }

}
