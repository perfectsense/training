package brightspot.module.list.podcast;

import brightspot.itemstream.DateRangeMatch;
import brightspot.itemstream.DynamicQuerySort;
import brightspot.itemstream.podcast.DynamicPodcastItemStreamAllMatch;
import brightspot.itemstream.podcast.DynamicPodcastItemStreamAnyMatch;
import brightspot.itemstream.podcast.DynamicPodcastItemStreamNoneMatch;
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
import com.psddev.dari.db.Recordable;

public class DynamicPodcastItemStreamAlteration extends Alteration<DynamicPodcastItemStream> {

    @InternalName(QueryBuilderDynamicQueryModifier.QUERY_BUILDER_FIELD)
    @Types({
            DateRangeMatch.class,
            AllSectionMatch.class,
            DynamicPodcastItemStreamAllMatch.class,
            DynamicPodcastItemStreamAnyMatch.class,
            DynamicPodcastItemStreamNoneMatch.class,
            TagMatch.class
    })
    private QueryBuilder queryBuilder;

    @Recordable.Types({
            NewestPublishDate.class,
            OldestPublishDate.class,
            AlphabeticalAscendingDynamicQuerySort.class,
            AlphabeticalDescendingDynamicQuerySort.class,
            PageViews.class
    })
    private DynamicQuerySort sort;
}
