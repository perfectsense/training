package brightspot.module.list.page;

import java.util.ArrayList;
import java.util.List;

import brightspot.author.AuthorMatch;
import brightspot.blog.BlogMatch;
import brightspot.itemstream.DateRangeMatch;
import brightspot.query.QueryBuilder;
import brightspot.query.QueryBuilderAnyMatch;
import brightspot.section.AllSectionMatch;
import brightspot.tag.TagMatch;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Any Match")
@Recordable.Embedded
public class DynamicPageItemStreamAnyMatch extends Record implements QueryBuilderAnyMatch {

    @CollectionMinimum(1)
    @Types({
        AuthorMatch.class,
        BlogMatch.class,
        DateRangeMatch.class,
        DynamicPageItemStreamAllMatch.class,
        DynamicPageItemStreamNoneMatch.class,
        AllSectionMatch.class,
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
