package brightspot.core.tool;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import brightspot.core.site.FrontEndSettings;
import com.ibm.icu.text.DateFormat;
import com.psddev.cms.db.Localization;
import com.psddev.cms.db.Site;
import com.psddev.dari.util.ObjectUtils;

public class DateTimeUtils {

    public static final String DEFAULT_DATE_TIME_FORMAT = "MMMM dd, yyyy hh:mm a";

    /**
     * 03-12-17
     */
    public static final String DATE_SKELETON = "MM-dd-yy";
    /**
     * March 20, 2017
     */
    public static final String DATE_STRING_SKELETON = "MMMM dd, yyyy";
    /**
     * 5:03 PM
     */
    public static final String TIME_12_HOUR_FORMAT_SKELETON = "hh:mm a";
    /**
     * 17:03
     */
    public static final String TIME_24_HOUR_FORMAT_SKELETON = "HH:mm";
    /**
     * March 20, 2017, 17:03
     */
    public static final String DATE_AND_TIME_STRING_SKELETON_24_HOUR_FORMAT = "MMMM dd, yyyy HH:mm";
    /**
     * March 20, 2017, 5:03 PM
     */
    public static final String DATE_AND_TIME_STRING_SKELETON_12_HOUR_FORMAT = "MMMM dd, yyyy hh:mm a";
    /**
     * 03/20/2017, 17:03
     */
    public static final String DATE_AND_TIME_SKELETON_24_HOUR_FORMAT = "MM-dd-yyyy HH:mm";
    /**
     * 03/20/2017, 5:03 PM
     */
    public static final String DATE_AND_TIME_SKELETON_12_HOUR_FORMAT = "MM-dd-yyyy hh:mm a";

    /**
     * Mon, 9:21 AM
     */
    public static final DateTimeFormatter TIME_FRIENDLY_FORMATTER = DateTimeFormatter.ofPattern("EEE, h:mm a");
    /**
     * Mon Jun 19, 9AM
     */
    public static final DateTimeFormatter THIS_WEEK_FRIENDLY_FORMATTER = DateTimeFormatter.ofPattern("EEE MMM d, ha");
    /**
     * Mon Jun 19, 9AM
     */
    public static final DateTimeFormatter THIS_MONTH_FRIENDLY_FORMATTER = DateTimeFormatter.ofPattern("EEE MMM d, ha");
    /**
     * Mon Jun 19, 2017
     */
    public static final DateTimeFormatter THIS_YEAR_FRIENDLY_FORMATTER = DateTimeFormatter.ofPattern("EEE MMM d, yyyy");
    /**
     * Mon Jun 19, 2017
     */
    public static final DateTimeFormatter DATE_TIME_FRIENDLY_FORMATTER = DateTimeFormatter.ofPattern("EEE MMM d, yyyy");

    public static CharSequence format(Date date, String timeZone, Locale locale, String skeleton) {
        if (timeZone == null) {
            timeZone = ZoneId.systemDefault().toString();
        }
        if (locale == null) {
            locale = Locale.getDefault();
        }
        DateFormat format = DateFormat.getPatternInstance(skeleton, locale);
        format.setTimeZone(com.ibm.icu.util.TimeZone.getTimeZone(timeZone));
        return format.format(date);
    }

    public static String printFriendlyDate(ZonedDateTime zonedDateTime) {

        ZonedDateTime yesterday = zonedDateTime.minusDays(1);
        ZonedDateTime tomorrow = zonedDateTime.plusDays(1);

        ZonedDateTime lastWeek = zonedDateTime.minusWeeks(1);
        ZonedDateTime nextWeek = zonedDateTime.plusWeeks(1);

        ZonedDateTime lastMonth = zonedDateTime.minusMonths(1);
        ZonedDateTime nextMonth = zonedDateTime.plusMonths(1);

        ZonedDateTime lastSixMonths = zonedDateTime.minusMonths(6);
        ZonedDateTime nextSixMonths = zonedDateTime.plusMonths(6);

        if (zonedDateTime.isAfter(yesterday) && zonedDateTime.isBefore(tomorrow)) {
            return TIME_FRIENDLY_FORMATTER.format(zonedDateTime);
        } else if (zonedDateTime.isAfter(lastWeek) && zonedDateTime.isBefore(nextWeek)) {
            return THIS_WEEK_FRIENDLY_FORMATTER.format(zonedDateTime);
        } else if (zonedDateTime.isAfter(lastMonth) && zonedDateTime.isBefore(nextMonth)) {
            return THIS_MONTH_FRIENDLY_FORMATTER.format(zonedDateTime);
        } else if (zonedDateTime.isAfter(lastSixMonths) && zonedDateTime.isBefore(nextSixMonths)) {
            return THIS_YEAR_FRIENDLY_FORMATTER.format(zonedDateTime);
        } else {
            return DATE_TIME_FRIENDLY_FORMATTER.format(zonedDateTime);
        }
    }

