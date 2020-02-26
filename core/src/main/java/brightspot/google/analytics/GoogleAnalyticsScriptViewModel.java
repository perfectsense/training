package brightspot.google.analytics;

import brightspot.core.thirdpartyintegration.AbstractThirdPartyViewModel;
import com.psddev.styleguide.google_analytics.GoogleAnalyticsScriptView;

public class GoogleAnalyticsScriptViewModel extends AbstractThirdPartyViewModel<GoogleAnalytics>
    implements GoogleAnalyticsScriptView {

    @Override
    public CharSequence getTrackingId() {
        // Plain text
        return model.getTrackingId();
    }

    @Override
    public CharSequence getAttributes() {
        return null;
    }
}
