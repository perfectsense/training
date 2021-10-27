package brightspot.seo;

import brightspot.author.PersonAuthor;
import brightspot.image.WebImageAssetViewModel;
import brightspot.util.RichTextUtils;
import com.psddev.cms.db.Site;
import com.psddev.cms.page.CurrentSite;
import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.jsonld.JsonLdNode;
import com.psddev.cms.view.jsonld.JsonLdType;

@JsonLdType("Person")
public class PersonSchemaViewModel extends ViewModel<PersonAuthor> implements PersonSchemaView {

    @CurrentSite
    private Site site;

    @JsonLdNode
    public CharSequence getName() {
        return RichTextUtils.richTextToPlainText(model.getName());
    }

    @JsonLdNode
    public CharSequence getAffiliation() {
        return model.getAffiliation();
    }

    @JsonLdNode
    public CharSequence getJobTitle() {
        return RichTextUtils.richTextToPlainText(model.getTitle());
    }

    @JsonLdNode
    public CharSequence getEmail() {
        return model.getEmail();
    }

    @JsonLdNode
    public WebImageAssetViewModel getImage() {
        return createView(WebImageAssetViewModel.class, model.getPagePromotableImage());
    }

    @JsonLdNode
    public CharSequence getDescription() {
        return RichTextUtils.richTextToPlainText(model.getPagePromotableDescription());
    }

    @JsonLdNode
    public CharSequence getUrl() {
        return model.getLinkableUrl(site);
    }
}
