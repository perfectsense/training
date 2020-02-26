package brightspot.core.tool;

public final class TextUtils {

    private static final String ELLIPSIS_CHARACTER = "…";

    private TextUtils() {
    }

    /**
     * Truncates text to the given length and appends ellipsis depending on the {@code ellipsis} flag.
     *
     * @param text text to truncate.
     * @param maxChars length to cut.
     * @param ellipsis flag whether to append ellipsis or not.
     * @return truncated text.
     */
    public static String truncate(String text, Integer maxChars, boolean ellipsis) {

        if (maxChars == null || maxChars <= 0) {
            return text;
        }
        String truncatedText;

        if (text != null && text.length() > maxChars) {
            truncatedText = text.substring(0, maxChars);

            if (truncatedText.endsWith(ELLIPSIS_CHARACTER)) {
                truncatedText = truncatedText.substring(0, truncatedText.length() - 1);
            }

            int spaceIndex = truncatedText.lastIndexOf(' ');

            if (spaceIndex != -1) {
                truncatedText = truncatedText.substring(0, spaceIndex);
            }
            return ellipsis ? truncatedText + ELLIPSIS_CHARACTER : truncatedText;

        } else {
            return text;
        }
    }

}
