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

import christophedelory.lang.StringUtils;

/**
 * The media object elements.
 * @version $Revision: 92 $
 * @author Christophe Delory
 * @castor.class xml="element" verify-constructable="false"
 */
public abstract class AbstractSmilElement extends Core
{
    /**
     *
     */
    private static final String INDEFINITE = "indefinite";

    /**
     * The parent of this element.
     */
    private transient AbstractSmilElement _parent = null;

    /**
     * The begin time of this element.
     */
    private String _begin = null;

    /**
     * The end time of this element.
     */
    private String _end = null;

    /**
     * A simple duration.
     */
    private Long _duration = null;

    /**
     * Specifies the number of iterations of the simple duration.
     */
    private Float _repeatCount = null;

    /**
     * Specifies the total duration for repeat.
     */
    private String _repeatDuration = null;

    /**
     * Specifies the minimum value of the active duration.
     */
    private String _min = null;

    /**
     * Specifies the maximum value of the active duration.
     */
    private String _max = null;

    /**
     * The region associated to this element.
     */
    private String _region = null;

    /**
     * Returns the parent of this element.
     * @return a SMIL element.
     * @see #setParent
     */
    public AbstractSmilElement getParent()
    {
        return _parent;
    }

    /**
     * Initializes the parent of this element.
     * @param parent a SMIL element.
     * @see #getParent
     */
    void setParent(final AbstractSmilElement parent)
    {
        _parent = parent;
    }

    /**
     * Returns the identifier of the region associated to this element.
     * If this specific element does not define a region identifier, the parent element is scanned.
     * @return a region identifier. May be <code>null</code>.
     * @see #setRegionString
     * @castor.field
     *  get-method="getRegionString"
     *  set-method="setRegionString"
     * @castor.field-xml
     *  name="region"
     *  node="attribute"
     */
    public String getRegionString()
    {
        String ret = null;

        if (_region != null) // NOPMD Avoid if (x != y) ..; else ..;
        {
            ret = _region;
        }
        else if (_parent != null)
        {
            ret = _parent.getRegionString();
        }

        return ret;
    }

    /**
     * Initializes the region identifier.
     * @param region a region identifier. May be <code>null</code>.
     * @see #getRegionString
     */
    public void setRegionString(final String region)
    {
        _region = StringUtils.normalize(region);
    }

    /**
     * Returns the number of iterations of the simple duration, as a string.
     * Could be equal to a numeric value or "indefinite".
     * @return a repeat count. May be <code>null</code>.
     * @see #setRepeatCountString
     * @see #getRepeatCount
     * @castor.field
     *  get-method="getRepeatCountString"
     *  set-method="setRepeatCountString"
     * @castor.field-xml
     *  name="repeatCount"
     *  node="attribute"
     */
    public String getRepeatCountString()
    {
        String ret = null;

        if (_repeatCount != null)
        {
            if (_repeatCount.intValue() < 0)
            {
                ret = INDEFINITE;
            }
            else
            {
                ret = _repeatCount.toString();
            }
        }

        return ret;
    }

    /**
     * Initializes the number of iterations of the simple duration, as a string.
     * @param repeatCount a repeat count. May be <code>null</code>.
     * @throws NumberFormatException if the string does not contain parsable values.
     * @see #getRepeatCountString
     * @see #setRepeatCount
     */
    public void setRepeatCountString(final String repeatCount)
    {
        if (repeatCount == null)
        {
            _repeatCount = null;
        }
        else
        {
            final String str = repeatCount.trim();

            if (INDEFINITE.equalsIgnoreCase(str))
            {
                _repeatCount = Float.valueOf(-1f);
            }
            else
            {
                setRepeatCount(Float.valueOf(str)); // May throw NumberFormatException.
            }
        }
    }

