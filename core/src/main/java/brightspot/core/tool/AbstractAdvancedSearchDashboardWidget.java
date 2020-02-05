package brightspot.core.tool;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.servlet.ServletException;

import com.psddev.cms.db.Draft;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.tool.Dashboard;
import com.psddev.cms.tool.DashboardWidget;
import com.psddev.cms.tool.QueryRestriction;
import com.psddev.cms.tool.Search;
import com.psddev.cms.tool.SearchResultField;
import com.psddev.cms.tool.ToolPageContext;
import com.psddev.dari.db.ComparisonPredicate;
import com.psddev.dari.db.CompoundPredicate;
import com.psddev.dari.db.ObjectField;
import com.psddev.dari.db.ObjectIndex;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Predicate;
import com.psddev.dari.db.PredicateParser;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.Sorter;
import com.psddev.dari.db.State;
import com.psddev.dari.util.ClassFinder;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.PaginatedResult;
import com.psddev.dari.util.StringUtils;
import com.psddev.dari.util.TypeReference;
import com.psddev.dari.util.UuidUtils;

public abstract class AbstractAdvancedSearchDashboardWidget extends DashboardWidget {

    private static final TypeReference<Map<String, Object>> MAP_TYPE_REFERENCE = new TypeReference<Map<String, Object>>() {

    };

    private static final String CMS_UI_SEARCH_KEY = "cms.ui.search";

    public static final String RESULTS_PARAM = "_results";

    public abstract Query<?> getQuery();

    @Required
    private String title;

    @Required
    @Values({ "10", "20", "50" })
    private String limit = "10";

    @Required
    private List<AdvancedSearchField> fields;

    public String getTitle() {
        return title;
    }

    public int getLimit() {
        return Integer.valueOf(limit != null ? limit : "10");
    }

    public List<AdvancedSearchField> getFields() {
        if (fields == null) {
            fields = new ArrayList<>();
        }
        return fields;
    }

    @Override
    public void writeHtml(ToolPageContext page, Dashboard dashboard) throws IOException, ServletException {

        Query<?> query = getQuery();
        if (query == null) {
            return;
        }

        Object searchObj = query.getState().get(CMS_UI_SEARCH_KEY);
        if (searchObj == null) {
            searchObj = query.getState().getSimpleValues();
        }

        Map<String, Object> searchMap = ObjectUtils.to(MAP_TYPE_REFERENCE, searchObj);
        Search search = new Search(page);
        long offset = search.getOffset();
        search.getState().putAll(searchMap);
        search.setOffset(offset);
        search.setLimit(getLimit());

        if (search.getVisibilities().contains("d")) {

            Set<Object> workflowStates = new HashSet<>();

            for (String visibility : search.getVisibilities()) {
                if (visibility.startsWith("w.")) {
                    workflowStates.add(visibility.substring(2));
                }
            }

            List<Sorter> sorters = query.getSorters();

            boolean isShowInitialDraft = search.getVisibilities().contains("b.cms.content.draft");

            query = Query.fromAll();

            Predicate predicate = null;

            if (isShowInitialDraft) {

                predicate = CompoundPredicate.combine(
                    PredicateParser.OR_OPERATOR,
                    predicate,
                    getDraftCompatiblePredicate(
                        "cms.content.draft",
                        search.getSelectedType(),
                        Collections.singleton("true"))
                );
            }

            if (!ObjectUtils.isBlank(workflowStates)) {

                predicate = CompoundPredicate.combine(
                    PredicateParser.OR_OPERATOR,
                    predicate,
                    getDraftCompatiblePredicate("cms.workflow.currentState", search.getSelectedType(), workflowStates)
                );

            }

            Predicate searchPredicate = PredicateParser.Static.parse(search.getAdvancedQuery());

            if (search.getVisibilities().contains("p")) {

                predicate = CompoundPredicate.combine(
                    PredicateParser.OR_OPERATOR,
                    predicate,
                    PredicateParser.Static.parse(
                        "_type = ? or _type = ?",
                        search.getSelectedType().findConcreteTypes(),
                        ObjectType.getInstance(Draft.class))
                );
            }

            predicate = CompoundPredicate.combine(
                PredicateParser.AND_OPERATOR,
                predicate,
                excludeVisibilityPredicates(searchPredicate)
            );

            query.and(predicate);

            query.setSorters(sorters);
        }

        Query<?> clonedQuery = query.clone();
        clonedQuery.getState().put(CMS_UI_SEARCH_KEY, searchObj);

        AdvancedRenderer renderer = new AdvancedRenderer(
            page,
            clonedQuery,
            getFields(),
            search.getOffset(),
            search.getLimit(),
            getTitle());

        renderer.render();
    }

