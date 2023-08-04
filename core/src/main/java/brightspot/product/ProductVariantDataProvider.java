package brightspot.product;

import java.util.Map;

import brightspot.image.WebImageAsset;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
public interface ProductVariantDataProvider extends Recordable {

    String getExternalId();

    String getTitle();

    Map<String, String> getOptions();

    WebImageAsset getImage();

    Double getPrice();

    Double getCompareAtPrice();

    Integer getQuantity();

    String getStockKeepingUnit();

    String getBarcode();

}
