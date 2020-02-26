package brightspot.core.ad;

import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

/**
 * Configured in {@link com.psddev.cms.db.SiteSettings} -> Ads
 */
@Recordable.Embedded
public class Dimension extends Record {

    private Integer width;

    private Integer height;

    /**
     * @return dimension width of an ad, typically the unit is in pixels.
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * @param width
     */
    public void setWidth(Integer width) {
        this.width = width;
    }

    /**
     * @return dimension height of an ad, typically the unit is in pixels.
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * @param height
     */
    public void setHeight(Integer height) {
        this.height = height;
    }
}
