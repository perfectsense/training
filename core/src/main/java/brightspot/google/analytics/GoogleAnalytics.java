package brightspot.google.analytics;

import brightspot.core.site.IntegrationHeadScripts;
import com.psddev.dari.db.Record;

public class GoogleAnalytics extends Record implements IntegrationHeadScripts {

    @Required
    private String trackingId;

    /**
     * @return Google Analytics Tracking Id.
     */
    public String getTrackingId() {
        return trackingId;
    }

    /**
     * @param trackingId The Google Analytics Tracking Id.
     */
    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    @Override
    public String getLabel() {
        return "Google Analytics";
    }
}
