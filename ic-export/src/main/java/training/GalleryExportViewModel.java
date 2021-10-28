package training;

import brightspot.core.gallery.Gallery;
import com.psddev.cms.view.JsonView;
import com.psddev.cms.view.ViewInterface;
import com.psddev.cms.view.ViewKey;
import com.psddev.dari.util.ObjectUtils;
import org.apache.commons.text.StringEscapeUtils;

@JsonView
@ViewInterface
public class GalleryExportViewModel extends PageExportViewModel<Gallery> implements ExportEntryView {

    @ViewKey("title")
    public CharSequence getTitle() {
        return StringEscapeUtils.escapeHtml4(model.getHeadline());
    }

    @ViewKey("description")
    public CharSequence getDescription() {
        return StringEscapeUtils.escapeHtml4(model.getSubHeadline());
    }

    @ViewKey("items")
    public AdvancedImageItemStreamViewModel getItems() {
        return createView(AdvancedImageItemStreamViewModel.class, model.getItemStream());
    }

    @ViewKey("hasUrlSlug.urlSlug")
    public String getUrlSlug() {
        return ObjectUtils.to(String.class, model.getState().get("sluggable.slug"));
    }

    @ViewKey("hasAuthorsWithField.authors")
    public Iterable<RefView> getAuthors() {
        return createViews(RefView.class, model.asAuthorableData().getAuthors());
    }

    @ViewKey("hasSectionWithField.section")
    public RefView getSection() {
        return createView(RefView.class, model.getSection());
    }

    //@ViewKey("hasSecondarySectionsWithField.secondarySections")
    //public List<RefViewModel> getSecondarySections() {
    //    return null;
    //}

    @ViewKey("hasTags.tags")
    public Iterable<RefView> getTags() {
        return createViews(RefView.class, model.asTaggableData().getTags());
    }
}
