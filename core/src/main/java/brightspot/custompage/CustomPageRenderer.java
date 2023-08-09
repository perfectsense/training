package brightspot.custompage;

import com.psddev.cms.page.PageRequest;
import com.psddev.cms.page.PageViewModelCreator;
import com.psddev.cms.render.Renderer;
import com.psddev.cms.view.PageEntryView;
import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.ViewModelCreator;
import com.psddev.cms.view.ViewOutput;
import com.psddev.cms.view.ViewRenderer;
import com.psddev.cms.view.ViewResponse;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.web.WebRequest;

/**
 * When this Renderer is assigned to a Custom Page, PageFilter will find it and use it instead of looking for a
 * ViewModel that implements PageEntryView.
 * <p>
 * This Renderer looks up that ViewModel (see: CustomPageViewModel) and delegates to it.
 * CustomPageViewModel#getInnerHtml then delegates back to CustomPageRenderer's delegateRenderer to render the
 * handlebars file or template published there.
 */
@Recordable.DisplayName("Custom Page")
public class CustomPageRenderer extends Renderer {

    @TypesExclude(CustomPageRenderer.class)
    @Required
    private Renderer delegateRenderer;

    @Override
    public String render(Recordable content) {

        ViewModel<?> viewModel = createViewModel(content, PageEntryView.class);
        ViewRenderer renderer = ViewRenderer.createRenderer(viewModel);
        ViewOutput result = renderer.renderTopmost(
            viewModel,
            WebRequest.getCurrent()
                .as(PageRequest.class)
                .getViewTemplateLoader());

        return result.get();
    }

    private static <T> ViewModel<? super T> createViewModel(T object, Class view) {
        if (ObjectUtils.isBlank(view)) {
            throw new IllegalArgumentException();
        }

        Class<? extends ViewModel<? super T>> viewModelClass = ViewModel.findViewModelClass(view, object);
        ViewModelCreator viewModelCreator = new PageViewModelCreator();
        ViewResponse viewResponse = new ViewResponse();
        ViewModel<? super T> viewModel = viewModelCreator.createViewModel(viewModelClass, object, viewResponse);

        return viewModel;
    }

    public Renderer getDelegateRenderer() {
        return delegateRenderer;
    }
}
