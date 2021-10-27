package brightspot.liveblog;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.StringException;

@Recordable.DisplayName("Manual")
public class ManualLiveEventOption extends LiveEventOption {

    @Required
    @ToolUi.Note("This is the date that the live blog will expire on.")
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

    // --- Recordable support ---

    @Override
    protected void onValidate() {

        Instant now = Instant.now();
        if (getExpirationDate() != null && getExpirationDate().toInstant().isBefore(now)) {

            getState().addError(
                    getState().getField("expirationDate"),
                    new StringException("Expiration date must be in the future!"));
        }
    }
}
