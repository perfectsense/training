package brightspot.product;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import com.psddev.cms.db.ToolUi;
import com.psddev.cms.ui.form.DynamicFieldClass;
import com.psddev.cms.ui.form.DynamicPlaceholderMethod;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("No Options")
public class SingleProductVariantType extends ProductVariantType {

    @DynamicPlaceholderMethod("getPriceFallback")
    @DynamicFieldClass(ReadOnlyIfProvidedDynamicField.class)
    private Double price;

    @DynamicPlaceholderMethod("getCompareAtPriceFallback")
    @DynamicFieldClass(ReadOnlyIfProvidedDynamicField.class)
    private Double compareAtPrice;

    @DynamicPlaceholderMethod("getQuantityFallback")
    @DynamicFieldClass(ReadOnlyIfProvidedDynamicField.class)
    private Integer quantity;

    @DisplayName("SKU")
    @DynamicPlaceholderMethod("getStockKeepingUnitFallback")
    @DynamicFieldClass(ReadOnlyIfProvidedDynamicField.class)
    private String stockKeepingUnit;

    @DynamicPlaceholderMethod("getBarcodeFallback")
    @DynamicFieldClass(ReadOnlyIfProvidedDynamicField.class)
    private String barcode;

    @ToolUi.Hidden
    private ProductVariantDataProvider dataProvider;

    public Double getPrice() {
        return Optional.ofNullable(price).orElseGet(this::getPriceFallback);
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    private Double getPriceFallback() {
        return getDataProviderValue(ProductVariantDataProvider::getPrice);
    }

    public Double getCompareAtPrice() {
        return Optional.ofNullable(compareAtPrice).orElseGet(this::getCompareAtPriceFallback);
    }

    public void setCompareAtPrice(Double compareAtPrice) {
        this.compareAtPrice = compareAtPrice;
    }

    private Double getCompareAtPriceFallback() {
        return getDataProviderValue(ProductVariantDataProvider::getCompareAtPrice);
    }

    public Integer getQuantity() {
        return Optional.ofNullable(quantity).orElseGet(this::getQuantityFallback);
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    private Integer getQuantityFallback() {
        return getDataProviderValue(ProductVariantDataProvider::getQuantity);
    }

    public String getStockKeepingUnit() {
        return Optional.ofNullable(stockKeepingUnit).orElseGet(this::getStockKeepingUnitFallback);
    }

    public void setStockKeepingUnit(String stockKeepingUnit) {
        this.stockKeepingUnit = stockKeepingUnit;
    }

    private String getStockKeepingUnitFallback() {
        return getDataProviderValue(ProductVariantDataProvider::getStockKeepingUnit);
    }

    public String getBarcode() {
        return Optional.ofNullable(barcode).orElseGet(this::getBarcodeFallback);
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    private String getBarcodeFallback() {
        return getDataProviderValue(ProductVariantDataProvider::getBarcode);
    }

    public ProductVariantDataProvider getDataProvider() {
        return dataProvider;
    }

    public void setDataProvider(ProductVariantDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    private <D> D getDataProviderValue(Function<ProductVariantDataProvider, D> function) {
        return dataProvider != null ? function.apply(dataProvider) : null;
    }

    @Override
    public List<ProductVariant> toVariants() {
        ProductVariant productVariant = new ProductVariant();
        productVariant.setDataProvider(getDataProvider());
        productVariant.setPrice(price);
        productVariant.setCompareAtPrice(compareAtPrice);
        productVariant.setBarcode(barcode);
        productVariant.setStockKeepingUnit(stockKeepingUnit);
        productVariant.setQuantity(quantity);
        return Collections.singletonList(productVariant);
    }

    @Override
    public void fromVariants(List<ProductVariant> fromVariants) {
        if (fromVariants == null || fromVariants.isEmpty()) {
            return;
        }
        ProductVariant onlyVariant = fromVariants.get(0);
        dataProvider = onlyVariant.getDataProvider();
        price = onlyVariant.price;
        compareAtPrice = onlyVariant.compareAtPrice;
        barcode = onlyVariant.barcode;
        stockKeepingUnit = onlyVariant.stockKeepingUnit;
        quantity = onlyVariant.quantity;
    }
}
