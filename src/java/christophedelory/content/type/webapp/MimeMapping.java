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
package christophedelory.content.type.webapp;

/**
 * Defines a mapping between a file extension and a MIME type.
 * @version $Revision: 92 $
 * @author Christophe Delory
 * @castor.class xml="mime-mapping" ns-uri="http://java.sun.com/xml/ns/j2ee"
 */
public class MimeMapping
{
	/**
	 * A (file) extension, for example: <code>txt</code>.
	 */
	private String _extension = null;

	/**
	 * A string describing the defined MIME type, for example: <code>text/plain</code>.
	 */
	private String _mimeType = null;

	/**
	 * Returns the corresponding extension.
	 * @return an extension. May be <code>null</code> if not yet initialized.
	 * @see #setExtension
     * @castor.field
	 *  get-method="getExtension"
	 *  set-method="setExtension"
	 *  required="true"
     * @castor.field-xml
	 *  name="extension"
	 *  node="element"
	 */
	public String getExtension()
	{
		return _extension;
	}

	/**
	 * Initializes the file extension.
	 * @param extension an extension. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>extension</code> is <code>null</code>.
	 * @see #getExtension
	 */
	public void setExtension(final String extension)
	{
		_extension = extension.trim().toLowerCase(); // Throws NullPointerException if extension is null.
	}

	/**
	 * Returns the corresponding MIME type.
	 * @return a MIME type. May be <code>null</code> if not yet initialized.
	 * @see #setMimeType
     * @castor.field
	 *  get-method="getMimeType"
	 *  set-method="setMimeType"
	 *  required="true"
     * @castor.field-xml
	 *  name="mime-type"
	 *  node="element"
	 */
	public String getMimeType()
	{
		return _mimeType;
	}

	/**
	 * Initializes the MIME type.
	 * @param mimeType a MIME type. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>mimeType</code> is <code>null</code>.
	 * @see #getMimeType
	 */
	public void setMimeType(final String mimeType)
	{
		_mimeType = mimeType.trim(); // Throws NullPointerException if mimeType is null.
	}
}
