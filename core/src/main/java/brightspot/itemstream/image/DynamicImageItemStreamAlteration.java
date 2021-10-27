package brightspot.itemstream.image;

import brightspot.imageitemstream.DynamicImageItemStream;
import brightspot.itemstream.DateRangeMatch;
import brightspot.itemstream.DynamicQuerySort;
import brightspot.query.QueryBuilder;
import brightspot.query.QueryBuilderDynamicQueryModifier;
import brightspot.search.sortalphabetical.AlphabeticalAscendingDynamicQuerySort;
import brightspot.search.sortalphabetical.AlphabeticalDescendingDynamicQuerySort;
import brightspot.sort.publishdate.NewestPublishDate;
import brightspot.sort.publishdate.OldestPublishDate;
import brightspot.tag.TagMatch;
import com.psddev.dari.db.Alteration;

public class DynamicImageItemStreamAlteration extends Alteration<DynamicImageItemStream> {

    @InternalName(QueryBuilderDynamicQueryModifier.QUERY_BUILDER_FIELD)
    @Types({
            DateRangeMatch.class,
            DynamicImageItemStreamAllMatch.class,
            DynamicImageItemStreamAnyMatch.class,
            DynamicImageItemStreamNoneMatch.class,
            TagMatch.class
    })
    private QueryBuilder queryBuilder;

    @Types({
            NewestPublishDate.class,
            OldestPublishDate.class,
            AlphabeticalAscendingDynamicQuerySort.class,
            AlphabeticalDescendingDynamicQuerySort.class
    })
    private DynamicQuerySort sort;
}
