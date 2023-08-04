package brightspot.product.shopify;

import java.util.HashMap;
import java.util.Map;

import brightspot.image.WebImageAsset;
import brightspot.product.ProductVariantDataProvider;
import com.psddev.dari.db.Record;

public class ShopifyProductVariantDataProvider extends Record implements ProductVariantDataProvider {

    private String variantId;

    private String title;

    @Raw
    private Map<String, String> options;

    private WebImageAsset image;

    private Double price;

    private Double compareAtPrice;

    private Integer quantity;

    @DisplayName("SKU")
    private String stockKeepingUnit;

    private String barcode;

    public String getVariantId() {
        return variantId;
    }

    public void setVariantId(String variantId) {
        this.variantId = variantId;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public Map<String, String> getOptions() {
        if (options == null) {
            options = new HashMap<>();
        }
        return options;
    }

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }

    @Override
    public WebImageAsset getImage() {
        return image;
    }

    public void setImage(WebImageAsset image) {
        this.image = image;
    }

    @Override
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public Double getCompareAtPrice() {
        return compareAtPrice;
    }

    public void setCompareAtPrice(Double compareAtPrice) {
        this.compareAtPrice = compareAtPrice;
    }

    @Override
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String getStockKeepingUnit() {
        return stockKeepingUnit;
    }

    public void setStockKeepingUnit(String stockKeepingUnit) {
        this.stockKeepingUnit = stockKeepingUnit;
    }

    @Override
    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    @Override
    public String getExternalId() {
        return getVariantId();
    }
}
