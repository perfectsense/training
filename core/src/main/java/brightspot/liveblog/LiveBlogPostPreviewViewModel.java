package brightspot.liveblog;

import com.psddev.cms.page.PageRequest;
import com.psddev.cms.view.DelegateView;
import com.psddev.cms.view.PreviewEntryView;
import com.psddev.cms.view.ViewModel;
import com.psddev.dari.web.WebRequest;
import com.psddev.styleguide.liveblog.LiveBlogPageView;

public class LiveBlogPostPreviewViewModel extends ViewModel<LiveBlogPost> implements
    DelegateView<LiveBlogPageView>,
    PreviewEntryView {

    @Override
    public LiveBlogPageView getDelegate() {

        // Get the parent live blog.
        LiveBlog liveBlog = model.getLiveBlog();
        if (liveBlog == null) {
            return null;
        }

        // Set the current post as the first pinned post.
        liveBlog.getPinnedPosts().add(0, model);

        // Swap out the main object to be the parent live blog.
        WebRequest.getCurrent().as(PageRequest.class).setMainContent(liveBlog);

        return createView(LiveBlogPageView.class, liveBlog);
    }
}
