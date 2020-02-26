package bex.training.release;

import java.util.Date;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;

@Recordable.FieldInternalNamePrefix("releasable.")
public class ReleasableData extends Modification<Releasable> {

    // Main.

    @Required
    @Indexed
    @ToolUi.Filterable
    private Date releaseDate;

    // Getters and Setters.

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }
}
