package brightspot.module.list.attachment;

import brightspot.itemstream.DateRangeMatch;
import brightspot.itemstream.DynamicQuerySort;
import brightspot.itemstream.attachment.DynamicAttachmentItemStreamAllMatch;
import brightspot.itemstream.attachment.DynamicAttachmentItemStreamAnyMatch;
import brightspot.itemstream.attachment.DynamicAttachmentItemStreamNoneMatch;
import brightspot.query.QueryBuilder;
import brightspot.query.QueryBuilderDynamicQueryModifier;
import brightspot.sort.publishdate.NewestPublishDate;
import brightspot.sort.publishdate.OldestPublishDate;
import brightspot.tag.TagMatch;
import com.psddev.dari.db.Alteration;

public class DynamicAttachmentItemStreamAlteration extends Alteration<DynamicAttachmentItemStream> {

    @InternalName(QueryBuilderDynamicQueryModifier.QUERY_BUILDER_FIELD)
    @Types({
            DateRangeMatch.class,
            DynamicAttachmentItemStreamAllMatch.class,
            DynamicAttachmentItemStreamAnyMatch.class,
            DynamicAttachmentItemStreamNoneMatch.class,
            TagMatch.class
    })
    private QueryBuilder queryBuilder;

    @Types({
            NewestPublishDate.class,
            OldestPublishDate.class
    })
    private DynamicQuerySort sort;
}
