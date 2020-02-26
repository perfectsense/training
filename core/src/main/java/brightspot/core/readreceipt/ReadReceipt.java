package brightspot.core.readreceipt;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.psddev.dari.db.Record;
import com.psddev.dari.util.ObjectUtils;

public class ReadReceipt extends Record {

    public static final String ENTITY_READABLE_FIELD = "getAuthenticationEntityIdReadableId";
    public static final String ENTITY_READ_DATE_FIELD = "getAuthenticationEntityIdReadDate";

    private UUID authenticationEntityId;

    private UUID readableId;

    private Date readDate;

    /**
     * Unique constraint against both the authentication entity ID and the readable ID prevents duplicate ReadReceipts
     * from being stored.
     *
     * @return unique key combining the {@link com.psddev.auth.AuthenticationEntity} ID and the {@link Readable} ID
     */
    @Indexed(unique = true)
    public String getAuthenticationEntityIdReadableId() {

        if (getAuthenticationEntityId() == null || getReadableId() == null) {
            return null;
        }

        //
        return getAuthenticationEntityId().toString() + "_" + getReadableId().toString();
    }

    /**
     * Indexed method used for sorting the ReadReceipts for a {@link com.psddev.auth.AuthenticationEntity} by the date
     * on which they were last read.  This index supports display of recently viewed content.
     *
     * @return combination of {@link com.psddev.auth.AuthenticationEntity} ID and the {@link #readDate} on which the
     * {@link Readable} was last read by the AuthenticationEntity.
     */
    @Indexed
    public String getAuthenticationEntityIdReadDate() {

        if (getAuthenticationEntityId() == null || getReadDate() == null) {
            return null;
        }

        // allows querying for existence based on read date for a given authentication entity
        return getAuthenticationEntityId().toString() + "_" + getReadDate().toString();
    }

    public UUID getAuthenticationEntityId() {
        return authenticationEntityId;
    }

    public void setAuthenticationEntityId(UUID authenticationEntityId) {
        this.authenticationEntityId = authenticationEntityId;
    }

    public UUID getReadableId() {
        return readableId;
    }

    public void setReadableId(UUID readableId) {
        this.readableId = readableId;
    }

    public Date getReadDate() {
        return readDate;
    }

    public void setReadDate(Date readDate) {
        this.readDate = readDate;
    }

    @Override
    public String getLabel() {

        return ObjectUtils.firstNonNull(
            Stream.of(
                authenticationEntityId != null ? "AuthenticationEntity: " + authenticationEntityId.toString() : null,
                readableId != null ? "Readable: " + readableId.toString() : null
            ).filter(Objects::nonNull).collect(Collectors.joining(", ")),
            "Untitled"
        );
    }
}
