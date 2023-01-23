package brightspot.sponsoredcontent;

import brightspot.link.Link;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;

@Recordable.FieldInternalNamePrefix("sponsoredcontent.")
public class SponsoredContentSiteSettings extends Modification<SiteSettings> {

    @ToolUi.Tab("Front-End")
    @ToolUi.Cluster("Sponsored Content")
    private Link sponsoredContentMeaningLink;

    public Link getSponsoredContentMeaningLink() {
        return sponsoredContentMeaningLink;
    }

    public void setSponsoredContentMeaningLink(Link sponsoredContentMeaningLink) {
        this.sponsoredContentMeaningLink = sponsoredContentMeaningLink;
    }
}
