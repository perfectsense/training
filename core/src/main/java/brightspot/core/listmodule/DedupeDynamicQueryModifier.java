package brightspot.core.listmodule;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;

import brightspot.core.imageitemstream.DynamicImageItemStream;
import brightspot.core.module.ModuleType;
import brightspot.core.timedcontentitemstream.DynamicTimedContentItemStream;
import com.psddev.cms.db.Localization;
import com.psddev.cms.db.PageFilter;
import com.psddev.cms.db.Site;
import com.psddev.cms.view.DataModelWrapper;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.State;
import com.psddev.dari.util.PageContextFilter;

/**
 * The class removes any content that has been previously rendered on the page.
 */
@Modification.Classes({
    DynamicItemStream.class,
    DynamicImageItemStream.class,
    DynamicTimedContentItemStream.class,
    DedupeDynamicQueryModifiable.class })
public class DedupeDynamicQueryModifier extends Modification<Object> implements DynamicQueryModifier {

    @Override
    public void updateQuery(Site site, Object mainObject, Query<?> query) {
        HttpServletRequest request = PageContextFilter.Static.getRequestOrNull();
        if (request != null) {
            Set<UUID> renderedIds = PageFilter.getRenderedObjects(request)
                .stream()
                .map(o -> o instanceof DataModelWrapper ? ((DataModelWrapper) o).unwrap() : o)
                .filter(Recordable.class::isInstance)
                .map(Recordable.class::cast)
                .flatMap(r -> {
                    if (r instanceof ModuleType) {
                        return Query.from(Recordable.class)
                            .where("_id = ?", ((ModuleType) r).getModuleTypeContentIds())
                            .selectAll()
                            .stream()
                            .map(Recordable::getState)
                            .filter(s -> s.getStatus() != null)
                            .map(State::getId);
                    } else {
                        State currentState = r.getState();
                        if (currentState.getStatus() != null) {
                            return Stream.of(currentState.getId());
                        }
                    }

                    return null;
                })
                .filter(Objects::nonNull)
                .map(UUID.class::cast)
                .collect(Collectors.toSet());

            query.and("_id != ?", renderedIds);
        }
    }

    @Override
    public String createLabel() {
        return Localization.currentUserText(getClass(), "label.filterContent",
            "Filter Content that has already rendered on the current page.");
    }
}
