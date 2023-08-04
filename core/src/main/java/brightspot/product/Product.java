package brightspot.product;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import brightspot.image.ImagePreviewHtml;
import brightspot.image.WebImage;
import brightspot.image.WebImageAsset;
import brightspot.link.Linkable;
import brightspot.promo.product.ProductPromotableWithOverrides;
import brightspot.promo.product.ProductVariantPromotable;
import brightspot.rte.MediumRichTextToolbar;
import brightspot.rte.TinyRichTextToolbar;
import brightspot.search.sortalphabetical.AlphabeticallySortable;
import brightspot.sharedcontent.SharedContent;
import brightspot.shopify.db.product.ShopifyStatus;
import brightspot.tag.HasTagsWithField;
import brightspot.util.RichTextUtils;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.ui.form.DynamicNoteMethod;
import com.psddev.cms.ui.form.DynamicPlaceholderMethod;
import com.psddev.cms.ui.form.EditablePlaceholder;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.html.Node;
import com.psddev.suggestions.Suggestable;
import org.apache.commons.lang3.ObjectUtils;

import static com.psddev.dari.html.Nodes.*;

@DynamicNoteMethod("isDraftWarning")
@Recordable.LabelFields("getTitle")
@ToolUi.IconName("shopping_cart")
@ToolUi.FieldDisplayOrder({
    "title",
    "description",
    "featuredImage",
    "media"
})
public class Product extends Content implements
    AlphabeticallySortable,
    HasTagsWithField,
    Linkable,
    ProductPromotableWithOverrides,
    SharedContent,
    Suggestable {

    private static final String CLASS_NAME = Product.class.getName();
    public static final String PRICE_MIN_FIELD = CLASS_NAME + "/getMinPrice";
    public static final String PRICE_MAX_FIELD = CLASS_NAME + "/getMinPrice";

    @ToolUi.RichText(toolbar = TinyRichTextToolbar.class)
    @DynamicPlaceholderMethod("getTitleFallback")
    @EditablePlaceholder
    private String title;

    @ToolUi.RichText(toolbar = MediumRichTextToolbar.class)
    @DynamicPlaceholderMethod("getDescriptionFallback")
    @EditablePlaceholder
    private String description;

    @DynamicNoteMethod("getFeaturedImageNote")
    private WebImage featuredImage;

    @DynamicNoteMethod("getMediaNote")
    private List<WebImage> media;

    @Required
    private ProductVariantType options = new SingleProductVariantType();

    @ToolUi.Hidden
    private ShopifyStatus status;

    @ToolUi.Hidden
    private ProductDataProvider dataProvider;

    @ToolUi.ReadOnly
    @Indexed
    private List<ProductCollection> collections;

    @Indexed
    @ToolUi.Hidden
    public String getTitle() {
        return Optional.ofNullable(title).orElseGet(this::getTitleFallback);
    }

    //TODO add index and filter for source
    @Indexed
    @ToolUi.Hidden
    public String getExternalId() {
        return getDataProviderValue(ProductDataProvider::getExternalId);
    }

    private String getTitleFallback() {
        return getDataProviderValue(ProductDataProvider::getTitle);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return Optional.ofNullable(description).orElseGet(this::getDescriptionFallback);
    }

    private String getDescriptionFallback() {
        return getDataProviderValue(ProductDataProvider::getDescription);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public WebImage getFeaturedImage() {
        return Optional.ofNullable(featuredImage).orElseGet(this::getFeaturedImageFallback);
    }

    public void setFeaturedImage(WebImage featuredImage) {
        this.featuredImage = featuredImage;
    }

    public WebImage getFeaturedImageFallback() {
        return getDataProviderValue(ProductDataProvider::getFeaturedImage);
    }

    public List<WebImage> getMedia() {
        return Optional.ofNullable(media).orElseGet(this::getMediaFallback);
    }

    public void setMedia(List<WebImage> media) {
        this.media = media;
    }

    public List<WebImage> getMediaFallback() {
        return getDataProviderValue(ProductDataProvider::getMedia);
    }

    private ProductVariantType getProductVariantType() {
        if (options == null) {
            options = new SingleProductVariantType();
        }
        return options;
    }

    public List<ProductVariant> getVariants() {
        populateVariantsFromDataProvider();
        return getProductVariantType().toVariants();
    }

    public void setVariants(List<ProductVariant> variants) {
        if (variants == null || variants.isEmpty()) {
            this.options = new SingleProductVariantType();
        } else if (variants.size() == 1) {
            this.options = new SingleProductVariantType();
            this.options.fromVariants(variants);
        } else {
            MultipleProductVariantTypes multiple = new MultipleProductVariantTypes();
            multiple.fromVariants(variants);
            this.options = multiple;
        }
    }

    public ShopifyStatus getStatus() {
        return getDataProviderValue(ProductDataProvider::getStatus);
    }

    public void setStatus(ShopifyStatus status) {
        this.status = status;
    }

    public ProductDataProvider getDataProvider() {
        return dataProvider;
    }

    public void setDataProvider(ProductDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    public List<ProductCollection> getCollections() {
        if (collections == null) {
            collections = new ArrayList<>();
        }
        return collections;
    }

    public void setCollections(List<ProductCollection> collections) {
        this.collections = collections;
    }

    @Indexed
    @ToolUi.Hidden
    public Double getMaxPrice() {
        return getVariants()
            .stream()
            .map(ProductVariant::getPrice)
            .filter(Objects::nonNull)
            .max(Double::compareTo)
            .orElse(null);
    }

    @Indexed
    @ToolUi.Hidden
    public Double getMinPrice() {
        return getVariants()
            .stream()
            .map(ProductVariant::getPrice)
            .filter(Objects::nonNull)
            .min(Double::compareTo)
            .orElse(null);
    }

    @Indexed
    @ToolUi.Hidden
    public Set<String> getOptionTypes() {
        if (options instanceof MultipleProductVariantTypes) {
            return ((MultipleProductVariantTypes) options).getOptions()
                .stream()
                .map(ProductOption::getName)
                .collect(Collectors.toSet());
        } else {
            return Collections.emptySet();
        }
    }

    /**
     * @return A set of string in format of "OPTION_NAME:OPTION_VALUE"
     */
    @Indexed
    @ToolUi.Hidden
    public Set<String> getOptionValues() {
        if (options instanceof MultipleProductVariantTypes) {
            return ((MultipleProductVariantTypes) options).getOptions()
                .stream()
                .map(option -> option.getValues().stream().map(value -> option.getName() + ":" + value).collect(
                    Collectors.toSet()))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        } else {
            return Collections.emptySet();
        }
    }

    private Node getFeaturedImageNote() {
        if (featuredImage == null) {
            return Optional.ofNullable(getFeaturedImageFallback())
                .map(WebImageAsset::getWebImageAssetFile)
                .map(ImagePreviewHtml::createPreviewImageHtml)
                .orElse(null);
        }
        return null;
    }

    private Node getMediaNote() {
        if (ObjectUtils.isEmpty(media)) {
            return DIV.with(getMediaFallback()
                .stream()
                .map(WebImageAsset::getWebImageAssetFile)
                .map(ImagePreviewHtml::createPreviewImageHtml)
                .collect(Collectors.toList()));
        }
        return null;
    }

    private Node isDraftWarning() {
        if (getStatus().equals(ShopifyStatus.DRAFT)) {
            return DIV.classList("Message", "is-warning")
                .with("This product is a draft in Shopify.");
        }
        if (getStatus().equals(ShopifyStatus.ARCHIVED)) {
            return DIV.classList("Message", "is-warning")
                .with("This product is archived in Shopify.");
        }
        return null;
    }

    @Override
    public String getSuggestableText() {
        return null;
    }

    @Override
    protected void beforeCommit() {
        populateVariantsFromDataProvider();
    }

    private <D> D getDataProviderValue(Function<ProductDataProvider, D> function) {
        return dataProvider != null ? function.apply(dataProvider) : null;
    }

    private void populateVariantsFromDataProvider() {
        if (dataProvider != null) {
            //Can't use getVariants directly since that calls this method, leading to endless recursion
            List<ProductVariant> existingVariants = new ArrayList<>(getProductVariantType().toVariants());
            existingVariants = existingVariants.stream()
                .filter(variant -> variant.getDataProvider() != null && variant.getExternalId() != null)
                .collect(Collectors.toList());
            List<ProductVariantDataProvider> providers = dataProvider.getVariants();
            if (providers != null) {
                Set<String> providerIds = providers.stream().map(ProductVariantDataProvider::getExternalId).collect(
                    Collectors.toSet());
                existingVariants.removeIf(variant -> !providerIds.contains(variant.getExternalId()));
                for (ProductVariantDataProvider provider : dataProvider.getVariants()) {
                    ProductVariant productVariant = existingVariants.stream()
                        .filter(recVariant -> recVariant.getExternalId() != null && recVariant.getExternalId()
                            .equals(provider.getExternalId()))
                        .findFirst()
                        .orElse(null);
                    if (productVariant == null) {
                        ProductVariant newVariant = new ProductVariant();
                        newVariant.setDataProvider(provider);
                        existingVariants.add(newVariant);
                    } else {
                        productVariant.setDataProvider(provider);
                    }
                }
            }
            setVariants(existingVariants);
        }
    }

    @Override
    public String getLinkableText() {
        return getTitle();
    }

    @Override
    public String getLinkableUrl(Site site) {
        return Linkable.super.getLinkableUrl(site);
    }

    @Override
    public String getProductPromotableTitleFallback() {
        return getTitle();
    }

    @Override
    public String getProductPromotableDescriptionFallback() {
        return getDescription();
    }

    @Override
    public WebImageAsset getProductPromotableImageFallback() {
        return getFeaturedImage();
    }

    @Override
    public Currency getProductPromotableCurrency() {
        return Currency.getInstance(Locale.US);
    }

    @Override
    public List<ProductVariantPromotable> getProductPromotableVariants() {
        return getVariants().stream().map(ProductVariantPromotable.class::cast).collect(Collectors.toList());
    }

    @Override
    public String getAlphabeticallySortableIndexValue() {
        return RichTextUtils.richTextToPlainText(getProductPromotableTitle());
    }
}
