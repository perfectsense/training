package brightspot.google.dfp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import brightspot.core.ad.AdSize;
import brightspot.core.ad.AdsFrontEndSettingsModification;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.PredicateParser;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.State;
import com.psddev.dari.util.CompactMap;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines front end site settings for Google Dfp.
 */
public class GoogleDfpFrontEndSettingsModification extends Modification<SiteSettings> {

    private static final Pattern PATTERN = Pattern.compile("googletag.defineSlot\\('\\/[^'/*]*\\/([^']*)',\\s*([^\\']*)");
    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleDfpFrontEndSettingsModification.class);

    @Deprecated
    @ToolUi.Cluster("Google DFP")
    @ToolUi.Tab("Front-End")
    private Set<WindowSizeWrapper> supportedWindowSizes;

    @Embedded
    @ToolUi.Cluster("Google DFP")
    @ToolUi.Tab("Front-End")
    @ToolUi.NoteHtml("<span data-dynamic-html='${modification.getInheritedWindowSizesLabel()}'></span>")
    private Set<WindowSize> windowSizes;

    @ToolUi.Cluster("Google DFP")
    @ToolUi.Note("Copy and paste the Document Header script from Google DFP's Generate Tags wizard.")
    @ToolUi.Tab("Front-End")
    @ToolUi.DisplayFirst
    @DisplayName("DFP Tag Import")
    private String dfpTagImport;

    /**
     * @return Set of available {@link WindowSizeWrapper}.
     * @deprecated functionality replaced by {@link #getWindowSizes()}
     */
    @Deprecated
    public Set<WindowSizeWrapper> getSupportedWindowSizes() {
        if (supportedWindowSizes == null) {
            supportedWindowSizes = new HashSet<>();
        }
        return supportedWindowSizes;
    }

    /**
     * @param supportedWindowSizes The {@link #supportedWindowSizes}.
     * @deprecated functionality replaced by {@link #setWindowSizes(Set)}
     */
    @Deprecated
    public void setSupportedWindowSizes(Set<WindowSizeWrapper> supportedWindowSizes) {
        this.supportedWindowSizes = supportedWindowSizes;
    }

    @Relocate
    public Set<WindowSize> getWindowSizes() {
        if (windowSizes == null) {
            windowSizes = new LinkedHashSet<>();
        }

        // Relocate wrapped windowSizes
        if (supportedWindowSizes != null) {
            supportedWindowSizes.stream().map(WindowSizeWrapper::getWindowSize).forEach(windowSizes::add);
            supportedWindowSizes = null;
        }

        return windowSizes;
    }

    public void setWindowSizes(Set<WindowSize> windowSizes) {
        this.windowSizes = windowSizes;
    }

    public String getInheritedWindowSizesLabel() {
        Site site = Optional.of(getOriginalObject())
            .filter(Site.class::isInstance)
            .map(Site.class::cast)
            .orElse(null);

        Set<String> adSizeNames = getWindowSizes().stream().map(WindowSize::getUniqueName).collect(Collectors.toSet());

        if (site != null) {
            String sizes = Query.from(WindowSize.class)
                .where(site.itemsPredicate())
                .selectAll().stream()
                .filter(size -> !adSizeNames.contains(size.getUniqueName()))
                .map(WindowSize::getUniqueName)
                .collect(Collectors.joining(", "));

            if (!StringUtils.isBlank(sizes)) {
                return "The following sizes are available globally or from other sites: " + sizes;
            }
        }
        return null;
    }

    /**
     * @return The DFP Tag Import JavaScript.
     */
    public String getDfpTagImport() {
        return dfpTagImport;
    }

    /**
     * @param dfpTagImport The DFP Tag Import Javascript.
     */
    public void setDfpTagImport(String dfpTagImport) {
        this.dfpTagImport = dfpTagImport;
    }

    /**
     * Overrides {@link com.psddev.dari.db.Record#beforeSave()} to add support for converting a Google DFP Generate Tags
     * wizard Ad Script {@link #dfpTagImport} into {@link AdSize}, {@link GoogleDfpAdModule} and {@link WindowSize}
     * {@link GoogleDfpFrontEndSettingsModification} values.
     */
    @Override
    protected void beforeSave() {
        super.beforeSave();

        if (!StringUtils.isBlank(dfpTagImport)) {
            Matcher matcher = PATTERN.matcher(dfpTagImport);
            while (matcher.find()) {
                String slot = matcher.group(1);

                String adSizes = matcher.group(2).trim();
                if (adSizes.endsWith(",")) {
                    adSizes = adSizes.substring(0, adSizes.length() - 1);
                }

                List<AdSize> sizes = new ArrayList<>();

                if (adSizes.startsWith("[[")) {
                    List<List<Long>> sizeList = (List) ObjectUtils.fromJson(adSizes);

                    for (List<Long> sizeItem : sizeList) {
                        sizes.add(findOrCreateAdSize(sizeItem.get(0).intValue(), sizeItem.get(1).intValue()));
                    }

                } else {
                    List<Long> sizeItem = (List) ObjectUtils.fromJson(adSizes);
                    sizes.add(findOrCreateAdSize(sizeItem.get(0).intValue(), sizeItem.get(1).intValue()));
                }

                AdsFrontEndSettingsModification adsFrontEndSettingsModification = this.as(
                    AdsFrontEndSettingsModification.class);
                for (AdSize adSize : sizes) {
                    boolean add = true;
                    for (AdSize existingAdSize : adsFrontEndSettingsModification.getAdSizes()) {
                        if (existingAdSize != null && existingAdSize.getWidth().equals(adSize.getWidth())
                            && existingAdSize.getHeight().equals(adSize.getHeight())) {
                            add = false;
                        }
                    }

                    if (add) {
                        adsFrontEndSettingsModification.getAdSizes().add(adSize);
                    }
                }

                GoogleDfpAdModule adModule = new GoogleDfpAdModule();
                adModule.setSlot(slot);
                adModule.setSizes(sizes.stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList()));
                adModule.setImporting(true);

                this.as(AdsFrontEndSettingsModification.class).getAdModules().add(adModule);

                dfpTagImport = null;
            }
        }
    }

    @Override
    protected void onValidate() {
        Set<WindowSize> sizes = getWindowSizes();

        String duplicateSizes = sizes.stream()
            .map(WindowSize::getUniqueName)
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
            state.addError(
                state.getField("windowSizes"),
                "This field contains duplicate Window Sizes: " + duplicateSizes);
        }
    }

    /**
     * Overrides {@link com.psddev.dari.db.Record#afterSave()} to support cloning of embedded {@link AdSize}, {@link
     * GoogleDfpAdModule} and {@link WindowSize} into non-embedded {@link com.psddev.dari.db.Record}.
     */
    @Override
    protected void afterSave() {
        processWindowSizes();
        deleteOrphans();
    }

    private void processWindowSizes() {
        Site site = Optional.of(getOriginalObject())
            .filter(Site.class::isInstance)
            .map(Site.class::cast)
            .orElse(null);

        boolean isGlobal = site == null;
        Set<WindowSize> windowSizes = getWindowSizes();

        windowSizes.stream()
            // Look for a WindowSize in the db that has the same dimensions (encapsulated by getUniqueName) as a WindowSize on the current site.
            // If not, fall back to the embedded record.
            .map(embedded -> Optional.ofNullable(Query.from(WindowSize.class)
                .where("getUniqueName = ?", embedded.getUniqueName()).first())
                .orElse(embedded))
            .map(record -> record.as(Site.ObjectModification.class))
            .peek(siteMod -> {
                // if the current site is global, the WindowSize should be set to global and have no consumers.
                if (isGlobal) {
                    siteMod.setGlobal(true);
                    siteMod.getConsumers().clear();
                    // if the current site is not global, and the WindowSize itself is not global, add the current site as a consumer of the WindowSize
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

        // Update adsizes that are not referenced on the current site.
        Query.from(WindowSize.class)
            .where(
                "getUniqueName != ?",
                windowSizes.stream().map(WindowSize::getUniqueName).collect(Collectors.toSet()))
            .selectAll()
            .forEach(windowSize -> {
                Site.ObjectModification siteMod = windowSize.as(Site.ObjectModification.class);
                // Case 1: The current site is global, and an WindowSizwe exists that is global and is no longer referenced on the global site.
                if (isGlobal && siteMod.isGlobal()) {
                    // mark as no longer global
                    siteMod.setGlobal(false);
                    // add any sites that have their own reference to the size as consumers on that size.
                    siteMod.getConsumers().addAll(Site.Static.findAll().stream().filter(nonGlobalSite ->
                        nonGlobalSite.as(GoogleDfpFrontEndSettingsModification.class).getWindowSizes()
                            .stream()
                            .map(WindowSize::getUniqueName)
                            .anyMatch(windowSize.getUniqueName()::equals)
                    ).collect(Collectors.toSet()));
                    // Case 2: A size exists that is not global and is not (or no longer) referenced on the current site.
                    // Doesn't matter if the site is global (read: null) b/c `consumers.remove(null)` is a noop. Also doesn't matter
                    // if consumers doesn't actually contain this site, since removing an element that doesn't exist
                    // from a set does nothing.
                } else if (!siteMod.isGlobal()) {
                    siteMod.getConsumers().remove(site);
                }
                // Other Case: A WindowSize exists that is global and the current site is not global: No action is needed

                try {
                    windowSize.saveImmediately(); // @Embedded Record#save -> cloned standalone instance in the database
                } catch (Exception e) {
                    LOGGER.warn(e.getMessage());
                }
            });

    }

    private void deleteOrphans() {
        Query.from(WindowSize.class)
            .where(Site.CONSUMERS_FIELD + " is missing")
            .and(Site.IS_GLOBAL_FIELD + " != ?", true)
            .deleteAllImmediately();
    }

    private transient Map<String, AdSize> adSizeMap;

    private AdSize findOrCreateAdSize(Integer width, Integer height) {
        AdSize newAdSize = new AdSize();
        newAdSize.setWidth(width);
        newAdSize.setHeight(height);
        newAdSize.getState().setEmbedded(true);

        Site site = Optional.of(getOriginalObject())
            .filter(Site.class::isInstance)
            .map(Site.class::cast)
            .orElse(null);

        String adSizeKey = newAdSize.getUniqueName();

        if (adSizeMap == null) {
            adSizeMap = new CompactMap<>();
        }

        if (adSizeMap.containsKey(adSizeKey)) {
            return adSizeMap.get(adSizeKey);
        }

        AdSize existingAdSize = Query.from(AdSize.class)
            .where("getUniqueName = ?", adSizeKey)
            .and("cms.content.trashed = true || cms.content.trashed is missing")
            .and(site != null ? site.itemsPredicate() : PredicateParser.Static.parse("cms.site.owner = missing"))
            .first();

        AdSize adSize = existingAdSize != null ? existingAdSize : newAdSize;

        adSizeMap.put(adSizeKey, adSize);

        return adSize;
    }
}
