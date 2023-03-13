package christophedelory.playlist;

import javax.xml.bind.*;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public abstract class JaxbPlaylistProvider<T> extends AbstractPlaylistProvider
{
    private final Class<T> xmlClass;
    private JAXBContext jaxbContext = null;

    public JaxbPlaylistProvider(Class proveriderClass, Class<T> xmlClass)
    {
        super(proveriderClass);
        this.xmlClass = xmlClass;
    }

    public Class<T> getXmlCLass()
    {
        return this.xmlClass;
    }

    private JAXBContext getJaxbContext() throws JAXBException
    {
        synchronized (this) {
            if (this.jaxbContext == null)
            {
                this.jaxbContext = JAXBContext.newInstance(this.getXmlCLass());
            }
            return this.jaxbContext;
        }
    }

    protected JAXBElement<T> unmarshal(final InputStream in, final String encoding) throws Exception
    {
        String applyEncoding = encoding == null ? StandardCharsets.US_ASCII.name() : encoding;
        Unmarshaller unmarshaller = this.getJaxbContext().createUnmarshaller();
        XMLStreamReader xmlStreamReader = this.getXmlStreamReader(in, applyEncoding);
        return unmarshaller.unmarshal(xmlStreamReader, this.getXmlCLass());
    }

    public void writeTo(Object xmlPlaylist, final OutputStream out, final String encoding) throws Exception
    {
        // Marshal the playlist.
        Marshaller marshaller = this.getJaxbContext().createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(xmlPlaylist, out);
        out.flush(); // May throw IOException.
    }

    protected XMLStreamReader getXmlStreamReader(final InputStream in, final String encoding) throws XMLStreamException
    {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();
        xmlInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);  // Prevent downloading DTD
        return xmlInputFactory.createXMLStreamReader(in, encoding);
    }
}
