package christophedelory.playlist.smil20;

import christophedelory.lang.StringUtils;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;

public class DurationValueXmlAdapter extends XmlAdapter<String, Long>
{
  private static String INDEFINITE = "indefinite";

  @Override
  public String marshal(Long _duration)
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
   * Convert from SMIL duration to timestamp
   *
   * @param xmlValue a duration. May be <code>null</code>.
   * @throws IllegalArgumentException if the given string is malformed.
   * @throws IllegalArgumentException if the specified duration is negative.
   * @throws NumberFormatException    if the string does not contain parsable values.
   */
  @Override
  public Long unmarshal(String xmlValue)
  {
    final String str = xmlValue.trim();

    if (str.equalsIgnoreCase("media"))
    {
      return null;
    }

    if (INDEFINITE.equalsIgnoreCase(str))
    {
      return Long.MAX_VALUE;
    }

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
    return (hours * 60L * 60L * 1000L) + (minutes * 60L * 1000L) + (seconds * 1000L) + millis;
  }

}
