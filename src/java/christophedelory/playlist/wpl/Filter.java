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
package christophedelory.playlist.wpl;

import java.util.ArrayList;
import java.util.List;

import christophedelory.lang.StringUtils;

/**
 * Contains elements that limit the size of a playlist, duration of a playlist, or number of media items in a playlist.
 * The filter element does not add any media elements to a playlist;
 * it simply removes or filters out content that was selected by the sourceFilter element.
 * The filter element limits the size of the result set.
 * <br>
 * Windows Media Player 9 Series or later.
 * @version $Revision: 92 $
 * @author Christophe Delory
 * @castor.class xml="filter"
 */
public class Filter
{
    /**
     * A list of fragment elements.
     */
    private final List<Fragment> _fragments = new ArrayList<Fragment>();

    /**
     * The type of filter object.
     */
    private String _type = null;

    /**
     * The GUID that uniquely identifies a filter object.
     */
    private String _id = "";

    /**
     * The name of the filter object.
     */
    private String _name = "";

    /**
     * Adds a fragment element to this filter.
     * @param fragment a fragment element. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>fragment</code> is <code>null</code>.
     * @see #getFragments
     */
    public void addFragment(final Fragment fragment)
    {
        if (fragment == null)
        {
            throw new NullPointerException("no fragment");
        }

        _fragments.add(fragment);
    }

    /**
     * Returns the list of fragment elements.
     * @return a list of fragment elements. May be empty but not <code>null</code>.
     * @see #addFragment
     * @castor.field
     *  get-method="getFragments"
     *  set-method="addFragment"
     *  type="christophedelory.playlist.wpl.Fragment"
     *  collection="arraylist"
     * @castor.field-xml
     *  name="fragment"
     *  node="element"
     */
    public List<Fragment> getFragments()
    {
        return _fragments;
    }

    /**
     * Returns the type of filter object.
     * There are no predefined values for this attribute.
     * @return a filter type. May be <code>null</code>.
     * @see #setType
     * @castor.field
     *  get-method="getType"
     *  set-method="setType"
     * @castor.field-xml
     *  name="type"
     *  node="attribute"
     */
    public String getType()
    {
        return _type;
    }

    /**
     * Initializes the type of filter object.
     * @param type a filter type. May be <code>null</code>.
     * @see #getType
     */
    public void setType(final String type)
    {
        _type = StringUtils.normalize(type);
    }

    /**
     * Returns the WPL GUID that uniquely identifies a filter object.
     * The filter object's methods are invoked to interpret the fragment elements contained in the filter element.
     * <br>
     * Values:
     * <dl>
     * <dt>{BC5E21B0-504C-46F6-82BF-FB975C911AD6}</dt>
     * <dd>The id for the "Microsoft Auto Playlist Filter -- Limits auto playlists by count, size or duration" filter.</dd>
     * <dt>{4202947A-A563-4B05-A754-A1B4B5989849}</dt>
     * <dd>The GUID for the "Music in my library" source filter.</dd>
     * <dt>{B2D9BDDC-8E49-444B-9BA4-193ABF9C7870}</dt>
     * <dd>The GUID for the "Video in my library" source filter.</dd>
     * <dt>{CC823400-A8E4-4081-B073-D3B6D952FE69}</dt>
     * <dd>The GUID for the "Pictures in my library" source filter.</dd>
     * <dt>{E5415A66-7763-4BDE-B97F-5557CA73C303}</dt>
     * <dd>The GUID for the "TV shows in my library" source filter.</dd>
     * </dl>
     * @return a filter identifier. Shall not be <code>null</code>.
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
     * Initializes the GUID that uniquely identifies a filter object.
     * @param id a filter identifier. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>id</code> is <code>null</code>.
     * @see #getId
     */
    public void setId(final String id)
    {
        _id = id.trim(); // Throws NullPointerException if id is null.
    }

    /**
     * Returns the name of the filter object.
     * <br>
     * Values:
     * <dl>
     * <dt>Microsoft Auto Playlist Filter -- Limits auto playlists by count, size or duration</dt>
     * <dd>Limits auto playlists by count, size, or duration.</dd>
     * <dt>Music in my library</dt>
     * <dd>A source filter object that selects specified items from the set of music items in the user's library.</dd>
     * <dt>Video in my library</dt>
     * <dd>A source filter object that selects specified items from the set of video items in the user's library.</dd>
     * <dt>Pictures in my library</dt>
     * <dd>A source filter object that selects specified items from the set of photo items in the user's library.</dd>
     * <dt>TV shows in my library</dt>
     * <dd>A source filter object that selects specified items from the set of TV items in the user's library.</dd>
     * </dl>
     * @return a filter name. Shall not be <code>null</code>.
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
     * Initializes the name of the filter object.
     * @param name a filter name. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>name</code> is <code>null</code>.
     * @see #getName
     */
    public void setName(final String name)
    {
        _name = name.trim(); // Throws NullPointerException if name is null.
    }
}
