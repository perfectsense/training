package brightspot.difficulty;

import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;

@Recordable.FieldInternalNamePrefix(HasDifficultyWithFieldData.FIELD_PREFIX)
public class HasDifficultyWithFieldData extends Modification<HasDifficultyWithField> {

    static final String FIELD_PREFIX = "hasDifficulty.";
    public static final String DIFFICULTY_FIELD = FIELD_PREFIX + "difficulty";

    private Difficulty difficulty;

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }
}
