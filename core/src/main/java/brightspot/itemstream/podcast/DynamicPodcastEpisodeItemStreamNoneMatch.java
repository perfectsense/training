package brightspot.itemstream.podcast;

import java.util.ArrayList;
import java.util.List;

import brightspot.itemstream.DateRangeMatch;
import brightspot.podcast.PodcastMatch;
import brightspot.query.QueryBuilder;
import brightspot.query.QueryBuilderNoneMatch;
import brightspot.section.AllSectionMatch;
import brightspot.tag.TagMatch;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("None Match")
@Recordable.Embedded
public class DynamicPodcastEpisodeItemStreamNoneMatch extends Record implements QueryBuilderNoneMatch {

    @CollectionMinimum(1)
    @Types({
        DateRangeMatch.class,
        AllSectionMatch.class,
        PodcastMatch.class,
        TagMatch.class,
        DynamicPodcastEpisodeItemStreamAllMatch.class,
        DynamicPodcastEpisodeItemStreamAnyMatch.class
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
