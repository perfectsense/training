package brightspot.core.ad;

import brightspot.core.footer.FooterModuleType;
import brightspot.core.module.ModuleType;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.view.ModelWrapper;
import com.psddev.dari.db.Recordable;

/**
 * Wraps an AdModule for use as a {@link ModuleType}.
 */
@Recordable.DisplayName("Ad")
public class AdModuleWrapper extends ModuleType implements ModelWrapper, FooterModuleType {

    @ToolUi.DropDown
    @ToolUi.Unlabeled
    @Required
    private AdModule adModule;

    /**
     * @return AdModule
     */
    public AdModule getAdModule() {
        return adModule;
    }

    /**
     * @param adModule
     */
    public void setAdModule(AdModule adModule) {
        this.adModule = adModule;
    }

    @Override
    public Object unwrap() {
        return adModule;
    }

    @Override
    public String getLabel() {
        return adModule != null ? adModule.getLabel() : super.getLabel();
    }
}
