package brightspot.liveblog;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import com.psddev.cms.db.Site;
import com.psddev.cms.ui.ToolLocalization;
import com.psddev.cms.ui.form.DynamicNoteMethod;
import com.psddev.cms.ui.form.Note;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.html.Node;

import static com.psddev.dari.html.Nodes.DIV;

@Recordable.DisplayName("Manual")
public class ManualLiveEventOption extends LiveEventOption {

    @Required
    @Note("This is the date that the live blog will expire on.")
    @DynamicNoteMethod("getPastExpirationDateNote")
    private Date expirationDate;

    public Date getExpirationDate() {

        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {

        this.expirationDate = expirationDate;
    }

    // --- LiveEventOption support ---

    @Override
    public boolean isLiveEvent(
        Site site,
        Instant lastUpdate,
        Instant now) {

        return Optional.ofNullable(getExpirationDate())
            .map(Date::toInstant)
            .map(instant -> instant.isAfter(now))
            .orElse(false);
    }

    private Node getPastExpirationDateNote() {
        Instant now = Instant.now();
        if (getExpirationDate().toInstant().isBefore(now)) {
            return DIV.classList("Message is-warning").with(ToolLocalization.text(ManualLiveEventOption.class, "note.expiredDate", "Expiration Date set in the past"));
        }
        return null;
    }
}
