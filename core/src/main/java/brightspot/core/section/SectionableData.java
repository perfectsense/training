package brightspot.core.section;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import brightspot.core.classification.ClassificationDataItem;
import brightspot.core.hierarchy.Hierarchy;
import brightspot.core.tool.TaxonUtils;
import com.google.common.collect.Lists;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.ConditionallyValidatable;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.ObjectField;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;

@Recordable.FieldInternalNamePrefix("sectionable.")
public class SectionableData extends Modification<Sectionable> implements
    ClassificationDataItem,
    ConditionallyValidatable {

    private static final String FIELD_PREFIX = "sectionable.";

    public static final String SECTION_AND_ANCESTORS_FIELD = FIELD_PREFIX + "getSectionAndAncestors";

    // @ToolUi.DropDown TODO: BSP-1515
    @Indexed
    private Section section;

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    @Override
    public boolean shouldValidate(ObjectField objectField) {
        return !ObjectUtils.equals("section", objectField.getInternalName());
    }

    /**
     * Returns the {@link List} of {@link Section Sections} representing the ancestors (or "Breadcrumbs") of the {@link
     * Sectionable} in order from the most distant ancestor {@link Section} to the {@link Sectionable}'s {@link
     * Section}.
     *
     * @return a {@link List} of {@link Section Sections} or {@code null}, if the section is blank.
     */
    public List<Hierarchy> getBreadcrumbs() {
        Section currentSection = getSection();
        return Optional.ofNullable(currentSection)
            .map(s -> TaxonUtils.getAncestors(s, t ->
                Optional.ofNullable(t.getParents()).orElseGet(Collections::emptySet).stream()
                    .filter(Section.class::isInstance)
                    .map(Section.class::cast)
                    .collect(Collectors.toSet())))
            .map(Lists::reverse)
            .map(list -> {
                list.add(currentSection);
                return list;
            })
            .map(list -> list.stream()
                .map(Hierarchy.class::cast)
                .collect(Collectors.toList())
            )
            .orElse(new ArrayList<>());
    }

    @ToolUi.Hidden
    @ToolUi.Filterable
    @Indexed
    @DisplayName("Section Hierarchy")
    public Set<? extends Hierarchy> getSectionAndAncestors() {

        return Optional.ofNullable(section)
            .map(Collections::singleton)
            .map(TaxonUtils::getTaxonsAndAncestors)
            .orElse(null);
    }

    @Override
    public Set<Recordable> getClassificationAttributes() {
        return new HashSet<>(Optional.ofNullable(getSectionAndAncestors())
            .orElse(Collections.emptySet()));
    }
}
