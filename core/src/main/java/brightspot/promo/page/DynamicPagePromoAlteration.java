package brightspot.promo.page;

import brightspot.author.AuthorMatch;
import brightspot.itemstream.DateRangeMatch;
import brightspot.itemstream.DynamicQuerySort;
import brightspot.itemstream.page.DynamicPageItemStreamAllMatch;
import brightspot.itemstream.page.DynamicPageItemStreamAnyMatch;
import brightspot.itemstream.page.DynamicPageItemStreamNoneMatch;
import brightspot.module.promo.page.dynamic.AbstractDynamicPagePromoModule;
import brightspot.query.QueryBuilder;
import brightspot.query.QueryBuilderDynamicQueryModifier;
import brightspot.section.AllSectionMatch;
import brightspot.sort.publishdate.NewestPublishDate;
import brightspot.sort.publishdate.OldestPublishDate;
import brightspot.tag.TagMatch;
import com.psddev.dari.db.Alteration;
import com.psddev.dari.db.Recordable;

public class DynamicPagePromoAlteration extends Alteration<AbstractDynamicPagePromoModule> {

    @Recordable.InternalName(QueryBuilderDynamicQueryModifier.QUERY_BUILDER_FIELD)
    @Types({
            AuthorMatch.class,
            DateRangeMatch.class,
            DynamicPageItemStreamAllMatch.class,
            DynamicPageItemStreamAnyMatch.class,
            DynamicPageItemStreamNoneMatch.class,
            AllSectionMatch.class,
            TagMatch.class
    })
    private QueryBuilder queryBuilder;

    @Recordable.Types({
            NewestPublishDate.class,
            OldestPublishDate.class
    })
    private DynamicQuerySort sort;
}
