package brightspot.product.shopify;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import brightspot.image.WebImage;
import brightspot.l10n.SiteLocaleProvider;
import brightspot.metadata.ImageMetadataExtractor;
import brightspot.product.Product;
import brightspot.product.ProductCollection;
import brightspot.shopify.db.collection.ShopifyCollection;
import brightspot.shopify.db.product.ShopifyProduct;
import brightspot.shopify.db.product.ShopifyProductImage;
import brightspot.shopify.db.product.ShopifyProductVariant;
import brightspot.shopify.db.product.ShopifyProductVariantOption;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Directory;
import com.psddev.cms.db.ExternalItemImported;
import com.psddev.cms.db.History;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUser;
import com.psddev.dari.db.AsyncDatabaseWriter;
import com.psddev.dari.db.Database;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.WriteOperation;
import com.psddev.dari.util.AsyncQueue;
import com.psddev.dari.util.StorageItem;
import com.psddev.dari.util.UrlStorageItem;
import com.psddev.dari.web.WebRequest;
import com.psddev.localization.LocalizationData;
import shopify.graphql.apollo.v3.GetCollectionProductQuery;
import shopify.graphql.apollo.v3.GetCollectionProductsIdsQuery;

import static brightspot.shopify.db.collection.ShopifyCollectionDatabase.*;

public class ShopifyProductImporter {

    private static final int DEFAULT_NUMBER_OF_WRITERS = 2;

    private final ShopifyCollection shopifyCollection;

    private final Integer numberOfWriters;

    private final Site site;

    private final ToolUser user;

    private final ProductCollection collection;

    private String endCursor;

    private boolean hasNextPage;

    public ShopifyProductImporter(
        ShopifyCollection shopifyCollection,
        Site site,
        ToolUser user,
        ProductCollection collection,
        String endCursor,
        boolean hasNextPage) {

        this.shopifyCollection = shopifyCollection;
        numberOfWriters = DEFAULT_NUMBER_OF_WRITERS;
        this.site = site;
        this.user = user;
        this.collection = collection;
        this.endCursor = endCursor;
        this.hasNextPage = hasNextPage;
    }

    public ShopifyProductImporter(
        ShopifyCollection shopifyCollection,
        Integer numberOfWriters,
        Site site,
        ToolUser user,
        ProductCollection collection,
        String endCursor,
        boolean hasNextPage) {

        this.shopifyCollection = shopifyCollection;
        this.numberOfWriters = numberOfWriters;
        this.site = site;
        this.user = user;
        this.collection = collection;
        this.endCursor = endCursor;
        this.hasNextPage = hasNextPage;
    }

    private Integer getNumberOfWriters() {

        return Optional.ofNullable(numberOfWriters)
            .orElse(DEFAULT_NUMBER_OF_WRITERS);
    }

