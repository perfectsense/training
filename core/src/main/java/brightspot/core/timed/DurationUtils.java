package brightspot.core.timed;

import java.text.DecimalFormat;
import java.time.Duration;

import com.psddev.dari.util.ObjectUtils;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Utilities around dealing with Duration of TimedContent.
 */
public class DurationUtils {

    /**
     * @param timedContent Nullable.
     * @return The duration of the {@code timedContent} or null if it cannot be found.
     */
    public static Duration getTimedContentDuration(TimedContent timedContent) {
        if (timedContent != null) {
            Long duration = timedContent.getTimedContentDuration();
            if (duration != null && duration >= 0) {
                return Duration.ofMillis(duration);
            }
        }
        return null;
    }

    /**
     * @param offset Nullable.
     * @param limit Nullable.
     * @return A label depicting the percentage an offset is to an associated limit or null if a valid percentage cannot
     * be found.
     */
    public static String durationsToPercentageLabel(Duration offset, Duration limit) {

        if (offset == null || limit == null) {
            return null;
        }

        return secondsToPercentageLabel(offset.getSeconds(), limit.getSeconds());
    }

    /**
     * @param offsetSeconds Must be greater than or equal to zero.
     * @param limitSeconds Must be greater than or equal to offset and greater than zero.
     * @return A label depicting the percentage an offset is to an associated limit or null if a valid percentage cannot
     * be found.
     */
    public static String secondsToPercentageLabel(long offsetSeconds, long limitSeconds) {

        if (offsetSeconds < 0 || offsetSeconds > limitSeconds || limitSeconds == 0) {
            return null;
        }

        return new DecimalFormat("#.#%").format((double) offsetSeconds / (double) limitSeconds);
    }

    /**
     * @param seconds Nullable.
     * @return A label for a duration in seconds roughly in the form 2:13:18.
     */
    public static String secondsToDurationLabel(Long seconds) {

        if (seconds == null || seconds < 0) {
            return null;
        }

        return durationToLabel(Duration.ofSeconds(seconds));
    }

    /**
     * @param duration Nullable.
     * @return A label for a duration roughly in the form 2:13:18.
     */
    public static String durationToLabel(Duration duration) {
        return durationToLabelHelper(duration, false);
    }

    /**
     * @param duration Nullable.
     * @return A label for a duration roughly in the form 2hr 13m 18s.
     */
    public static String durationToLabelWithUnits(Duration duration) {
        return durationToLabelHelper(duration, true);
    }

    private static String durationToLabelHelper(Duration duration, boolean withUnits) {

        if (duration == null) {
            return null;
        }

        long hrs = duration.toHours();
        long min = (duration = duration.minusHours(hrs)).toMinutes();
        long sec = duration.minusMinutes(min).getSeconds();

        if (hrs > 0) {
            return withUnits
                ? String.format("%dhr %02dm %02ds", hrs, min, sec)
                : String.format("%d:%02d:%02d", hrs, min, sec);
        } else if (min > 0) {
            return withUnits ? String.format("%dm %02ds", min, sec) : String.format("%d:%02d", min, sec);
        } else {
            return withUnits ? String.format("%ds", sec) : String.format("0:%02d", sec);
        }
    }

    /**
     * @param timeFormat Nullable.
     * @return Converts a duration label of the form 1:02:32 into a duration object.
     * @see #timeFormatToSeconds
     */
    public static Duration timeFormatToDuration(String timeFormat) {
        Long seconds = timeFormatToSeconds(timeFormat);
        return seconds != null ? Duration.ofSeconds(seconds) : null;
    }

    /**
     * @param timeFormat Nullable.
     * @return Converts a duration label of the form 1:02:32 into seconds.
     * @see #timeFormatToDuration
     */
    // hh:mm:ss (1:02:32 --> 1*3600 + 2*60 + 32*1 --> 3750)
    public static Long timeFormatToSeconds(String timeFormat) {

        if (timeFormat == null) {
            return null;
        }

        String[] parts = timeFormat.split(":");
        ArrayUtils.reverse(parts);

        if (parts.length > 3) {
            return null;
        }

        Long hours = parts.length > 2 ? ObjectUtils.to(Long.class, parts[2]) : 0L;
        Long minutes = parts.length > 1 ? ObjectUtils.to(Long.class, parts[1]) : 0L;
        Long seconds = parts.length > 0 ? ObjectUtils.to(Long.class, parts[0]) : 0L;

        if (hours == null || hours < 0
            || minutes == null || minutes < 0
            || seconds == null || seconds < 0) {
            return null;
        }

        return hours * 3600 + minutes * 60 + seconds;
    }
}
