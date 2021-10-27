package brightspot.liveblog;

import java.util.Date;
import java.util.Locale;
import java.util.Optional;

import brightspot.l10n.CurrentLocale;
import brightspot.page.CurrentPageViewModel;
import brightspot.page.PageViewModel;
import brightspot.util.DateTimeUtils;
import brightspot.util.RichTextUtils;
import com.psddev.cms.db.Site;
import com.psddev.cms.page.CurrentSite;
import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.liveblog.LiveBlogPostView;
import com.psddev.styleguide.liveblog.LiveBlogPostViewActionsField;
import com.psddev.styleguide.liveblog.LiveBlogPostViewAuthorsField;
import com.psddev.styleguide.liveblog.LiveBlogPostViewBodyField;
import com.psddev.styleguide.liveblog.LiveBlogPostViewHeadlineField;

public class LiveBlogPostViewModel extends ViewModel<LiveBlogPost> implements LiveBlogPostView {

    public static final String DATE_FORMAT_KEY = "dateFormat";

    public static final String DEFAULT_DATE_FORMAT = "MMMM d, yyyy 'at' h:mm a z";

    @CurrentPageViewModel(PageViewModel.class)
    protected PageViewModel page;

    @CurrentSite
    protected Site currentSite;

    @CurrentLocale
    protected Locale locale;

    @Override
    public Iterable<? extends LiveBlogPostViewActionsField> getActions() {

        return createViews(LiveBlogPostViewActionsField.class, model);
    }

    @Override
    public CharSequence getAnchor() {

        return model.getAnchorLinkableAnchor();
    }

    @Override
    public Iterable<? extends LiveBlogPostViewBodyField> getBody() {
        return RichTextUtils.buildHtml(model, LiveBlogPost::getBody,
                e -> createView(LiveBlogPostViewBodyField.class, e));
    }

    @Override
    public CharSequence getPostedDate() {

        return Optional.ofNullable(model.getPublishDate())
                .map(date -> DateTimeUtils.format(date, LiveBlogPostView.class,
                        DATE_FORMAT_KEY, currentSite, locale, DEFAULT_DATE_FORMAT))
                .orElse(null);
    }

    @Override
    public Number getPostedDateTimestamp() {

        return Optional.ofNullable(model.getPublishDate())
                .map(Date::getTime)
                .orElse(null);
    }

    @Override
    public CharSequence getUpdateDate() {

        return Optional.ofNullable(model.getUpdateDateOverride())
                .map(date -> DateTimeUtils.format(date, LiveBlogPostView.class,
                        DATE_FORMAT_KEY, currentSite, locale, DEFAULT_DATE_FORMAT))
                .orElse(null);
    }

    @Override
    public Number getUpdateDateTimestamp() {

        return Optional.ofNullable(model.getUpdateDateOverride())
                .map(Date::getTime)
                .orElse(null);
    }

    @Override
    public CharSequence getPostId() {
        return model.getId().toString();
    }

    @Override
    public CharSequence getPostUrl() {
        return model.getPostUrl(currentSite);
    }

    @Override
    public Iterable<? extends LiveBlogPostViewHeadlineField> getHeadline() {
        return RichTextUtils.buildInlineHtml(model, LiveBlogPost::getHeadline,
                e -> createView(LiveBlogPostViewHeadlineField.class, e));
    }

    @Override
    public Iterable<? extends LiveBlogPostViewAuthorsField> getAuthors() {
        return createViews(LiveBlogPostViewAuthorsField.class, model.asHasAuthorsData().getAuthors());
    }
}
