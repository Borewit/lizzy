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

import java.net.URL;

import javax.sound.midi.MidiFileFormat;
import javax.sound.midi.MidiSystem;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.commons.logging.Log;

/**
 * A content metadata provider based on the <a href="http://java.sun.com/products/java-media/sound/index.jsp">Java Sound API</a>.
 * WAV, AU, AIFF, MIDI and RMF formats are supported.
 * @version $Revision: 90 $
 * @author Christophe Delory
 * @since 1.0.0
 */
public class SoundMetadataProvider implements ContentMetadataProvider
{
    @Override
    public void fillMetadata(final Content content, final Log logger) throws Exception
    {
        final URL url = content.getURL(); // Throws NullPointerException if content is null. May throw SecurityException, IllegalArgumentException, MalformedURLException.

        try
        {
            final AudioFileFormat audioFileFormat = AudioSystem.getAudioFileFormat(url); // May throw UnsupportedAudioFileException, IOException.

            content.setWidth(0);
            content.setHeight(0);

            final AudioFormat audioFormat = audioFileFormat.getFormat();

            if ((audioFileFormat.getFrameLength() == AudioSystem.NOT_SPECIFIED) || (audioFormat.getSampleRate() == (float) AudioSystem.NOT_SPECIFIED))
            {
                logger.debug("Unknown audio duration");
            }
            else
            {
                content.setDuration((long)(((float) audioFileFormat.getFrameLength() * 1000.0f) / audioFormat.getSampleRate()));
            }

            return;
        }
        catch (UnsupportedAudioFileException e)
        {
            // Try the next format.
            logger.debug(e.toString());
        }

        final MidiFileFormat midiFormat = MidiSystem.getMidiFileFormat(url); // May throw InvalidMidiDataException, IOException.

        content.setWidth(0);
        content.setHeight(0);

        final long duration = midiFormat.getMicrosecondLength();

        if (duration == MidiFileFormat.UNKNOWN_LENGTH)
        {
            logger.debug("Unknown MIDI duration");
        }
        else
        {
            content.setDuration((duration + 999L) / 1000L);
        }
    }
}