    public void createOrUpdateProduct() {

        AsyncQueue<Product> queue = new AsyncQueue<>();
        for (int i = 0; i < getNumberOfWriters(); i++) {

            new AsyncDatabaseWriter<>(
                "Shopify Product Importer",
                queue,
                Database.Static.getDefault(),
                WriteOperation.SAVE,
                10,
                true).submit();
        }

        List<ShopifyProduct> collectionProducts = shopifyCollection.getShopifyProducts(); //Gets the first 10 products from the first request in ShopifyCollectionDatabase

        List<GetCollectionProductsIdsQuery.Products> productIdsResponse = new ArrayList<>();

        GetCollectionProductsIdsQuery.Products collectionProductsIds;

        List<String> productIDs = new ArrayList<>();

        while (hasNextPage) {
            collectionProductsIds = getCollectionProductsIds(
                shopifyCollection.getShopifyAccount(),
                shopifyCollection.getCollectionId(),
                10,
                endCursor);

            productIdsResponse.add(collectionProductsIds);

            if (collectionProductsIds != null) {
                hasNextPage = collectionProductsIds.pageInfo.hasNextPage;
                endCursor = collectionProductsIds.pageInfo.endCursor;
            }
        }

        for (GetCollectionProductsIdsQuery.Products response : productIdsResponse) {
            List<String> responseProductIDs = response.nodes.stream()
                .map(node -> node.id)
                .collect(Collectors.toList());
            productIDs.addAll(responseProductIDs);
        }

        List<GetCollectionProductQuery.Product> products = new ArrayList<>();

        for (String productId : productIDs) {
            GetCollectionProductQuery.Product product = getCollectionProduct(
                shopifyCollection.getShopifyAccount(),
                productId);
            products.add(product);
        }

        for (GetCollectionProductQuery.Product product : products) {
            if (product != null) {
                ShopifyProduct newProduct = buildShopifyProduct(product);
                collectionProducts.add(newProduct);
            }
        }

        //looping through all products within the collection
        for (ShopifyProduct shopifyProduct : collectionProducts) {

            List<ProductCollection> collectionList = new ArrayList<>();

            String shopifyProductId = shopifyProduct.getProductId();

            //querying to see if a product was already imported
            Product productImported = Query.from(Product.class)
                .where("cms.externalItemImported.itemId = ?", shopifyProductId)
                .or(
                    "cms.content.trashed = 'true' OR cms.content.trashed = 'missing' AND cms.externalItemImported.itemId = ?",
                    shopifyProductId)
                .or(
                    "cms.content.draft = 'true' OR cms.content.draft = 'missing' AND cms.externalItemImported.itemId = ?",
                    shopifyProductId)
                .first();

            collectionList.add(collection);

            if (productImported != null) {
                List<ProductCollection> importedCollection = productImported.getCollections();
                importedCollection.add(collection); //if product is already imported through ShopifyProductDatabase, adds to collection field
                productImported.setCollections(importedCollection);

                queue.add(productImported);
            } else {
                Date now = new Date(Database.Static.getDefault().now());
                Product newProduct = new Product();
                ShopifyProductDataProvider provider = new ShopifyProductDataProvider();

                provider.setTitle(shopifyProduct.getProductTitle());
                provider.setDescription(shopifyProduct.getDescription());

                final Set<ShopifyProductImage> allMedia = new HashSet<>();
                Optional.ofNullable(shopifyProduct.getFeaturedImage()).ifPresent(allMedia::add);
                Optional.ofNullable(shopifyProduct.getMedia()).ifPresent(allMedia::addAll);
                Optional.ofNullable(shopifyProduct.getProductVariants())
                    .map(variants -> variants.stream().map(ShopifyProductVariant::getProductVariantImage).collect(
                        Collectors.toSet()))
                    .ifPresent(allMedia::addAll);
                Map<String, WebImage> images = new HashMap<>();
                allMedia.stream().filter(Objects::nonNull).forEach(productImage -> {
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
                    if (!Objects.equals(
                        image.as(ExternalItemImported.class).getItemId(),
                        productImage.getShopifyId())) {
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
                        Content.Static.publish(image, site, user);
                    }
                    images.put(url, image);
                });

                Optional.ofNullable(shopifyProduct.getFeaturedImage())
                    .map(ShopifyProductImage::getUrl)
                    .map(images::get)
                    .ifPresent(provider::setFeaturedImage);

                provider.setMedia(shopifyProduct.getMedia()
                    .stream()
                    .map(ShopifyProductImage::getUrl)
                    .map(images::get)
                    .collect(Collectors.toList()));

                provider.setVariants(
                    shopifyProduct.getProductVariants()
                        .stream()
                        .map(sourceVariant -> {
                            ShopifyProductVariantDataProvider variantDataProvider = new ShopifyProductVariantDataProvider();
                            variantDataProvider.setVariantId(sourceVariant.getVariantId());
                            variantDataProvider.setTitle(sourceVariant.getVariantTitle());
                            variantDataProvider.setImage(images.get(Optional.ofNullable(sourceVariant.getProductVariantImage())
                                .map(ShopifyProductImage::getUrl)
                                .orElse(null)));
                            List<ShopifyProductVariantOption> options = sourceVariant.getOptions();
                            if (options != null && options.size() > 1) {
                                variantDataProvider.setOptions(sourceVariant.getOptions()
                                    .stream()
                                    .collect(Collectors.toMap(
                                        ShopifyProductVariantOption::getName,
                                        ShopifyProductVariantOption::getValue
                                    )));
                            }
                            variantDataProvider.setPrice(sourceVariant.getPrice());
                            variantDataProvider.setCompareAtPrice(sourceVariant.getCompareAtPrice());
                            variantDataProvider.setQuantity(sourceVariant.getQuantity());
                            variantDataProvider.setStockKeepingUnit(sourceVariant.getSku());
                            variantDataProvider.setBarcode(sourceVariant.getBarcode());
                            return variantDataProvider;
                        })
                        .collect(Collectors.toList())
                );

                provider.setStatus(shopifyProduct.getStatusSelection());
                provider.setProductId(shopifyProductId);
                newProduct.setCollections(collectionList);
                newProduct.as(ExternalItemImported.class).setItemId(shopifyProductId);
                newProduct.as(ExternalItemImported.class).setSourceType("brightspot.shopify.db.ShopifyProduct");
                newProduct.as(Site.ObjectModification.class).setOwner(site);
                newProduct.as(Content.ObjectModification.class).setPublishUser(user);
                newProduct.as(Content.ObjectModification.class).setUpdateUser(user);
                newProduct.as(Content.ObjectModification.class).setPublishDate(now);
                newProduct.as(Content.ObjectModification.class).setUpdateDate(now);
                newProduct.as(LocalizationData.class)
                    .setLocale(newProduct.as(SiteLocaleProvider.class).provideRequestedLocale(WebRequest.getCurrent()));
                newProduct.as(Directory.ObjectModification.class)
                    .addSitePath(site, shopifyProduct.getOnlineStoreUrl(), Directory.PathType.PERMALINK);
                History history = new History(user, newProduct);
                history.saveImmediately();
                newProduct.setDataProvider(provider);
                queue.add(newProduct);

            }
        }

        queue.closeAutomatically();

    }
}
