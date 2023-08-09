package brightspot.module.list.page;

import brightspot.author.AuthorMatch;
import brightspot.blog.BlogMatch;
import brightspot.itemstream.DateRangeMatch;
import brightspot.itemstream.DynamicQuerySort;
import brightspot.query.QueryBuilder;
import brightspot.query.QueryBuilderDynamicQueryModifier;
import brightspot.search.sortalphabetical.AlphabeticalAscendingDynamicQuerySort;
import brightspot.search.sortalphabetical.AlphabeticalDescendingDynamicQuerySort;
import brightspot.section.AllSectionMatch;
import brightspot.sort.pageviews.PageViews;
import brightspot.sort.publishdate.NewestPublishDate;
import brightspot.sort.publishdate.OldestPublishDate;
import brightspot.tag.TagMatch;
import com.psddev.dari.db.Alteration;

public class DynamicPageItemStreamAlteration extends Alteration<DynamicPageItemStream> {

    @InternalName(QueryBuilderDynamicQueryModifier.QUERY_BUILDER_FIELD)
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
        AlphabeticalAscendingDynamicQuerySort.class,
        AlphabeticalDescendingDynamicQuerySort.class,
        PageViews.class
    })
    private DynamicQuerySort sort;
}
