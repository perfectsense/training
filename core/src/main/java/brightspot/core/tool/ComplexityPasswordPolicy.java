package brightspot.core.tool;

import java.util.Map;

import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.PasswordException;
import com.psddev.dari.util.UserPasswordPolicy;

/**
 * Password policy requiring minimum length and complexity.
 *
 * <pre>
 *     <Environment name="cms/tool/userPasswordPolicy" type="java.lang.String" value="complexity" override="false" />
 *     <Environment name="dari/userPasswordPolicy/complexity/class" type="java.lang.String" value="brightspot.core.tool.ComplexityPasswordPolicy" override="false" />
 *     &lt;!-- Optional overrides: --&gt;
 *     <Environment name="dari/userPasswordPolicy/complexity/minLength" type="java.lang.Integer" value="8" override="false" /> &lt;!-- default: 8 --&gt;
 *     <Environment name="dari/userPasswordPolicy/complexity/minComplexityClasses" type="java.lang.Integer" value="2" override="false" /> &lt;!-- default: 2 --&gt;
 * </pre>
 */
public class ComplexityPasswordPolicy implements UserPasswordPolicy {

    public static final String MIN_LENGTH_SETTINGS_KEY = "minLength";
    public static final String MIN_COMPLEXITY_CLASSES_SETTINGS_KEY = "minComplexityClasses";

    private static final int DEFAULT_MIN_COMPLEXITY_CLASSES = 2;
    private static final int DEFAULT_MIN_LENGTH = 8;

    private int minComplexityClasses;
    private int minLength;

    @Override
    public void initialize(String settingsKey, Map<String, Object> settings) {
        minComplexityClasses = ObjectUtils.to(
            Integer.class,
            settings.getOrDefault(MIN_COMPLEXITY_CLASSES_SETTINGS_KEY, DEFAULT_MIN_COMPLEXITY_CLASSES));
        minLength = ObjectUtils.to(Integer.class, settings.getOrDefault(MIN_LENGTH_SETTINGS_KEY, DEFAULT_MIN_LENGTH));
        if (minComplexityClasses > 4 || minComplexityClasses < 0) {
            minComplexityClasses = DEFAULT_MIN_COMPLEXITY_CLASSES;
        }
        if (minLength < 0) {
            minLength = DEFAULT_MIN_LENGTH;
        }
    }

    @Override
    public void validate(Object user, String password) throws PasswordException {
        StringBuilder error = new StringBuilder();

        char[] pwArr = password.toCharArray();

        if (pwArr.length < minLength) {
            error.append("Password must be a minimum of ").append(minLength).append(" characters in length. ");
        }

        boolean hasUpperLetter = false;
        boolean hasLowerLetter = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;
        int complexityClasses = 0;

        for (char ch : pwArr) {
            if (Character.isLetter(ch) && Character.isUpperCase(ch)) {
                hasUpperLetter = true;

            } else if (Character.isLetter(ch) && Character.isLowerCase(ch)) {
                hasLowerLetter = true;

            } else if (Character.isDigit(ch)) {
                hasNumber = true;

            } else if (!Character.isLetterOrDigit(ch)) {
                hasSpecial = true;
            }
        }

        if (hasUpperLetter) {
            complexityClasses++;
        }

        if (hasLowerLetter) {
            complexityClasses++;
        }

        if (hasNumber) {
            complexityClasses++;
        }

        if (hasSpecial) {
            complexityClasses++;
        }

        if (complexityClasses < minComplexityClasses) {
            error.append("Password must contain characters from at least ")
                .append(minComplexityClasses)
                .append(" complexity classes (upper case, lower case, numerals, non-alphanumeric). ");
        }

        if (error.length() > 0) {
            throw new PasswordException(error.toString());
        }
    }
}
