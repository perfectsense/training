package brightspot.core.navigation;

import brightspot.core.link.Link;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.StringUtils;

/**
 * A linked navigation (or sub-navigation) item class.
 */
@Recordable.Embedded
public class NavigationLink extends Record implements
    NavigationItemTitle,
    SubNavigationItem {

    @ToolUi.Placeholder(dynamicText = "${content.textFallback}", editable = true)
    private String text;

    @Required
    private Link link = Link.createDefault();

    /**
     * @return designated or default display text for the {@link NavigationLink}
     */
    public String getText() {
        return !StringUtils.isBlank(text)
            ? text
            : getTextFallback();
    }

    public void setText(String text) {
        this.text = text;
    }

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }

    @Override
    public String getLabel() {
        return getText();
    }

    // NavigationItemTitle
    @Override
    public String getTitleText() {
        return getText();
    }

    public String getTextFallback() {
        Link link = getLink();
        return link != null
            ? link.getLinkTextFallback()
            : null;
    }
}
