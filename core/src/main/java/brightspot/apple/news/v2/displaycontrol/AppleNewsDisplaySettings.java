package brightspot.apple.news.v2.displaycontrol;

import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.tool.CmsTool;
import com.psddev.cms.ui.ToolLocalization;
import com.psddev.cms.ui.form.DynamicPlaceholderMethod;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.Singleton;

@Recordable.FieldInternalNamePrefix(AppleNewsDisplaySettings.PREFIX)
public class AppleNewsDisplaySettings extends Modification<SiteSettings> {

    static final String PREFIX = "apple.news.display.";

    @DynamicPlaceholderMethod("getPlaceholder")
    @ToolUi.Tab("Integrations")
    @ToolUi.Cluster("Apple News")
    private AppleNewsEnabled enabled;

    public Boolean isAppleNewsEnabled() {
        // No fallback for the Global Site
        if (getOriginalObject().isInstantiableTo(CmsTool.class)) {
            return AppleNewsEnabled.ENABLED.equals(enabled);
        }

        // Pass null so a Site can fallback to the Global site
        return enabled != null
            ? AppleNewsEnabled.ENABLED.equals(enabled)
            : null;
    }

    public AppleNewsEnabled getEnabled() {
        return enabled;
    }

    public void setEnabled(AppleNewsEnabled enabled) {
        this.enabled = enabled;
    }

    private String getPlaceholder() {
        // Global site default is disabled
        if (getOriginalObject().isInstantiableTo(CmsTool.class)) {
            return AppleNewsEnabled.DISABLED.getLabel();
        }

        // Grab the inherited value off the Global Site
        return ToolLocalization.text(AppleNewsEnabled.class, "message.inherit") + " - "
            + (Boolean.TRUE.equals(Singleton.getInstance(CmsTool.class)
            .as(AppleNewsDisplaySettings.class).isAppleNewsEnabled())
            ? AppleNewsEnabled.ENABLED.getLabel()
            : AppleNewsEnabled.DISABLED.getLabel());

    }
}
