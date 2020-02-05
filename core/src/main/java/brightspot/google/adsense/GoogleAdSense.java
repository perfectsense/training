package brightspot.google.adsense;

import brightspot.core.site.IntegrationHeadScripts;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Record;

@ToolUi.DisplayName("Google AdSense")
public class GoogleAdSense extends Record implements IntegrationHeadScripts {

    @Required
    @ToolUi.Note("Ex: ca-pub-1417247832694614")
    private String clientId;

    /**
     * @return Google AdSense Client Id.
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * @param clientId The Google AdSense Client Id.
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public String getLabel() {
        return "Google AdSense";
    }
}
