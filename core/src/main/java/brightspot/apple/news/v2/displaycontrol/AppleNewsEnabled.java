package brightspot.apple.news.v2.displaycontrol;

import com.psddev.cms.ui.ToolLocalization;

public enum AppleNewsEnabled {

    ENABLED,
    DISABLED;

    public String getLabel() {
        return ToolLocalization.text(AppleNewsEnabled.class, this.toString());
    }
}
