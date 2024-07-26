package brightspot.difficulty;

import java.util.Optional;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.UnresolvedState;

@Recordable.FieldInternalNamePrefix(HasDifficultyData.FIELD_PREFIX)
public class HasDifficultyData extends Modification<HasDifficulty> {

    static final String FIELD_PREFIX = "hasDifficulty.";
    public static final String DIFFICULTY_FIELD = FIELD_PREFIX + "getDifficulty";
    public static final String DIFFICULTY_LEVEL_FIELD = FIELD_PREFIX + "getDifficultyLevel";

    @Indexed
    @ToolUi.Filterable
    @ToolUi.Hidden
    public Difficulty getDifficulty() {
        return UnresolvedState.resolveAndGet(getOriginalObject(), HasDifficulty::getDifficulty);
    }

    @Indexed
    @ToolUi.Hidden
    @ToolUi.Sortable
    public Integer getDifficultyLevel() {
        return Optional.ofNullable(getDifficulty())
            .map(Difficulty::getLevel)
            .orElse(null);
    }
}