    /**
     * Attempts to format the provided {@link Date} based on the localized format pattern (resolved based on the
     * provided {@link Class} and key). The method falls back to the format specified in the provided {@link Site} or
     * global settings or, if all else fails, the {@link DateTimeUtils#DEFAULT_DATE_TIME_FORMAT}.
     *
     * @param date a {@link Date} (nullable).
     * @param type a {@link Class} identifying a localization bundle (nullable).
     * @param dateTimeFormatLocalizationKey identifying a value within a localization bundle (nullable).
     * @param site a {@link Site} (nullable).
     * @return a formatted Date/Time {@link String} or {@code null}, if the provided {@link Date} is {@link null}.
     */
    public static String format(Date date, Class<?> type, String dateTimeFormatLocalizationKey, Site site) {

        return format(date, type, dateTimeFormatLocalizationKey, site, null);
    }

    /**
     * Attempts to format the provided {@link Date} based on the localized format pattern (resolved based on the
     * provided {@link Class} and key). The method falls back to the provided default format, the format specified in
     * the provided {@link Site} or global settings or, if all else fails, the {@link
     * DateTimeUtils#DEFAULT_DATE_TIME_FORMAT}.
     *
     * @param date a {@link Date} (nullable).
     * @param type a {@link Class} identifying a localization bundle (nullable).
     * @param dateTimeFormatLocalizationKey identifying a value within a localization bundle (nullable).
     * @param site a {@link Site} (nullable).
     * @param defaultDateTimeFormatPattern the default Date/Time format pattern (nullable).
     * @return a formatted Date/Time {@link String} or {@code null}, if the provided {@link Date} is {@link null}.
     */
    public static String format(
        Date date, Class<?> type, String dateTimeFormatLocalizationKey, Site site,
        String defaultDateTimeFormatPattern) {

        if (date == null) {

            return null;
        }

        String dateTimeFormatPattern = defaultDateTimeFormatPattern;

        if (!ObjectUtils.isBlank(dateTimeFormatLocalizationKey)) {
            dateTimeFormatPattern = Localization
                .text(getLocaleOrDefault(site), type, dateTimeFormatLocalizationKey, defaultDateTimeFormatPattern);
        }
        return getLocalizedDateTimeFormatter(dateTimeFormatPattern, site)
            .format(getZonedDateTime(date, site));
    }

    /**
     * Returns a {@link ZonedDateTime} corresponding to the provided {@link Date}, using the {@link ZoneId} defined in
     * the provided {@link Site}, the {@link ZoneId} in the global settings, or the system default {@link ZoneId}.
     *
     * @param date a {@link Date} (nullable).
     * @param site a {@link Site} (nullable).
     * @return a {@link ZonedDateTime} or {@code null}, if the provided {@link Date} is {@code null}.
     */
    public static ZonedDateTime getZonedDateTime(Date date, Site site) {

        if (date == null) {
            return null;
        }

        return ZonedDateTime.ofInstant(date.toInstant(), getZoneIdOrDefault(site));
    }

    // Date/Time Pattern

    /**
     * Determines whether the provided {@link String} represents a valid date/time pattern by attempting to initialize a
     * {@link DateTimeFormatter} with it.
     *
     * @param dateTimePattern a {@link String} date/time pattern (nullable).
     * @return {@code true}, if the provided {@link String} represents a valid date/time pattern; {@code false}
     * otherwise.
     */
    public static boolean isDateTimePatternValid(String dateTimePattern) {

        if (dateTimePattern == null) {

            return true;
        }

        try {
            return DateTimeFormatter.ofPattern(dateTimePattern) != null;

        } catch (Exception e) {

            return false;
        }
    }

    /**
     * Returns the provided {@link String}, if it is a valid date/time pattern, or the {@link
     * DateTimeUtils#DEFAULT_DATE_TIME_FORMAT}.
     *
     * @param dateTimePattern a {@link String} date/time pattern (nullable).
     * @return a {@link String} date/time pattern (non-null).
     */
    public static String getDateTimePatternOrDefault(String dateTimePattern) {

        if (dateTimePattern != null && isDateTimePatternValid(dateTimePattern)) {
            return dateTimePattern;
        }

        return DEFAULT_DATE_TIME_FORMAT;
    }

