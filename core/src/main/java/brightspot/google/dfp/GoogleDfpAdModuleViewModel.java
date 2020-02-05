package brightspot.google.dfp;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import brightspot.amp.google.dfp.DefaultAmpGoogleDfpAdModuleViewModel;
import brightspot.core.ad.AbstractAdViewModel;
import brightspot.core.ad.AdSize;
import brightspot.core.ad.AdsFrontEndSettingsModification;
import brightspot.core.page.CurrentPageViewModel;
import brightspot.core.page.HttpPreview;
import brightspot.core.site.FrontEndSettings;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.view.servlet.CurrentSite;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StringUtils;
import com.psddev.styleguide.dfp.GoogleDfpAdModuleView;

/**
 * Renders a {@link GoogleDfpAdModule} as a {@link GoogleDfpAdModuleView}.
 */
public class GoogleDfpAdModuleViewModel extends AbstractAdViewModel<GoogleDfpAdModule>
    implements GoogleDfpAdModuleView {

    @CurrentPageViewModel(DefaultAmpGoogleDfpAdModuleViewModel.class)
    protected DefaultAmpGoogleDfpAdModuleViewModel ampAdModule;

    @CurrentSite
    protected Site site;

    @HttpPreview
    protected boolean isPreview;

    private transient GoogleDfpAdModule resolvedAdModule;

    /**
     * Attempts to resolve the {@link GoogleDfpAdModule} corresponding to the {@link
     * com.psddev.cms.view.ViewModel#model} to the {@link GoogleDfpAdModule} embedded in the current {@link Site} or the
     * global settings, if no {@link Site} is present. If there is no {@link GoogleDfpAdModule} with the same slot name,
     * falls back to the {@link com.psddev.cms.view.ViewModel#model}.
     *
     * @return a {@link GoogleDfpAdModule} (non null)
     */
    protected GoogleDfpAdModule getResolvedAdModule() {

        if (resolvedAdModule == null) {

            resolvedAdModule = Optional.ofNullable(SiteSettings
                .get(site, siteSettings -> siteSettings.as(AdsFrontEndSettingsModification.class)
                    .getAdModule(GoogleDfpAdModule.class, model.getUniqueName())))
                .orElse(model);
        }

        return resolvedAdModule;
    }

    protected void setResolvedAdModule(GoogleDfpAdModule resolvedAdModule) {
        this.resolvedAdModule = resolvedAdModule;
    }

    /**
     * @return The slot name configured in {@link FrontEndSettings#getIntegrations()}.
     */
    @Override
    public String getSlot() {
        return FrontEndSettings.get(site, FrontEndSettings::getIntegrations)
            .stream()
            .filter(Objects::nonNull)
            .filter(integrationHeadScripts -> integrationHeadScripts instanceof GoogleDfp)
            .findFirst()
            .map(integrationHeadScripts -> (GoogleDfp) integrationHeadScripts)
            .filter(googleDfp -> !StringUtils.isBlank(googleDfp.getClientId(isPreview)) && getResolvedAdModule() != null
                && getResolvedAdModule().getSlot() != null)
            .map(googleDfp -> "/" + googleDfp.getClientId(isPreview) + "/" + getResolvedAdModule().getSlot())
            .orElse(null);
    }

    /**
     * @return Formatted {@link AdSizeMap} String.
     */
    @Override
    public CharSequence getAdSizeMaps() {
        if (ObjectUtils.isBlank(getResolvedAdModule().getSizeMappings())) {
            return null;
        }

        return "[" + getResolvedAdModule().getSizeMappings().stream()
            .map(AdSizeMap::toAdSizeMapString)
            .collect(Collectors.joining(", ")) + "]";
    }

    /**
     * @return Unique Identifier for this Ad Index.
     */
    @Override
    public String getAdIndex() {
        return UUID.randomUUID().toString();
    }

    /**
     * @return Formatted {@link AdSize} List.
     */
    @Override
    public CharSequence getSizes() {
        if (ObjectUtils.isBlank(getResolvedAdModule().getSizes())) {
            return null;
        }

        return "[" + getResolvedAdModule().getSizes().stream()
            .map(AdSize::toDimensionString)
            .collect(Collectors.joining(", ")) + "]";
    }

    // Amp Ad Module Fields.

    @Override
    public Boolean getAmpEnabled() {
        return Optional.ofNullable(ampAdModule)
            .map(DefaultAmpGoogleDfpAdModuleViewModel::isAmpEnabled)
            .orElse(null);
    }

    @Override
    public Number getAmpWidth() {
        return Optional.ofNullable(ampAdModule)
            .map(defaultAmpGoogleDfpAdModuleViewModel -> defaultAmpGoogleDfpAdModuleViewModel.getAmpWidth(
                getResolvedAdModule().getSizes()))
            .orElse(null);
    }

    @Override
    public Number getAmpHeight() {
        return Optional.ofNullable(ampAdModule)
            .map(defaultAmpGoogleDfpAdModuleViewModel -> defaultAmpGoogleDfpAdModuleViewModel.getAmpHeight(
                getResolvedAdModule().getSizes()))
            .orElse(null);
    }

    @Override
    public CharSequence getAmpSizes() {
        return Optional.ofNullable(ampAdModule)
            .map(defaultAmpGoogleDfpAdModuleViewModel -> defaultAmpGoogleDfpAdModuleViewModel.getAmpSizes(
                getResolvedAdModule().getSizes()))
            .orElse(null);
    }

    @Override
    public Boolean getAmpMultiSizeValidation() {
        return Optional.ofNullable(ampAdModule)
            .map(DefaultAmpGoogleDfpAdModuleViewModel::hasAmpMultiSizeValidation)
            .orElse(null);
    }
}
