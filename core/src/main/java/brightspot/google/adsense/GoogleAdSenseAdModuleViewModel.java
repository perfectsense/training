package brightspot.google.adsense;

import java.util.ArrayList;
import java.util.Optional;

import brightspot.amp.google.adsense.DefaultAmpGoogleAdSenseViewModel;
import brightspot.core.ad.AbstractAdViewModel;
import brightspot.core.ad.AdsFrontEndSettingsModification;
import brightspot.core.page.CurrentPageViewModel;
import brightspot.core.site.FrontEndSettings;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.view.servlet.CurrentSite;
import com.psddev.styleguide.adsense.GoogleAdsenseAdModuleView;

public class GoogleAdSenseAdModuleViewModel extends AbstractAdViewModel<GoogleAdSenseAdModule>
    implements GoogleAdsenseAdModuleView {

    @CurrentPageViewModel(DefaultAmpGoogleAdSenseViewModel.class)
    protected DefaultAmpGoogleAdSenseViewModel ampAdModule;

    @CurrentSite
    protected Site site;

    private transient GoogleAdSenseAdModule resolvedAdModule;

    /**
     * Attempts to resolve the {@link GoogleAdSenseAdModule} corresponding to the {@link
     * com.psddev.cms.view.ViewModel#model} to the {@link GoogleAdSenseAdModule} embedded in the current {@link Site} or
     * the global settings, if no {@link Site} is present. If there is no {@link GoogleAdSenseAdModule} with the same
     * slot name, falls back to the {@link com.psddev.cms.view.ViewModel#model}.
     *
     * @return a {@link GoogleAdSenseAdModule} (non null)
     */
    protected GoogleAdSenseAdModule getResolvedAdModule() {

        if (resolvedAdModule == null) {

            resolvedAdModule = Optional.ofNullable(SiteSettings
                .get(site, siteSettings -> siteSettings.as(AdsFrontEndSettingsModification.class)
                    .getAdModule(GoogleAdSenseAdModule.class, model.getUniqueName())))
                .orElse(model);
        }

        return resolvedAdModule;
    }

    protected void setResolvedAdModule(GoogleAdSenseAdModule resolvedAdModule) {
        this.resolvedAdModule = resolvedAdModule;
    }

    /**
     * @return Google AdSense Client Id defined in {@link FrontEndSettings#getIntegrations()}.
     */
    @Override
    public CharSequence getClient() {
        return Optional.ofNullable(FrontEndSettings.get(site, FrontEndSettings::getIntegrations))
            .orElse(new ArrayList<>())
            .stream()
            .filter(GoogleAdSense.class::isInstance)
            .map(GoogleAdSense.class::cast)
            .findFirst()
            .map(GoogleAdSense::getClientId)
            .orElse(null);
    }

    @Override
    public CharSequence getSlot() {
        // Plain text
        return getResolvedAdModule().getSlot();
    }

    @Override
    public Number getHeight() {
        return getResolvedAdModule().getAdSize() != null ? getResolvedAdModule().getAdSize().getHeight() : null;
    }

    @Override
    public Number getWidth() {
        return getResolvedAdModule().getAdSize() != null ? getResolvedAdModule().getAdSize().getWidth() : null;
    }

    // Amp Ad Module Fields

    @Override
    public Boolean getAmpEnabled() {
        return Optional.ofNullable(ampAdModule)
            .map(DefaultAmpGoogleAdSenseViewModel::isAmpEnabled)
            .orElse(null);
    }

}