    /**
     * Returns the number of iterations of the simple duration.
     * @return a repeat count. May be <code>null</code>.
     * @see #setRepeatCount
     * @see #getRepeatCountString
     */
    public Float getRepeatCount()
    {
        return _repeatCount;
    }

    /**
     * Initializes the number of iterations of the simple duration.
     * @param repeatCount a repeat count. May be <code>null</code>.
     * @see #getRepeatCount
     * @see #setRepeatCountString
     */
    public void setRepeatCount(final Float repeatCount)
    {
        if (repeatCount == null)
        {
            _repeatCount = null;
        }
        else
        {
            final int i = repeatCount.intValue();

            if (i == 1)
            {
                _repeatCount = null;
            }
            else if (i < 0)
            {
                _repeatCount = Float.valueOf(-1f);
            }
            else
            {
                _repeatCount = repeatCount;
            }
        }
    }

    /**
     * Returns the total duration for repeat as a string.
     * Could be equal to a clock value or "indefinite".
     * @return the total duration for repeat as a string. May be <code>null</code>.
     * @see #setRepeatDuration
     * @castor.field
     *  get-method="getRepeatDuration"
     *  set-method="setRepeatDuration"
     * @castor.field-xml
     *  name="repeatDur"
     *  node="attribute"
     */
    public String getRepeatDuration()
    {
        return _repeatDuration;
    }

    /**
     * Initializes the total duration for repeat as a string.
     * @param duration the total duration for repeat. May be <code>null</code>.
     * @see #getRepeatDuration
     */
    public void setRepeatDuration(final String duration)
    {
        _repeatDuration = StringUtils.normalize(duration);
    }

    /**
     * Returns a simple duration, in milliseconds.
     * If no duration has been defined in this element, or if the duration shall be those of the media itself, <code>null</code> is returned.
     * @return a duration value. May be <code>null</code>.
     * @see #setDuration
     * @see #getDurationString
     */
    public Long getDuration()
    {
        return _duration;
    }

    /**
     * Initializes the simple duration, in milliseconds.
     * @param duration a duration. May be <code>null</code>.
     * @throws IllegalArgumentException if the specified duration is negative.
     * @see #getDuration
     * @see #setDurationString
     */
    public void setDuration(final Long duration)
    {
        if ((duration != null) && (duration.longValue() < 0L))
        {
            throw new IllegalArgumentException("Negative duration");
        }

        _duration = duration;
    }

    /**
     * Returns a simple duration as a string.
     * Could be equal to a clock value, to "media" or to "indefinite".
     * @return a duration. May be <code>null</code>.
     * @see #setDurationString
     * @see #getDuration
     * @castor.field
     *  get-method="getDurationString"
     *  set-method="setDurationString"
     * @castor.field-xml
     *  name="dur"
     *  node="attribute"
     */
    public String getDurationString()
    {
        String ret = null;

        if (_duration != null) // Thus "media" can never be generated.
        {
            if (_duration.equals(Long.MAX_VALUE))
            {
                ret = INDEFINITE;
            }
            else
            {
                final StringBuilder sb = new StringBuilder();
                long millis = _duration.longValue();

                // Hours.
                long i = millis / (60L * 60L * 1000L);

                if (i > 0L)
                {
                    sb.append(StringUtils.toString(i, 2));
                    sb.append(':');
                    millis -= i * 60L * 60L * 1000L;
                }

                // Minutes.
                i = millis / (60L * 1000L);

                if (i > 0L)
                {
                    sb.append(StringUtils.toString(i, 2));
                    sb.append(':');
                    millis -= i * 60L * 1000L;
                }

                // Seconds.
                i = millis / 1000L;

                // If the string is only composed of seconds, do not append any leading zeroes.
                String suffix;

                if (sb.length() <= 0)
                {
                    sb.append(i);
                    suffix = "s";
                }
                else
                {
                    sb.append(StringUtils.toString(i, 2));
                    suffix = "";
                }

                millis -= i * 1000L;

                // Hundredths of seconds.
                if (millis > 0L)
                {
                    sb.append('.');
                    sb.append(StringUtils.toString(millis, 3));
                }

                sb.append(suffix);

                ret = sb.toString();
            }
        }

        return ret;
    }

