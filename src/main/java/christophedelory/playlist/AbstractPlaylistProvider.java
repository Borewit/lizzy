package christophedelory.playlist;

import christophedelory.io.IOUtils;
import christophedelory.xml.XmlSerializer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
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

  @Override
  public SpecificPlaylist readFrom(final InputStream in, final String encoding, final Log logger) throws Exception
  {
    // Unmarshal the WPL playlist.
    final XmlSerializer serializer = XmlSerializer.getMapping("christophedelory/playlist/xspf"); // May throw Exception.
    serializer.getUnmarshaller().setIgnoreExtraElements(true);

    final SpecificPlaylist ret = (SpecificPlaylist) serializer.unmarshal(in); // May throw Exception.
    ret.setProvider(this);

    return ret;
  }

  protected final String preProcessXml(final InputStream in, final String encoding) throws IOException
  {
    String enc = encoding;

    if (enc == null)
    {
      enc = "UTF-8";
    }

    String str = IOUtils.toString(in, enc); // May throw IOException. Throws NullPointerException if in is null.

    // Replace all occurrences of a single '&' with "&amp;" (or leave this construct as is).
    // First replace blindly all '&' to its corresponding character reference.
    str = str.replace("&", "&amp;");
    // Then restore any existing character reference.
    str = str.replaceAll("&amp;([a-zA-Z0-9#]+;)", "&$1"); // Shall not throw PatternSyntaxException.

    // Workaround Castor bug 2521:
    str = str.replace("xmlns=\"http://www.w3.org/2005/Atom\"", "");

    // An XML element name cannot begin with a digit (like in "0").
    // Thus the document we are about to parse is NOT well-formed.
    // But I can't set the "well-formed" parameter to the DOMConfiguration before parsing, nor call setStrictErrorChecking(false).
    // Thus this trick.
    str = str.replaceAll("<([0-9]+) ", "<x$1 "); // Shall not throw PatternSyntaxException.
    str = str.replaceAll("</([0-9]+)", "</x$1"); // Shall not throw PatternSyntaxException.

    return str;
  }

}
