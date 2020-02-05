package brightspot.google.analytics;

import brightspot.core.site.IntegrationExtraBodyAttributes;
import com.psddev.dari.db.Record;

class GoogleVideoAnalytics extends Record implements IntegrationExtraBodyAttributes {

    @Override
    public String getLabel() {
        return "Google Video Analytics";
    }
}
