package brightspot.itemstream.podcast;

import java.util.ArrayList;
import java.util.List;

import brightspot.itemstream.DateRangeMatch;
import brightspot.query.QueryBuilder;
import brightspot.query.QueryBuilderAnyMatch;
import brightspot.section.AllSectionMatch;
import brightspot.tag.TagMatch;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Any Match")
@Recordable.Embedded
public class DynamicPodcastItemStreamAnyMatch extends Record implements QueryBuilderAnyMatch {

    @CollectionMinimum(1)
    @Types({
        DateRangeMatch.class,
        AllSectionMatch.class,
        DynamicPodcastItemStreamAllMatch.class,
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