    /**
     * Initializes a simple duration as a string.
     * @param duration a duration. May be <code>null</code>.
     * @throws IllegalArgumentException if the given string is malformed.
     * @throws IllegalArgumentException if the specified duration is negative.
     * @throws NumberFormatException if the string does not contain parsable values.
     * @see #getDurationString
     */
    public void setDurationString(final String duration)
    {
        if (duration == null)
        {
            _duration = null;
        }
        else
        {
            final String str = duration.trim();

            if (str.equalsIgnoreCase("media"))
            {
                _duration = null;
            }
            else if (INDEFINITE.equalsIgnoreCase(str))
            {
                _duration = Long.MAX_VALUE;
            }
            else
            {
                long hours = 0L;
                long minutes = 0L;
                long seconds = 0L;
                long millis = 0L;
                final String[] array = str.split(":"); // Should not throw PatternSyntaxException.

                switch (array.length) // NOPMD A high ratio of statements to labels in a switch statement. Consider refactoring
                {
                    case 3: // Full-clock-value ::= Hours ":" Minutes ":" Seconds ("." Fraction)?
                    {
                        hours = Long.parseLong(array[0]); // May throw NumberFormatException.

                        if (hours < 0L)
                        {
                            throw new IllegalArgumentException("Negative hours");
                        }

                        minutes = Long.parseLong(array[1]); // May throw NumberFormatException.

                        if ((minutes < 0L) || (minutes > 59L))
                        {
                            throw new IllegalArgumentException("Invalid minutes");
                        }

                        final String[] subArray = array[2].split("\\."); // Should not throw PatternSyntaxException.

                        if (subArray.length > 2)
                        {
                            throw new IllegalArgumentException("Invalid duration format " + str);
                        }

                        seconds = Long.parseLong(subArray[0]); // May throw NumberFormatException.

                        if ((seconds < 0L) || (seconds > 59L))
                        {
                            throw new IllegalArgumentException("Invalid seconds");
                        }

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

                        break;
                    }

                    case 2: // Partial-clock-value ::= Minutes ":" Seconds ("." Fraction)?
                    {
                        minutes = Long.parseLong(array[0]); // May throw NumberFormatException.

                        if ((minutes < 0L) || (minutes > 59L))
                        {
                            throw new IllegalArgumentException("Invalid minutes");
                        }

                        final String[] subArray = array[1].split("\\."); // Should not throw PatternSyntaxException.

                        if (subArray.length > 2)
                        {
                            throw new IllegalArgumentException("Invalid duration format " + str);
                        }

                        seconds = Long.parseLong(subArray[0]); // May throw NumberFormatException.

                        if ((seconds < 0L) || (seconds > 59L))
                        {
                            throw new IllegalArgumentException("Invalid seconds");
                        }

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

                        break;
                    }

                    case 1: // Timecount-value ::= Timecount ("." Fraction)? (Metric)?
                    {
                        String input = array[0].toLowerCase(); // Default value.
                        float multiplier = 1000f; // Default value.

                        if (input.endsWith("h"))
                        {
                            input = input.substring(0, input.length() - 1); // Shall not throw IndexOutOfBoundsException.
                            multiplier = 60f * 60f * 1000f;
                        }
                        else if (input.endsWith("min"))
                        {
                            input = input.substring(0, input.length() - 3); // Shall not throw IndexOutOfBoundsException.
                            multiplier = 60f * 1000f;
                        }
                        else if (input.endsWith("ms"))
                        {
                            input = input.substring(0, input.length() - 2); // Shall not throw IndexOutOfBoundsException.
                            multiplier = 1f;
                        }
                        // To be tested AFTER the "ms" case!!!
                        else if (input.endsWith("s"))
                        {
                            input = input.substring(0, input.length() - 1); // Shall not throw IndexOutOfBoundsException.
                        }

                        float f = Float.parseFloat(input); // May throw NumberFormatException.
                        f *= multiplier;
                        millis = (long) f;

                        if (millis < 0L)
                        {
                            throw new IllegalArgumentException("Negative time");
                        }

                        break;
                    }

                    default:
                        throw new IllegalArgumentException("Invalid SMIL duration format " + str);
                }

                _duration = Long.valueOf((hours * 60L * 60L * 1000L) + (minutes * 60L * 1000L) + (seconds * 1000L) + millis);
            }
        }
    }

