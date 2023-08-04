package brightspot.module.list.product;

import brightspot.itemstream.DateRangeMatch;
import brightspot.itemstream.DynamicQuerySort;
import brightspot.itemstream.product.DynamicProductItemStreamAllMatch;
import brightspot.itemstream.product.DynamicProductItemStreamAnyMatch;
import brightspot.itemstream.product.DynamicProductItemStreamNoneMatch;
import brightspot.query.QueryBuilder;
import brightspot.query.QueryBuilderDynamicQueryModifier;
import brightspot.search.sortalphabetical.AlphabeticalAscendingDynamicQuerySort;
import brightspot.search.sortalphabetical.AlphabeticalDescendingDynamicQuerySort;
import brightspot.sort.publishdate.NewestPublishDate;
import brightspot.sort.publishdate.OldestPublishDate;
import brightspot.tag.TagMatch;
import com.psddev.dari.db.Alteration;

public class DynamicProductItemStreamAlteration extends Alteration<DynamicProductItemStream> {

    @InternalName(QueryBuilderDynamicQueryModifier.QUERY_BUILDER_FIELD)
    @Types({
        DateRangeMatch.class,
        DynamicProductItemStreamAllMatch.class,
        DynamicProductItemStreamAnyMatch.class,
        DynamicProductItemStreamNoneMatch.class,
        ProductPriceRangeMatch.class,
        TagMatch.class
    })
    private QueryBuilder queryBuilder;

    @Types({
        NewestPublishDate.class,
        OldestPublishDate.class,
        AlphabeticalAscendingDynamicQuerySort.class,
        AlphabeticalDescendingDynamicQuerySort.class,
        ProductPriceAscendingDynamicQuerySort.class,
        ProductPriceDescendingDynamicQuerySort.class
    })
    private DynamicQuerySort sort;
}
