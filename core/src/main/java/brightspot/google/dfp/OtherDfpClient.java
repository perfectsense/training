package brightspot.google.dfp;

import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Other")
public class OtherDfpClient extends GoogleDfpAdClient {

    private String clientId;

    @Override
    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
