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

import java.io.OutputStream;

import christophedelory.content.Content;
import christophedelory.playlist.AbstractTimeContainer;
import christophedelory.playlist.Media;
import christophedelory.playlist.Parallel;
import christophedelory.playlist.Playlist;
import christophedelory.playlist.Sequence;
import christophedelory.playlist.SpecificPlaylist;
import io.github.borewit.playlist.smil20.*;

/**
 * Acts as the root element for all SMIL Host Language conformant language profiles.
 * @author Borewit
 * @author Christophe Delory
 */
public class SmilAdapter implements SpecificPlaylist
{
    /**
     * The provider of this specific playlist.
     */
    private final SmilProvider provider;

    private final Smil smil;

    public SmilAdapter(final SmilProvider provider, Smil smil)
    {
        this.provider = provider;
        this.smil = smil;
    }

    @Override
    public SmilProvider getProvider()
    {
        return provider;
    }

    @Override
    public void writeTo(final OutputStream out, final String encoding) throws Exception
    {
        this.provider.writeTo(this.smil, out, encoding);
    }

    @Override
    public Playlist toPlaylist()
    {
        final Playlist ret = new Playlist();

        SmilContainerBody body = this.smil.getBody();

        if (body != null)
        {
            body.getSeqOrParOrExl()
                .forEach(timingElement -> {
                    addToContainer(timingElement, ret.getRootSequence());
                });

            ret.normalize();
        }

        return ret;
    }

    /**
     * Adds the specified SMIL element, and its childs if appropriate, to the given timing container.
     * @param timeContainer the SMIL element to handle. Shall not be <code>null</code>.
     * @param currentContainer the parent timing container. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>smilElement</code> is <code>null</code>.
     * @throws NullPointerException if <code>currentContainer</code> is <code>null</code>.
     */
    private void addToContainer(final SmilTimeContainer timeContainer, final AbstractTimeContainer currentContainer)
    {
        timeContainer.getAudioOrImgOrRef().stream()
            .map(SmilReference::getSrc)
            .map(Content::new)
            .map(Media::new)
            .forEach(currentContainer::addComponent);

        AbstractTimeContainer abstractTimeContainer;
        if (timeContainer instanceof SmilSequence)
        {
            abstractTimeContainer = new Sequence();
        }
        else if (timeContainer instanceof SmilParallel)
        {
            abstractTimeContainer = new Parallel();
        }
        else if (timeContainer instanceof SmilMutuallyExclusive)
        {
            // TODO What can we do with an ExclusiveTimingElement? More complicated than at first sight...
            return;
        }
        else return;

        final Integer repeatCount = timeContainer.getRepeatCount();

        if (repeatCount != null)
        {
            abstractTimeContainer.setRepeatCount(repeatCount);
        }
        currentContainer.addComponent(abstractTimeContainer);

        // Recursion
        timeContainer.getSeqOrParOrExl()
            .forEach(timingElement -> {
                addToContainer(timingElement, currentContainer);
            });


    }
}
