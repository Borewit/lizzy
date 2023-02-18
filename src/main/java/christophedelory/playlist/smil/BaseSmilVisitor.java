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

/**
 * A starting point for a {@link Smil SMIL} playlist visitor.
 * @version $Revision: 90 $
 * @author Christophe Delory
 */
public class BaseSmilVisitor implements SmilVisitor
{
    @Override
    public void beginVisitSmil(final Smil target)
    {
        // No-op.
    }

    @Override
    public void endVisitSmil(final Smil target)
    {
        // No-op.
    }

    @Override
    public void beginVisitHead(final Head target)
    {
        // No-op.
    }

    @Override
    public void endVisitHead(final Head target)
    {
        // No-op.
    }

    @Override
    public void beginVisitLayout(final Layout target)
    {
        // No-op.
    }

    @Override
    public void endVisitLayout(final Layout target)
    {
        // No-op.
    }

    @Override
    public void beginVisitRootLayout(final RootLayout target)
    {
        // No-op.
    }

    @Override
    public void endVisitRootLayout(final RootLayout target)
    {
        // No-op.
    }

    @Override
    public void beginVisitRegion(final Region target)
    {
        // No-op.
    }

    @Override
    public void endVisitRegion(final Region Region)
    {
        // No-op.
    }

    @Override
    public void beginVisitBody(final Body target)
    {
        // No-op.
    }

    @Override
    public void endVisitBody(final Body target)
    {
        // No-op.
    }

    @Override
    public void beginVisitParTimingElement(final ParallelTimingElement target)
    {
        // No-op.
    }

    @Override
    public void endVisitParTimingElement(final ParallelTimingElement target)
    {
        // No-op.
    }

    @Override
    public void beginVisitSeqTimingElement(final SequentialTimingElement target)
    {
        // No-op.
    }

    @Override
    public void endVisitSeqTimingElement(final SequentialTimingElement target)
    {
        // No-op.
    }

    @Override
    public void beginVisitExclTimingElement(final ExclusiveTimingElement target)
    {
        // No-op.
    }

    @Override
    public void endVisitExclTimingElement(final ExclusiveTimingElement target)
    {
        // No-op.
    }

    @Override
    public void beginVisitReference(final Reference target)
    {
        // No-op.
    }

    @Override
    public void endVisitReference(final Reference target)
    {
        // No-op.
    }
}
