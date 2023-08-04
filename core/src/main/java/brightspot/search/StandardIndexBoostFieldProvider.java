package brightspot.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import brightspot.author.HasAuthorsData;
import brightspot.author.PersonAuthor;
import brightspot.person.PersonPage;
import brightspot.search.boost.HasSiteSearchBoostIndexesData;
import brightspot.search.boost.IndexBoost;
import brightspot.search.boost.IndexBoostField;
import brightspot.search.boost.IndexBoostFieldProvider;
import brightspot.util.DefaultImplementationSupplier;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.psddev.cms.db.Content;
import com.psddev.dari.db.ObjectIndex;
import com.psddev.dari.db.ObjectStruct;

/**
 * Returns a curated set of indexes for use with boosting.
 *
 * The fields will be filtered to only those matching the type of the given {@link IndexBoost#getIndexFieldType()}.
 */
public class StandardIndexBoostFieldProvider implements IndexBoostFieldProvider {

    // map of fully-qualified class name to set of indexed field internal names
    private static final Map<String, Set<String>> INDEXES = ImmutableMap.of(
        Content.ObjectModification.class.getName(), Collections.singleton(Content.PUBLISH_DATE_FIELD),
        HasAuthorsData.class.getName(), Collections.singleton(HasAuthorsData.AUTHOR_NAMES_FIELD),
        HasSiteSearchBoostIndexesData.class.getName(), ImmutableSet.of(
            HasSiteSearchBoostIndexesData.SEARCH_BOOST_DESCRIPTION_FIELD,
            HasSiteSearchBoostIndexesData.SEARCH_BOOST_TITLE_FIELD),
        PersonAuthor.class.getName(), ImmutableSet.of("firstName", "lastName"),
        PersonPage.class.getName(), ImmutableSet.of("firstName", "lastName")
    );

    @Override
    public Collection<IndexBoostField> getIndexedFields(IndexBoost indexBoost, SiteSearch siteSearch) {

        if (siteSearch == null) {
            return Collections.emptyList();
        }

        IncludeTypes includeTypes = Optional.ofNullable(siteSearch.getSiteSearchTypes())
            .orElseGet(() -> DefaultImplementationSupplier.createDefault(IncludeTypes.class, null));

        Set<ObjectStruct> objectTypes = new HashSet<>();

        objectTypes.addAll(Optional.ofNullable(includeTypes)
            .map(IncludeTypes::getIncludedTypes)
            .orElse(Collections.emptyList()));

        objectTypes.add(indexBoost.getState().getDatabase().getEnvironment());

        List<IndexBoostField> indexedFields = new ArrayList<>();

        for (ObjectStruct type : objectTypes) {
            for (ObjectIndex index : type.getIndexes()) {
                if (Objects.equals(index.getType(), indexBoost.getIndexFieldType())) {
                    Set<String> internalNames = INDEXES.getOrDefault(
                        index.getJavaDeclaringClassName(),
                        Collections.emptySet());
                    if (internalNames.contains(index.getField())) {
                        indexedFields.add(new IndexBoostField(type, index));
                    }
                }
            }
        }

        return indexedFields;
    }
}
