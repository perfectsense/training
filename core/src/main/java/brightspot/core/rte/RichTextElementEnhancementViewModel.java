package brightspot.core.rte;

import java.util.Optional;

import com.psddev.cms.db.RichTextElement;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.view.ViewModel;
import com.psddev.dari.db.State;
import com.psddev.styleguide.core.enhancement.EnhancementView;
import com.psddev.styleguide.core.enhancement.EnhancementViewItemField;

public class RichTextElementEnhancementViewModel extends ViewModel<RichTextElement> implements EnhancementView {

    @Override
    public Boolean getInline() {
        return Optional.ofNullable(model.getState())
            .map(State::getType)
            .map(type -> type.as(ToolUi.class))
            .map(ToolUi::getRichTextElementTagSettings)
            .map(s -> !s.isBlock())
            .orElse(true);
    }

    @Override
    public Iterable<? extends EnhancementViewItemField> getItem() {
        Object item = model;

        while (item instanceof RichTextElementWrapper) {
            item = ((RichTextElementWrapper) item).unwrap();
        }

        return createViews(EnhancementViewItemField.class, item);
    }
}
