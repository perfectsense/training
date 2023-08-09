package brightspot.custompage;

import java.util.Optional;

import brightspot.page.AbstractPageViewModel;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.view.PageEntryView;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.State;
import com.psddev.styleguide.RawHtml;
import com.psddev.styleguide.page.CustomPageView;

/**
 * This ViewModel is intended to be invoked by CustomPageRenderer. See the documentation there for details.
 */
public class CustomPageViewModel extends AbstractPageViewModel<CustomPage> implements PageEntryView, CustomPageView {

    @Override
    public CharSequence getInnerHtml() {
        return Optional.of(model)
            .map(Recordable::getState)
            .map(State::getType)
            .map(t -> t.as(ToolUi.class).getRenderer())
            .filter(CustomPageRenderer.class::isInstance)
            .map(CustomPageRenderer.class::cast)
            .map(CustomPageRenderer::getDelegateRenderer)
            .map(delegate -> delegate.render(model))
            .map(RawHtml::of)
            .orElse(null);
    }
}
