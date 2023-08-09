package brightspot.cascading;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import brightspot.page.TypeSpecificCascadingPageElements;
import brightspot.page.TypeSpecificOverridesSettings;
import brightspot.page.TypeSpecificPageElements;
import brightspot.page.TypeSpecificPageElementsModification;
import brightspot.section.HasSection;
import com.psddev.cms.db.Site;
import com.psddev.cms.tool.CmsTool;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.Singleton;

/**
 * Cascading settings strategy with the following order of precedence:
 *
 * 1) The asset 2) If the asset is HasSection, find the parent Section 2a) The Section type-specific overrides 2b) The
 * Section 2c) If the Section has a parent, the Section's parent, and so on 3) Find the Site 3a) Site type-specific
 * overrides 3b) Site 4) Find GlobalSettings 4a) GlobalSettings type-specific overrides 4b) GlobalSettings
 */
public class DefaultCascadingStrategy extends CascadingStrategy {

    @Override
    public String getNoteHtml() {
        return "Asset → Section → Section Parent → Site → Global Settings";
    }

    @Override
    protected <T> void doResolve(Object asset, Site site, List<CascadingResult<T>> queue) {

        String typeSpecificNote = null;
        if (asset instanceof Recordable) {
            String type = ((Recordable) asset).getState().getType().getDisplayName();
            typeSpecificNote = " (" + type + " override)";
        }

        // 1) Asset
        queue.add(new CascadingResult<>(asset, asset));

        // 2) Section and 2c) parents
        if (asset instanceof HasSection) {

            HasSection hasSection = (HasSection) asset;
            Collection<HasSection> visitedSections = new HashSet<>();

            while ((hasSection = hasSection.getSectionParent()) != null) {
                if (visitedSections.contains(hasSection)) {
                    break;
                }
                visitedSections.add(hasSection);

                // 2a) type specific
                if (hasSection instanceof TypeSpecificCascadingPageElements) {
                    queue.add(new CascadingResult<>(findPageTypePageElements(
                        asset,
                        (TypeSpecificCascadingPageElements) hasSection), hasSection, typeSpecificNote));
                }
                // 2b) section
                queue.add(new CascadingResult<>(hasSection, hasSection));
            }
        }

        // 3) Site
        if (site != null) {
            // 3a) type specific
            queue.add(new CascadingResult<>(
                filterTypeSpecificPageElements(
                    asset,
                    site.as(TypeSpecificOverridesSettings.class).getTypeSpecificOverrides()),
                site,
                typeSpecificNote));
            // 3b) site
            queue.add(new CascadingResult<>(site, site));
        }

        // 4) Global
        CmsTool globalSettings = Singleton.getInstance(CmsTool.class);
        // 4a) type specific
        queue.add(new CascadingResult<>(
            filterTypeSpecificPageElements(
                asset,
                globalSettings.as(TypeSpecificOverridesSettings.class).getTypeSpecificOverrides()),
            globalSettings,
            typeSpecificNote));
        // 4b) site
        queue.add(new CascadingResult<>(globalSettings, globalSettings));
    }

    private TypeSpecificPageElements findPageTypePageElements(
        Object asset,
        TypeSpecificCascadingPageElements typeSpecificCascadingPageElements) {
        return typeSpecificCascadingPageElements != null
            ? filterTypeSpecificPageElements(
            asset,
            typeSpecificCascadingPageElements.as(TypeSpecificPageElementsModification.class).getTypeSpecificOverrides())
            : null;
    }

    /**
     * Returns the first {@link TypeSpecificPageElements} in the set that matches the type of the given asset.
     */
    private TypeSpecificPageElements filterTypeSpecificPageElements(
        Object asset,
        Collection<TypeSpecificPageElements> typeSpecificPageElements) {
        if (typeSpecificPageElements != null && asset instanceof Recordable) {
            ObjectType type = ((Recordable) asset).getState().getType();
            Set<String> typeGroups = type.getGroups();
            for (TypeSpecificPageElements tspe : typeSpecificPageElements) {
                Set<ObjectType> types = tspe.getTypes();
                if (types.stream().anyMatch(t -> typeGroups.contains(t.getInternalName()))) {
                    return tspe;
                }
            }
        }

        return null;
    }
}
