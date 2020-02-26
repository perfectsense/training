package brightspot.core.listmodule;

import javax.servlet.http.HttpServletRequest;

import brightspot.core.footer.FooterModuleType;
import brightspot.core.image.ImageOption;
import brightspot.core.lead.Lead;
import brightspot.core.link.Link;
import brightspot.core.tool.SmallRichTextToolbar;
import com.psddev.cms.db.PageFilter;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.view.ModelWrapper;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.PageContextFilter;
import com.psddev.dari.util.StringUtils;

@Recordable.DisplayName("List")
public class ListModule extends AbstractListModule implements Lead, ModelWrapper, FooterModuleType {

    @ToolUi.Placeholder(dynamicText = "${content.getTitleFallback()}")
    private String title;

    @ToolUi.RichText(toolbar = SmallRichTextToolbar.class)
    private String description;

    @Required
    @Embedded
    private ListModuleItemStream items = ListModuleItemStream.createDefault();

    private Link callToAction;

    /**
     * Returns the {@code title}, falling back to the {@link AbstractListModule#getTitleFallback()}.
     *
     * @return a plain text {@link String} (optional)
     */
    @Override
    public String getTitle() {
        return !StringUtils.isBlank(title)
            ? title
            : getTitleFallback();
    }

    /**
     * Sets the {@code title}.
     *
     * @@param title a plain text {@link String} (optional)
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the {@code description}.
     *
     * @return an inline RichText {@link String} (optional).
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the {@code description}.
     *
     * @param description an inline RichText {@link String} (optional).
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the {@code item stream}.
     *
     * @return a {@link ListModuleItemStream} (required).
     */
    @Override
    public ListModuleItemStream getItemStream() {
        return items;
    }

    /**
     * Sets the {@code item stream}.
     *
     * @param items a {@link ListModuleItemStream} (required).
     */
    public void setItemStream(ListModuleItemStream items) {
        this.items = items;
    }

    @Override
    public ImageOption getLeadImage() {
        return null;
    }

    /**
     * Returns the {@code link}
     *
     * @return a {@link Link} (optional).
     */
    public Link getCallToAction() {
        return callToAction;
    }

    /**
     * Sets the {@code link}
     *
     * @param callToAction a {@link Link}
     */
    public void setCallToAction(Link callToAction) {
        this.callToAction = callToAction;
    }

    @Override
    public Object unwrap() {

        if (items == null) {
            return null;
        }

        HttpServletRequest request = PageContextFilter.Static.getRequestOrNull();

        Site currentSite = null;
        Object mainObject = null;
        if (request != null) {
            currentSite = PageFilter.Static.getSite(request);
            mainObject = PageFilter.Static.getMainObject(request);
        }

        return !items.hasMoreThan(currentSite, mainObject, 0)
            ? items.getNoResultsModule()
            : this;
    }
}
