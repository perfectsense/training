package brightspot.core.byline;

import java.util.List;

import brightspot.core.person.Author;
import brightspot.core.person.AuthorByline;
import brightspot.core.tool.DefaultImplementationSupplier;
import com.psddev.cms.db.Site;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.StorageItem;

@Deprecated
@Recordable.Embedded
public abstract class Byline extends Record {

    public abstract String getBylineUrl(Site site);

    public String getBylineName() {
        return getLabel();
    }

    public abstract StorageItem getBylineImage();

    public abstract String getBylineBiography();

    public abstract List<Byline> getBylineOthers();

    public abstract List<Author> getAuthors();

    public static Byline createDefault() {
        return DefaultImplementationSupplier.createDefault(Byline.class, AuthorByline.class);
    }
}