    private Predicate excludeVisibilityPredicates(Predicate predicate) {

        if (predicate == null) {
            return null;
        }

        if (predicate instanceof CompoundPredicate) {

            CompoundPredicate compoundPredicate = (CompoundPredicate) predicate;

            Predicate returnPredicate = null;

            List<Predicate> children = compoundPredicate.getChildren().stream()
                .map(this::excludeVisibilityPredicates)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

            for (Predicate child : children) {

                returnPredicate = CompoundPredicate.combine(compoundPredicate.getOperator(), returnPredicate, child);
            }

            return returnPredicate;

        } else if (predicate instanceof ComparisonPredicate) {

            ComparisonPredicate comparisonPredicate = (ComparisonPredicate) predicate;

            if ("_type".equals(comparisonPredicate.getKey())) {
                return null;
            }

            if ("_any".equals(comparisonPredicate.getKey()) && comparisonPredicate.getValues().contains("*")) {
                return null;
            }

            ObjectIndex objectIndex = predicate.getState()
                .getDatabase()
                .getEnvironment()
                .getIndex(comparisonPredicate.getKey());

            if (objectIndex != null && objectIndex.isVisibility()) {
                return null;
            }

            return predicate;
        }

        return predicate;
    }

    public Predicate getDraftCompatiblePredicate(String field, ObjectType selectedType, Set<Object> values) {

        Set<ObjectType> types = new HashSet<>(selectedType.findConcreteTypes());

        types.add(ObjectType.getInstance(Draft.class));

        Set<UUID> selectedTypeIds = createVisibilityTypeIds(selectedType, field, values);
        Set<UUID> draftTypeIds = createVisibilityTypeIds(ObjectType.getInstance(Draft.class), field, values);

        Predicate predicate = PredicateParser.Static.parse("_any matches *");

        if ("cms.content.draft".equals(field)) {
            draftTypeIds = Collections.singleton(ObjectType.getInstance(Draft.class).getId());
            predicate = CompoundPredicate.combine(
                PredicateParser.AND_OPERATOR,
                predicate,
                PredicateParser.Static.parse(
                    "(_type = ? and com.psddev.cms.db.Draft/objectType = ? and com.psddev.cms.db.Draft/newContent != true) or (_type = ? and "
                        + field + " = ?)",
                    draftTypeIds,
                    selectedType.findConcreteTypes(),
                    selectedTypeIds,
                    values)
            );
        } else {

            predicate = CompoundPredicate.combine(
                PredicateParser.AND_OPERATOR,
                predicate,
                PredicateParser.Static.parse(
                    field
                        + " = ? and (_type = ? and com.psddev.cms.db.Draft/objectType = ? and com.psddev.cms.db.Draft/newContent != true) or (_type = ?)",
                    values,
                    draftTypeIds,
                    selectedType.findConcreteTypes(),
                    selectedTypeIds)
            );
        }

        return predicate;
    }

    private static Set<UUID> createVisibilityTypeIds(ObjectType selectedType, String field, Set<Object> values) {
        Set<UUID> typeIds = new HashSet<>();

        for (ObjectType type : selectedType.findConcreteTypes()) {
            for (Object value : values) {
                byte[] md5 = StringUtils.md5(field + "/" + value.toString().trim().toLowerCase(Locale.ENGLISH));
                UUID typeId = type.getId();
                byte[] visibilityTypeId = UuidUtils.toBytes(typeId);

                for (int i = 0, length = visibilityTypeId.length; i < length; ++i) {
                    visibilityTypeId[i] ^= md5[i];
                }

                typeIds.add(UuidUtils.fromBytes(visibilityTypeId));
            }
        }

        return typeIds;
    }

    /**
     * Contains all the rendering logic for the AdvancedSearchDashboardWidget
     */
    public static class AdvancedRenderer {

        protected final ToolPageContext page;
        protected final long offset;
        protected final int limit;
        protected final PaginatedResult<?> result;
        protected final List<AdvancedSearchField> fields;
        protected final String title;

        protected Exception queryError;

        public AdvancedRenderer(
            ToolPageContext page,
            Query query,
            List<AdvancedSearchField> fields,
            long offset,
            int limit,
            String title) throws IOException {
            this.limit = limit;
            this.offset = offset;
            this.page = page;
            this.fields = fields != null ? new ArrayList<>(fields) : new ArrayList<>();
            this.title = title;

            QueryRestriction.updateQueryUsingAll(query, page);

            PaginatedResult<?> r = null;

            try {
                r = query.select(offset, limit);

            } catch (IllegalArgumentException | Query.NoFieldException error) {
                queryError = error;
            }

            result = r;
        }

