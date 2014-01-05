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

import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import christophedelory.content.Content;
import christophedelory.lang.StringUtils;
import christophedelory.playlist.Media;
import christophedelory.playlist.Playlist;
import christophedelory.playlist.Sequence;
import christophedelory.playlist.SpecificPlaylist;
import christophedelory.playlist.SpecificPlaylistProvider;
import christophedelory.xml.XmlSerializer;

/**
 * A Windows Media metafile.
 * Defines a file as an Advanced Stream Redirector (ASX) file.
 * @version $Revision: 92 $
 * @author Christophe Delory
 * @castor.class xml="asx"
 */
public class Asx extends AsxOrEntryElement implements SpecificPlaylist, AsxElementContainer
{
    /**
     * The provider of this specific playlist.
     */
    private transient SpecificPlaylistProvider _provider = null;

    /**
     * Decimal number representing the version number of the syntax for the metafile.
     */
    private String _version = "3.0";

    /**
     * Value indicating whether Windows Media Player enters preview mode before playing the first clip.
     */
    private boolean _previewMode = false;

    /**
     * Value indicating whether Windows Media Player reserves space for a banner graphic.
     */
    private String _bannerBar = null;

    /**
     * The list of ASX elements of the metafile.
     */
    private final List<AsxElement> _asxElements = new ArrayList<AsxElement>();

    @Override
    public void setProvider(final SpecificPlaylistProvider provider)
    {
        _provider = provider;
    }

    @Override
    public SpecificPlaylistProvider getProvider()
    {
        return _provider;
    }

    @Override
    public void writeTo(final OutputStream out, final String encoding) throws Exception
    {
        // Marshal the ASX playlist.
        final StringWriter writer = new StringWriter();
        final XmlSerializer serializer = XmlSerializer.getMapping("christophedelory/playlist/asx"); // May throw Exception.
        // Specifies whether XML documents (as generated at marshalling) should use indentation or not. Default is false.
        serializer.getMarshaller().setProperty("org.exolab.castor.indent", "true");
        serializer.marshal(this, writer, false); // May throw Exception.

        String enc = encoding;

        if (enc == null)
        {
            enc = "UTF-8"; // FIXME US-ASCII?
        }

        final byte[] bytes = writer.toString().getBytes(enc); // May throw UnsupportedEncodingException.
        out.write(bytes); // Throws NullPointerException if out is null. May throw IOException.
        out.flush(); // May throw IOException.
    }

    @Override
    public Playlist toPlaylist()
    {
        final Playlist ret = new Playlist();

        for (AsxElement asxElement : _asxElements)
        {
            addToSequence(asxElement, ret.getRootSequence());
        }

        ret.normalize();

        return ret;
    }

    /**
     * Adds the specified ASX element, and its childs if appropriate, to the given sequence.
     * @param asxElement the ASX element to handle. Shall not be <code>null</code>.
     * @param currentSequence the parent sequence. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>asxElement</code> is <code>null</code>.
     * @throws NullPointerException if <code>currentSequence</code> is <code>null</code>.
     */
    private void addToSequence(final AsxElement asxElement, final Sequence currentSequence)
    {
        if (asxElement instanceof Repeat) // Throws NullPointerException if asxElement is null.
        {
            final Repeat repeat = (Repeat) asxElement;
            final Sequence seq = new Sequence();
            seq.setRepeatCount((repeat.getCount() == null) ? -1 : (repeat.getCount().intValue() + 1));
            currentSequence.addComponent(seq);

            for (AsxElement asxElem : repeat.getAsxElements())
            {
                addToSequence(asxElem, seq);
            }
        }
        else if (asxElement instanceof Entry)
        {
            final Entry entry = (Entry) asxElement;

            for (Reference reference : entry.getReferences())
            {
                // We keep only the first valid one.
                if (reference.getHref() != null)
                {
                    Duration duration = reference.getDuration();

                    if (duration == null)
                    {
                        duration = entry.getDuration();
                    }

                    if ((duration == null) || (duration.getValue() > 0L))
                    {
                        final Media media = new Media(); // NOPMD Avoid instantiating new objects inside loops
                        media.setSource(new Content(reference.getHref())); // NOPMD Avoid instantiating new objects inside loops

                        if (duration != null) // NOPMD Deeply nested if..then statements are hard to read
                        {
                            // FIXME We can't check here if the expressed duration is in fact the effective media duration
                            // (in which case we don't need to specify it, and we would be more compatible to simple playlist formats).
                            media.setDuration(duration.getValue()); // Shall not throw IllegalArgumentException.
                        }

                        currentSequence.addComponent(media);
                        break;
                    }
                }
            }
        }
        else if (asxElement instanceof Entryref)
        {
            final Entryref entryRef = (Entryref) asxElement;

            if (entryRef.getHref() != null)
            {
                final Media media = new Media();
                media.setSource(new Content(entryRef.getHref()));
                currentSequence.addComponent(media);
            }
        }
    }

