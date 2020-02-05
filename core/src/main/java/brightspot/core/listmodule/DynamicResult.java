package brightspot.core.listmodule;

import com.psddev.cms.db.Localization;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.State;

@Recordable.Embedded
public class DynamicResult extends Record implements DynamicPin {

    private transient Object item;

    public Object getItem() {
        return item;
    }

    public void setItem(Object item) {
        this.item = item;
    }

    @Override
    public String getLabel() {
        Object item = getItem();

        if (item != null) {
            State itemState = State.getInstance(item);
            ObjectType itemType = itemState.getType();
            return Localization.currentUserText(itemType, "displayName", itemType.getDisplayName()) + ": "
                + itemState.getLabel();

        } else {
            return Localization.currentUserText(getClass(), "label.empty", "Empty");
        }
    }
}
