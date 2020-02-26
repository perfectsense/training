package brightspot.core.timed;

import java.util.UUID;

import com.psddev.dari.db.Recordable;

public interface TimedContentMetaData extends Recordable {

    /**
     * Returns the {@code headline} or "title".
     *
     * @return a plain text {@link String} (required).
     */
    String getHeadline();

    /**
     * Returns the {@code subHeadline} or "description".
     *
     * @return a plain text {@link String} (optional).
     */
    String getSubHeadline();

    /**
     * Returns the duration (in milliseconds) of the content.
     *
     * @return a positive {@link Long} value (optional).
     */
    Long getDuration();

    /**
     * Convenience method to return the {@code ID} of the object.
     *
     * @return a {@link UUID} (Never {@code null}).
     */
    UUID getId();
}
