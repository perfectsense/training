package brightspot.difficulty;

import com.psddev.dari.db.Recordable;

public interface HasDifficulty extends Recordable {

    Difficulty getDifficulty();
}
