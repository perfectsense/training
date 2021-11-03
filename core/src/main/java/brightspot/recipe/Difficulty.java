package brightspot.recipe;

import com.psddev.dari.util.Utils;

/**
 * A level of difficulty for a {@link Recipe}.
 *
 * Higher value for {@link #code} means higher level of difficulty.
 */
public enum Difficulty {

    EASY(1),
    INTERMEDIATE(2),
    EXPERT(3);

    private final int code;

    Difficulty(int code) {
        this.code = code;
    }

    // --- Getters ---

    public int getCode() {

        return code;
    }

    // --- Object support ---

    @Override
    public String toString() {
        return Utils.toLabel(name());
    }
}
