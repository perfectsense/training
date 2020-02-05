package brightspot.core.person;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import brightspot.core.byline.Byline;
import brightspot.core.image.ImageOption;
import brightspot.core.tool.DirectoryItemUtils;
import com.psddev.cms.db.Site;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.StorageItem;

@Deprecated
@Recordable.DisplayName("Author")
public class AuthorByline extends Byline {

    @Indexed
    // @ToolUi.DropDown TODO: BSP-1515
    private Author author;

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @Override
    public String getLabel() {
        return Optional.ofNullable(getAuthor())
            .map(AbstractPerson::getLabel)
            .orElseGet(super::getLabel);
    }

    @Override
    public String getBylineUrl(Site site) {
        return Optional.ofNullable(getAuthor())
            .map(person -> DirectoryItemUtils.getCanonicalUrl(site, person))
            .orElse(null);
    }

    @Override
    public StorageItem getBylineImage() {
        return Optional.ofNullable(getAuthor())
            .map(Author::getImage)
            .map(ImageOption::getImageOptionFile)
            .orElse(null);
    }

    @Override
    public String getBylineBiography() {
        return Optional.ofNullable(getAuthor())
            .map(Author::getShortBiography)
            .orElse(null);
    }

    @Override
    public List<Byline> getBylineOthers() {
        return Collections.emptyList();
    }

    @Override
    public List<Author> getAuthors() {
        if (author != null) {
            return Collections.singletonList(author);
        }
        return Collections.emptyList();
    }

    @Override
    public String getBylineName() {
        return Optional.ofNullable(getAuthor())
            .map(Author::getName)
            .orElse(null);
    }
}
