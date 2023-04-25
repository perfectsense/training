package brightspot.nasa;

import com.psddev.cms.db.ToolUi;
import com.psddev.cms.tool.CmsTool;
import com.psddev.cms.ui.form.Note;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;

@Recordable.FieldInternalNamePrefix("nasa-rss.")
public class NasaIngesterGlobalSettingsModification extends Modification<CmsTool> {

    @ToolUi.Cluster("NASA")
    @Note("Example Note.")
    @ToolUi.Tab("Integrations")
    IngestGlobalSettings ingestGlobalSettings;

    public IngestGlobalSettings getIngestGlobalSettings() {
        return ingestGlobalSettings;
    }

    public void setIGS(IngestGlobalSettings igs) {
        this.ingestGlobalSettings = igs;
    }
}