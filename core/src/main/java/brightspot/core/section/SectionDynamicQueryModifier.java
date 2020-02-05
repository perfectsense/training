package brightspot.core.section;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import brightspot.core.hierarchy.HierarchicalDynamicQueryModifier;
import brightspot.core.listmodule.DynamicItemStream;
import brightspot.core.timedcontentitemstream.DynamicTimedContentItemStream;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;

@Recordable.FieldInternalNamePrefix("sections.")
@Modification.Classes({
    DynamicItemStream.class,
    DynamicTimedContentItemStream.class,
    SectionDynamicQueryModifiable.class })
public class SectionDynamicQueryModifier extends Modification<Object> implements
    HierarchicalDynamicQueryModifier {

    @ToolUi.DropDown
    @ToolUi.Cluster(HIERARCHICAL_CLUSTER)
    private Set<SectionOrCurrentSection> sections;

    public Set<SectionOrCurrentSection> getSections() {
        if (sections == null) {
            sections = new HashSet<>();
        }
        return sections;
    }

    public void setSections(Set<SectionOrCurrentSection> sections) {
        this.sections = sections;
    }

    @Override
    public void updateQuery(Site site, Object mainObject, Query<?> query) {

        this.updateQueryWithHierarchicalQuery(query, mainObject, getSections(), CurrentSection.class, Section.class);
    }

    @Override
    public String createLabel() {
        return getSections().stream()
            .map(section -> section.getState().getLabel())
            .collect(Collectors.joining(", "));
    }
}
