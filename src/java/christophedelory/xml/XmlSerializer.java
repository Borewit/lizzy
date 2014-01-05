/*
 * Copyright (c) 2008, Christophe Delory
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY CHRISTOPHE DELORY ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL CHRISTOPHE DELORY BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package christophedelory.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.util.Hashtable;

import org.xml.sax.InputSource;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * A collection of utilities when manipulating the Castor framework.
 * The 3d-party <a href="http://www.castor.org/">Castor</a> package achieves the Java-XML data binding.
 * @version $Revision: 55 $
 * @author Christophe Delory
 */
public final class XmlSerializer
{
    /**
     * The list of mappings already built.
     */
    private static Hashtable<String,XmlSerializer> _mappings = new Hashtable<String,XmlSerializer>();

    /**
     * Retrieves the XML serializer instance associated to the specified package name.
     * The full Castor mapping file name will be <code><i>packageName</i>/mapping.xml</code>.
     * Thus the separator character used in <code>packageName</code> <u>shall be the slash</u> ("<code>/</code>").
     * @param packageName the package name. May be <code>null</code>.
     * @return a <code>XmlSerializer</code> instance. Shall not be <code>null</code>.
     * @throws FileNotFoundException if the mapping file cannot be found.
     * @throws IOException an error occured when reading the mapping file.
     * @throws MappingException the mapping file is invalid.
     * @throws SecurityException if a security manager exists and its <code>checkPermission</code> method denies access to the class loader for the class.
     */
    public static XmlSerializer getMapping(final String packageName) throws IOException, MappingException
    {
        final StringBuilder sb = new StringBuilder();

        if ((packageName != null) && (packageName.length() > 0))
        {
            sb.append(packageName);
            sb.append('/');
        }

        sb.append("mapping.xml");

        final String mappingFileName = sb.toString();

        synchronized(_mappings)
        {
            XmlSerializer mappingDescriptor = _mappings.get(mappingFileName); // Should not throw NullPointerException.

            if (mappingDescriptor == null)
            {
                mappingDescriptor = new XmlSerializer();
                final URL mappingURL = XmlSerializer.class.getClassLoader().getResource(mappingFileName); // May throw SecurityException.

                if (mappingURL == null)
                {
                    // Development error.
                    // Shall not occur, since this file is included in the jar.
                    // May occur if the jar has been "hacked".
                    throw new FileNotFoundException(mappingFileName);
                }

                final Mapping mapping = new Mapping(XmlSerializer.class.getClassLoader());

                // Load the mapping information from the file.
                mapping.loadMapping(mappingURL); // May throw IOException, MappingException.
                mappingDescriptor.setMapping(mapping); // Should not throw NullPointerException. May throw MappingException.

                _mappings.put(mappingFileName, mappingDescriptor); // Should not throw NullPointerException.
            }

            return mappingDescriptor;
        }
    }

    /**
     * Unmarshalls an object from an input file, according to the underlying mapping.
     * @param fileName an XML file name. Shall not be <code>null</code>.
     * @throws MappingException an exception indicating an invalid mapping error.
     * @throws FileNotFoundException if the file does not exist, is a directory rather than a regular file, or for some other reason cannot be opened for reading.
     * @throws SecurityException if a security manager exists and its <code>checkRead</code> method denies read access to the file.
     * @throws org.exolab.castor.xml.MarshalException when there is an error during the unmarshalling process.
     * @throws org.exolab.castor.xml.ValidationException when there is a validation error.
     * @throws NullPointerException if <code>fileName</code> is <code>null</code>.
     * @see #unmarshal(InputStream)
     * @see Mapping
     */
    public Object unmarshal(final String fileName) throws Exception
    {
        return unmarshal(new FileInputStream(fileName)); // Throws NullPointerException if fileName is null. May throw MappingException, MarshalException, ValidationException, FileNotFoundException, SecurityException.
    }

