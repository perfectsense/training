package brightspot.search;

import brightspot.author.AuthorMatch;
import brightspot.itemstream.DateRangeMatch;
import brightspot.module.list.page.DynamicPageItemStreamAllMatch;
import brightspot.module.list.page.DynamicPageItemStreamAnyMatch;
import brightspot.module.list.page.DynamicPageItemStreamNoneMatch;
import brightspot.query.QueryBuilder;
import brightspot.query.QueryBuilderDynamicQueryModifier;
import brightspot.search.boost.BoostConfiguration;
import brightspot.section.AllSectionMatch;
import brightspot.tag.TagMatch;
import com.psddev.cms.ui.form.DynamicNoteClass;
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

    @DynamicNoteClass(SiteSearchPageBoostDynamicNote.class)
    @InternalName("searchboost.boostConfiguration")
    private BoostConfiguration boostConfiguration;
}
