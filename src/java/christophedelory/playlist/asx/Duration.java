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

import christophedelory.lang.StringUtils;

/**
 * Defines the length of time the stream must be rendered.
 * This element can appear either within a REF element or within an ENTRY element.
 * However, a DURATION element defined within a REF element overrides one that appears within the REF element's parent ENTRY element.
 * <br>
 * The DURATION element overrides a PREVIEWDURATION element.
 * <br>
 * If the value exceeds the length of the content stream, the stream terminates at its normal end-point.
 * The default value is the entire length of the content stream or media.
 * If the entry is a graphic file, a duration value must be specified.
 * <br>
 * Windows Media Player version 7.0 or later.
 * @version $Revision: 92 $
 * @author Christophe Delory
 * @castor.class xml="duration"
 */
public class Duration extends Child
{
    /**
     * The duration value in milliseconds.
     */
    private long _value = 0L;

    /**
     * Returns the duration value as a string in hours, minutes, seconds, and hundredths of a second.
     * Format: "<code>hh:mm:ss.fract</code>".
     * @return a duration value. Shall not be <code>null</code>.
     * @see #setValueString
     * @see #getValue
     * @castor.field
     *  get-method="getValueString"
     *  set-method="setValueString"
     *  required="true"
     * @castor.field-xml
     *  name="value"
     *  node="attribute"
     */
    public String getValueString()
    {
        final StringBuilder sb = new StringBuilder();
        long millis = _value;

        // Hours.
        long i = millis / (60L * 60L * 1000L);

        //if (i > 0L)
        {
            sb.append(StringUtils.toString(i, 2));
            sb.append(':');
            millis -= i * 60L * 60L * 1000L;
        }

        // Minutes.
        i = millis / (60L * 1000L);

        //if (i > 0L)
        {
            sb.append(StringUtils.toString(i, 2));
            sb.append(':');
            millis -= i * 60L * 1000L;
        }

        // Seconds.
        i = millis / 1000L;
        sb.append(StringUtils.toString(i, 2));
        millis -= i * 1000L;

        // Hundredths of seconds.
        if (millis > 0L)
        {
            sb.append('.');
            sb.append(StringUtils.toString(millis, 3));
        }

        return sb.toString();
    }

    /**
     * Initializes the duration value as a string.
     * @param value a duration value. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>value</code> is <code>null</code>.
     * @throws IllegalArgumentException if the given string is malformed.
     * @throws IllegalArgumentException if the resulting number of milliseconds is negative.
     * @throws NumberFormatException if the string does not contain parsable values.
     * @see #getValueString
     * @see #setValue
     */
    public void setValueString(final String value)
    {
        final String[] array = value.trim().split(":"); // Throws NullPointerException if value is null. Should not throw PatternSyntaxException.

        if (array.length != 3)
        {
            throw new IllegalArgumentException("Invalid duration format " + value);
        }

        final long hours = Long.parseLong(array[0]); // May throw NumberFormatException.

        if (hours < 0L)
        {
            throw new IllegalArgumentException("Negative hours");
        }

        final long minutes = Long.parseLong(array[1]); // May throw NumberFormatException.

        if ((minutes < 0L) || (minutes > 59L))
        {
            throw new IllegalArgumentException("Invalid minutes");
        }

        final String[] subArray = array[2].split("\\."); // Should not throw PatternSyntaxException.

        if (subArray.length > 2)
        {
            throw new IllegalArgumentException("Invalid duration format " + value);
        }

        final long seconds = Long.parseLong(subArray[0]); // May throw NumberFormatException.

        if ((seconds < 0L) || (seconds > 59L))
        {
            throw new IllegalArgumentException("Invalid seconds");
        }

        long millis = 0L;

        if (subArray.length > 1)
        {
            final StringBuilder sb = new StringBuilder(subArray[1]);

            switch (sb.length())
            {
                case 1:
                    sb.append("00");
                    break;
                case 2:
                    sb.append('0');
                    break;
                case 3:
                    break;
                default:
                    sb.delete(3, sb.length()); // Shall not throw StringIndexOutOfBoundsException.
                    break;
            }

            millis = Long.parseLong(sb.toString()); // May throw NumberFormatException.

            if (millis < 0L)
            {
                throw new IllegalArgumentException("Negative milliseconds");
            }
        }

        setValue((hours * 60L * 60L * 1000L) + (minutes * 60L * 1000L) + (seconds * 1000L) + millis); // May throw IllegalArgumentException.
    }

    /**
     * Initializes the duration value in milliseconds.
     * @param millis a duration value.
     * @throws IllegalArgumentException if <code>millis</code> is negative.
     * @see #getValue
     * @see #setValueString
     */
    public void setValue(final long millis)
    {
        if (millis < 0L)
        {
            throw new IllegalArgumentException("Negative milliseconds duration " + millis);
        }

        _value = millis;
    }

    /**
     * Returns the duration value in milliseconds.
     * @return a duration value.
     * @see #setValue
     * @see #getValueString
     */
    public long getValue()
    {
        return _value;
    }
}
