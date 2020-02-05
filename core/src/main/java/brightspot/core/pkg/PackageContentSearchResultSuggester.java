package brightspot.core.pkg;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import brightspot.core.search.NoSearchResultSuggester;
import com.psddev.cms.db.Content;
import com.psddev.cms.tool.Search;
import com.psddev.cms.tool.SearchResultRenderer;
import com.psddev.cms.tool.SearchResultSuggester;
import com.psddev.cms.tool.ToolPageContext;
import com.psddev.dari.db.Database;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.State;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StringUtils;

public class PackageContentSearchResultSuggester implements SearchResultSuggester {

    @Override
    public double getPriority(Search search) {
        return NoSearchResultSuggester.MINIMUM_SEARCH_RESULT_SUGGESTER_PRIORITY + 10;
    }

    @Override
    public void writeHtml(Search search, ToolPageContext page) throws IOException {

        Object object = getParentObject(page);

        if (!(object instanceof Package)) {
            return;
        }

        Set<ObjectType> searchTypes = search.getTypes();

        Set<ObjectType> concreteTypes = new HashSet<>();

        for (ObjectType searchType : searchTypes) {
            concreteTypes.addAll(searchType.findConcreteTypes());
        }

        State objectState = State.getInstance(object);

        List<State> states = Query.fromAll().where(Packageable.class.getName() + "/packageable.pkg = ?", objectState)
            .select(search.getOffset(), search.getLimit())
            .getItems()
            .stream()
            .map(State::getInstance)
            .filter(s -> concreteTypes.isEmpty() || concreteTypes.contains(s.getType()))
            .sorted(Comparator.comparing(
                s -> s.as(Content.ObjectModification.class).getPublishDate(),
                Comparator.reverseOrder()))
            .collect(Collectors.toList());

        page.writeStart("div", "class", "searchSuggestions");
        {
            page.writeStart("h2");
            {
                page.writeHtml("Package Content");
            }
            page.writeEnd();
            new SearchResultRenderer(page, search).renderList(states);
        }
        page.writeEnd();
    }

    public Object getParentObject(ToolPageContext page) {

        String objectString = page.param(String.class, "object");

        if (StringUtils.isBlank(objectString)) {
            return null;
        }

        Map<String, Object> objectData = (Map<String, Object>) ObjectUtils.fromJson(objectString);

        if (ObjectUtils.isBlank(objectData)) {
            return null;
        }

        Object object = Database.Static.getDefault()
            .getEnvironment()
            .createObject(
                ObjectUtils.to(UUID.class, objectData.get("_type")),
                ObjectUtils.to(UUID.class, objectData.get("_id")));
        State objectState = State.getInstance(object);

        objectState.setValues(objectData);

        return object;
    }
}
