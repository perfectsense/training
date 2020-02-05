package brightspot.core.tag;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import brightspot.core.classification.ClassificationDataItem;
import brightspot.core.tool.TaxonUtils;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;

@Recordable.FieldInternalNamePrefix("taggable.")
public class TaggableData extends Modification<Taggable> implements ClassificationDataItem {

    private static final String FIELD_PREFIX = "taggable.";

    public static final String TAGS_AND_ANCESTORS_FIELD = FIELD_PREFIX + "getTagsAndAncestors";
    public static final String VISIBLE_TAGS_AND_ANCESTORS_FIELD = FIELD_PREFIX + "getVisibleTagsAndAncestors";

    private List<Tag> tags;

    public List<Tag> getTags() {
        if (tags == null) {
            tags = new ArrayList<>();
        }
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    @ToolUi.Hidden
    @ToolUi.Filterable
    @Indexed
    @DisplayName("Tag")
    public Set<Tag> getTagsAndAncestors() {
        return TaxonUtils.getTaxonsAndAncestors(getTags());
    }

    @ToolUi.Hidden
    @Indexed
    public Set<Tag> getVisibleTagsAndAncestors() {

        return getTagsAndAncestors()
            .stream()
            .filter(tag -> !tag.isHidden())
            .collect(Collectors.toSet());
    }

    @Override
    public Set<Recordable> getClassificationAttributes() {
        return new HashSet<>(getTagsAndAncestors());
    }

}
