package brightspot.core.tag;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import brightspot.core.imageitemstream.DynamicImageItemStream;
import brightspot.core.listmodule.DynamicItemStream;
import brightspot.core.listmodule.DynamicQueryModifier;
import brightspot.core.timedcontentitemstream.DynamicTimedContentItemStream;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;

@Recordable.FieldInternalNamePrefix("tags.")
@Modification.Classes({
    DynamicItemStream.class,
    DynamicImageItemStream.class,
    DynamicTimedContentItemStream.class,
    TagDynamicQueryModifiable.class })
public class TagDynamicQueryModifier extends Modification<Object> implements
    DynamicQueryModifier {

    @ToolUi.DropDown
    private Set<TagOrCurrentTags> tags;

    public Set<TagOrCurrentTags> getTags() {
        if (tags == null) {
            tags = new HashSet<>();
        }
        return tags;
    }

    public void setTags(Set<TagOrCurrentTags> tags) {
        this.tags = tags;
    }

    @Override
    public void updateQuery(Site site, Object mainObject, Query<?> query) {
        Set<TagOrCurrentTags> tags = new HashSet<>(getTags());
        CurrentTags currentTags = Query.from(CurrentTags.class).first();
        boolean useCurrentTags = tags.remove(currentTags);

        if (useCurrentTags) {
            Set<Tag> thisTags = new HashSet<>();
            if (mainObject instanceof Taggable) {
                Optional.ofNullable(((Taggable) mainObject).asTaggableData())
                    .map(TaggableData::getTags)
                    .ifPresent(thisTags::addAll);

            } else if (mainObject instanceof Tag) {
                thisTags.add((Tag) mainObject);
            }

            tags.addAll(thisTags);
        }

        if (!tags.isEmpty()) {
            query.where(TaggableData.class.getName() + "/" + TaggableData.TAGS_AND_ANCESTORS_FIELD + " = ?", tags);

            if (mainObject instanceof Recordable) {

                // Filter current object
                query.and("id != " + ((Recordable) mainObject).getState().getId());
            }
        } else if (useCurrentTags) {
            // ONLY Cause empty set to be returned (IDs are never null)
            // IF no tags were found AND ONLY the current tags were specified
            query.where("id = null");
        }
    }

    @Override
    public String createLabel() {
        return getTags().stream()
            .map(tag -> tag.getState().getLabel())
            .collect(Collectors.joining(", "));
    }
}
