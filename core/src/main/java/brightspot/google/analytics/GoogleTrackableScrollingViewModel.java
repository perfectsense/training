package brightspot.google.analytics;

import brightspot.core.site.FrontEndSettings;
import com.psddev.cms.db.Site;
import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.servlet.CurrentSite;
import com.psddev.cms.view.servlet.HttpParameter;
import com.psddev.styleguide.google_analytics.GoogleTrackableScrollingAttributeView;
import org.apache.commons.lang3.StringUtils;

public class GoogleTrackableScrollingViewModel extends ViewModel<GoogleTrackableScrolling>
    implements GoogleTrackableScrollingAttributeView {

    @CurrentSite
    protected Site site;

    @HttpParameter
    protected String disable3rdParty;

    @Override
    protected boolean shouldCreate() {
        boolean gaEnabled = FrontEndSettings.get(site, FrontEndSettings::getIntegrations)
            .stream().anyMatch(script -> script instanceof GoogleAnalytics)
            && FrontEndSettings.get(site, FrontEndSettings::getIntegrations)
            .stream().anyMatch(script -> script instanceof GoogleTrackableScrolling);

        return gaEnabled
            && (StringUtils.isBlank(disable3rdParty)
            || !StringUtils.equals(
            disable3rdParty,
            FrontEndSettings.get(site, FrontEndSettings::getDisableThirdPartyParameterValue)));
    }

}
