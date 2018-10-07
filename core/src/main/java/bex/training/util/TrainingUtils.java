package bex.training.util;

import com.psddev.dari.util.StringUtils;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Utility class containing various methods to assist throughout the project.
 */
public final class TrainingUtils {

    /**
     * Helper method to join strings that have been verified to be non-blank.
     *
     * @param delimiter The string between joined strings.
     * @param strings The list of strings to join.
     * @return The joined non-blank strings.
     */
    public static String joinNonBlankStrings(String delimiter, String... strings) {

        return Optional.ofNullable(Arrays.stream(strings)
                .filter(s -> !StringUtils.isBlank(s))
                .collect(Collectors.toList()))
                .map(filteredStrings -> String.join(delimiter, filteredStrings))
                .orElse(null);
    }
}
