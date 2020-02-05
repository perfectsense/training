package brightspot.google.dfp;

import java.util.Optional;

import brightspot.core.site.IntegrationHeadScripts;
import brightspot.core.video.VideoProviderExtraAttributes;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Record;

/**
 * Defines the Google DFP settings for {@link IntegrationHeadScripts} and {@link VideoProviderExtraAttributes}.
 */
@ToolUi.DisplayName("Google DFP")
public class GoogleDfp extends Record implements IntegrationHeadScripts, VideoProviderExtraAttributes {

    public static final String BRIGHTSPOT_DFP_TEST_CLIENT_ID = "13236445";

    @ToolUi.Note("Ex: " + BRIGHTSPOT_DFP_TEST_CLIENT_ID)
    @Required
    private String clientId;

    @ToolUi.Note("DFP Client to use in CMS Preview")
    private GoogleDfpAdClient previewAdClient;

    private String dfpVideoTagUrl;

    @Override
    public String getLabel() {
        return "Google DFP";
    }

    /**
     * @deprecated {@link #getClientId(boolean)} instead.
     */
    @Deprecated
    public String getClientId() {
        return clientId;
    }

    /**
     * @param clientId The Google DFP Client Id.
     */
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

    /**
     * @return Google DFP Video Tag Url.
     */
    public String getDfpVideoTagUrl() {
        return dfpVideoTagUrl;
    }

    /**
     * @param dfpVideoTagUrl The Google DFP Video Tag Url.
     */
    public void setDfpVideoTagUrl(String dfpVideoTagUrl) {
        this.dfpVideoTagUrl = dfpVideoTagUrl;
    }

    public GoogleDfpAdClient getPreviewAdClient() {
        return previewAdClient;
    }

    public void setPreviewAdClient(GoogleDfpAdClient previewAdClient) {
        this.previewAdClient = previewAdClient;
    }
}
