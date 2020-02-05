package brightspot.core.cascading;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import brightspot.core.hierarchy.Hierarchical;
import brightspot.core.page.TypeSpecificCascadingPageElements;
import brightspot.core.page.TypeSpecificFrontEndSettingsModification;
import brightspot.core.page.TypeSpecificPageElements;
import brightspot.core.page.TypeSpecificPageElementsModification;
import brightspot.core.pkg.Package;
import brightspot.core.pkg.Packageable;
import brightspot.core.pkg.PackageableData;
import brightspot.core.site.FrontEndSettings;
import com.psddev.cms.db.Site;
import com.psddev.cms.tool.CmsTool;
import com.psddev.dari.db.Application;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Recordable;

/**
 * Cascading settings strategy with the following order of precedence:
 *
 * 1) The asset 2) If the asset is Packageable, find the Package 2a) The Package type-specific overrides 2b) The Package
 * 3) If the asset is Hierarchical, find the parent Hiearchy (Section) 3a) The Section type-specific overrides 3b) The
 * Section 3c) If the Section has a parent, the Section's parent, and so on 4) Find the Site 4a) Site type-specific
 * overrides 4b) Site 5) Find GlobalSettings 5a) GlobalSettings type-specific overrides 5b) GlobalSettings
 */
public class DefaultCascadingStrategy extends CascadingStrategy {

    @Override
    public String getNoteHtml() {
        return "Asset → Package → Section → Section Parent → Site → Global Settings";
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

        // 2) Package
        if (asset instanceof Packageable) {
            Package pkg = ((Packageable) asset).as(PackageableData.class).getPkg();
            if (pkg != null) {
                // 2a) type specific
                queue.add(new CascadingResult<>(findPageTypePageElements(asset, pkg), pkg, typeSpecificNote));
                // 2b) package
                queue.add(new CascadingResult<>(pkg, pkg));
            }
        }

        // 3) Section and 3c) parents
        if (asset instanceof Hierarchical) {
            Hierarchical section = ((Hierarchical) asset).getHierarchicalParent();
            Collection<Hierarchical> visitedSections = new HashSet<>();
            do {
                if (visitedSections.contains(section)) {
                    break;
                }
                visitedSections.add(section);

                // 3a) type specific
                if (section instanceof TypeSpecificCascadingPageElements) {
                    queue.add(new CascadingResult<>(findPageTypePageElements(
                        asset,
                        (TypeSpecificCascadingPageElements) section), section, typeSpecificNote));
                }
                // 3b) section
                queue.add(new CascadingResult<>(section, section));
            } while (section != null && (section = section.getHierarchicalParent()) != null);
        }

        // 4) Site
        if (site != null) {
            // 4a) type specific
            queue.add(new CascadingResult<>(
                filterTypeSpecificPageElements(
                    asset,
                    site.as(TypeSpecificFrontEndSettingsModification.class).getTypeSpecificOverrides()),
                site,
                typeSpecificNote));
            // 4b) site
            queue.add(new CascadingResult<>(site, site));
        }

        // 5) Global
        CmsTool globalSettings = Application.Static.getInstance(CmsTool.class);
        // 5a) type specific
        queue.add(new CascadingResult<>(
            filterTypeSpecificPageElements(
                asset,
                globalSettings.as(TypeSpecificFrontEndSettingsModification.class).getTypeSpecificOverrides()),
            globalSettings,
            typeSpecificNote));
        // 5b) site
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

    private TypeSpecificPageElements findFrontEndSettingsTypePageElements(
        Object asset,
        FrontEndSettings frontEndSettings) {
        return frontEndSettings != null
            ? filterTypeSpecificPageElements(
            asset,
            frontEndSettings.as(TypeSpecificFrontEndSettingsModification.class).getTypeSpecificOverrides())
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
