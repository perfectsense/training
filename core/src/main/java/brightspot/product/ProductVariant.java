package brightspot.product;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import brightspot.image.ImagePreviewHtml;
import brightspot.image.WebImageAsset;
import brightspot.promo.product.ProductVariantPromotable;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.ui.form.DynamicFieldClass;
import com.psddev.cms.ui.form.DynamicNoteMethod;
import com.psddev.cms.ui.form.DynamicPlaceholderMethod;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.html.Node;

@Recordable.Embedded
public class ProductVariant extends Record implements ProductVariantPromotable {

    @DynamicPlaceholderMethod("getTitleFallback")
    private String title;

    @DynamicFieldClass(ReadOnlyIfProvidedDynamicField.class)
    private Set<ProductVariantOption> options;

    @DynamicNoteMethod("getImageNote")
    private WebImageAsset image;

    @DynamicPlaceholderMethod("getPriceFallback")
    @DynamicFieldClass(ReadOnlyIfProvidedDynamicField.class)
    Double price;

    @DynamicPlaceholderMethod("getCompareAtPriceFallback")
    @DynamicFieldClass(ReadOnlyIfProvidedDynamicField.class)
    Double compareAtPrice;

    @DynamicPlaceholderMethod("getQuantityFallback")
    @DynamicFieldClass(ReadOnlyIfProvidedDynamicField.class)
    Integer quantity;

    @DisplayName("SKU")
    @DynamicPlaceholderMethod("getStockKeepingUnitFallback")
    @DynamicFieldClass(ReadOnlyIfProvidedDynamicField.class)
    String stockKeepingUnit;

    @DynamicPlaceholderMethod("getBarcodeFallback")
    @DynamicFieldClass(ReadOnlyIfProvidedDynamicField.class)
    String barcode;

    @ToolUi.Hidden
    private ProductVariantDataProvider dataProvider;

    @Indexed
    @ToolUi.Hidden
    public String getExternalId() {
        return getDataProviderValue(ProductVariantDataProvider::getExternalId);
    }

    public String getTitle() {
        return Optional.ofNullable(title).orElseGet(this::getTitleFallback);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String getTitleFallback() {
        return Optional.ofNullable(getDataProviderValue(ProductVariantDataProvider::getTitle))
            .orElseGet(() -> getOptions()
                .stream()
                .map(ProductVariantOption::getValue)
                .collect(Collectors.joining(" / ")));
    }

    public Set<ProductVariantOption> getOptions() {
        if (options == null) {
            options = new HashSet<>();
        }
        return options;
    }

    public void setOptions(Set<ProductVariantOption> options) {
        this.options = options;
    }

    public WebImageAsset getImage() {
        return Optional.ofNullable(image).orElseGet(this::getImageFallback);
    }

    public void setImage(WebImageAsset image) {
        this.image = image;
    }

    public WebImageAsset getImageFallback() {
        return getDataProviderValue(ProductVariantDataProvider::getImage);
    }

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

    @Override
    public String getLabel() {
        return getTitle();
    }

    private <D> D getDataProviderValue(Function<ProductVariantDataProvider, D> function) {
        return dataProvider != null ? function.apply(dataProvider) : null;
    }

    @Override
    protected void beforeCommit() {
        if (dataProvider != null) {
            Map<String, String> dataProviderOptions = dataProvider.getOptions();
            Set<ProductVariantOption> options = new LinkedHashSet<>();
            if (dataProviderOptions != null) {
                dataProviderOptions.entrySet().stream().map(entry -> {
                        ProductVariantOption newOption = new ProductVariantOption();
                        newOption.setName(entry.getKey());
                        newOption.setValue(entry.getValue());
                        return newOption;
                    })
                    .forEach(options::add);
            }
            this.setOptions(options);
        }
        super.beforeCommit();
    }

    @Override
    public String getProductVariantPromotableTitle() {
        return getTitle();
    }

    @Override
    public String getProductVariantPromotableDescription() {
        return null;
    }

    @Override
    public WebImageAsset getProductVariantPromotableImage() {
        return getImage();
    }

    @Override
    public BigDecimal getProductVariantPromotablePrice() {
        return getPrice() != null ? BigDecimal.valueOf(getPrice()) : null;
    }

    @Override
    public BigDecimal getProductVariantPromotableCompareAtPrice() {
        return getCompareAtPrice() != null ? BigDecimal.valueOf(getCompareAtPrice()) : null;
    }

    @Override
    public Currency getProductVariantPromotableCurrency() {
        //TODO product object should provide a currency
        return Currency.getInstance(Locale.US);
    }

    private Node getImageNote() {
        if (image == null) {
            return Optional.ofNullable(getImageFallback())
                .map(WebImageAsset::getWebImageAssetFile)
                .map(ImagePreviewHtml::createPreviewImageHtml)
                .orElse(null);
        }
        return null;
    }
}
