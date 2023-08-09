package brightspot.product.shopify;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import brightspot.image.WebImage;
import brightspot.metadata.ImageMetadataExtractor;
import brightspot.product.ProductVariantDataProvider;
import brightspot.shopify.db.ShopifyAccount;
import brightspot.shopify.db.ShopifySiteSettings;
import brightspot.shopify.db.product.ShopifyProduct;
import brightspot.shopify.db.product.ShopifyStatus;
import brightspot.shopify.db.webhook.Image;
import brightspot.shopify.db.webhook.Product;
import brightspot.shopify.db.webhook.ShopifyProductWebhookProcessor;
import brightspot.shopify.db.webhook.ShopifyProductWebhookTopic;
import brightspot.shopify.db.webhook.ShopifyWebhookSettings;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Directory;
import com.psddev.cms.db.Draft;
import com.psddev.cms.db.ExternalItemImported;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.db.ToolUser;
import com.psddev.cms.tool.CmsTool;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.Singleton;
import com.psddev.dari.db.State;
import com.psddev.dari.util.StorageItem;
import com.psddev.dari.util.UrlStorageItem;

/**
 * Default implementation of {@link ShopifyProductWebhookProcessor}.
 */
@Recordable.DisplayName("Default")
public class DefaultShopifyProductWebhookProcessor extends ShopifyProductWebhookProcessor {

    private static final String OVERLAID_DRAFT_EXTRA = "cms.tool.overlaidDraft";
    private static final String PRODUCT_ID_PREFIX = "gid://shopify/Product/";
    private static final String PRODUCTS_PATH = "products/";

    @Override
    public void create(ShopifyAccount account, Product data) {
        /* Check to see if a Product exists already.
        If so, do nothing as the update or delete will be the more recent state.
        Sometimes the Create webhooks are slower to come in than Updates or Deletes. */
        brightspot.product.Product product = getExistingProduct(data);
        if (product != null) {
            return;
        }

        ShopifyProductDataProvider dataProvider = new ShopifyProductDataProvider();

        Optional.ofNullable(data.getId())
            .map(id -> Long.toString(id))
            .map(PRODUCT_ID_PREFIX::concat)
            .ifPresent(dataProvider::setProductId);

        populateProductDataProvider(data, dataProvider, account);

        product = new brightspot.product.Product();
        product.setDataProvider(dataProvider);

        // Set external item ID source type
        product.as(ExternalItemImported.class).setItemId(dataProvider.getProductId());
        product.as(ExternalItemImported.class).setSourceType(ShopifyProduct.class.getName());

        // Set site path from product URL
        product.as(Directory.ObjectModification.class)
            .addSitePath(
                getPrimarySiteForShopifyAccount(account),
                constructProductUrl(data, account.getStoreUrl()),
                Directory.PathType.PERMALINK);

        // Set visibilities from Status
        ShopifyStatus statusFromRequest = Optional.ofNullable(data.getStatus())
            .map(ShopifyStatus::fromApiValue)
            .orElse(null);
        if (ShopifyStatus.DRAFT.equals(statusFromRequest)) {
            product.as(Content.ObjectModification.class).setDraft(true);
        }

        List<Site> sites = getSitesForShopifyAccount(account);

        // Select primary site (first in the sorted list by "name") and separate it from other sites
        Site primarySite = sites.isEmpty() ? null : sites.remove(0);

        // Set site owner (and consumers if applicable)
        product.as(Site.ObjectModification.class).setOwner(primarySite);
        if (!sites.isEmpty()) {
            product.as(Site.ObjectModification.class).setConsumers(new HashSet<>(sites));
        }

        Content.Static.publish(product, primarySite, getAutoUpdateUserForShopifyAccount(account));
    }