    /**
     * Unmarshalls an object from an input file, according to the underlying mapping.
     * @param file an XML file.
     * @throws MappingException an exception indicating an invalid mapping error.
     * @throws FileNotFoundException if the file does not exist, is a directory rather than a regular file, or for some other reason cannot be opened for reading.
     * @throws SecurityException if a security manager exists and its <code>checkRead</code> method denies read access to the file.
     * @throws org.exolab.castor.xml.MarshalException when there is an error during the unmarshalling process.
     * @throws org.exolab.castor.xml.ValidationException when there is a validation error.
     * @throws NullPointerException if <code>file</code> is <code>null</code>.
     * @see #unmarshal(InputStream)
     * @see Mapping
     */
    public Object unmarshal(final File file) throws Exception
    {
        return unmarshal(new FileInputStream(file)); // Throws NullPointerException if file is null. May throw MappingException, MarshalException, ValidationException, FileNotFoundException, SecurityException.
    }

    /**
     * Unmarshalls an object from an input URL, according to the underlying mapping.
     * @param url an URL.
     * @throws MappingException an exception indicating an invalid mapping error.
     * @throws IOException if an I/O exception occurs.
     * @throws org.exolab.castor.xml.MarshalException when there is an error during the unmarshalling process.
     * @throws org.exolab.castor.xml.ValidationException when there is a validation error.
     * @throws NullPointerException if <code>url</code> is <code>null</code>.
     * @see #unmarshal(InputStream)
     * @see Mapping
     */
    public Object unmarshal(final URL url) throws Exception
    {
        return unmarshal(url.openStream()); // Throws NullPointerException if url is null. May throw MappingException, IOException, MarshalException, ValidationException.
    }

    /**
     * Unmarshalls an object from an input stream, according to the underlying mapping.
     * @param in an input stream.
     * @throws MappingException an exception indicating an invalid mapping error.
     * @throws org.exolab.castor.xml.MarshalException when there is an error during the unmarshalling process.
     * @throws org.exolab.castor.xml.ValidationException when there is a validation error.
     * @throws NullPointerException if <code>in</code> is <code>null</code>.
     * @see Mapping
     */
    public Object unmarshal(final InputStream in) throws Exception
    {
        final InputSource is = new InputSource(in); // Throws NullPointerException if in is null.

        synchronized(_unmarshaller)
        {
            return _unmarshaller.unmarshal(is); // May throw MarshalException, ValidationException, ClassCastException.
        }
    }

    /**
     * Unmarshalls an object from a reader, according to the underlying mapping.
     * @param reader a reader.
     * @throws MappingException an exception indicating an invalid mapping error.
     * @throws org.exolab.castor.xml.MarshalException when there is an error during the unmarshalling process.
     * @throws org.exolab.castor.xml.ValidationException when there is a validation error.
     * @throws NullPointerException if <code>reader</code> is <code>null</code>.
     * @see Mapping
     */
    public Object unmarshal(final Reader reader) throws Exception
    {
        synchronized(_unmarshaller)
        {
            return _unmarshaller.unmarshal(reader); // Throws NullPointerException if reader is null. May throw MarshalException, ValidationException, ClassCastException.
        }
    }

    /**
     * Writes the specified object as an XML stream to an output file.
     * @param o the object to serialize.
     * @param fileName an XML file name.
     * @param asDocument if <code>true</code>, indicates to marshal as a complete XML document, which includes the XML declaration, and if necessary the DOCTYPE declaration.
     * @throws IOException if the named file exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened for any other reason.
     * @throws IOException if any I/O error occurred.
     * @throws MappingException an exception indicating an invalid mapping error.
     * @throws org.exolab.castor.xml.MarshalException a marshalling exception.
     * @throws org.exolab.castor.xml.ValidationException an XML validation error occurred.
     * @throws NullPointerException if <code>o</code> is <code>null</code>.
     * @throws NullPointerException if <code>fileName</code> is <code>null</code>.
     * @see #marshal(Object,Writer,boolean)
     * @see Mapping
     */
    public void marshal(final Object o, final String fileName, final boolean asDocument) throws Exception
    {
        final FileWriter out = new FileWriter(fileName, false); // May throw IOException.
        marshal(o, out, asDocument); // May throw MappingException, MarshalException, ValidationException.
        out.flush(); // May throw IOException.
        out.close(); // May throw IOException.
    }

