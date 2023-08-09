package brightspot.product.shopify;

import brightspot.image.WebImage;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;

@Recordable.FieldInternalNamePrefix(ShopifyWebImage.PREFIX)
public class ShopifyWebImage extends Modification<WebImage> {

    static final String PREFIX = "shopify.";

    @ToolUi.Hidden
    private String shopifyAltText;

    public String getShopifyAltText() {
        return shopifyAltText;
    }

    public void setShopifyAltText(String shopifyAltText) {
        this.shopifyAltText = shopifyAltText;
    }
}
