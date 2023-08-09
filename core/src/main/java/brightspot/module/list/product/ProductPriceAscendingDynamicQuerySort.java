package brightspot.module.list.product;

import brightspot.itemstream.DynamicQuerySort;
import brightspot.product.Product;
import com.psddev.cms.db.Site;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Price - Low -> High")
@Recordable.Embedded
public class ProductPriceAscendingDynamicQuerySort extends Record implements DynamicQuerySort {

    @Override
    public void updateQuery(Site site, Object mainObject, Query<?> query) {
        query.sortAscending(Product.PRICE_MIN_FIELD);
    }
}
