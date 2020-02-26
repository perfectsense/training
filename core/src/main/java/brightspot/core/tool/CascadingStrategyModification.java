package brightspot.core.tool;

import brightspot.core.cascading.CascadingStrategy;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.tool.CmsTool;
import com.psddev.dari.db.Modification;

public class CascadingStrategyModification extends Modification<CmsTool> {

    @Required
    @ToolUi.NoteHtml("Cascading settings and page element will be selected using the following precedence:<br /> <span data-dynamic-html=\"${modification.getCascadingStrategy().getNoteHtml()}\"></span>")
    @ToolUi.Tab("Page Defaults")
    private CascadingStrategy cascadingSettingsAlgorithm;

    public CascadingStrategy getCascadingStrategy() {
        if (cascadingSettingsAlgorithm == null) {
            cascadingSettingsAlgorithm = CascadingStrategy.createDefault();
        }
        return cascadingSettingsAlgorithm;
    }

    public void setCascadingStrategy(CascadingStrategy cascadingSettings) {
        this.cascadingSettingsAlgorithm = cascadingSettings;
    }

    @Override
    protected void beforeSave() {
        if (cascadingSettingsAlgorithm == null) {
            cascadingSettingsAlgorithm = CascadingStrategy.createDefault();
        }
    }
}
