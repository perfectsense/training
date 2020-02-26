package brightspot.core.site;

import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;

public interface IntegrationItems extends Recordable {

    default IntegrationItemsData asIntegrationItemsData() {
        return as(IntegrationItemsData.class);
    }

    @FieldInternalNamePrefix("integrations.")
    class IntegrationItemsData extends Modification<IntegrationItems> {

        private boolean disabled;

        public boolean isDisabled() {
            return disabled;
        }

        public void setDisabled(boolean disabled) {
            this.disabled = disabled;
        }
    }
}