    /**
     * Returns the decimal number representing the version number of the syntax for the metafile.
     * Acceptable values include both "3.0" and "3" (with or without the decimal point).
     * Defaults to "3.0".
     * @return a version string. Shall not be <code>null</code>.
     * @see #setVersion
     * @castor.field
     *  get-method="getVersion"
     *  set-method="setVersion"
     *  required="true"
     * @castor.field-xml
     *  name="version"
     *  node="attribute"
     */
    public String getVersion()
    {
        return _version;
    }

    /**
     * Initializes the decimal number representing the version number of the syntax for the metafile.
     * @param version a version string. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>version</code> is <code>null</code>.
     * @see #getVersion
     */
    public void setVersion(final String version)
    {
        _version = version.trim(); // Throws NullPointerException if version is null.
    }

    /**
     * Returns a value indicating whether Windows Media Player reserves space for a banner graphic.
     * A banner is a graphic that is displayed in the video display area while media content is playing
     * (use the BANNER element to add a banner to the content).
     * <br>
     * Must be one of the following values:
     * <dl>
     * <dt>AUTO</dt><dd>The default value. Windows Media Player reserves space for the banner bar only when a piece of content includes one.</dd>
     * <dt>FIXED</dt><dd>Windows Media Player reserves a fixed space for a banner graphic for every piece of content played, whether or not there is an associated banner.</dd>
     * </dl>
     * If the value of BANNERBAR is FIXED, Windows Media Player reserves banner space for every piece of media content, whether or not the media content has a banner.
     * If a piece of media content does not have a banner associated with it, the space reserved for one is black.
     * If the value of the BANNERBAR attribute is AUTO, Windows Media Player reserves space for the banner only when the media content includes one.
     * <br>
     * If you create a metafile with multiple clips (ENTRY or ENTRYREF elements) and set the value of the BANNERBAR attribute to AUTO,
     * Windows Media Player might resize to allow space for a banner graphic for one clip,
     * and then resize again if the next clip does not contain a banner graphic.
     * If you want the size of the window to stay the same (except when the video size changes), use the FIXED value for the BANNERBAR attribute.
     * <br>
     * The space reserved for a banner graphic is 32 pixels high by 194 pixels wide.
     * The reserved space appears below any rendered video content and 6 pixels above the lower edge of the video area,
     * allowing space for the 6-pixel video area border.
     * The reserved banner space is centered horizontally.
     * <br>
     * Windows Media Player renders the graphic beginning in the leftmost pixel of the banner space.
     * If the graphic fills the entire space, it will appear centered horizontally.
     * Otherwise there will be trailing space.
     * Note that the minimum width of Windows Media Player is always wider than the size of the video clip,
     * regardless of the value of the BANNERBAR attribute. 
     * <br>
     * As banners are not supported in Windows CE, this attribute is set to "AUTO" and any attempts to change it are ignored.
     * Even if content includes a banner, no space is reserved for it.
     * @return a banner bar indicator. May be <code>null</code>.
     * @see #setBannerBar
     * @castor.field
     *  get-method="getBannerBar"
     *  set-method="setBannerBar"
     * @castor.field-xml
     *  name="bannerbar"
     *  node="attribute"
     */
    public String getBannerBar()
    {
        return _bannerBar;
    }

