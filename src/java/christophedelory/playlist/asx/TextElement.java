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
 * A text container.
 * @version $Revision: 92 $
 * @author Christophe Delory
 * @castor.class xml="textElement"
 */
public class TextElement extends Child
{
    /**
     * The text itself.
     */
    private String _text = "";

    /**
     * Returns the text itself.
     * @return a text. Shall not be <code>null</code>.
     * @see #setText
     * @castor.field
     *  get-method="getText"
     *  set-method="setText"
     *  required="true"
     * @castor.field-xml
     *  node="text"
     */
    public String getText()
    {
        return _text;
    }

    /**
     * Initializes the text itself.
     * @param text a text. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>text</code> is <code>null</code>.
     * @see #getText
     */
    public void setText(final String text)
    {
        _text = text.trim(); // Throws NullPointerException if text is null.
    }
}
