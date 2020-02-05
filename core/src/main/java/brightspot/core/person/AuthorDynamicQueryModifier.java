package brightspot.core.person;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import brightspot.core.classification.ClassificationDynamicQueryModifier;
import brightspot.core.listmodule.DynamicItemStream;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;

@Recordable.FieldInternalNamePrefix("authors.")
@Modification.Classes({ DynamicItemStream.class, AuthorDynamicQueryModifiable.class })
public class AuthorDynamicQueryModifier extends Modification<Object> implements ClassificationDynamicQueryModifier {

    @ToolUi.DropDown // TODO: BSP-1515
    @ToolUi.Cluster(CLASSIFICATION_CLUSTER)
    private Set<PersonOrCurrentPerson> authors;

    public Set<PersonOrCurrentPerson> getAuthors() {
        if (authors == null) {
            authors = new HashSet<>();
        }
        return authors;
    }

    public void setAuthors(Set<PersonOrCurrentPerson> authors) {
        this.authors = authors;
    }

    @Override
    public void updateQuery(Site site, Object mainObject, Query<?> query) {

        this.updateQueryWithClassificationQuery(query, mainObject, getAuthors(), CurrentPerson.class, Author.class);
    }

    @Override
    public String createLabel() {
        return getAuthors().stream()
            .map(author -> author.getState().getLabel())
            .collect(Collectors.joining(", "));
    }
}
