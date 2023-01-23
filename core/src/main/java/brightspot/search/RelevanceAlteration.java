package brightspot.search;

import brightspot.itemstream.DynamicQuerySort;
import brightspot.search.sortalphabetical.AlphabeticalAscendingDynamicQuerySort;
import brightspot.search.sortalphabetical.AlphabeticalDescendingDynamicQuerySort;
import brightspot.sort.publishdate.NewestPublishDate;
import brightspot.sort.publishdate.OldestPublishDate;
import com.psddev.dari.db.Alteration;

public class RelevanceAlteration extends Alteration<Relevance> {

    @Types({
            NewestPublishDate.class,
            OldestPublishDate.class,
            AlphabeticalAscendingDynamicQuerySort.class,
            AlphabeticalDescendingDynamicQuerySort.class
    })
    private DynamicQuerySort fallbackSort;
}
