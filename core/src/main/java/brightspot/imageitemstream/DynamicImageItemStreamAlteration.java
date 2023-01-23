package brightspot.imageitemstream;

import brightspot.itemstream.DateRangeMatch;
import brightspot.itemstream.DynamicQuerySort;
import brightspot.itemstream.image.DynamicImageItemStreamAllMatch;
import brightspot.itemstream.image.DynamicImageItemStreamAnyMatch;
import brightspot.itemstream.image.DynamicImageItemStreamNoneMatch;
import brightspot.query.QueryBuilder;
import brightspot.query.QueryBuilderDynamicQueryModifier;
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
            OldestPublishDate.class
    })
    private DynamicQuerySort sort;
}
