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
package christophedelory.playlist.smil;

import java.util.ArrayList;
import java.util.List;

/**
 * The base class of time containers.
 * There are three types of time containers.
 * These can be declared with the elements par, seq, and excl, or in some integration profiles with a time container attribute.
 * It also represents the principal Element of a SMIL body and it could be contain one or more Reference.
 * @version $Revision: 92 $
 * @author Christophe Delory
 * @castor.class xml="timing" verify-constructable="false"
 */
public abstract class AbstractTimingElement extends AbstractSmilElement
{
    /**
     * A list of elements composing the SMIL presentation.
     */
    private final List<AbstractSmilElement> _smilElements = new ArrayList<AbstractSmilElement>();

    /**
     * Adds a SMIL element to this time container.
     * @param smilElement a SMIL element. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>smilElement</code> is <code>null</code>.
     * @see #getSmilElements
     */
    public void addSmilElement(final AbstractSmilElement smilElement)
    {
        smilElement.setParent(this); // Throws NullPointerException if smilElement is null.
        _smilElements.add(smilElement);
    }

    /**
     * Returns the list of SMIL elements (media objects, time containers and others) composing the SMIL presentation.
     * @return a list of SMIL elements. May be empty but not <code>null</code>.
     * @see #addSmilElement
     * @castor.field
     *  get-method="getSmilElements"
     *  set-method="addSmilElement"
     *  type="christophedelory.playlist.smil.AbstractSmilElement"
     *  collection="arraylist"
     * @castor.field-xml
     *  auto-naming="deriveByClass"
     *  node="element"
     */
    public List<AbstractSmilElement> getSmilElements()
    {
        return _smilElements;
    }

    @Override
    public void acceptDown(final SmilVisitor visitor)
    {
        // First keep a local copy of the list, just in case the visitor modifies it.
        final AbstractSmilElement[] smilElements = new AbstractSmilElement[_smilElements.size()];
        _smilElements.toArray(smilElements); // Shall not throw NullPointerException, ArrayStoreException.

        // Then visit the SMIL elements.
        for (AbstractSmilElement smilElement : smilElements)
        {
            smilElement.acceptDown(visitor); // Throws NullPointerException if visitor is null.
        }
    }
}
