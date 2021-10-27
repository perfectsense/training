package brightspot.google.dfp;

import java.util.Optional;

import brightspot.head.IntegrationHeadScripts;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Record;

/**
 * Defines the Google DFP settings for {@link IntegrationHeadScripts}.
 */
@ToolUi.DisplayName("Google DFP")
public class GoogleDfp extends Record implements IntegrationHeadScripts {

    public static final String BRIGHTSPOT_DFP_TEST_CLIENT_ID = "13236445";

    @DisplayName("Client ID")
    @Required
    @ToolUi.Note("Ex: " + BRIGHTSPOT_DFP_TEST_CLIENT_ID)
    private String clientId;

    @ToolUi.Note("DFP Client to use in CMS Preview")
    private GoogleDfpAdClient previewAdClient;

    @Override
    public String getLabel() {
        return "Google DFP";
    }

    public String getClientId(boolean cmsPreview) {
        return cmsPreview
            ? Optional.ofNullable(previewAdClient)
            .map(googleDfpAdClient -> googleDfpAdClient.getClientId())
            .orElse(null)
            : clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public GoogleDfpAdClient getPreviewAdClient() {
        return previewAdClient;
    }

    public void setPreviewAdClient(GoogleDfpAdClient previewAdClient) {
        this.previewAdClient = previewAdClient;
    }
}
