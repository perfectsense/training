package brightspot.product.shopify;

import java.util.ArrayList;
import java.util.List;

import brightspot.image.WebImage;
import brightspot.product.ProductDataProvider;
import brightspot.product.ProductVariantDataProvider;
import brightspot.shopify.db.product.ShopifyStatus;
import com.psddev.dari.db.Record;

public class ShopifyProductDataProvider extends Record implements ProductDataProvider {

    private String id;

    private String title;

    private String description;

    private WebImage featuredImage;

    private List<WebImage> media;

    private List<ProductVariantDataProvider> variants;

    private ShopifyStatus status;

    public String getProductId() {
        return id;
    }

    public void setProductId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFeaturedImage(WebImage featuredImage) {
        this.featuredImage = featuredImage;
    }

    public void setMedia(List<WebImage> media) {
        this.media = media;
    }

    public void setVariants(List<ProductVariantDataProvider> variants) {
        this.variants = variants;
    }

    public void setStatus(ShopifyStatus status) {
        this.status = status;
    }

    @Override
    public String getExternalId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public WebImage getFeaturedImage() {
        return featuredImage;
    }

    @Override
    public List<WebImage> getMedia() {
        return media;
    }

    @Override
    public List<ProductVariantDataProvider> getVariants() {
        if (variants == null) {
            variants = new ArrayList<>();
        }
        return variants;
    }

    @Override
    public ShopifyStatus getStatus() {
        return status;
    }
}
