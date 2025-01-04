package io.github.borewit.lizzy.playlist.rss;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * RFC822 date and time-related methods.
 * See also <a href="http://www.ietf.org/rfc/rfc0822.txt">RFC 822</a>.
 *
 * @author Christophe Delory
 * @version $Revision: 91 $
 */
public final class RFC822 {
  /**
   * RFC822 date and time format, full version.
   */
  private static final ThreadLocal<DateFormat> fullRfc822DatetimeFormat = ThreadLocal.withInitial(() -> new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.US)); // Should not throw NullPointerException, IllegalArgumentException.

  /**
   * RFC822 date and time format, full version, without seconds.
   */
  private static final ThreadLocal<DateFormat> fullRfc822DatetimeFormat2 = ThreadLocal.withInitial(() -> new SimpleDateFormat("EEE, d MMM yyyy HH:mm Z", Locale.US)); // Should not throw NullPointerException, IllegalArgumentException.

  /**
   * RFC822 date and time format, compact version.
   */
  private static final ThreadLocal<DateFormat> compactRfc822DatetimeFormat = ThreadLocal.withInitial(() -> new SimpleDateFormat("d MMM yyyy HH:mm:ss Z", Locale.US)); // Should not throw NullPointerException, IllegalArgumentException.

  /**
   * RFC822 date and time format, compact version, without seconds.
   */
  private static final ThreadLocal<DateFormat> compactRfc822DatetimeFormat2 = ThreadLocal.withInitial(() -> new SimpleDateFormat("d MMM yyyy HH:mm Z", Locale.US)); // Should not throw NullPointerException, IllegalArgumentException.

  /**
   * The ISO8601 {@link Date} formatter for date-time without time zone.
   * The {@link java.util.TimeZone} used here is the default local time zone.
   * The input {@link Date} is a GMT date and time.
   */
  public static final ThreadLocal<DateFormat> iso8601DatetimeFormat = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)); // Should not throw NullPointerException, IllegalArgumentException.

  /**
   * Returns a RFC822 date and time string representation of the specified date.
   *
   * @param date the date to represent as a RFC822 date and time string.
   * @return a RFC822 date and time string.
   * @throws NullPointerException if <code>date</code> is <code>null</code>.
   */
  public static String toString(final Date date) {
    return fullRfc822DatetimeFormat.get().format(date); // Throws NullPointerException if date is null.
  }

  /**
   * Returns a date representation of the specified RFC822 date and time string.
   *
   * @param dateString the RFC822 date and time string to decode as a date.
   * @return a date. Is <code>null</code> if the <code>dateString</code> does not represent a valid RFC822 date and time string.
   * @throws NullPointerException if <code>dateString</code> is <code>null</code>.
   */
  public static Date valueOf(final String dateString) {
    DateFormat[] formatsToTry = {
      fullRfc822DatetimeFormat.get(),
      fullRfc822DatetimeFormat2.get(),
      compactRfc822DatetimeFormat.get(),
      compactRfc822DatetimeFormat2.get()
    };

    for (DateFormat dateFormat : formatsToTry) {
      try {
        Date ret = dateFormat.parse(dateString); // May throw ParseException. Throws NullPointerException if dateString is null.
        if (ret != null)
          return ret;
      } catch (ParseException ignore) {
        // Try next format
      }
    }
    return null;
  }

  /**
   * The no-arg constructor shall not be accessible.
   */
  private RFC822() {
  }
}