    /**
     * Writes the specified object as an XML stream to an output file.
     * @param o the object to serialize.
     * @param file an XML file.
     * @param asDocument if <code>true</code>, indicates to marshal as a complete XML document, which includes the XML declaration, and if necessary the DOCTYPE declaration.
     * @throws IOException if the named file exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened for any other reason.
     * @throws IOException if any I/O error occurred.
     * @throws MappingException an exception indicating an invalid mapping error.
     * @throws org.exolab.castor.xml.MarshalException a marshalling exception.
     * @throws org.exolab.castor.xml.ValidationException an XML validation error occurred.
     * @throws NullPointerException if <code>o</code> is <code>null</code>.
     * @throws NullPointerException if <code>file</code> is <code>null</code>.
     * @see #marshal(Object,Writer,boolean)
     * @see Mapping
     */
    public void marshal(final Object o, final File file, final boolean asDocument) throws Exception
    {
        final FileWriter out = new FileWriter(file, false); // May throw IOException.
        marshal(o, out, asDocument); // May throw MappingException, MarshalException, ValidationException.
        out.flush(); // May throw IOException.
        out.close(); // May throw IOException.
    }

    /**
     * Writes the specified object as an XML stream to an output writer.
     * @param o the object to serialize.
     * @param out the writer.
     * @param asDocument if <code>true</code>, indicates to marshal as a complete XML document, which includes the XML declaration, and if necessary the DOCTYPE declaration.
     * @throws MappingException an exception indicating an invalid mapping error.
     * @throws org.exolab.castor.xml.MarshalException a marshalling exception.
     * @throws org.exolab.castor.xml.ValidationException an XML validation error occurred.
     * @throws NullPointerException if <code>o</code> is <code>null</code>.
     * @throws NullPointerException if <code>out</code> is <code>null</code>.
     * @see Mapping
     */
    public void marshal(final Object o, final Writer out, final boolean asDocument) throws Exception
    {
        synchronized(_marshaller)
        {
            _marshaller.setWriter(out); // May throw IOException.
            _marshaller.setMarshalAsDocument(asDocument);
            _marshaller.setEncoding("ISO-8859-1");
            // Do not use marshal(Object object, Writer out): IT DOESN'T WORK !!!
            _marshaller.marshal(o); // May throw MarshalException, ValidationException.
        }
    }

    /**
     * The associated marshaller.
     */
    private Marshaller _marshaller = null;

    /**
     * The associated unmarshaller.
     */
    private Unmarshaller _unmarshaller = null;

    /**
     * Builds a new and empty XML serializer based on the Castor framework.
     */
    private XmlSerializer()
    {
    }

    /**
     * Returns the associated Castor marshaller.
     * @return a Castor marshaller. Shall not be <code>null</code>.
     */
    public Marshaller getMarshaller()
    {
        return _marshaller;
    }

    /**
     * Returns the associated Castor unmarshaller.
     * @return a Castor unmarshaller. Shall not be <code>null</code>.
     */
    public Unmarshaller getUnmarshaller()
    {
        return _unmarshaller;
    }

    /**
     * Initializes the Castor mapping instance.
     * @param mapping a Castor mapping. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>mapping</code> is <code>null</code>.
     * @throws MappingException an exception indicating an invalid mapping error.
     */
    private void setMapping(final Mapping mapping) throws MappingException
    {
        _unmarshaller = new Unmarshaller(mapping); // May throw MappingException.
        _unmarshaller.setValidation(false);
        _unmarshaller.setIgnoreExtraElements(true);

        _marshaller = new Marshaller();
        _marshaller.setMapping(mapping); // May throw MappingException.
        _marshaller.setValidation(false);
        //_marshaller.setDebug(true);
        // Specifies whether to support XML namespaces by default. Default is false.
        //_marshaller.setProperty("org.exolab.castor.parser.namespaces", "true");
        // Specifies whether XML documents (as generated at marshalling) should use indentation or not. Default is false.
        //_marshaller.setProperty("org.exolab.castor.indent", "true");
    }
}
