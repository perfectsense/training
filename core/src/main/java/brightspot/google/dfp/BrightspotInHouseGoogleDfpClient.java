package brightspot.google.dfp;

import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Brightspot In House")
public class BrightspotInHouseGoogleDfpClient extends GoogleDfpAdClient {

    @Override
    public String getClientId() {
        return GoogleDfp.BRIGHTSPOT_DFP_TEST_CLIENT_ID;
    }
}