        public void render() throws IOException {

            page.writeStart("div", "class", "widget");
            {

                if (queryError == null) {

                    // Title
                    page.writeStart("h1");
                    {
                        page.writeHtml(title);
                    }
                    page.writeEnd();

                    // Filters
                    page.writeStart("div", "class", "widget-filters");
                    {
                        for (Class<? extends QueryRestriction> c : QueryRestriction.classIterable()) {
                            page.writeQueryRestrictionForm(c);
                        }

                    }
                    page.writeEnd();

                    // Pagination
                    page.writeStart("div", "class", "searchPagination", "style", "top: 0;");
                    {
                        renderPagination();
                    }
                    page.writeEnd();

                    // Results
                    page.writeStart("div", "class", "searchResultList", "style", "float: none;");
                    {
                        if (result.hasPages()) {
                            renderList(result.getItems());
                        } else {
                            renderEmpty();
                        }
                    }
                    page.writeEnd();
                } else {
                    page.writeRaw(queryError);
                }

            }
            page.writeEnd();

        }

        public void renderList(Collection<?> listItems) throws IOException {
            List<Object> items = new ArrayList<Object>(listItems);

            if (!items.isEmpty()) {
                page.writeStart("table", "class", "searchResultTable links table-striped pageThumbnails");
                {
                    boolean printHeading = fields.stream().anyMatch(field -> field.getHeading() != null);
                    if (printHeading) {
                        page.writeStart("thead");
                        {
                            page.writeStart("tr");
                            {
                                for (AdvancedSearchField field : fields) {
                                    Integer width = field.getWidth();
                                    if (width != null) {
                                        page.writeStart("th", "width", width);
                                    } else {
                                        page.writeStart("th");
                                    }
                                    {
                                        String heading = field.getHeading();
                                        if (heading != null) {
                                            page.write(heading);
                                        }
                                    }
                                    page.writeEnd();
                                }
                            }
                            page.writeEnd();
                        }
                        page.writeEnd();
                    }
                    page.writeStart("tbody");
                    {
                        for (Object item : items) {
                            renderRow(item);
                        }
                    }
                    page.writeEnd();
                }
                page.writeEnd();
            }
        }

        protected void renderRow(Object item) throws IOException {
            State state = State.getInstance(item);

            if (state != null) {
                page.writeStart("tr");

                for (AdvancedSearchField field : fields) {
                    page.writeStart("td");
                    {
                        if (field instanceof LabelField) {
                            page.writeStart("a", "target", "_top", "href", page.objectUrl("/content/edit.jsp", item));
                            page.writeObjectLabel(item);
                            page.writeEnd();
                        } else {
                            renderValue(field.getDisplayValue(item));
                        }
                    }
                    page.writeEnd();
                }

                page.writeEnd();
            }
        }

        protected void renderValue(Object value) throws IOException {

            if (value instanceof String) {
                page.writeRaw(value);
            } else if (value instanceof Recordable) {
                page.writeObjectLabel(value);
            } else if (value instanceof Date) {

                page.write(
                    DateTimeUtils.printFriendlyDate(
                        ((Date) value).toInstant().atZone(
                            ToolUserUtils.getUserZoneIdOrDefault(
                                ToolUserUtils.getCurrentToolUser(), ZoneId.systemDefault()
                            )
                        )
                    )
                );

            } else if (value instanceof Collection) {
                long numElements = ((Collection) value).size();
                int i = 0;
                for (Object element : (Collection) value) {
                    renderValue(element);
                    if (++i < numElements) {
                        page.writeRaw(", ");
                    }
                }
            } else if (value != null) {
                page.writeRaw(value);
            }
        }

        public void renderPagination() throws IOException {
            page.writeStart("ul", "class", "pagination");

            if (result.hasPrevious()) {
                page.writeStart("li", "class", "previous");
                page.writeStart(
                    "a",
                    "href",
                    page.url("", RESULTS_PARAM, true, Search.OFFSET_PARAMETER, result.getPreviousOffset()));
                page.writeHtml("Previous ");
                page.writeHtml(result.getLimit());
                page.writeEnd();
                page.writeEnd();
            }

            page.writeStart("li");
            page.writeHtml(result.getFirstItemIndex());
            page.writeHtml(" to ");
            page.writeHtml(result.getLastItemIndex());
            page.writeHtml(" of ");
            page.writeStart("strong").writeHtml(result.getCount()).writeEnd();
            page.writeEnd();

            if (result.hasNext()) {
                page.writeStart("li", "class", "next");
                page.writeStart(
                    "a",
                    "href",
                    page.url("", RESULTS_PARAM, true, Search.OFFSET_PARAMETER, result.getNextOffset()));
                page.writeHtml("Next ");
                page.writeHtml(result.getLimit());
                page.writeEnd();
                page.writeEnd();
            }

            page.writeEnd();
        }

