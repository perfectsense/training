package brightspot.difficulty;

import java.util.Optional;

public interface HasDifficultyWithField extends HasDifficulty {

    default Difficulty getDifficultyFallback() {
        return null;
    }

    default HasDifficultyWithFieldData asHasDifficultyWithFieldData() {
        return as(HasDifficultyWithFieldData.class);
    }

    // --- HasDifficulty support ---

    @Override
    default Difficulty getDifficulty() {
        return Optional.ofNullable(asHasDifficultyWithFieldData().getDifficulty())
            .orElseGet(this::getDifficultyFallback);
    }
}