    @Override
    public void update(ShopifyAccount account, Product data) {
        // Find the existing Product
        brightspot.product.Product product = getExistingProduct(data);
        if (product == null) {
            ShopifyWebhookSettings autoUpdateSettings = account.getAutoUpdateSettings();

            if (autoUpdateSettings != null && autoUpdateSettings
                .getSubscriptions()
                .stream()
                .anyMatch(s -> s.getTopics().contains(ShopifyProductWebhookTopic.PRODUCTS_CREATE.getShopifyId()))) {
                /* Create the product.
                There may be a use case in which multiple webhooks come in for the same Product before processing occurs.
                Because the webhook with the most recent state of the Product is used at process time,
                it's possible to process an "Update" before (or instead of) a "Create".
                If the Product is yet to be created, we should do it here. */
                create(account, data);
            }
        } else {
            ShopifyProductDataProvider dataProvider = product.getDataProvider()
                .as(ShopifyProductDataProvider.class);

            populateProductDataProvider(data, dataProvider, account);

            // Clear existing site path(s) and set new one from product URL
            product.as(Directory.ObjectModification.class).clearSitePaths(getPrimarySiteForShopifyAccount(account));
            product.as(Directory.ObjectModification.class)
                .addSitePath(
                    getPrimarySiteForShopifyAccount(account),
                    constructProductUrl(data, account.getStoreUrl()),
                    Directory.PathType.PERMALINK);

            // Set visibilities from Status
            ShopifyStatus statusFromRequest = Optional.ofNullable(data.getStatus())
                .map(ShopifyStatus::fromApiValue)
                .orElse(null);

            Draft draft = (Draft) State.getInstance(product).getExtra(OVERLAID_DRAFT_EXTRA);
            State state = State.getInstance(draft != null ? draft : product);

            if (ShopifyStatus.ARCHIVED.equals(statusFromRequest)) {
                state.as(Content.ObjectModification.class).setTrash(true);
            } else if (ShopifyStatus.DRAFT.equals(statusFromRequest)) {
                state.as(Content.ObjectModification.class).setDraft(true);
            } else if (ShopifyStatus.ACTIVE.equals(statusFromRequest)) {
                state.as(Content.ObjectModification.class).setTrash(false);
                state.as(Content.ObjectModification.class).setDraft(false);
            }

            Content.Static.publish(
                state,
                getPrimarySiteForShopifyAccount(account),
                getAutoUpdateUserForShopifyAccount(account));
        }
    }

    @Override
    public void delete(ShopifyAccount account, String productId) {
        brightspot.product.Product product = Query.from(brightspot.product.Product.class)
            .where("getExternalId = ?", PRODUCT_ID_PREFIX + productId)
            .first();
        if (product != null) {
            product.delete();
        }
    }

    private void populateProductDataProvider(
        Product data,
        ShopifyProductDataProvider dataProvider,
        ShopifyAccount account) {

        // Title
        Optional.ofNullable(data.getTitle())
            .ifPresent(dataProvider::setTitle);

        // Description
        Optional.ofNullable(data.getDescription())
            .ifPresent(dataProvider::setDescription);

        // Featured Image
        Optional.ofNullable(convertFeaturedImageFromRequest(data, account))
            .ifPresent(dataProvider::setFeaturedImage);

        // Media
        Optional.ofNullable(convertMediaFromRequest(data, account))
            .ifPresent(dataProvider::setMedia);

        // Variants
        Optional.ofNullable(convertVariantsFromRequest(data, account))
            .ifPresent(dataProvider::setVariants);

        // Status
        Optional.ofNullable(data.getStatus())
            .map(ShopifyStatus::fromApiValue)
            .ifPresent(dataProvider::setStatus);
    }

    private WebImage convertFeaturedImageFromRequest(Product request, ShopifyAccount account) {
        return Optional.ofNullable(request.getImage())
            .map(image -> queryForOrCreateImage(image, account))
            .orElse(null);
    }

    private List<WebImage> convertMediaFromRequest(Product request, ShopifyAccount account) {
        return Optional.ofNullable(request.getImages())
            .map(images -> {
                List<WebImage> webImages = new ArrayList<>();
                String featuredImageUrl = Optional.ofNullable(request.getImage())
                    .map(Image::getSrc)
                    .orElse(null);

                images.forEach(img -> {
                    if (img.getSrc().equals(featuredImageUrl)) {
                        // Skip the Image if it is the same as the Featured Image
                        return;
                    }

                    WebImage image = queryForOrCreateImage(img, account);

                    webImages.add(image);
                });

                return webImages;
            }).orElse(null);
    }

    private List<ProductVariantDataProvider> convertVariantsFromRequest(Product request, ShopifyAccount account) {
        return Optional.ofNullable(request.getVariants()).map(variants -> {
            List<ProductVariantDataProvider> variantDataProviders = new ArrayList<>();

            variants.forEach(variant -> {
                ShopifyProductVariantDataProvider variantDataProvider = new ShopifyProductVariantDataProvider();

                Optional.ofNullable(variant.getId()).map(Object::toString).ifPresent(variantDataProvider::setVariantId);

                Optional.ofNullable(variant.getTitle()).ifPresent(variantDataProvider::setTitle);

                request.getImages()
                    .stream()
                    .filter(image -> image.getId().equals(variant.getImageId()))
                    .findFirst()
                    .map(image -> queryForOrCreateImage(image, account))
                    .ifPresent(variantDataProvider::setImage);

                Optional.ofNullable(variant.getPrice()).map(Double::valueOf).ifPresent(variantDataProvider::setPrice);

                Optional.ofNullable(variant.getCompareAtPrice())
                    .map(Double::valueOf)
                    .ifPresent(variantDataProvider::setCompareAtPrice);

                Optional.ofNullable(variant.getInventoryQuantity())
                    .map(Long::intValue)
                    .ifPresent(variantDataProvider::setQuantity);

                Optional.ofNullable(variant.getSku()).ifPresent(variantDataProvider::setStockKeepingUnit);

                Optional.ofNullable(variant.getBarcode()).ifPresent(variantDataProvider::setBarcode);

                processOption(variantDataProvider, request, variant::getOption1, 1);
                processOption(variantDataProvider, request, variant::getOption2, 2);
                processOption(variantDataProvider, request, variant::getOption3, 3);

                variantDataProviders.add(variantDataProvider);
            });

            return variantDataProviders;
        }).orElse(null);
    }

