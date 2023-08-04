package brightspot.liveblog;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import com.google.common.collect.ImmutableMap;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.tool.ToolPageContext;
import com.psddev.cms.ui.LocalizationContext;
import com.psddev.cms.ui.ToolLocalization;
import com.psddev.cms.ui.form.EditablePlaceholder;
import com.psddev.cms.ui.form.Placeholder;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Automatic")
@ToolUi.NoteHtml("<span data-dynamic-html='${content.isLiveEventNote(toolPageContext)}'></span>")
public class AutomaticLiveEventOption extends LiveEventOption {

    private static final int DEFAULT_HOURS_BEFORE_NOT_LIVE = 24;

    @Placeholder(DEFAULT_HOURS_BEFORE_NOT_LIVE + "")
    @EditablePlaceholder
    private Integer hoursBeforeBlogIsNotLive;

    public String isLiveEventNote(ToolPageContext page) {

        final int numberOfHoursBeforeNotLive = getHoursBeforeBlogIsNotLive();

        return ToolLocalization.text(new LocalizationContext(
            getClass(),
            ImmutableMap.of("hours", numberOfHoursBeforeNotLive)), "note.isLiveEvent");
    }

    // --- LiveEventOption support ---

    @Override
    public boolean isLiveEvent(
        Site site,
        Instant lastUpdate,
        Instant now) {

        final int numberOfHoursBeforeNotLive = getHoursBeforeBlogIsNotLive();

        // Ensure live blog has been updated within the past X hours
        Instant buffer = now.minus(numberOfHoursBeforeNotLive, ChronoUnit.HOURS);
        return lastUpdate.isAfter(buffer);
    }

    public int getHoursBeforeBlogIsNotLive() {
        return hoursBeforeBlogIsNotLive != null
            ? hoursBeforeBlogIsNotLive
            : DEFAULT_HOURS_BEFORE_NOT_LIVE;
    }

    public void setHoursBeforeBlogIsNotLive(Integer hoursBeforeBlogIsNotLive) {
        this.hoursBeforeBlogIsNotLive = hoursBeforeBlogIsNotLive;
    }
}
