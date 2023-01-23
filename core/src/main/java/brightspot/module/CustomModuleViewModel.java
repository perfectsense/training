package brightspot.module;

import java.util.Optional;

import com.psddev.cms.db.ToolUi;
import com.psddev.cms.view.ViewModel;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.State;
import com.psddev.styleguide.RawHtml;
import com.psddev.styleguide.custom.CustomModuleView;

/**
 * Render any Editorial Content Type extensions of {@link CustomModule} anywhere
 * {@link CustomModuleView} can be placed.
 */
public class CustomModuleViewModel extends ViewModel<CustomModule> implements CustomModuleView {

    @Override
    public CharSequence getHtml() {

        return Optional.of(model)
            .map(Recordable::getState)
            .map(State::getType)
            .map(t -> t.as(ToolUi.class).getRenderer())
            .map(renderer -> renderer.render(model))
            .map(RawHtml::of)
            .orElse(null);
    }
}
