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

import christophedelory.lang.StringUtils;

/**
 * Defines a collection of rendering regions that determine how the elements in the document's body are positioned on an abstract rendering surface.
 * @version $Revision: 92 $
 * @author Christophe Delory
 * @castor.class xml="layout"
 */
public class Layout extends Core
{
    /**
     * The list of top layouts which are associated to this layout.
     */
    private final List<TopLayout> _topLayouts = new ArrayList<TopLayout>();

    /**
     * The root layout associated to this layout.
     */
    private RootLayout _rootLayout = null;

    /**
     * The list of regions associated to this layout.
     */
    private final List<Region> _regions = new ArrayList<Region>();

    /**
     * The list of registration points which are associated to the layout.
     */
    private final List<RegistrationPoint> _regPoints = new ArrayList<RegistrationPoint>();

    /**
     * Specifies which layout language is used in the layout element.
     */
    private String _type = null;

    /**
     * Returns the list of top layouts defined in this layout.
     * @return a list of top layouts. May be empty but not <code>null</code>.
     * @see #addTopLayout
     * @castor.field
     *  get-method="getTopLayouts"
     *  set-method="addTopLayout"
     *  type="christophedelory.playlist.smil.TopLayout"
     *  collection="arraylist"
     * @castor.field-xml
     *  name="topLayout"
     *  node="element"
     */
    public List<TopLayout> getTopLayouts()
    {
        return _topLayouts;
    }

    /**
     * Adds the specified top layout to this layout.
     * @param topLayout a top layout. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>topLayout</code> is <code>null</code>.
     * @see #getTopLayouts
     */
    public void addTopLayout(final TopLayout topLayout)
    {
        if (topLayout == null)
        {
            throw new NullPointerException("no top layout");
        }

        _topLayouts.add(topLayout);
    }

    /**
     * Returns the root layout defined in this layout.
     * @return a root layout. May be <code>null</code>.
     * @see #setRootLayout
     * @castor.field
     *  get-method="getRootLayout"
     *  set-method="setRootLayout"
     * @castor.field-xml
     *  name="root-layout"
     *  node="element"
     */
    public RootLayout getRootLayout()
    {
        return _rootLayout;
    }

    /**
     * Initializes the root layout associated to this layout.
     * @param rootLayout a root layout. May be <code>null</code>.
     * @see #getRootLayout
     */
    public void setRootLayout(final RootLayout rootLayout)
    {
        _rootLayout = rootLayout;
    }

    /**
     * Returns the list of regions defined in this layout.
     * @return a list of regions. May be empty but not <code>null</code>.
     * @see #addRegion
     * @castor.field
     *  get-method="getRegions"
     *  set-method="addRegion"
     *  type="christophedelory.playlist.smil.Region"
     *  collection="arraylist"
     * @castor.field-xml
     *  name="region"
     *  node="element"
     */
    public List<Region> getRegions()
    {
        return _regions;
    }

    /**
     * Adds a region to this layout.
     * @param region a region. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>region</code> is <code>null</code>.
     * @see #getRegions
     */
    public void addRegion(final Region region)
    {
        if (region == null)
        {
            throw new NullPointerException("no region");
        }

        _regions.add(region);
    }

    /**
     * Returns the first region found with the specified identifier.
     * @param regionId the region identifier. Shall not be <code>null</code>.
     * @return a region, or <code>null</code> if none is found.
     * @throws NullPointerException if <code>regionId</code> is <code>null</code>.
     * @see Region#getId
     */
    public Region findRegionById(final String regionId)
    {
        Region ret = null;

        for (Region region : _regions)
        {
            if (regionId.equals(region.getId())) // region.getId() may be null. Throws NullPointerException if regionId is null.
            {
                ret = region;
                break;
            }
        }

        return ret;
    }

    /**
     * Returns the list of registration points defined in this layout.
     * @return a list of registration points. May be empty but not <code>null</code>.
     * @see #addRegistrationPoint
     * @castor.field
     *  get-method="getRegistrationPoints"
     *  set-method="addRegistrationPoint"
     *  type="christophedelory.playlist.smil.RegistrationPoint"
     *  collection="arraylist"
     * @castor.field-xml
     *  name="regPoint"
     *  node="element"
     */
    public List<RegistrationPoint> getRegistrationPoints()
    {
        return _regPoints;
    }

    /**
     * Adds a registration point to this layout.
     * @param regPoint a registration point. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>regPoint</code> is <code>null</code>.
     * @see #getRegistrationPoints
     */
    public void addRegistrationPoint(final RegistrationPoint regPoint)
    {
        if (regPoint == null)
        {
            throw new NullPointerException("no registration point");
        }

        _regPoints.add(regPoint);
    }

    /**
     * Returns the type of layout language which is used in the layout element.
     * The default value is "text/smil-basic-layout".
     * If the type value equals "text/smil-basic-layout", it may contain the rootLayout and region elements.
     * @return the type of layout language. May be <code>null</code>.
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
     * Initializes the type of layout language which is used in the layout element.
     * @param type the type of layout language. May be <code>null</code>.
     * @see #getType
     */
    public void setType(final String type)
    {
        _type = StringUtils.normalize(type);
    }

    /**
     * Accepts the specified SMIL playlist visitor.
     * @param visitor a SMIL playlist visitor. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>visitor</code> is <code>null</code>.
     */
    public void acceptDown(final SmilVisitor visitor)
    {
        visitor.beginVisitLayout(this); // Throws NullPointerException if visitor is null.

        if (_rootLayout != null)
        {
            _rootLayout.acceptDown(visitor);
        }

        // First keep a local copy of the list, just in case the visitor modifies it.
        final Region[] regions = new Region[_regions.size()];
        _regions.toArray(regions); // Shall not throw NullPointerException, ArrayStoreException.

        // Then visit the regions defined in this layout.
        for (Region region : regions)
        {
            region.acceptDown(visitor);
        }

        visitor.endVisitLayout(this);
    }
}
