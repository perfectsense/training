package brightspot.core.slug;

import com.psddev.dari.db.Recordable;

public interface Sluggable extends Recordable {

    default SluggableData asSluggableData() {
        return as(SluggableData.class);
    }

    default String getSluggableSlugFallback() {
        return null;
    }
}
