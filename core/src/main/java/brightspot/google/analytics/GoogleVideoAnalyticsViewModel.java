package brightspot.google.analytics;

import brightspot.core.site.FrontEndSettings;
import com.psddev.cms.db.Site;
import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.servlet.CurrentSite;
import com.psddev.cms.view.servlet.HttpParameter;
import com.psddev.styleguide.google_analytics.GoogleVideoAnalyticsAttributeView;
import org.apache.commons.lang3.StringUtils;

public class GoogleVideoAnalyticsViewModel extends ViewModel<GoogleVideoAnalytics>
    implements GoogleVideoAnalyticsAttributeView {

    @CurrentSite
    protected Site site;

    @HttpParameter
    protected String disable3rdParty;

    @Override
    protected boolean shouldCreate() {
        boolean gaEnabled = FrontEndSettings.get(site, FrontEndSettings::getIntegrations)
            .stream().anyMatch(script -> script instanceof GoogleAnalytics);

        return gaEnabled
            && (StringUtils.isBlank(disable3rdParty)
            || !StringUtils.equals(
            disable3rdParty,
            FrontEndSettings.get(site, FrontEndSettings::getDisableThirdPartyParameterValue)));
    }
}
