package bex.training.release;

import com.psddev.dari.db.Recordable;

import java.util.Date;

public interface Releasable extends Recordable {

    default ReleasableData asReleasableData() {
        return as(ReleasableData.class);
    }

    default Date getReleaseDate() {
        return asReleasableData().getReleaseDate();
    }

    default void setReleaseDate(Date releaseDate) {
        asReleasableData().setReleaseDate(releaseDate);
    }
}
