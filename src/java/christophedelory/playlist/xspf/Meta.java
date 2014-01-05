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
package christophedelory.playlist.xspf;

/**
 * The meta element allows metadata fields to be added to XSPF elements.
 * <br>
 * Examples:
 * <pre>
 * &lt;meta rel="http://example.org/key"&gt;value&lt;/meta&gt;
 * &lt;meta rel="playCount"&gt;14&lt;/meta&gt;
 * &lt;meta rel="http://example.com/track/meta/playCount/1/0/"&gt;14&lt;/meta&gt;
 * </pre>
 * @version $Revision: 92 $
 * @author Christophe Delory
 * @castor.class xml="meta" ns-uri="http://xspf.org/ns/0/"
 */
public class Meta
{
    /**
     * URI of a resource defining the metadata.
     */
    private String _rel = null;

    /**
     * Value of the metadata element.
     */
    private String _content = null;

    /**
     * Returns the URI of a resource defining the metadata.
     * @return an URI. May be <code>null</code> if not yet initialized.
     * @see #setRel
     * @castor.field
     *  get-method="getRel"
     *  set-method="setRel"
     *  required="true"
     * @castor.field-xml
     *  name="rel"
     *  node="attribute"
     */
    public String getRel()
    {
        return _rel;
    }

    /**
     * Initializes the URI of a resource defining the metadata.
     * @param rel an URI. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>rel</code> is <code>null</code>.
     * @see #getRel
     */
    public void setRel(final String rel)
    {
        _rel = rel.trim(); // Throws NullPointerException if rel is null.
    }

    /**
     * Returns the value of the metadata element.
     * This is plain old text or character data, not XML nor HTML, and it may not contain markup.
     * @return a value. May be <code>null</code> if not yet initialized.
     * @see #setContent
     * @castor.field
     *  get-method="getContent"
     *  set-method="setContent"
     *  required="true"
     * @castor.field-xml
     *  node="text"
     */
    public String getContent()
    {
        return _content;
    }

    /**
     * Initializes the value of the metadata element.
     * @param content a value. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>content</code> is <code>null</code>.
     * @see #getContent
     */
    public void setContent(final String content)
    {
        _content = content.trim(); // Throws NullPointerException if content is null.
    }
}
