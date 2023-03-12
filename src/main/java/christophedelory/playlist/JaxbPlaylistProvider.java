package christophedelory.playlist;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
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

    protected JAXBElement<T> unmarshal(final InputStream in, final String encoding) throws Exception
    {
        synchronized (this) {
            if (this.jaxbContext == null)
            {
                this.jaxbContext = JAXBContext.newInstance(this.getXmlCLass());
            }
        }
        String applyEncoding = encoding == null ? StandardCharsets.US_ASCII.name() : encoding;
        Unmarshaller unmarshaller = this.jaxbContext.createUnmarshaller();
        XMLStreamReader xmlStreamReader = this.getXmlStreamReader(in, applyEncoding);
        return unmarshaller.unmarshal(xmlStreamReader, this.getXmlCLass());
    }

    protected XMLStreamReader getXmlStreamReader(final InputStream in, final String encoding) throws XMLStreamException
    {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();
        xmlInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);  // Prevent downloading DTD
        return xmlInputFactory.createXMLStreamReader(in, encoding);
    }
}
