package brightspot.core.ad;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.State;
import com.psddev.dari.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FrontEnd settings for Ads
 */
public class AdsFrontEndSettingsModification extends Modification<SiteSettings> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdsFrontEndSettingsModification.class);

    @Minimum(5)
    @ToolUi.Cluster("Ads")
    @ToolUi.NoteHtml("<span data-dynamic-html='${modification.getDisableAdsHtml()}'></span>")
    @ToolUi.Tab("Front-End")
    private String disableAdsParameterValue;

    @Deprecated
    @ToolUi.Cluster("Ads")
    @ToolUi.Tab("Front-End")
    private Set<AdSizeWrapper> supportedAdSizes;

    @Embedded
    @ToolUi.Cluster("Ads")
    @ToolUi.Tab("Front-End")
    @ToolUi.NoteHtml("<span data-dynamic-html='${modification.getInheritedAdSizesLabel()}'></span>")
    private Set<AdSize> adSizes;

    @ToolUi.Cluster("Ads")
    @ToolUi.Tab("Front-End")
    @ToolUi.Note("These Ad Sizes if detected when trying to load a CMS preview will not load")
    private Set<AdSize> cmsPreviewDisabledAdSizes;

    @Embedded
    @ToolUi.Cluster("Ads")
    @ToolUi.DisplayLast
    @ToolUi.Tab("Front-End")
    private Set<AdModule> adModules;

    /**
     * @return Parameter value used to disable Ads on the frontend for performance testing.
     */
    public String getDisableAdsParameterValue() {
        return disableAdsParameterValue;
    }

    /**
     * @param disableAdsParameterValue
     */
    public void setDisableAdsParameterValue(String disableAdsParameterValue) {
        this.disableAdsParameterValue = disableAdsParameterValue;
    }

    /**
     * @return HTML used in disableAdsParameterValue tool Note to demonstrate a disableAds request.
     */
    public String getDisableAdsHtml() {
        if (!StringUtils.isBlank(disableAdsParameterValue)) {
            return "Add the parameter disableAds to any page with your secret. Ex http://foo.com?disableAds="
                + disableAdsParameterValue;
        }

        return null;
    }

    /**
     * @return A set of {@link AdSizeWrapper} that are allowed on this {@link SiteSettings}. {@link AdSizeWrapper} are
     * used instead of {@link AdSize} to allow for inline editing and reference usage of the {@link AdSize}.
     * @deprecated functionality replaced by {@link #getAdSizes()}
     */
    @Deprecated
    public Set<AdSizeWrapper> getSupportedAdSizes() {
        if (supportedAdSizes == null) {
            supportedAdSizes = new HashSet<>();
        }
        return supportedAdSizes;
    }

    /**
     * @param supportedAdSizes
     * @deprecated functionality replaced by {@link #setAdSizes(Set)} Sets supported ad sizes.
     */
    @Deprecated
    public void setSupportedAdSizes(Set<AdSizeWrapper> supportedAdSizes) {
        this.supportedAdSizes = supportedAdSizes;
    }

    @Relocate
    public Set<AdSize> getAdSizes() {
        if (adSizes == null) {
            adSizes = new LinkedHashSet<>();
        }

        // Relocate wrapped adSizes
        if (supportedAdSizes != null) {
            supportedAdSizes.stream().map(AdSizeWrapper::getAdSize).forEach(adSizes::add);
            supportedAdSizes = null;
        }
        return adSizes;
    }

    public void setAdSizes(Set<AdSize> adSizes) {
        this.adSizes = adSizes;
    }

    /**
     * @return Ad sizes that should be skipped in CMS Preview.
     */
    public Set<AdSize> getCmsPreviewDisabledAdSizes() {
        if (cmsPreviewDisabledAdSizes == null) {
            cmsPreviewDisabledAdSizes = new LinkedHashSet<>();
        }
        return cmsPreviewDisabledAdSizes;
    }

    /**
     * @param cmsPreviewDisabledAdSizes Ad sizes that should be skipped in CMS Preview.
     */
    public void setCmsPreviewDisabledAdSizes(Set<AdSize> cmsPreviewDisabledAdSizes) {
        this.cmsPreviewDisabledAdSizes = cmsPreviewDisabledAdSizes;
    }

    /**
     * @return Ad modules.
     */
    public Set<AdModule> getAdModules() {
        if (adModules == null) {
            adModules = new LinkedHashSet<>();
        }
        return adModules;
    }

    /**
     * Set ad modules.
     *
     * @param adModules
     */
    public void setAdModules(Set<AdModule> adModules) {
        this.adModules = adModules;
    }

    public String getInheritedAdSizesLabel() {
        Site site = Optional.of(getOriginalObject())
            .filter(Site.class::isInstance)
            .map(Site.class::cast)
            .orElse(null);

        Set<String> adSizeNames = getAdSizes().stream().map(AdSize::getUniqueName).collect(Collectors.toSet());

        if (site != null) {
            String sizes = Query.from(AdSize.class)
                .where(site.itemsPredicate())
                .selectAll().stream()
                .filter(size -> !adSizeNames.contains(size.getUniqueName()))
                .map(AdSize::getUniqueName)
                .collect(Collectors.joining(", "));

            if (!StringUtils.isBlank(sizes)) {
                return "The following sizes are available globally or from other sites: " + sizes;
            }
        }
        return null;
    }

    @Override
    protected void onValidate() {
        Set<AdSize> sizes = getAdSizes();

        String duplicateSizes = sizes.stream()
            .map(AdSize::getUniqueName)
            .collect(Collectors.groupingBy(
                name -> name,
                Collectors.counting())) // Creates a Map of UniqueName to the number of occurrences of that name.
            .entrySet()
            .stream()
            .filter(map -> map.getValue() > 1) // Filters out names that have only one occurrence.
            .map(Map.Entry::getKey)
            .collect(Collectors.joining(", "));

        if (!StringUtils.isBlank(duplicateSizes)) {
            State state = getState();
            state.addError(state.getField("adSizes"), "This field contains duplicate Ad Sizes: " + duplicateSizes);
        }
    }

    @Override
    protected void afterSave() {
        // Process AdSizes
        processEmbeds(AdSize.class, AdsFrontEndSettingsModification::getAdSizes, AdSize::getUniqueName);

        // Process AdModules
        processEmbeds(AdModule.class, AdsFrontEndSettingsModification::getAdModules, AdModule::getUniqueName);

        // Removed orphaned record
        deleteOrphans();
    }

    private <T extends Record> void processEmbeds(
        Class<T> type,
        Function<AdsFrontEndSettingsModification, Set<T>> getEmbeds,
        Function<T, String> getUniqueName) {
        Site site = Optional.of(getOriginalObject())
            .filter(Site.class::isInstance)
            .map(Site.class::cast)
            .orElse(null);

        boolean isGlobal = site == null;

        Set<T> embeds = getEmbeds.apply(this);

        embeds.stream()
            // Look for an record of type T in the db that has the same fields (encapsulated by getUniqueName) as a provided embedded record on the current site.
            // If not, fall back to the embedded record.
            .map(embedded -> Optional.ofNullable(Query.from(type)
                .where("getUniqueName = ?", getUniqueName.apply(embedded))
                .first())
                .orElse(embedded))
            .map(record -> record.as(Site.ObjectModification.class))
            .peek(siteMod -> {
                // if the current site is global, the record should be set to global and have no consumers.
                if (isGlobal) {
                    siteMod.setGlobal(true);
                    siteMod.getConsumers().clear();
                    // if the current site is not global, and the record itself is not global, add the current site as a consumer of the record
                } else if (!siteMod.isGlobal()) {
                    siteMod.getConsumers().add(site);
                }
                siteMod.setOwner(null);
            })
            .forEach(record -> {
                try {
                    record.saveImmediately(); // @Embedded Record#save -> cloned standalone instance in the database
                } catch (Exception e) {
                    LOGGER.warn(e.getMessage());
                }
            });

        // Update record that are not referenced on the current site.
        Query.from(type)
            .where("getUniqueName != ?", embeds.stream().map(getUniqueName).collect(Collectors.toSet()))
            .selectAll()
            .forEach(record -> {
                Site.ObjectModification siteMod = record.as(Site.ObjectModification.class);
                // Case 1: The current site is global, and an record exists that is global and is no longer referenced on the global site.
                if (isGlobal && siteMod.isGlobal()) {
                    // mark as no longer global
                    siteMod.setGlobal(false);
                    // add any sites that have their own reference to the record as consumers on that record.
                    siteMod.getConsumers().addAll(Site.Static.findAll().stream().filter(nonGlobalSite ->
                        getEmbeds.apply(nonGlobalSite.as(AdsFrontEndSettingsModification.class))
                            .stream()
                            .map(getUniqueName)
                            .anyMatch(getUniqueName.apply(record)::equals)
                    ).collect(Collectors.toSet()));
                    // Case 2: A record exists that is not global and is not (or no longer) referenced on the current site.
                    // Doesn't matter if the site is global (read: null) b/c `consumers.remove(null)` is a noop. Also doesn't matter
                    // if consumers doesn't actually contain this site, since removing an element that doesn't exist
                    // from a set does nothing.
                } else if (!siteMod.isGlobal()) {
                    siteMod.getConsumers().remove(site);
                }
                // Other Case: A record exists that is global and the current site is not global: No action is needed

                try {
                    record.saveImmediately(); // @Embedded Record#save -> cloned standalone instance in the database
                } catch (Exception e) {
                    LOGGER.warn(e.getMessage());
                }
            });

    }

    private void deleteOrphans() {
        Arrays.asList(AdSize.class, AdModule.class)
            .forEach(type -> Query.from(type)
                .where(Site.CONSUMERS_FIELD + " is missing")
                .and(Site.IS_GLOBAL_FIELD + " != ?", true)
                .deleteAllImmediately());
    }

    /**
     * Helper method to get an AdModule by Unique Name.
     *
     * @param type Class that extends {@link AdModule}.
     * @param uniqueName The {@link AdModule#getUniqueName()} for the requested {@link AdModule}.
     * @param <T> Class that extends {@link AdModule}.
     * @return {@link AdModule} of Class {@link T}
     */
    public <T extends AdModule> T getAdModule(Class<T> type, String uniqueName) {

        if (uniqueName == null || type == null) {
            return null;
        }

        return this.getAdModules()
            .stream()
            .filter(type::isInstance)
            .map(type::cast)
            .filter(adModule -> uniqueName.equals(adModule.getUniqueName()))
            .findFirst()
            .orElse(null);

    }
}