        public void renderEmpty() throws IOException {
            page.writeStart("div", "class", "message message-warning");
            page.writeStart("p");
            page.writeHtml("No matching items!");
            page.writeEnd();
            page.writeEnd();
        }
    }

    @Override
    public String getLabel() {
        return getTitle();
    }

    /**
     * All the types of fields that can display in an AdvancedSearchDashboardWidget
     */
    @Embedded
    public abstract static class AdvancedSearchField extends Record {

        public static final String ADVANCED_TAB = "Advanced";

        public abstract String getHeading();

        public abstract Integer getWidth();

        public abstract Object getDisplayValue(Object object);
    }

    /**
     * A custom field based of the field's internalName
     */
    public static class CustomField extends AdvancedSearchField {

        private String heading;

        @Required
        private String internalName;

        @ToolUi.Note("Leave blank for automatic width")
        private Integer width;

        @ToolUi.Tab(ADVANCED_TAB)
        @ToolUi.Note("For Date field types, the format can be customized w/ a valid pattern.")
        private String pattern;

        public String getInternalName() {
            return internalName;
        }

        @Override
        public String getHeading() {
            return heading;
        }

        @Override
        public Integer getWidth() {
            return width;
        }

        public String getPattern() {
            return pattern;
        }

        @Override
        public Object getDisplayValue(Object object) {
            State state = State.getInstance(object);
            if (state == null) {
                return null;
            }

            Object value;
            if (object instanceof Draft) {

                UUID objectId = ((Draft) object).getObjectId();

                Object original = Query.fromAll().where("_id = ?", objectId).first();

                if (original == null) {
                    return null;
                }

                ((Draft) object).merge(original);

                State originalState = State.getInstance(original);

                value = originalState.getByPath(internalName);
            } else {
                value = state.getByPath(internalName);
            }

            if (value instanceof Date && !StringUtils.isBlank(getPattern())) {
                Date date = ObjectUtils.to(Date.class, value);
                try {
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(getPattern());
                    ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(
                        date.toInstant(),
                        ZoneId.of("America/New_York"));
                    value = dateTimeFormatter.format(zonedDateTime);
                } catch (IllegalArgumentException e) {
                    // do nothing
                }
            }

            return value;
        }
    }

    /**
     * Field for the Label. Typically only need to use this once.
     */
    public static class LabelField extends AdvancedSearchField {

        public String heading;

        @ToolUi.Note("Leave blank for automatic width")
        public Integer width;

        @Override
        public String getLabel() {
            return "Label";
        }

        @Override
        public String getHeading() {
            return heading;
        }

        public String getInternalName() {
            return "getLabel()";
        }

        @Override
        public Integer getWidth() {
            return width;
        }

        @Override
        public Object getDisplayValue(Object object) {
            State state = State.getInstance(object);
            return state == null ? null : state.getLabel();
        }
    }

    /**
     * A {@link SearchResultField} search field
     */
    public static class OtherSearchResultField extends AdvancedSearchField {

        private String heading;
        @Required
        @DisplayName("Field")
        @SearchResultFieldPicker
        private String searchResultFieldClassName;
        private Integer width;

        @Override
        public String getHeading() {
            return heading;
        }

        @Override
        public Integer getWidth() {
            return width;
        }

        private transient SearchResultField searchResultField;

        private SearchResultField getSearchResultField() {
            if (searchResultField == null && searchResultFieldClassName != null) {
                Class<?> cls = null;
                try {
                    cls = Class.forName(searchResultFieldClassName);
                    Object obj = cls.newInstance();
                    searchResultField = (SearchResultField) obj;
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ignored) {
                    throw new RuntimeException("Invalid search result field class: " + searchResultFieldClassName);
                }
            }
            return searchResultField;
        }

        @Override
        public Object getDisplayValue(Object object) {
            State state = State.getInstance(object);
            if (state == null) {
                return null;
            }
            SearchResultField field = getSearchResultField();
            return field == null ? null : field.createDataCellText(state);
        }
    }

    @ObjectField.AnnotationProcessorClass(SearchResultFieldPickerProcessor.class)
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface SearchResultFieldPicker {

    }

    static class SearchResultFieldPickerProcessor implements ObjectField.AnnotationProcessor<SearchResultFieldPicker> {

        @Override
        public void process(ObjectType type, ObjectField field, SearchResultFieldPicker annotation) {
            Set<ObjectField.Value> values = new TreeSet<>();

            for (Class<?> cls : ClassFinder.findClasses(SearchResultField.class)) {
                ObjectField.Value value = new ObjectField.Value();
                value.setLabel(StringUtils.toLabel(cls.getSimpleName()));
                value.setValue(cls.getCanonicalName());
                values.add(value);
            }
            field.setValues(values);
        }
    }
}
