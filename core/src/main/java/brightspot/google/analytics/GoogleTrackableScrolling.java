package brightspot.google.analytics;

import java.util.Arrays;
import java.util.List;

import brightspot.core.site.IntegrationExtraBodyAttributes;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Google Scrolling Analytics")
public class GoogleTrackableScrolling extends Record implements IntegrationExtraBodyAttributes {

    private static final String CATEGORY = "scroll tracking";
    private static final List<String> EVENTS = Arrays.asList("scroll 25%", "scroll 50%", "scroll 75%", "scroll 100%");
    private static final String LINK_LABEL = "<current page>";

    @Override
    public String getLabel() {
        return "Google Scrolling Analytics";
    }

    @ToolUi.ReadOnly
    private String category = CATEGORY;

    @ToolUi.ReadOnly
    private List<String> trackedEvents = EVENTS;

    @ToolUi.ReadOnly
    private String label = LINK_LABEL;
}
