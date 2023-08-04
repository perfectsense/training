package brightspot.module.list.author;

import brightspot.itemstream.DynamicQuerySort;
import brightspot.search.sortalphabetical.AlphabeticalAscendingDynamicQuerySort;
import brightspot.search.sortalphabetical.AlphabeticalDescendingDynamicQuerySort;
import brightspot.sort.pageviews.PageViews;
import brightspot.sort.publishdate.NewestPublishDate;
import brightspot.sort.publishdate.OldestPublishDate;
import com.psddev.dari.db.Alteration;
import com.psddev.dari.db.Recordable;

public class DynamicAuthorItemStreamAlteration extends Alteration<DynamicAuthorItemStream> {

    @Recordable.Types({
        NewestPublishDate.class,
        OldestPublishDate.class,
        AlphabeticalAscendingDynamicQuerySort.class,
        AlphabeticalDescendingDynamicQuerySort.class,
        PageViews.class
    })
    private DynamicQuerySort sort;

}
