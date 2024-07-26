package brightspot.difficulty;

import brightspot.recipe.Recipe;
import com.psddev.dari.util.DariUtils;

/**
 * A level of difficulty for a {@link Recipe}.
 *
 * Higher value for {@link #level} corresponds with a higher level of difficulty.
 */
public enum Difficulty {

    EASY(1),
    INTERMEDIATE(2),
    EXPERT(3);

    private final int level;

    Difficulty(int level) {
        this.level = level;
    }

    // --- Getters ---

    public int getLevel() {
        return level;
    }

    // --- Object support ---

    @Override
    public String toString() {
        return DariUtils.toLabel(name());
    }
}
