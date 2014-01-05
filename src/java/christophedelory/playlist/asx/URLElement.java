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
package christophedelory.playlist.asx;

/**
 * An URL (HREF) container.
 * @version $Revision: 92 $
 * @author Christophe Delory
 * @castor.class xml="url"
 */
public abstract class URLElement extends AsxElement
{
    /**
     * The URL itself.
     */
    private String _href = null;

    /**
     * Returns the URL, as a string.
     * @return an URL. May be <code>null</code> if not yet initialized.
     * @see #setHref
     * @castor.field
     *  get-method="getHref"
     *  set-method="setHref"
     *  required="true"
     * @castor.field-xml
     *  name="href"
     *  node="attribute"
     */
    public String getHref()
    {
        return _href;
    }

    /**
     * Initializes the URL from the specified a string.
     * @param href an URL. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>href</code> is <code>null</code>.
     * @see #getHref
     */
    public void setHref(final String href)
    {
        _href = href.trim().replace('\\', '/'); // Throws NullPointerException if href is null.
    }
}
