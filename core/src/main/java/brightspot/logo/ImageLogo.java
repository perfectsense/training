package brightspot.logo;

import brightspot.image.WebImage;
import brightspot.link.ExternalLink;
import brightspot.link.Link;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Recordable;

@Recordable.PreviewField("image/file")
public class ImageLogo extends Logo {

    @Required
    private String internalName;

    @Required
    private WebImage image;

    @ToolUi.Note("Optional.  If not specified, will link to the site index page ('/').")
    private Link link;

    public String getInternalName() {
        return internalName;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    public WebImage getImage() {
        return image;
    }

    public void setImage(WebImage image) {
        this.image = image;
    }

    public Link getLink() {
        if (link == null) {
            ExternalLink link = new ExternalLink();
            link.setUrl("/");
            link.setTarget(null);
            return link;
        }
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }

    @Override
    public String getLabel() {
        return getInternalName();
    }
}
