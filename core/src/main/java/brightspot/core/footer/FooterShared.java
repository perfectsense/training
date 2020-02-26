package brightspot.core.footer;

import java.util.Optional;

import com.psddev.cms.view.ModelWrapper;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Shared")
@Recordable.Embedded
public class FooterShared extends Record implements ModelWrapper, FooterModuleType {

    @Where("isFooterModuleType = true")
    private SharedFooterType module;

    public SharedFooterType getModule() {
        return module;
    }

    public void setModule(SharedFooterType module) {
        this.module = module;
    }

    @Override
    public Object unwrap() {
        return getModule();
    }

    @Override
    public String getLabel() {
        return Optional.ofNullable(module)
            .map(m -> m.getState().getLabel())
            .orElse(null);
    }
}
