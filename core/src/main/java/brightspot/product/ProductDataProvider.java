package brightspot.product;

import java.util.List;

import brightspot.image.WebImage;
import brightspot.shopify.db.product.ShopifyStatus;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
public interface ProductDataProvider extends Recordable {

    String getExternalId();

    String getTitle();

    String getDescription();

    WebImage getFeaturedImage();

    List<WebImage> getMedia();

    List<ProductVariantDataProvider> getVariants();

    ShopifyStatus getStatus();
}
