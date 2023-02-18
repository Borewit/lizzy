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

import com.sun.jna.ptr.PointerByReference;

import net.sf.ffmpeg_java.AVCodecLibrary;
import net.sf.ffmpeg_java.AVFormatLibrary;
import net.sf.ffmpeg_java.AVCodecLibrary.AVCodecContext;
import net.sf.ffmpeg_java.AVFormatLibrary.AVFormatContext;
import net.sf.ffmpeg_java.AVFormatLibrary.AVStream;

/**
 * A content metadata provider based on a <a href="http://ffmpeg.mplayerhq.hu/">FFMPEG</a> Java binding, <a href="http://fmj-sf.net/ffmpeg-java/getting_started.php">FFMPEG-Java</a>.
 * @version $Revision: 91 $
 * @author Christophe Delory
 * @since 1.0.0
 */
public class FFMPEGMetadataProvider implements ContentMetadataProvider
{
    /**
     * The main AVFormat instance.
     */
    private transient AVFormatLibrary _avFormatLibrary = null;

    @Override
    public void fillMetadata(final Content content, final Log logger) throws Exception
    {
        final String filename = content.toString(); // Throws NullPointerException if content is null.

        synchronized(this)
        {
            if (_avFormatLibrary == null)
            {
                final AVFormatLibrary lib = AVFormatLibrary.INSTANCE;
                lib.av_register_all();
                _avFormatLibrary = lib;
            }
        }

        // Open video file.
        final PointerByReference ppFormatCtx = new PointerByReference();

        if (_avFormatLibrary.av_open_input_file(ppFormatCtx, filename, null, 0, null) != 0)
        {
            throw new RuntimeException("Couldn't open " + filename);
        }

        final AVFormatContext formatCtx = new AVFormatContext(ppFormatCtx.getValue());

        // Retrieve stream information.
        if (_avFormatLibrary.av_find_stream_info(formatCtx) < 0)
        {
            throw new RuntimeException("Couldn't find stream information in " + filename);
        }

        //_avFormatLibrary.dump_format(formatCtx, 0, filename, 0);

        if (formatCtx.bit_rate > 0)
        {
            content.setDuration((formatCtx.duration + 999L) / 1000L);
        }
        else
        {
            content.setDuration(0L);
        }

        content.setWidth(0);
        content.setHeight(0);

        // Find the first video stream.
        for (int i = 0; i < formatCtx.nb_streams; i++)
        {
            final AVStream stream = new AVStream(formatCtx.getStreams()[i]); // NOPMD Avoid instantiating new objects inside loops
            final AVCodecContext codecCtx = new AVCodecContext(stream.codec); // NOPMD Avoid instantiating new objects inside loops

            if (codecCtx.codec_type == AVCodecLibrary.CODEC_TYPE_VIDEO)
            {
                content.setWidth(codecCtx.width);
                content.setHeight(codecCtx.height);
                break;
            }
        }

        // Close the video file.
        _avFormatLibrary.av_close_input_file(formatCtx);
    }
}
