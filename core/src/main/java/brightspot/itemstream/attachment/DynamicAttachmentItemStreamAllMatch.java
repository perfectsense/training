package brightspot.itemstream.attachment;

import java.util.ArrayList;
import java.util.List;

import brightspot.itemstream.DateRangeMatch;
import brightspot.query.QueryBuilder;
import brightspot.query.QueryBuilderAllMatch;
import brightspot.tag.TagMatch;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("All Match")
@Recordable.Embedded
public class DynamicAttachmentItemStreamAllMatch extends Record implements QueryBuilderAllMatch {

    @CollectionMinimum(1)
    @Types({
            DateRangeMatch.class,
            DynamicAttachmentItemStreamAnyMatch.class,
            DynamicAttachmentItemStreamNoneMatch.class,
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
