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
package christophedelory.content;

import org.apache.commons.logging.Log;

import org.videolan.jvlc.JVLC;
import org.videolan.jvlc.MediaDescriptor;
import org.videolan.jvlc.MediaPlayer;
import org.videolan.jvlc.Video;

/**
 * A content metadata provider based on a <a href="http://www.videolan.org/">VideoLAN VLC</a> Java binding, <a href="http://trac.videolan.org/jvlc/wiki">JVLC</a>.
 * @deprecated Unable to make it work at the moment...
 * @version $Revision: 90 $
 * @author Christophe Delory
 * @since 1.0.0
 */
@Deprecated
public class VLCMetadataProvider implements ContentMetadataProvider
{
    /**
     * The main VLC instance.
     */
    private transient JVLC _jvlc = null;

    @Override
    public void fillMetadata(final Content content, final Log logger) throws Exception
    {
        synchronized(this)
        {
            if (_jvlc == null)
            {
                _jvlc = new JVLC(new String[] { "-vvv", /*"--ignore-config", "--no-media-library",*/ });
                //_jvlc.release();
            }
        }

        final MediaDescriptor mediaDescriptor = new MediaDescriptor(_jvlc, content.toString());
        final MediaPlayer mediaPlayer = mediaDescriptor.getMediaPlayer();

        content.setDuration(mediaPlayer.getLength()); // Throws NullPointerException if content is null.

        final Video video = new Video(_jvlc);
        content.setWidth(video.getWidth(mediaPlayer));
        content.setHeight(video.getHeight(mediaPlayer));
        video.destroyVideo(mediaPlayer); // FIXME Sure of that?

        mediaDescriptor.release();
    }
}