    /**
     * Initializes the value indicating whether Windows Media Player reserves space for a banner graphic.
     * @param bannerBar a banner bar indicator. May be <code>null</code>.
     * @see #getBannerBar
     */
    public void setBannerBar(final String bannerBar)
    {
        final String banner = StringUtils.normalize(bannerBar);

        _bannerBar = "FIXED".equalsIgnoreCase(banner) ? "FIXED" : null;
    }

    /**
     * Returns a value indicating whether Windows Media Player enters preview mode before playing the first clip.
     * Must be one of the following values:
     * <dl>
     * <dt>YES</dt><dd>Windows Media Player enters preview mode before playing the first clip.</dd>
     * <dt>NO</dt><dd>The default value. Windows Media Player does not enter preview mode before playing the first clip.</dd>
     * </dl>
     * If the value of the PREVIEWMODE attribute is YES, Windows Media Player immediately enters preview mode before playing the first clip.
     * When Windows Media Player enters preview mode, it previews each clip referenced in the metafile.
     * The PREVIEWDURATION element determines the duration of each preview.
     * As preview mode is not supported in Microsoft&reg; Windows&reg; CE .NET, this attribute is set to "NO" and any attempts to change it are ignored.
     * @return the preview mode. May be <code>null</code>.
     * @see #setPreviewModeString
     * @see #isPreviewMode
     * @castor.field
     *  get-method="getPreviewModeString"
     *  set-method="setPreviewModeString"
     * @castor.field-xml
     *  name="previewmode"
     *  node="attribute"
     */
    public String getPreviewModeString()
    {
        String ret = null;

        if (_previewMode)
        {
            ret = "YES";
        }

        return ret;
    }

    /**
     * Initializes the value indicating whether Windows Media Player enters preview mode before playing the first clip.
     * @param previewMode a preview mode. May be <code>null</code>.
     * @see #getPreviewModeString
     * @see #setPreviewMode
     */
    public void setPreviewModeString(final String previewMode)
    {
        final String preview = StringUtils.normalize(previewMode);

        _previewMode = "YES".equalsIgnoreCase(preview);
    }

    /**
     * Indicates whether Windows Media Player enters preview mode before playing the first clip.
     * @return the preview mode.
     * @see #setPreviewMode
     * @see #getPreviewModeString
     */
    public boolean isPreviewMode()
    {
        return _previewMode;
    }

    /**
     * Indicates whether Windows Media Player enters preview mode before playing the first clip.
     * @param previewMode the preview mode.
     * @see #isPreviewMode
     * @see #setPreviewModeString
     */
    public void setPreviewMode(final boolean previewMode)
    {
        _previewMode = previewMode;
    }

    @Override
    public void addAsxElement(final AsxElement asxElement)
    {
        if (asxElement instanceof Asx)  // Throws NullPointerException if asxElement is null.
        {
            throw new IllegalStateException("Element not valid here: " + asxElement);
        }

        asxElement.setParent(this);
        _asxElements.add(asxElement);
    }

    /**
     * @castor.field
     *  get-method="getAsxElements"
     *  set-method="addAsxElement"
     *  type="christophedelory.playlist.asx.AsxElement"
     *  collection="arraylist"
     * @castor.field-xml
     *  auto-naming="deriveByClass"
     *  node="element"
     */
    @Override
    public List<AsxElement> getAsxElements()
    {
        return _asxElements;
    }
}