    /**
     * Attempts to return the provided {@link String}, if it is a valid date/time pattern, the format pattern of the
     * provided {@link Site}, the global format pattern, or the {@link DateTimeUtils#DEFAULT_DATE_TIME_FORMAT}.
     *
     * @param dateTimePattern a {@link String} date/time pattern (nullable).
     * @param site a {@link Site} (nullable).
     * @return a {@link String} date/time pattern (non-null).
     */
    public static String getDateTimePatternOrDefault(String dateTimePattern, Site site) {

        String pattern = dateTimePattern;
        if (pattern != null && isDateTimePatternValid(pattern)) {
            return pattern;
        }

        pattern = FrontEndSettings.get(site, FrontEndSettings::getDefaultDateTimeFormat);
        if (pattern != null && isDateTimePatternValid(pattern)) {
            return pattern;
        }

        return DEFAULT_DATE_TIME_FORMAT;
    }

    // Locale

    /**
     * Attempts to return the {@link Locale} of the {@link Site}, the global {@link Locale}, or the system default
     * {@link Locale} (in that order).
     *
     * @param site the {@link Site} (nullable).
     * @return a {@link Locale} (non-null).
     */
    public static Locale getLocaleOrDefault(Site site) {

        return getLocaleOrDefault(FrontEndSettings.get(site, FrontEndSettings::getLocale));
    }

    /**
     * Returns the provided {@link Locale} (if non-null) or the system default {@link Locale}.
     *
     * @param locale a {@link Locale} (nullable).
     * @return a {@link Locale} (non-null).
     */
    public static Locale getLocaleOrDefault(Locale locale) {
        return locale != null ? locale : Locale.getDefault();
    }

    // TimeZone

    /**
     * Attempts to return the {@link ZoneId} of the provided {@link Site}, the global {@link ZoneId}, or the system
     * default {@link ZoneId} (in that order).
     *
     * @param site the {@link Site} (nullable).
     * @return a {@link ZoneId} (non-null).
     */
    public static ZoneId getZoneIdOrDefault(Site site) {
        return getZoneIdOrDefault(FrontEndSettings.get(site, FrontEndSettings::getTimeZone));
    }

    /**
     * Returns the {@link ZoneId} corresponding to the provided {@link String} or the system default {@link ZoneId}, if
     * parsing of the {@link String} fails.
     *
     * @param timeZoneId a {@link String} timezone ID (nullable).
     * @return a {@link ZoneId} (non-null).
     */
    public static ZoneId getZoneIdOrDefault(String timeZoneId) {

        try {

            return ZoneId.of(timeZoneId);

        } catch (Exception e) {

            return ZoneId.systemDefault();
        }
    }

    /**
     * Returns the provided {@link ZoneId} (if non-null) or the system default {@link ZoneId}.
     *
     * @param zoneId a {@link ZoneId} (nullable).
     * @return a {@link ZoneId} (non-null).
     */
    public static ZoneId getZoneIdOrDefault(ZoneId zoneId) {
        return zoneId != null ? zoneId : ZoneId.systemDefault();
    }

    // Formatter

    /**
     * Returns the {@link DateTimeFormatter} corresponding to the provided {@link String} pattern (falling back to the
     * pattern defined in the {@link Site}, the global settings, or the {@link DateTimeUtils#DEFAULT_DATE_TIME_FORMAT})
     * using the {@link ZoneId} and {@link Locale} of the provided {@link Site} (or falling back to the values defined
     * in the global settings or the system defaults).
     *
     * @param dateTimePattern a {@link String} representing a Date/Time pattern (nullable).
     * @param site a {@link Site} (nullable).
     * @return a {@link DateTimeFormatter} (non-null).
     */
    public static DateTimeFormatter getLocalizedDateTimeFormatter(String dateTimePattern, Site site) {

        return DateTimeFormatter.ofPattern(getDateTimePatternOrDefault(dateTimePattern, site))
            .withLocale(getLocaleOrDefault(site))
            .withZone(getZoneIdOrDefault(site));
    }

    /**
     * Returns the {@link DateTimeFormatter} corresponding to the provided {@link String} pattern (falling back to the
     * {@link DateTimeUtils#DEFAULT_DATE_TIME_FORMAT}, if invalid) using the provided {@link ZoneId} and {@link Locale}
     * (or falling back to the system defaults).
     *
     * @param dateTimePattern a {@link String} representing a Date/Time pattern (nullable).
     * @param locale a {@link Locale} (nullable).
     * @param zoneId a time zone ID (nullable).
     * @return a {@link DateTimeFormatter} (non-null).
     */
    public static DateTimeFormatter getLocalizedDateTimeFormatter(
        String dateTimePattern, Locale locale,
        String zoneId) {

        return DateTimeFormatter.ofPattern(getDateTimePatternOrDefault(dateTimePattern))
            .withLocale(getLocaleOrDefault(locale))
            .withZone(getZoneIdOrDefault(zoneId));
    }
}
