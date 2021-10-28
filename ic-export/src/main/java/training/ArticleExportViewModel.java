package training;

import java.util.Optional;

import brightspot.core.article.Article;
import brightspot.core.article.RichTextBody;
import com.psddev.cms.view.JsonView;
import com.psddev.cms.view.ViewInterface;
import com.psddev.cms.view.ViewKey;
import com.psddev.dari.util.ObjectUtils;
import org.apache.commons.text.StringEscapeUtils;

@JsonView
@ViewInterface
public class ArticleExportViewModel extends PageExportViewModel<Article> implements ExportEntryView {

    @ViewKey("headline")
    public CharSequence getHeadline() {
        return StringEscapeUtils.escapeHtml4(model.getHeadline());
    }

    @ViewKey("subheadline")
    public CharSequence getSubheadline() {
        return StringEscapeUtils.escapeHtml4(model.getSubHeadline());
    }

    @ViewKey("body")
    public CharSequence getBody() {
        if (Optional.ofNullable(model.getBody())
            .filter(b -> !(b instanceof RichTextBody))
            .isPresent()) {
            throw new IllegalArgumentException("Unhandled ArticleBody type " + model.getBody().getClass().getName());
        }

        return ExportUtils.processRichText(
            model,
            a -> Optional.ofNullable(a.getBody())
                .map(RichTextBody.class::cast)
                .map(RichTextBody::getRichText)
                .orElse(null));
    }

    @ViewKey("lead")
    public ArticleLeadView getLead() {
        return createView(ArticleLeadView.class, model.getLead());
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
