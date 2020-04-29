package bex.training.alphasort;

import com.psddev.dari.db.Recordable;

public interface AlphaSortable extends Recordable {

    String getAlphaSortValue();

    default AlphaSortableData asAlphaSortableData() {
        return as(AlphaSortableData.class);
    }
}