    private void processOption(
        ProductVariantDataProvider dataProvider,
        Product product,
        Supplier<String> optionSupplier,
        int position) {
        String optionValue = optionSupplier.get();
        if (optionValue != null) {
            product.getOptions()
                .stream()
                .filter(option -> option.getPosition() == position)
                .findFirst()
                .ifPresent(option -> dataProvider.getOptions().put(option.getName(), optionValue));
        }
    }

    private List<Site> getSitesForShopifyAccount(ShopifyAccount account) {
        // Get Sites associated with Shopify Account
        List<Site> sites = Site.Static.findAll().stream()
            .filter(site -> SiteSettings.get(site, s -> s.as(ShopifySiteSettings.class).getShopifyAccounts())
                .contains(account))
            .sorted(Comparator.comparing(Site::getName))
            .collect(Collectors.toList());

        // Add Global to sites if Shopify account is configured there
        Singleton.getInstance(CmsTool.class).as(ShopifySiteSettings.class).getShopifyAccounts().stream()
            .filter(acct -> acct.equals(account))
            .findFirst()
            .ifPresent(a -> sites.add(0, null));

        return sites;
    }

    private Site getPrimarySiteForShopifyAccount(ShopifyAccount account) {
        // Select primary site (first in the sorted list by "name") and separate it from other sites
        List<Site> sites = getSitesForShopifyAccount(account);
        return sites.isEmpty() ? null : sites.remove(0);
    }

    private WebImage queryForOrCreateImage(Image shopifyImage, ShopifyAccount account) {
        WebImage image = Query.from(WebImage.class)
            .where("cms.externalItemImported.itemId = ?", shopifyImage.getAdminGraphqlApiId())
            .findFirst()
            .orElseGet(WebImage::new);

        boolean save = false;
        final String url = shopifyImage.getSrc();
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

        if (!Objects.equals(image.as(ExternalItemImported.class).getItemId(), shopifyImage.getAdminGraphqlApiId())) {
            image.as(ExternalItemImported.class).setItemId(shopifyImage.getAdminGraphqlApiId());
            save = true;
        }

        final String shopifyAltText = shopifyImage.getAlt();
        final String previousShopifyAltText = image.as(ShopifyWebImage.class).getShopifyAltText();
        final String publishedAltText = image.getAltTextOverride();
        if (Objects.equals(previousShopifyAltText, publishedAltText)) {
            image.setAltTextOverride(shopifyAltText);
            save = true;
        }
        if (!Objects.equals(shopifyAltText, previousShopifyAltText)) {
            image.as(ShopifyWebImage.class).setShopifyAltText(shopifyAltText);
            save = true;
        }
        if (save) {
            Content.Static.publish(
                image,
                getPrimarySiteForShopifyAccount(account),
                getAutoUpdateUserForShopifyAccount(account));
        }
        return image;
    }

    private brightspot.product.Product getExistingProduct(Product data) {
        return Query.from(brightspot.product.Product.class)
            .where("getExternalId = ?", PRODUCT_ID_PREFIX + data.getId())
            .and("cms.content.trashed = true || cms.content.trashed is missing")
            .and("cms.content.draft = true || cms.content.draft is missing")
            .first();
    }

    private ToolUser getAutoUpdateUserForShopifyAccount(ShopifyAccount account) {
        return Optional.ofNullable(account)
            .map(ShopifyAccount::getAutoUpdateSettings)
            .map(ShopifyWebhookSettings::getAutoUpdateUser)
            .orElse(null);
    }

    private String constructProductUrl(Product data, String storeUrl) {
        if (storeUrl != null) {
            StringBuilder builder = new StringBuilder(storeUrl);

            return Optional.ofNullable(data.getHandle())
                .map(builder.append("/").append(PRODUCTS_PATH)::append)
                .map(StringBuilder::toString)
                .orElse(null);
        }

        return null;
    }
}
