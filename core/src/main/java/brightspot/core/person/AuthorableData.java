package brightspot.core.person;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import brightspot.core.byline.Byline;
import brightspot.core.byline.OneOffByline;
import brightspot.core.classification.ClassificationDataItem;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;

@Recordable.FieldInternalNamePrefix("authorable.")
public class AuthorableData extends Modification<Authorable> implements ClassificationDataItem {

    private static final String FIELD_PREFIX = "authorable.";

    public static final String AUTHORS_FIELD = FIELD_PREFIX + "getAuthors";

    @Indexed
    @ToolUi.Filterable
    @DisplayName("Author(s)")
    private List<Author> authors;

    @Indexed
    @Deprecated
    @DisplayName("Byline (Deprecated)")
    private Byline byline;

    @Deprecated
    public Byline getByline() {
        return byline;
    }

    @Deprecated
    public void setByline(Byline byline) {
        this.byline = byline;
    }

    public void setAuthors(List<Author> authorsByline) {
        this.authors = authorsByline;
    }

    @Indexed
    @ToolUi.Hidden
    @Relocate
    public List<Author> getAuthors() {
        // preserve existing instances of one off byline
        if (byline instanceof OneOffByline) {
            return byline.getAuthors();
        }

        if (authors == null) {
            authors = new ArrayList<>();
        }

        // relocate data
        if (byline != null) {
            authors.addAll(byline.getAuthors());
            byline = null;
        }

        return authors;
    }

    @Override
    public Set<Recordable> getClassificationAttributes() {
        return new HashSet<>(getAuthors());
    }
}
