package brightspot.nasa;

import java.util.Date;

import brightspot.image.WebImage;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;

@Recordable.FieldInternalNamePrefix("ingest.")
public class IngestedImageModification extends Modification<WebImage> {

    @ToolUi.ReadOnly
    @Recordable.Indexed
    Date ingestPublishedDate;

    public Date getIngestPublishedDate() {
        return ingestPublishedDate;
    }

    public void setIngestPublishedDate(Date ingestPublishedDate) {
        this.ingestPublishedDate = ingestPublishedDate;
    }
}
