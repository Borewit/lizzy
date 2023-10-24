package io.github.borewit.lizzy.playlist;

import jakarta.xml.bind.*;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public abstract class JaxbPlaylistProvider<T> extends ProviderWithTextEncoding
{
  private final Class<T> xmlClass;
  private JAXBContext jaxbContext = null;

  public static final Charset defaultXmlTextEncoding = StandardCharsets.UTF_8;

  public JaxbPlaylistProvider(Class<T> xmlClass)
  {
    super(defaultXmlTextEncoding);
    this.xmlClass = xmlClass;
  }

  public Class<T> getXmlCLass()
  {
    return this.xmlClass;
  }

  private JAXBContext getJaxbContext() throws JAXBException
  {
    synchronized (this)
    {
      if (this.jaxbContext == null)
      {
        this.jaxbContext = JAXBContext.newInstance(this.getXmlCLass());
      }
      return this.jaxbContext;
    }
  }

  protected JAXBElement<T> unmarshal(final InputStream in) throws JAXBException, XMLStreamException
  {
    Unmarshaller unmarshaller = this.getJaxbContext().createUnmarshaller();
    XMLStreamReader xmlStreamReader = this.getXmlStreamReader(in, this.textEncoding.name());
    return unmarshaller.unmarshal(xmlStreamReader, this.getXmlCLass());
  }

  protected Marshaller makeMarshaller(String encoding) throws JAXBException
  {
    Marshaller marshaller = this.getJaxbContext().createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    marshaller.setProperty(Marshaller.JAXB_ENCODING, this.textEncoding.name());
    return marshaller;
  }

  public void writeTo(Object xmlPlaylist, final OutputStream out) throws IOException
  {
    try
    {
      // Marshal the playlist.
      Marshaller marshaller = makeMarshaller(this.textEncoding.name());

      marshaller.marshal(xmlPlaylist, out);
      out.flush(); // May throw IOException.
    }
    catch (JAXBException exception)
    {
      throw new IOException(exception);
    }
  }

  protected XMLStreamReader getXmlStreamReader(final InputStream in, final String encoding) throws XMLStreamException
  {
    XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();
    xmlInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);  // Prevent downloading DTD
    return xmlInputFactory.createXMLStreamReader(in, encoding);
  }
}
