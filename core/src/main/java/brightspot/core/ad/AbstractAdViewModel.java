package brightspot.core.ad;

import brightspot.core.site.FrontEndSettings;
import com.psddev.cms.db.Site;
import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.servlet.CurrentSite;
import com.psddev.cms.view.servlet.HttpParameter;
import org.apache.commons.lang3.StringUtils;

/**
 * The {@link AbstractAdViewModel} provides an abstract {@link ViewModel} for an Ad {@link M}. This base {@link
 * ViewModel} is used to disable Ads per request via a configured {@link #disableAds} parameter value. This is useful
 * for debugging and analyzing site performance excluding Ads.
 *
 * @param <M> Ad to render.
 */
public abstract class AbstractAdViewModel<M> extends ViewModel<M> {

    @CurrentSite
    protected Site site;

    @HttpParameter
    protected String disableAds;

    @Override
    protected boolean shouldCreate() {
        return StringUtils.isBlank(disableAds)
            || !StringUtils.equals(
            disableAds,
            FrontEndSettings.get(
                site,
                frontEndSettings -> frontEndSettings.as(AdsFrontEndSettingsModification.class)
                    .getDisableAdsParameterValue()));
    }
}
