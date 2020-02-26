package bex.training.movie.query;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import bex.training.movie.Phase;
import brightspot.core.listmodule.DynamicItemStream;
import brightspot.core.listmodule.DynamicQueryModifier;
import brightspot.core.section.Section;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.LazyLoad;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;

@Recordable.FieldInternalNamePrefix("phases.")
@Modification.Classes({DynamicItemStream.class})
@LazyLoad
public class PhaseDynamicQueryModifier extends Modification<Object> implements DynamicQueryModifier {

    // Main.

    @ToolUi.DropDown
    @ToolUi.Cluster("Hierarchy")
    private Set<Phase> phases;

    // Getters and Setters.

    public Set<Phase> getPhases() {
        if (phases == null) {
            phases = new HashSet<>();
        }

        return phases;
    }

    public void setPhases(Set<Phase> phases) {
        this.phases = phases;
    }

    // Overrides.

    @Override
    public void updateQuery(Site site, Object mainObject, Query<?> query) {
        if (!ObjectUtils.isBlank(getPhases())) {
            query.where("bex.training.movie.Movie/phase != missing AND bex.training.movie.Movie/phase = ?", getPhases());
        }
    }

    @Override
    public String createLabel() {
        return getPhases().stream()
                .map(Section::getLabel)
                .collect(Collectors.joining(", "));
    }
}
