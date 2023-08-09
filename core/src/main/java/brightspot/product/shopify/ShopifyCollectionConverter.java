package brightspot.product.shopify;

import java.util.Collection;
import java.util.Collections;

import brightspot.product.ProductCollection;
import brightspot.shopify.db.collection.ShopifyCollection;
import com.psddev.cms.db.ExternalItemConverter;
import com.psddev.cms.ui.ToolRequest;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.web.WebRequest;

public class ShopifyCollectionConverter extends ExternalItemConverter<ShopifyCollection, Recordable> {

    @Override
    public Collection<? extends Recordable> convert(ShopifyCollection source) {

        ToolRequest toolRequest = WebRequest.getCurrent().as(ToolRequest.class);

        ProductCollection collection = new ProductCollection();
        collection.setTitle(source.getTitle());

        new ShopifyProductImporter(source, toolRequest.getCurrentSite(), toolRequest.getCurrentUser(), collection, source.getEndCursor(), source.isHasNextPage()).createOrUpdateProduct();

        return Collections.singleton(collection);
    }
}
