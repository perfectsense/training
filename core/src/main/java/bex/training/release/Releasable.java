package bex.training.release;

import java.util.Date;

import com.psddev.dari.db.Recordable;

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
