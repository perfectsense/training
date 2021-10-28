package training;

import brightspot.core.person.Author;
import com.psddev.cms.view.JsonView;
import com.psddev.cms.view.ViewInterface;
import com.psddev.cms.view.ViewKey;
import org.apache.commons.text.StringEscapeUtils;

@JsonView
@ViewInterface
public class AuthorExportViewModel extends PageExportViewModel<Author> implements ExportEntryView {

    @ViewKey("name")
    public CharSequence getName() {
        return StringEscapeUtils.escapeHtml4(model.getName());
    }

    @ViewKey("image")
    public RefView getImage() {
        return createView(RefView.class, model.getImage());
    }

    @ViewKey("title")
    public CharSequence getTitle() {
        return StringEscapeUtils.escapeHtml4(model.getTitle());
    }

    @ViewKey("fullBiography")
    public CharSequence getFullBiography() {
        return ExportUtils.processRichText(model, Author::getFullBiography);
    }

    @ViewKey("shortBiography")
    public CharSequence getShortBiography() {
        return ExportUtils.processRichText(model, Author::getShortBiography);
    }

    @ViewKey("affiliation")
    public CharSequence getAffiliation() {
        return StringEscapeUtils.escapeHtml4(model.getAffiliation());
    }
}
