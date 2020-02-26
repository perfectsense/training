package brightspot.core.module;

import com.psddev.cms.view.ModelWrapper;

public class Shared extends ModuleType implements ModelWrapper {

    @Required
    @Where("isFooterModuleTypeOnly != true")
    private Module module;

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    @Override
    public String getLabel() {
        Module module = getModule();
        return module != null ? module.getLabel() : super.getLabel();
    }

    @Override
    public Object unwrap() {
        return getModule();
    }
}
