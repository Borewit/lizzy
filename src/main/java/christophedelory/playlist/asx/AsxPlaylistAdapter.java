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

import christophedelory.content.Content;
import christophedelory.playlist.*;
import io.github.borewit.playlist.asx.*;

import java.io.IOException;
import java.io.OutputStream;

/**
 * A Windows Media metafile.
 * Defines a file as an Advanced Stream Redirector (ASX) file.
 *
 * @author Borewit
 * @author Christophe Delory

 */
public class AsxPlaylistAdapter implements SpecificPlaylist
{
    /**
     * The provider of this specific playlist.
     */
    private final AsxProvider provider;

    /**
     * JAXB component
     */
    private final Asx asx;

    private static Asx makeAsx() {
        Asx asx = new Asx();
        asx.setVERSION("3.0");
        return asx;
    }

    public AsxPlaylistAdapter(AsxProvider provider)
    {
        this(provider, makeAsx());
    }

    public AsxPlaylistAdapter(AsxProvider provider, Asx asx)
    {
        this.provider = provider;
        this.asx = asx;
    }

    public Asx getAsx()
    {
        return this.asx;
    }

    @Override
    public AsxProvider getProvider()
    {
        return provider;
    }

    @Override
    public void writeTo(final OutputStream out, final String encoding) throws IOException
    {
        this.provider.writeTo(this.asx, out, encoding);
    }

    @Override
    public Playlist toPlaylist()
    {
        final Playlist ret = new Playlist();

        this.asx.getENTRY().forEach(entry -> addToSequence(entry, ret.getRootSequence()));
        this.asx.getENTRYREF().forEach(entry -> addToSequence(entry, ret.getRootSequence()));
        this.asx.getREPEAT().forEach(entry -> addToSequence(entry, ret.getRootSequence()));

        ret.normalize();
        return ret;
    }


    /**
     * Adds the ASX "ENTRY element", to the given sequence.
     *
     * @param entry           the ASX "ENTRY element" to add. Shall not be <code>null</code>.
     * @param currentSequence the parent sequence. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>asxElement</code> is <code>null</code>.
     * @throws NullPointerException if <code>currentSequence</code> is <code>null</code>.
     */
    private void addToSequence(final EntryElement entry, final Sequence currentSequence)
    {

        // We keep only the first valid one.
        if (entry.getREF() != null && entry.getREF().getHREF() != null)
        {
            final Media media = new Media(); // NOPMD Avoid instantiating new objects inside loops
            media.setSource(new Content(entry.getREF().getHREF())); // NOPMD Avoid instantiating new objects inside loops

            DurationElement duration = entry.getDURATION();

            if (duration == null)
            {
                duration = entry.getDURATION();
            }

            // ToDO process duration


            currentSequence.addComponent(media);
        }

    }

    /**
     * Adds the specified ASX element, and its childs if appropriate, to the given sequence.
     *
     * @param repeat          the ASX "REPEAT Element" to handle. Shall not be <code>null</code>.
     * @param currentSequence the parent sequence. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>asxElement</code> is <code>null</code>.
     * @throws NullPointerException if <code>currentSequence</code> is <code>null</code>.
     */
    private void addToSequence(final RepeatElement repeat, final Sequence currentSequence)
    {
        final Sequence seq = new Sequence();
        seq.setRepeatCount((repeat.getCOUNT() == null) ? 1.0f : (repeat.getCOUNT() + 1));
        currentSequence.addComponent(seq);

        for (Object asxElem : repeat.getENTRYOrENTRYREF())
        {
            if (asxElem instanceof EntryElement)
            {
                addToSequence((EntryElement) asxElem, seq);
            }

            if (asxElem instanceof EntryrefElement)
            {
                addToSequence((EntryrefElement) asxElem, seq);
            }
        }
    }

    /**
     * Adds the ASX "ENTRYREF Element" to the given sequence.
     *
     * @param entryRef        the ASX "ENTRYREF Element" to handle. Shall not be <code>null</code>.
     * @param currentSequence the parent sequence. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>asxElement</code> is <code>null</code>.
     * @throws NullPointerException if <code>currentSequence</code> is <code>null</code>.
     */
    private void addToSequence(final EntryrefElement entryRef, final Sequence currentSequence)
    {
        if (entryRef.getHREF() != null)
        {
            final Media media = new Media();
            media.setSource(new Content(entryRef.getHREF()));
            currentSequence.addComponent(media);
        }
    }
}
