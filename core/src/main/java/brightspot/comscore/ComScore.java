package brightspot.comscore;

import brightspot.core.site.IntegrationHeadScripts;
import com.psddev.dari.db.Record;

public class ComScore extends Record implements IntegrationHeadScripts {

    @Required
    private String clientId;

    /**
     * @return ComScore Client Id.
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * @param clientId The ComScore Client Id.
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public String getLabel() {
        return "ComScore";
    }
}
