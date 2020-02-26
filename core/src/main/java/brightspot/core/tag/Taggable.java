package brightspot.core.tag;

import com.psddev.dari.db.Recordable;

public interface Taggable extends Recordable {

    default TaggableData asTaggableData() {
        return as(TaggableData.class);
    }
}
