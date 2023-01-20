package brightspot.playlist.video;

import brightspot.itemstream.DateRangeMatch;
import brightspot.itemstream.DynamicQuerySort;
import brightspot.itemstream.video.DynamicVideoItemStreamAllMatch;
import brightspot.itemstream.video.DynamicVideoItemStreamAnyMatch;
import brightspot.itemstream.video.DynamicVideoItemStreamNoneMatch;
import brightspot.query.QueryBuilder;
import brightspot.query.QueryBuilderDynamicQueryModifier;
import brightspot.sort.publishdate.NewestPublishDate;
import brightspot.sort.publishdate.OldestPublishDate;
import brightspot.tag.TagMatch;
import com.psddev.dari.db.Alteration;

public class DynamicVideoItemStreamAlteration extends Alteration<DynamicVideoItemStream> {

    @InternalName(QueryBuilderDynamicQueryModifier.QUERY_BUILDER_FIELD)
    @Types({
            DateRangeMatch.class,
            DynamicVideoItemStreamAllMatch.class,
            DynamicVideoItemStreamAnyMatch.class,
            DynamicVideoItemStreamNoneMatch.class,
            TagMatch.class
    })
    private QueryBuilder queryBuilder;

    @Types({
            NewestPublishDate.class,
            OldestPublishDate.class
    })
    private DynamicQuerySort sort;
}
