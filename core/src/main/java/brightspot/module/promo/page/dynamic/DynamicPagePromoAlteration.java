package brightspot.module.promo.page.dynamic;

import brightspot.author.AuthorMatch;
import brightspot.blog.BlogMatch;
import brightspot.itemstream.DateRangeMatch;
import brightspot.itemstream.DynamicQuerySort;
import brightspot.module.list.page.DynamicPageItemStreamAllMatch;
import brightspot.module.list.page.DynamicPageItemStreamAnyMatch;
import brightspot.module.list.page.DynamicPageItemStreamNoneMatch;
import brightspot.query.QueryBuilder;
import brightspot.query.QueryBuilderDynamicQueryModifier;
import brightspot.section.AllSectionMatch;
import brightspot.sort.pageviews.PageViews;
import brightspot.sort.publishdate.NewestPublishDate;
import brightspot.sort.publishdate.OldestPublishDate;
import brightspot.tag.TagMatch;
import com.psddev.dari.db.Alteration;
import com.psddev.dari.db.Recordable;

public class DynamicPagePromoAlteration extends Alteration<AbstractDynamicPagePromoModule> {

    @Recordable.InternalName(QueryBuilderDynamicQueryModifier.QUERY_BUILDER_FIELD)
    @Types({
        AuthorMatch.class,
        BlogMatch.class,
        DateRangeMatch.class,
        DynamicPageItemStreamAllMatch.class,
        DynamicPageItemStreamAnyMatch.class,
        DynamicPageItemStreamNoneMatch.class,
        AllSectionMatch.class,
        TagMatch.class
    })
    private QueryBuilder queryBuilder;

    @Types({
        NewestPublishDate.class,
        OldestPublishDate.class,
        PageViews.class
    })
    private DynamicQuerySort sort;
}
