package brightspot.pressrelease;

import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

import brightspot.author.AuthoringPageViewModel;
import brightspot.l10n.CurrentLocale;
import brightspot.page.AbstractContentPageViewModel;
import brightspot.page.CurrentPageViewModel;
import brightspot.page.PageViewModel;
import brightspot.update.LastUpdatedProvider;
import brightspot.util.DateTimeUtils;
import brightspot.util.RichTextUtils;
import com.psddev.cms.view.PageEntryView;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.styleguide.page.CreativeWorkPageViewAuthorsField;
import com.psddev.styleguide.page.CreativeWorkPageViewHeadlineField;
import com.psddev.styleguide.page.CreativeWorkPageViewSponsorLogoField;
import com.psddev.styleguide.page.CreativeWorkPageViewSponsorNameField;
import com.psddev.styleguide.page.CreativeWorkPageViewSubHeadlineField;
import com.psddev.styleguide.page.PageViewPageSubHeadingField;
import com.psddev.styleguide.pressrelease.PressReleasePageView;
import com.psddev.styleguide.pressrelease.PressReleasePageViewArticleBodyField;
import com.psddev.styleguide.pressrelease.PressReleasePageViewAttributionBodyField;
import com.psddev.styleguide.pressrelease.PressReleasePageViewLeadField;

public class PressReleasePageViewModel extends AbstractContentPageViewModel<PressRelease> implements
    PressReleasePageView,
    PageEntryView {

    private static final String DATE_FORMAT_KEY = "dateFormat";

    @CurrentLocale
    private Locale locale;

    @CurrentPageViewModel(AuthoringPageViewModel.class)
    AuthoringPageViewModel authoringPage;

    @Override
    public CharSequence getDateModified() {
        // Plain text
        return DateTimeUtils.format(
            LastUpdatedProvider.getMostRecentUpdateDate(model), PressReleasePageView.class,
            DATE_FORMAT_KEY, page.getSite(), locale, PageViewModel.DEFAULT_DATE_FORMAT
        );
    }

    @Override
    public CharSequence getDateModifiedISO() {
        return Optional.ofNullable(ObjectUtils.firstNonNull(
                LastUpdatedProvider.getMostRecentUpdateDate(model),
                model.getPublishDate()))
            .map(Date::toInstant)
            .map(Instant::toString)
            .orElse(null);
    }

    @Override
    public CharSequence getDatePublished() {
        // Plain text
        return DateTimeUtils.format(model.getPublishDate(), PressReleasePageView.class, DATE_FORMAT_KEY, page.getSite(),
            locale, PageViewModel.DEFAULT_DATE_FORMAT
        );
    }

    @Override
    public CharSequence getDatePublishedISO() {
        return Optional.ofNullable(model.getPublishDate())
            .map(Date::toInstant)
            .map(Instant::toString)
            .orElse(null);
    }

    @Override
    public Iterable<? extends CreativeWorkPageViewHeadlineField> getHeadline() {
        return RichTextUtils.buildInlineHtml(
            model,
            PressRelease::getHeadline,
            e -> createView(CreativeWorkPageViewHeadlineField.class, e));
    }

    @Override
    public Iterable<? extends CreativeWorkPageViewSubHeadlineField> getSubHeadline() {
        return RichTextUtils.buildInlineHtml(
            model,
            PressRelease::getSubheadline,
            e -> createView(CreativeWorkPageViewSubHeadlineField.class, e));
    }

    @Override
    public Iterable<? extends PressReleasePageViewArticleBodyField> getArticleBody() {
        return RichTextUtils.buildHtml(
            model,
            PressRelease::getBody,
            e -> createView(PressReleasePageViewArticleBodyField.class, e));
    }

    @Override
    public Iterable<? extends PressReleasePageViewLeadField> getLead() {
        return createViews(PressReleasePageViewLeadField.class, model.getLead());
    }

    @Override
    public Iterable<? extends PressReleasePageViewAttributionBodyField> getAttributionBody() {
        return null;
    }

    @Override
    public Iterable<? extends PageViewPageSubHeadingField> getPageSubHeading() {
        return null;
    }

    @Override
    public CharSequence getSource() {
        return null;
    }

    @Override
    public Iterable<? extends CreativeWorkPageViewSponsorLogoField> getSponsorLogo() {
        return null;
    }

    @Override
    public CharSequence getSponsorMeaningTarget() {
        return null;
    }

    @Override
    public CharSequence getSponsorMeaningUrl() {
        return null;
    }

    @Override
    public CharSequence getSponsorDisplayText() {
        return null;
    }

    @Override
    public Iterable<? extends CreativeWorkPageViewSponsorNameField> getSponsorName() {
        return null;
    }

    @Override
    public CharSequence getSponsorTarget() {
        return null;
    }

    @Override
    public CharSequence getSponsorUrl() {
        return null;
    }

    @Override
    public Boolean getSponsored() {
        return null;
    }

    @Override
    public Iterable<? extends CreativeWorkPageViewAuthorsField> getAuthors() {
        return null;
    }
}
