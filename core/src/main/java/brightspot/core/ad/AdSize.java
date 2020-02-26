package brightspot.core.ad;

import com.psddev.cms.db.Managed;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.tool.ToolPageContext;
import com.psddev.dari.db.Record;

/**
 * An {@link AdSize} defines size restrictions for an {@link AdModule}.
 */
@ToolUi.FieldDisplayOrder({ "width", "height" })
public class AdSize extends Record implements Managed {

    @Required
    private Integer height;

    @Required
    private Integer width;

    @Indexed(unique = true)
    @ToolUi.Hidden
    public String getUniqueName() {
        return (width != null ? width : "")
            + "x"
            + (height != null ? height : "");
    }

    /**
     * @return The height of an ad, typically the unit is in pixels.
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * @param height Typically the unit is in pixels.
     */
    public void setHeight(Integer height) {
        this.height = height;
    }

    /**
     * @return The width of an ad, typically the unit is in pixels.
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * @param width Typically the unit is in pixels.
     */
    public void setWidth(Integer width) {
        this.width = width;
    }

    /**
     * @return Ad dimension in [width, height] format, typically the units are in pixels.
     */
    public String toDimensionString() {

        int width = this.width != null ? this.width : 0;
        int height = this.height != null ? this.height : 0;

        return "[" + width + ", " + height + "]";
    }

    @Override
    public String createManagedEditUrl(ToolPageContext page) {
        return page.objectUrl("/admin/sites.jsp", SiteSettings.get(page.getSite(), f -> f));
    }
}
