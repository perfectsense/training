package brightspot.google.dfp;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import brightspot.integrations.IntegrationItemSettings;
import brightspot.module.ad.AbstractAdViewModel;
import brightspot.module.ad.AdSettings;
import brightspot.module.ad.AdSize;
import brightspot.page.CurrentPageViewModel;
import brightspot.page.DefaultAmpGoogleDfpAdModuleViewModel;
import brightspot.page.HttpPreview;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.styleguide.dfp.GoogleDfpAdModuleView;
import org.apache.commons.lang3.StringUtils;

/**
 * Renders a {@link GoogleDfpAdModule} as a {@link GoogleDfpAdModuleView}.
 */
public class GoogleDfpAdModuleViewModel extends AbstractAdViewModel<GoogleDfpAdModule> implements GoogleDfpAdModuleView {

    @CurrentPageViewModel(DefaultAmpGoogleDfpAdModuleViewModel.class)
    protected DefaultAmpGoogleDfpAdModuleViewModel ampAdModule;

    @HttpPreview
    protected boolean isPreview;

    private transient GoogleDfpAdModule resolvedAdModule;

    @Override
    protected boolean shouldCreate() {
        return getResolvedAdModule() != null
                && !getResolvedAdModule().getSizeMappings().isEmpty()
                && !getResolvedAdModule().getSizes().isEmpty();
    }

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
                .get(site, siteSettings -> siteSettings.as(AdSettings.class)
                    .getAdModule(GoogleDfpAdModule.class, model.getUniqueName())))
                .orElse(model);
        }

        return resolvedAdModule;
    }

    protected void setResolvedAdModule(GoogleDfpAdModule resolvedAdModule) {
        this.resolvedAdModule = resolvedAdModule;
    }

    /**
     * @return The slot name configured in {@link IntegrationItemSettings#getIntegrations()}.
     */
    @Override
    public String getSlot() {
        return SiteSettings.get(site, siteSettings -> siteSettings.as(IntegrationItemSettings.class).getIntegrations())
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
        List<AdSizeMap> sizeMaps = Optional.ofNullable(getResolvedAdModule())
                .map(GoogleDfpAdModule::getSizeMappings)
                .orElseGet(Collections::emptyList);

        if (ObjectUtils.isBlank(sizeMaps)) {
            return null;
        }

        return "[" + sizeMaps.stream()
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
        List<AdSize> sizes = Optional.ofNullable(getResolvedAdModule())
                .map(GoogleDfpAdModule::getSizes)
                .orElseGet(Collections::emptyList);

        if (ObjectUtils.isBlank(sizes)) {
            return null;
        }

        return "[" + sizes.stream()
            .map(AdSize::toDimensionString)
            .collect(Collectors.joining(", ")) + "]";
    }

    // Amp Ad Module Fields.

    @Override
    public Boolean getAmpEnabled() {

        // TODO re-do when AMP support is included
        return false;
    }

    @Override
    public Number getAmpWidth() {

        // TODO re-do when AMP support is included
        return null;
    }

    @Override
    public Number getAmpHeight() {

        // TODO re-do when AMP support is included
        return null;
    }

    @Override
    public CharSequence getAmpSizes() {

        // TODO re-do when AMP support is included
        return null;
    }

    @Override
    public Boolean getAmpMultiSizeValidation() {

        // TODO re-do when AMP support is included
        return null;
    }
}
