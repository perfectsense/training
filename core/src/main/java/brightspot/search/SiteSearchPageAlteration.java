package brightspot.search;

import brightspot.author.AuthorMatch;
import brightspot.itemstream.DateRangeMatch;
import brightspot.itemstream.page.DynamicPageItemStreamAllMatch;
import brightspot.itemstream.page.DynamicPageItemStreamAnyMatch;
import brightspot.itemstream.page.DynamicPageItemStreamNoneMatch;
import brightspot.query.QueryBuilder;
import brightspot.query.QueryBuilderDynamicQueryModifier;
import brightspot.section.AllSectionMatch;
import brightspot.tag.TagMatch;
import com.psddev.dari.db.Alteration;

public class SiteSearchPageAlteration extends Alteration<SiteSearchPage> {

    @InternalName(QueryBuilderDynamicQueryModifier.QUERY_BUILDER_FIELD)
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
}
