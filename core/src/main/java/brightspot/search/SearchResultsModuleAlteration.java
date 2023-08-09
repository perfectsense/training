package brightspot.search;

import brightspot.author.AuthorMatch;
import brightspot.blog.BlogMatch;
import brightspot.itemstream.DateRangeMatch;
import brightspot.module.list.page.DynamicPageItemStreamAllMatch;
import brightspot.module.list.page.DynamicPageItemStreamAnyMatch;
import brightspot.module.list.page.DynamicPageItemStreamNoneMatch;
import brightspot.query.QueryBuilder;
import brightspot.query.QueryBuilderDynamicQueryModifier;
import brightspot.section.AllSectionMatch;
import brightspot.tag.TagMatch;
import com.psddev.dari.db.Alteration;

public class SearchResultsModuleAlteration extends Alteration<SearchResultsModule> {

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
}
