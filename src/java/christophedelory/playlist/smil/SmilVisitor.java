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
 * A {@link Smil SMIL} hierarchy visitor.
 * @see Smil#acceptDown
 * @see Head#acceptDown
 * @see Layout#acceptDown
 * @see RootLayout#acceptDown
 * @see Region#acceptDown
 * @see Body#acceptDown
 * @see ParallelTimingElement#acceptDown
 * @see SequentialTimingElement#acceptDown
 * @see ExclusiveTimingElement#acceptDown
 * @see Reference#acceptDown
 * @version $Revision: 92 $
 * @author Christophe Delory
 */
public interface SmilVisitor
{
    /**
     * Starts the visit of the specified SMIL playlist.
     * @param target the SMIL playlist being visited. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>target</code> is <code>null</code>.
     * @see #endVisitSmil
     */
    void beginVisitSmil(final Smil target);

    /**
     * Finishes the visit of the specified SMIL playlist.
     * @param target the SMIL playlist being visited. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>target</code> is <code>null</code>.
     * @see #beginVisitSmil
     */
    void endVisitSmil(final Smil target);

    /**
     * Starts the visit of the specified header.
     * @param target the header being visited. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>target</code> is <code>null</code>.
     * @see #endVisitHead
     */
    void beginVisitHead(final Head target);

    /**
     * Finishes the visit of the specified head element.
     * @param target the head element being visited. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>target</code> is <code>null</code>.
     * @see #beginVisitHead
     */
    void endVisitHead(final Head target);

    /**
     * Starts the visit of the specified layout.
     * @param target the layout being visited. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>target</code> is <code>null</code>.
     * @see #endVisitLayout
     */
    void beginVisitLayout(final Layout target);

    /**
     * Finishes the visit of the specified layout.
     * @param target the layout being visited. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>target</code> is <code>null</code>.
     * @see #beginVisitLayout
     */
    void endVisitLayout(final Layout target);

    /**
     * Starts the visit of the specified root layout.
     * @param target the root layout being visited. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>target</code> is <code>null</code>.
     * @see #endVisitRootLayout
     */
    void beginVisitRootLayout(final RootLayout target);

    /**
     * Finishes the visit of the specified root layout element.
     * @param target the root layout being visited. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>target</code> is <code>null</code>.
     * @see #beginVisitRootLayout
     */
    void endVisitRootLayout(final RootLayout target);

    /**
     * Starts the visit of the specified region.
     * @param target the region being visited. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>target</code> is <code>null</code>.
     * @see #endVisitRegion
     */
    void beginVisitRegion(final Region target);

    /**
     * Finishes the visit of the specified Region element.
     * @param Region the Region being visited. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>target</code> is <code>null</code>.
     * @see #beginVisitRegion
     */
    void endVisitRegion(final Region Region);

    /**
     * Starts the visit of the specified body.
     * @param target the body being visited. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>target</code> is <code>null</code>.
     * @see #endVisitBody
     */
    void beginVisitBody(final Body target);

    /**
     * Finishes the visit of the specified body.
     * @param target the body being visited. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>target</code> is <code>null</code>.
     * @see #beginVisitBody
     */
    void endVisitBody(final Body target);

    /**
     * Starts the visit of the specified parallel time container.
     * @param target the parallel time container being visited. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>target</code> is <code>null</code>.
     * @see #endVisitParTimingElement
     */
    void beginVisitParTimingElement(final ParallelTimingElement target);

    /**
     * Finishes the visit of the specified parallel time container.
     * @param target the parallel time container being visited. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>target</code> is <code>null</code>.
     * @see #beginVisitParTimingElement
     */
    void endVisitParTimingElement(final ParallelTimingElement target);

    /**
     * Starts the visit of the specified sequence.
     * @param target the sequence being visited. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>target</code> is <code>null</code>.
     * @see #endVisitSeqTimingElement
     */
    void beginVisitSeqTimingElement(final SequentialTimingElement target);

    /**
     * Finishes the visit of the specified sequence.
     * @param target the sequence being visited. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>target</code> is <code>null</code>.
     * @see #beginVisitSeqTimingElement
     */
    void endVisitSeqTimingElement(final SequentialTimingElement target);

    /**
     * Starts the visit of the specified exclusive time container.
     * @param target the exclusive time container being visited. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>target</code> is <code>null</code>.
     * @see #endVisitExclTimingElement
     */
    void beginVisitExclTimingElement(final ExclusiveTimingElement target);

    /**
     * Finishes the visit of the specified exclusive time container.
     * @param target the exclusive time container being visited. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>target</code> is <code>null</code>.
     * @see #beginVisitExclTimingElement
     */
    void endVisitExclTimingElement(final ExclusiveTimingElement target);

    /**
     * Starts the visit of the specified SMIL reference.
     * @param target the SMIL reference being visited. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>target</code> is <code>null</code>.
     * @see #endVisitReference
     */
    void beginVisitReference(final Reference target);

    /**
     * Finishes the visit of the specified SMIL reference.
     * @param target the SMIL reference being visited. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>target</code> is <code>null</code>.
     * @see #beginVisitReference
     */
    void endVisitReference(final Reference target);
}
