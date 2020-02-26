package brightspot.core.module;

import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.core.module.ModuleTypeView;
import com.psddev.styleguide.core.module.ModuleTypeViewContentField;

public class ModuleRichTextElementViewModel extends ViewModel<ModuleRichTextElement> implements ModuleTypeView {

    @Override
    public Iterable<? extends ModuleTypeViewContentField> getContent() {
        return createViews(ModuleTypeViewContentField.class, model.getModule());
    }
}
