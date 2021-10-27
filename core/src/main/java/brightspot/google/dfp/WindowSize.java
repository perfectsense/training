package brightspot.google.dfp;

import com.psddev.cms.db.Managed;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.tool.ToolPageContext;
import com.psddev.cms.ui.ToolRequest;
import com.psddev.dari.db.Record;
import com.psddev.dari.web.WebRequest;

/**
 * Defines window size restrictions for an {@link GoogleDfpAdModule}.
 */
public class WindowSize extends Record implements Managed {

    private Integer width;

    private Integer height;

    @Indexed(unique = true)
    @ToolUi.Hidden
    public String getUniqueName() {
        return (width != null ? width : "")
            + "x"
            + (height != null ? height : "");
    }

    /**
     * @return The window size width in pixels.
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * @param width Window size width in pixels.
     */
    public void setWidth(Integer width) {
        this.width = width;
    }

    /**
     * @return The window size height in pixels.
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * @param height Window size height in pixels.
     */
    public void setHeight(Integer height) {
        this.height = height;
    }

    /**
     * @return Formats the {@link #width} and {@link #height} for {@link com.psddev.cms.view.ViewModel} use such as
     * {@link GoogleDfpAdModuleViewModel#getSizes}.
     */
    public String toDimensionString() {

        int width = this.width != null ? this.width : 0;
        int height = this.height != null ? this.height : 0;

        return "[" + width + ", " + height + "]";
    }

    @Override
    public String createManagedEditUrl(ToolPageContext page) {
        return page.objectUrl("/admin/sites.jsp", SiteSettings.get(WebRequest.getCurrent()
            .as(ToolRequest.class)
            .getCurrentSite(), f -> f));
    }

    // --- Recordable support ---

    @Override
    public String getLabel() {
        return getUniqueName();
    }
}
