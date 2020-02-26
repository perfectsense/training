package brightspot.core.byline;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import brightspot.core.image.ImageOption;
import brightspot.core.person.Author;
import brightspot.core.tool.SmallRichTextToolbar;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.StorageItem;

@Deprecated
@Recordable.DisplayName("One-Off")
public class OneOffByline extends Byline {

    @Required
    private String name;

    private ImageOption image;

    @ToolUi.RichText(toolbar = SmallRichTextToolbar.class)
    private String biography;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ImageOption getImage() {
        return image;
    }

    public void setImage(ImageOption image) {
        this.image = image;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    @Override
    public String getBylineUrl(Site site) {
        return null;
    }

    @Override
    public StorageItem getBylineImage() {
        return Optional.ofNullable(image)
            .map(ImageOption::getImageOptionFile)
            .orElse(null);
    }

    @Override
    public String getBylineBiography() {
        return getBiography();
    }

    @Override
    public List<Byline> getBylineOthers() {
        return Collections.emptyList();
    }

    @Override
    public List<Author> getAuthors() {
        Author author = new Author();
        author.setName(this.getName());
        author.setImage(this.getImage());
        author.setShortBiography(this.getBiography());

        return Collections.singletonList(author);
    }

    @Override
    public String getBylineName() {
        return getName();
    }
}
