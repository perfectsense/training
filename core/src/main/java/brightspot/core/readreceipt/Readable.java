package brightspot.core.readreceipt;

import java.util.Date;
import java.util.UUID;

import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.ValidationException;
import com.psddev.dari.util.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Readable extends Recordable {

    Logger LOGGER = LoggerFactory.getLogger(Readable.class);

    /**
     * Returns {@code true} if the {@link Readable} has been read by the {@link com.psddev.auth.AuthenticationEntity}
     * with the specified ID, {@code false} otherwise.
     *
     * @param authenticationEntityId ID of the {@link com.psddev.auth.AuthenticationEntity} to check against.
     * @return {@code true} if the {@link Readable} has been ready the {@link com.psddev.auth.AuthenticationEntity} with
     * the specified ID, {@code false} otherwise.
     */
    default boolean isRead(UUID authenticationEntityId) {
        return Query.from(ReadReceipt.class)
            .where(
                ReadReceipt.ENTITY_READABLE_FIELD + " = ?",
                authenticationEntityId.toString() + "_" + getState().getId().toString())
            .hasMoreThan(0);
    }

    /**
     * Marks the {@link Readable} as having been read by the {@link com.psddev.auth.AuthenticationEntity} with the
     * specified ID on the specified {@link Date}.
     *
     * @param authenticationEntityId ID of the {@link com.psddev.auth.AuthenticationEntity} for which to mark this
     * {@link Readable} as read.
     * @param readDate the {@link Date} on which this {@link Readable} was read by the {@link
     * com.psddev.auth.AuthenticationEntity} with the specified ID.
     */
    default void markRead(UUID authenticationEntityId, Date readDate) {
        if (!Query.from(ReadReceipt.class)
            .where(
                ReadReceipt.ENTITY_READABLE_FIELD + " = ?",
                authenticationEntityId.toString() + "_" + getState().getId().toString())
            .noCache()
            .hasMoreThan(0)) {

            try {
                ReadReceipt receipt = new ReadReceipt();
                receipt.setAuthenticationEntityId(authenticationEntityId);
                receipt.setReadableId(getState().getId());
                receipt.setReadDate(readDate);
                receipt.save();
            } catch (ValidationException e) {

                LOGGER.warn("Attempted to save duplicate ReadReceipt for authenticationEntityId: "
                    + authenticationEntityId.toString() + ", readableId: " + getState().getId().toString());
                // Ignore.
            }
        } else {
            ReadReceipt receipt = Query.from(ReadReceipt.class)
                .where(
                    ReadReceipt.ENTITY_READABLE_FIELD + " = ?",
                    authenticationEntityId.toString() + "_" + getState().getId().toString())
                .noCache()
                .first();

            if (!ObjectUtils.equals(receipt.getReadDate(), readDate)) {
                receipt.setReadDate(readDate);
                receipt.save();
            }
        }
    }
}
