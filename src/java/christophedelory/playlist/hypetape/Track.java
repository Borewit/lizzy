/*
 * Copyright (c) 2008-2009, Christophe Delory
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
package christophedelory.playlist.hypetape;

/**
 * A track definition.
 * @version $Revision: 92 $
 * @author Christophe Delory
 * @castor.class xml="track"
 */
public class Track
{
    /**
     * The track identifier.
     */
    private String _id = null;

    /**
     * The track title.
     */
    private String _name = null;

    /**
     * The track location.
     */
    private String _mp3 = null;

    /**
     * Returns the track identifier.
     * @return a text identifier. May be <code>null</code> only if not yet initialized.
     * @see #setId
     * @castor.field
     *  get-method="getId"
     *  set-method="setId"
     *  required="true"
     * @castor.field-xml
     *  name="id"
     *  node="attribute"
     */
    public String getId()
    {
        return _id;
    }

    /**
     * Initializes the track identifier.
     * @param id a text identifier. Shall not be <code>null</code>.
     * @see #getId
     */
    public void setId(final String id)
    {
        _id = id.trim(); // Throws NullPointerException if id is null.
    }

    /**
     * Returns the track title.
     * @return a title. May be <code>null</code> only if not yet initialized.
     * @see #setName
     * @castor.field
     *  get-method="getName"
     *  set-method="setName"
     *  required="true"
     * @castor.field-xml
     *  name="name"
     *  node="attribute"
     */
    public String getName()
    {
        return _name;
    }

    /**
     * Initializes the track title.
     * @param name a title. Shall not be <code>null</code>.
     * @see #getName
     */
    public void setName(final String name)
    {
        _name = name.trim(); // Throws NullPointerException if name is null.
    }

    /**
     * Returns the track location.
     * @return a track location. May be <code>null</code> only if not yet initialized.
     * @see #setMP3
     * @castor.field
     *  get-method="getMP3"
     *  set-method="setMP3"
     *  required="true"
     * @castor.field-xml
     *  name="mp3"
     *  node="attribute"
     */
    public String getMP3()
    {
        return _mp3;
    }

    /**
     * Initializes the track location.
     * @param mp3 a track location. Shall not be <code>null</code>.
     * @see #getMP3
     */
    public void setMP3(final String mp3)
    {
        _mp3 = mp3.trim(); // Throws NullPointerException if mp3 is null.
    }
}
