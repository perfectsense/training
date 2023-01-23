package brightspot.itemstream.podcast;

import java.util.ArrayList;
import java.util.List;

import brightspot.itemstream.DateRangeMatch;
import brightspot.query.QueryBuilder;
import brightspot.query.QueryBuilderAllMatch;
import brightspot.section.AllSectionMatch;
import brightspot.tag.TagMatch;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("All Match")
@Recordable.Embedded
public class DynamicPodcastItemStreamAllMatch extends Record implements QueryBuilderAllMatch {

    @CollectionMinimum(1)
    @Types({
        DateRangeMatch.class,
        AllSectionMatch.class,
        DynamicPodcastItemStreamAnyMatch.class,
        DynamicPodcastItemStreamNoneMatch.class,
        TagMatch.class
    })
    private List<QueryBuilder> rules;

    @Override
    public List<QueryBuilder> getRules() {
        if (rules == null) {
            rules = new ArrayList<>();
        }
        return rules;
    }

    @Override
    public void setRules(List<QueryBuilder> rules) {
        this.rules = rules;
    }
}
