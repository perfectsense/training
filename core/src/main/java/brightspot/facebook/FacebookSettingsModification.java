package brightspot.facebook;

import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;

public class FacebookSettingsModification extends Modification<SiteSettings> {

    @ToolUi.Cluster("Facebook")
    @ToolUi.Tab("Front-End")
    @ToolUi.Placeholder("None")
    private FacebookSettings facebookSettings;

    public FacebookSettings getFacebookSettings() {
        return facebookSettings;
    }

    public String getFacebookAppId() {
        return facebookSettings != null
            ? facebookSettings.getAppId()
            : null;
    }

    public void setFacebookSettings(FacebookSettings settings) {
        this.facebookSettings = settings;
    }
}
