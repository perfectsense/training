package brightspot.core.person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import brightspot.core.byline.Byline;
import brightspot.core.image.ImageOption;
import brightspot.core.tool.DirectoryItemUtils;
import com.psddev.cms.db.Site;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.StorageItem;

@Deprecated
@Recordable.DisplayName("Authors")
public class AuthorsByline extends Byline {

    @Indexed
    // @ToolUi.DropDown TODO: BSP-1515
    private List<Author> authors;

    @Override
    public List<Author> getAuthors() {
        if (authors == null) {
            authors = new ArrayList<>();
        }
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    @Override
    public String getLabel() {
        return getAuthors()
            .stream()
            .map(Author::getLabel)
            .collect(Collectors.joining(", "));
    }

    private Optional<Author> getPrimaryAuthor() {
        return getAuthors().stream().findFirst();
    }

    @Override
    public String getBylineUrl(Site site) {
        return getPrimaryAuthor()
            .map(author -> DirectoryItemUtils.getCanonicalUrl(site, author))
            .orElse(null);
    }

    @Override
    public String getBylineName() {
        return getPrimaryAuthor()
            .map(Author::getName)
            .orElse(null);
    }

    @Override
    public StorageItem getBylineImage() {
        return getPrimaryAuthor()
            .map(Author::getImage)
            .map(ImageOption::getImageOptionFile)
            .orElse(null);
    }

    @Override
    public String getBylineBiography() {
        return getPrimaryAuthor()
            .map(Author::getShortBiography)
            .orElse(null);
    }

    @Override
    public List<Byline> getBylineOthers() {
        List<Author> authors = getAuthors();
        int authorsSize = authors.size();

        if (authorsSize < 2) {
            return Collections.emptyList();
        }

        return authors.subList(1, authorsSize).stream()
            .map(a -> {
                AuthorByline b = new AuthorByline();
                b.setAuthor(a);
                return b;
            })
            .collect(Collectors.toList());
    }
}
