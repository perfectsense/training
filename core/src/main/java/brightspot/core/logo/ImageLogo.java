package brightspot.core.logo;

import brightspot.core.image.ImageOption;
import brightspot.core.link.ExternalLink;
import brightspot.core.link.Link;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Recordable;

@Recordable.PreviewField("image/getImageOptionImage/file")
public class ImageLogo extends Logo {

    @Required
    private String name;

    @Required
    private ImageOption image;

    @ToolUi.Note("Optional.  If not specified, will link to the site index page ('/').")
    private Link link;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ImageOption getImageOption() {
        return image;
    }

    public void setImageOption(ImageOption image) {
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
}
