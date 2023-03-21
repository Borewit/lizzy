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
package io.github.borewit.lizzy.playlist.asx;

import io.github.borewit.lizzy.content.Content;
import io.github.borewit.lizzy.playlist.AbstractPlaylist;
import io.github.borewit.lizzy.playlist.Media;
import io.github.borewit.lizzy.playlist.Playlist;
import io.github.borewit.lizzy.playlist.Sequence;
import io.github.borewit.lizzy.playlist.xml.asx.*;

import java.io.IOException;
import java.io.OutputStream;

/**
 * A Windows Media metafile.
 * Defines a file as an Advanced Stream Redirector (ASX) file.
 *
 * @author Borewit
 * @author Christophe Delory
 */
public class AsxPlaylistAdapter extends AbstractPlaylist
{
  /**
   * The provider of this specific playlist.
   */
  private final AsxProvider provider;

  /**
   * JAXB component
   */
  private final Asx asx;

  private static Asx makeAsx()
  {
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

    this.asx.getENTRYOrENTRYREF().forEach(entry -> addToSequence(entry, ret.getRootSequence()));
    this.asx.getREPEAT().forEach(entry -> addRepeatToSequence(entry, ret.getRootSequence()));

    ret.normalize();
    return ret;
  }


  /**
   * Adds the ASX "ENTRY element", to the given sequence.
   *
   * @param entryOrEntryRef the ASX "ENTRY or "ENTRYREF" element" to add. Shall not be <code>null</code>.
   * @param currentSequence the parent sequence. Shall not be <code>null</code>.
   * @throws NullPointerException if <code>asxElement</code> is <code>null</code>.
   * @throws NullPointerException if <code>currentSequence</code> is <code>null</code>.
   */
  private void addToSequence(final Object entryOrEntryRef, final Sequence currentSequence)
  {
    if (entryOrEntryRef instanceof ENTRY)
    {
      ENTRY entry = (ENTRY) entryOrEntryRef;

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
    else if (entryOrEntryRef instanceof RefElement)
    {
      // ASX ENTRYREF element
      RefElement entryRef = (RefElement) entryOrEntryRef;
      final Media media = new Media();
      media.setSource(new Content(entryRef.getHREF()));
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
  private void addRepeatToSequence(final REPEAT repeat, final Sequence currentSequence)
  {
    final Sequence repeatSeq = new Sequence();
    repeatSeq.setRepeatCount((repeat.getCOUNT() == null) ? 1.0f : (repeat.getCOUNT() + 1));
    currentSequence.addComponent(repeatSeq);
    repeat.getENTRYOrENTRYREF().forEach(asxElem -> {
      addToSequence(asxElem, repeatSeq);
    });
  }

}
