package brightspot.article;

import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;

@Recordable.FieldInternalNamePrefix(ListicleItemModification.FIELD_PREFIX)
public class ListicleItemModification extends Modification<ListicleItem> {

    public static final String FIELD_PREFIX = "listicleItem.";

    private transient int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
