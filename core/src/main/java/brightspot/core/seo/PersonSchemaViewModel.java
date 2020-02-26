package brightspot.core.seo;

import brightspot.core.image.ImageOptionViewModel;
import brightspot.core.person.Author;
import com.psddev.cms.db.Site;
import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.jsonld.JsonLdNode;
import com.psddev.cms.view.jsonld.JsonLdType;
import com.psddev.cms.view.servlet.CurrentSite;

@JsonLdType("Person")
public class PersonSchemaViewModel extends ViewModel<Author> implements PersonSchemaView {

    @CurrentSite
    private Site site;

    @JsonLdNode
    public CharSequence getName() {
        return model.getName();
    }

    @JsonLdNode
    public CharSequence getAffiliation() {
        return model.getAffiliation();
    }

    @JsonLdNode
    public CharSequence getJobTitle() {
        return model.getTitle();
    }

    @JsonLdNode
    public CharSequence getEmail() {
        return model.getEmail();
    }

    @JsonLdNode
    public ImageOptionViewModel getImage() {
        return createView(ImageOptionViewModel.class, model.getPromotableImage());
    }

    @JsonLdNode
    public CharSequence getDescription() {
        return model.getPromotableDescription();
    }

    @JsonLdNode
    public CharSequence getUrl() {
        return model.getLinkableUrl(site);
    }
}
