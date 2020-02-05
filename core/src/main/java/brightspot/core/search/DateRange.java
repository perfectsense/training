package brightspot.core.search;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Predicate;
import com.psddev.dari.db.PredicateParser;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
public class DateRange extends Record {

    @Required
    private String label;

    @DisplayName("From (Days Ago)")
    @ToolUi.Placeholder("Today")
    private Integer start;

    @DisplayName("To (Days Ago)")
    @ToolUi.Placeholder("Now")
    private Integer end;

    @ToolUi.Hidden
    private String parameterValue;

    @Override
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    public String getParameterValue() {
        return parameterValue;
    }

    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }

    public Predicate toPredicate() {
        Integer start = getStart();

        if (start == null || start < 0) {
            start = 0;
        }

        Integer end = getEnd();

        if (end == null || end < 0) {
            end = 0;
        }

        Instant now = Instant.now();

        return PredicateParser.Static.parse(
            "cms.content.publishDate >= ? and cms.content.publishDate <= ?",
            now.minus(start + 1, ChronoUnit.DAYS).toEpochMilli(),
            now.minus(end, ChronoUnit.DAYS).toEpochMilli());
    }
}