    /**
     * Returns the begin time of this element as a string.
     * The begin time of an element can be defined in a variety of ways,
     * ranging from simple clock times to the time that an event (e.g. a mouse click) happens.
     * The simple duration of an element is specified as a simple time value.
     * Could be equal to a time, to "indefinite" or other.
     * @return the begin time. May be <code>null</code>.
     * @see #setBeginString
     * @castor.field
     *  get-method="getBeginString"
     *  set-method="setBeginString"
     * @castor.field-xml
     *  name="begin"
     *  node="attribute"
     */
    public String getBeginString()
    {
        return _begin;
    }

    /**
     * Initializes the begin time of this element as a string.
     * @param begin the begin time. May be <code>null</code>.
     * @see #getBeginString
     */
    public void setBeginString(final String begin)
    {
        _begin = StringUtils.normalize(begin);
    }

    /**
     * Returns the end time of the active duration as a string.
     * The end attribute constrains the active duration by specifying an end value using a simple offset,
     * a time base, an event-base, a syncbase, or DOM methods calls.
     * Could be equal to a time, to "indefinite" or other.
     * @return the end time as a string. May be <code>null</code>.
     * @see #setEnd
     * @castor.field
     *  get-method="getEnd"
     *  set-method="setEnd"
     * @castor.field-xml
     *  name="end"
     *  node="attribute"
     */
    public String getEnd()
    {
        return _end;
    }

    /**
     * Initializes the end time of this element as a string.
     * @param end the end time. May be <code>null</code>.
     * @see #getEnd
     */
    public void setEnd(final String end)
    {
        _end = StringUtils.normalize(end);
    }

    /**
     * Returns the minimum value of the active duration as a string.
     * Could be equal to a clock value or "media".
     * @return the minimum value of the active duration as a string. May be <code>null</code>.
     * @see #setMin
     * @castor.field
     *  get-method="getMin"
     *  set-method="setMin"
     * @castor.field-xml
     *  name="min"
     *  node="attribute"
     */
    public String getMin()
    {
        return _min;
    }

    /**
     * Initializes the minimum value of the active duration as a string.
     * @param min the minimum value of the active duration. May be <code>null</code>.
     * @see #getMin
     */
    public void setMin(final String min)
    {
        _min = StringUtils.normalize(min);
    }

    /**
     * Returns the maximum value of the active duration as a string.
     * Could be equal to a clock value, or "media" or "indefinite".
     * @return the maximum value of the active duration as a string. May be <code>null</code>.
     * @see #setMax
     * @castor.field
     *  get-method="getMax"
     *  set-method="setMax"
     * @castor.field-xml
     *  name="max"
     *  node="attribute"
     */
    public String getMax()
    {
        return _max;
    }

    /**
     * Initializes the maximum value of the active duration as a string.
     * @param max the maximum value of the active duration. May be <code>null</code>.
     * @see #getMax
     */
    public void setMax(final String max)
    {
        _max = StringUtils.normalize(max);
    }

    /**
     * Accepts the specified SMIL playlist visitor.
     * @param visitor a SMIL playlist visitor. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>visitor</code> is <code>null</code>.
     */
    public abstract void acceptDown(SmilVisitor visitor);
}
