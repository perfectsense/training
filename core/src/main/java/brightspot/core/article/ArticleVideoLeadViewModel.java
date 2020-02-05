package brightspot.core.article;

import java.util.Optional;

import brightspot.core.video.Video;
import com.psddev.cms.view.DelegateView;
import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.core.article.ArticlePageViewLeadField;

/**
 * Delegates to an {@link ArticlePageViewLeadField} from {@link Video#getVideoProvider()}.
 */
public class ArticleVideoLeadViewModel extends ViewModel<Video>
    implements DelegateView<ArticlePageViewLeadField>, ArticlePageViewLeadField {

    @Override
    public ArticlePageViewLeadField getDelegate() {
        return Optional.ofNullable(model.getVideoProvider())
            .map(provider -> createView(ArticlePageViewLeadField.class, provider))
            .orElse(null);
    }
}
