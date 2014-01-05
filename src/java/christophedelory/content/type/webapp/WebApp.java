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

import java.util.ArrayList;
import java.util.List;

/**
 * A container of mappings between MIME types and extensions.
 * @version $Revision: 92 $
 * @author Christophe Delory
 * @castor.class xml="web-app" ns-uri="http://java.sun.com/xml/ns/j2ee"
 */
public class WebApp
{
	/**
	 * A list of mappings.
	 */
	private final List<MimeMapping> _mappings;

	/**
	 * Builds a new and empty mappings container.
	 */
	public WebApp()
	{
		_mappings = new ArrayList<MimeMapping>();
	}

	/**
	 * Returns the list of mappings.
	 * @return a list of mappings. May be empty but not <code>null</code>.
	 * @see #addMimeMapping
     * @castor.field
	 *  type="christophedelory.content.type.webapp.MimeMapping"
	 *  get-method="getMimeMappings"
	 *  set-method="addMimeMapping"
     *  collection="arraylist"
     * @castor.field-xml
	 *  name="mime-mapping"
	 *  node="element"
	 */
	public List<MimeMapping> getMimeMappings()
	{
		return _mappings;
	}

	/**
	 * Adds a MIME type mapping.
	 * @param mapping a mapping. Shall not be <code>null</code>.
	 * @throws NullPointerException if <code>mapping</code> is <code>null</code>.
	 * @see #getMimeMappings
	 */
	public void addMimeMapping(final MimeMapping mapping)
	{
        if (mapping == null)
        {
            throw new NullPointerException("no mapping");
        }

		_mappings.add(mapping);
	}
}
