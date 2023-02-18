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

import java.util.ArrayList;
import java.util.List;

/**
 * An ordered list of URIs.
 * The purpose is to satisfy licenses allowing modification but requiring attribution.
 * If you modify such a playlist, move its //playlist/location or //playlist/identifier element
 * to the top of the items in the //playlist/attribution element.
 * <br>
 * Such a list can grow without limit, so as a practical matter we suggest deleting ancestors more than ten generations back.
 * <pre>
 * &lt;attribution&gt;
 *  &lt;location&gt;http://bar.com/modified_version_of_original_playlist.xspf&lt;/location&gt;
 *  &lt;identifier&gt;somescheme:original_playlist.xspf&lt;/identifier&gt;
 * &lt;/attribution&gt;
 * </pre>
 * @version $Revision: 92 $
 * @author Christophe Delory
 * @castor.class xml="attribution" ns-uri="http://xspf.org/ns/0/"
 */
public class Attribution
{
    /**
     * A list of locations or identifiers.
     */
    private final List<StringContainer> _stringContainers = new ArrayList<StringContainer>();

    /**
     * Returns a list of string containers.
     * @return a list of string containers. May be empty but not <code>null</code>.
     * @see #addStringContainer
     * @castor.field
     *  get-method="getStringContainers"
     *  set-method="addStringContainer"
     *  type="christophedelory.playlist.xspf.StringContainer"
     *  collection="arraylist"
     * @castor.field-xml
     *  auto-naming="deriveByClass"
     *  node="element"
     */
    public List<StringContainer> getStringContainers()
    {
        return _stringContainers;
    }

    /**
     * Adds a string container.
     * @param stringContainer a string container. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>stringContainer</code> is <code>null</code>.
     * @throws IllegalArgumentException if the container contains no string.
     * @see #getStringContainers
     */
    public void addStringContainer(final StringContainer stringContainer)
    {
        if (stringContainer.getText() == null) // Throws NullPointerException if stringContainer is null.
        {
            throw new IllegalArgumentException("Empty string container");
        }

        _stringContainers.add(stringContainer);
    }
}
