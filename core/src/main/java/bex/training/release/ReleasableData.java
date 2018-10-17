package bex.training.release;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;

import java.util.Date;

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
