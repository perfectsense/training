package brightspot.product.shopify;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import brightspot.image.WebImage;
import brightspot.metadata.ImageMetadataExtractor;
import brightspot.product.Product;
import brightspot.shopify.db.product.ShopifyProduct;
import brightspot.shopify.db.product.ShopifyProductImage;
import brightspot.shopify.db.product.ShopifyProductVariant;
import brightspot.shopify.db.product.ShopifyProductVariantOption;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Directory;
import com.psddev.cms.db.ExternalItemConverter;
import com.psddev.cms.db.ExternalItemImported;
import com.psddev.cms.ui.ToolRequest;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.StorageItem;
import com.psddev.dari.util.UrlStorageItem;
import com.psddev.dari.web.WebRequest;

public class ShopifyProductConverter extends ExternalItemConverter<ShopifyProduct, Recordable> {

    @Override
    public Collection<? extends Recordable> convert(ShopifyProduct source) {
        List<Recordable> output = new ArrayList<>();

        ShopifyProductDataProvider shopifyProductDataProvider = new ShopifyProductDataProvider();

        shopifyProductDataProvider.setProductId(source.getProductId());

        shopifyProductDataProvider.setTitle(source.getProductTitle());
        shopifyProductDataProvider.setDescription(source.getDescription());

        final Set<ShopifyProductImage> allMedia = new HashSet<>();
        Optional.ofNullable(source.getFeaturedImage()).ifPresent(allMedia::add);
        Optional.ofNullable(source.getMedia()).ifPresent(allMedia::addAll);
        Optional.ofNullable(source.getProductVariants())
            .map(variants -> variants.stream().map(ShopifyProductVariant::getProductVariantImage).collect(
                Collectors.toSet()))
            .ifPresent(allMedia::addAll);
        Map<String, WebImage> images = new HashMap<>();
        allMedia.stream().filter(Objects::nonNull).forEach(productImage -> {
            //TODO option to fully download
            WebImage image = Query.from(WebImage.class)
                .where("cms.externalItemImported.itemId = ?", productImage.getShopifyId())
                .findFirst()
                .orElseGet(WebImage::new);

            boolean save = false;
            final String url = productImage.getUrl();
            StorageItem storageItem = StorageItem.Static.createUrl(url);
            String contentType = UrlStorageItem.fetchContentType(url);
            storageItem.setContentType(contentType);
            if (contentType != null && contentType.startsWith("image/")) {
                Map<String, Object> metadata = ImageMetadataExtractor.extractMetadata(storageItem);
                storageItem.setMetadata(metadata);
            }
            if (image.getFile() == null || !Objects.equals(
                image.getFile().getSecurePublicUrl(),
                storageItem.getSecurePublicUrl())) {
                image.setFile(storageItem);
                save = true;
            }

            if (!Objects.equals(image.as(ExternalItemImported.class).getItemId(), productImage.getShopifyId())) {
                image.as(ExternalItemImported.class).setItemId(productImage.getShopifyId());
                save = true;
            }

            final String shopifyAltText = productImage.getAltText();
            final String previousShopifyAltText = image.as(ShopifyWebImage.class).getShopifyAltText();
            final String publishedAltText = image.getAltTextOverride();
            if (Objects.equals(previousShopifyAltText, publishedAltText)) {
                image.setAltTextOverride(shopifyAltText);
                save = true;
            }
            if (!Objects.equals(shopifyAltText, previousShopifyAltText)) {
                image.as(ShopifyWebImage.class).setShopifyAltText(productImage.getAltText());
                save = true;
            }
            if (save) {
                ToolRequest toolRequest = WebRequest.getCurrent().as(ToolRequest.class);
                Content.Static.publish(image, toolRequest.getCurrentSite(), toolRequest.getCurrentUser());
            }
            images.put(url, image);
        });

        Optional.ofNullable(source.getFeaturedImage())
            .map(ShopifyProductImage::getUrl)
            .map(images::get)
            .ifPresent(shopifyProductDataProvider::setFeaturedImage);

        shopifyProductDataProvider.setMedia(source.getMedia()
            .stream()
            .map(ShopifyProductImage::getUrl)
            .map(images::get)
            .collect(Collectors.toList()));

        shopifyProductDataProvider.setVariants(
            source.getProductVariants()
                .stream()
                .map(sourceVariant -> {
                    ShopifyProductVariantDataProvider provider = new ShopifyProductVariantDataProvider();
                    provider.setVariantId(sourceVariant.getVariantId());
                    provider.setTitle(sourceVariant.getVariantTitle());
                    provider.setImage(images.get(Optional.ofNullable(sourceVariant.getProductVariantImage())
                        .map(ShopifyProductImage::getUrl)
                        .orElse(null)));
                    List<ShopifyProductVariantOption> options = sourceVariant.getOptions();
                    if (options != null && options.size() > 1) {
                        provider.setOptions(sourceVariant.getOptions()
                            .stream()
                            .collect(Collectors.toMap(
                                ShopifyProductVariantOption::getName,
                                ShopifyProductVariantOption::getValue)));
                    }
                    provider.setPrice(sourceVariant.getPrice());
                    provider.setCompareAtPrice(sourceVariant.getCompareAtPrice());
                    provider.setQuantity(sourceVariant.getQuantity());
                    provider.setStockKeepingUnit(sourceVariant.getSku());
                    provider.setBarcode(sourceVariant.getBarcode());
                    return provider;
                })
                .collect(Collectors.toList())
        );

        shopifyProductDataProvider.setStatus(source.getStatusSelection());

        Product product = new Product();

        product.as(Directory.ObjectModification.class)
            .addSitePath(
                WebRequest.getCurrent().as(ToolRequest.class).getCurrentSite(),
                source.getOnlineStoreUrl(),
                Directory.PathType.PERMALINK);
        product.setDataProvider(shopifyProductDataProvider);

        output.add(product);
        return output;
    }
}
