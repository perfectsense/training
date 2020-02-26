package brightspot.core.article;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import brightspot.core.image.ImageOption;
import brightspot.core.image.ImageOptionViewModel;
import brightspot.core.page.AbstractCreativeWorkPageViewModel;
import brightspot.core.page.CurrentPageViewModel;
import brightspot.core.page.PageViewModel;
import brightspot.core.person.Author;
import brightspot.core.seo.PersonSchemaViewModel;
import brightspot.core.tool.DateTimeUtils;
import brightspot.core.tool.DirectoryItemUtils;
import brightspot.core.update.LastUpdatedProvider;
import com.google.common.collect.ImmutableMap;
import com.psddev.cms.view.PageEntryView;
import com.psddev.cms.view.jsonld.JsonLdNode;
import com.psddev.cms.view.jsonld.JsonLdType;
import com.psddev.dari.util.StorageItem;
import com.psddev.styleguide.core.article.ArticlePageView;
import com.psddev.styleguide.core.article.ArticlePageViewArticleBodyField;
import com.psddev.styleguide.core.article.ArticlePageViewLeadField;

@JsonLdType("Article")
public class ArticlePageViewModel extends AbstractCreativeWorkPageViewModel<Article>
    implements ArticlePageView, PageEntryView {

    private static final String DATE_FORMAT_KEY = "dateFormat";

    @CurrentPageViewModel
    protected PageViewModel page;

    @Override
    public Iterable<? extends ArticlePageViewArticleBodyField> getArticleBody() {
        return createViews(ArticlePageViewArticleBodyField.class, model.getBody());
    }

    @Override
    public Iterable<? extends ArticlePageViewLeadField> getLead() {
        return createViews(ArticlePageViewLeadField.class, model.getLead());
    }

    @Override
    public CharSequence getUpdatesCorrections() {
        return page.getUpdatesCorrections();
    }

    @Override
    public CharSequence getPageSubHeading() {
        return null;
    }

    @JsonLdNode("author")
    public Iterable<? extends PersonSchemaViewModel> getPersonData() {
        return model.asAuthorableData().getAuthors().stream()
            .map(a -> createView(PersonSchemaViewModel.class, a))
            .collect(Collectors.toList());
    }

    @JsonLdNode("image")
    public ImageOptionViewModel getImageData() {
        return createView(ImageOptionViewModel.class, model.getPromotableImage());
    }

    @JsonLdNode("mainEntityOfPage")
    public Map<String, Object> getMainEntityOfPageData() {
        return ImmutableMap.of(
            "@type", "WebPage",
            "@id", DirectoryItemUtils.getCanonicalUrl(page.getSite(), model)
        );
    }

    @JsonLdNode("publisher")
    public Map<String, Object> getPublisherData() {
        String logoUrl = model.asAuthorableData().getAuthors().stream()
            .findFirst()
            .map(Author::getPromotableImage)
            .map(ImageOption::getImageOptionFile)
            .map(StorageItem::getPublicUrl)
            .orElse(Optional.ofNullable(model.getPromotableImage())
                .map(ImageOption::getImageOptionFile)
                .map(StorageItem::getPublicUrl)
                .orElse(null));

        if (logoUrl == null) {
            return null;
        }
        return ImmutableMap.of(
            "@type", "Organization",
            "name", page.getSite() != null ? page.getSite().getName() : "",
            "logo", ImmutableMap.of(
                "@context", "http://schema.org",
                "@type", "ImageObject",
                "url", logoUrl)
        );
    }

    @Override
    public CharSequence getDateExpired() {
        return null;
    }

    @Override
    public CharSequence getDateExpiredISO() {
        return null;
    }

    @JsonLdNode
    @Override
    public CharSequence getDateModified() {
        // Plain text
        return DateTimeUtils.format(LastUpdatedProvider.getMostRecentUpdateDate(model), ArticlePageView.class,
            DATE_FORMAT_KEY, page.getSite(), PageViewModel.DEFAULT_DATE_FORMAT
        );
    }

    @Override
    public CharSequence getDateModifiedISO() {
        Date updateDate = LastUpdatedProvider.getMostRecentUpdateDate(model);
        return updateDate != null ? updateDate.toInstant().toString() : null;
    }

    @JsonLdNode
    @Override
    public CharSequence getDatePublished() {
        // Plain text
        return DateTimeUtils.format(model.getPublishDate(), ArticlePageView.class, DATE_FORMAT_KEY, page.getSite(),
            PageViewModel.DEFAULT_DATE_FORMAT
        );
    }

    @Override
    public CharSequence getDatePublishedISO() {
        Date publishDate = model.getPublishDate();
        return publishDate != null ? publishDate.toInstant().toString() : null;
    }

    @JsonLdNode
    @Override
    public CharSequence getHeadline() {
        // Plain text
        return model.getHeadline();
    }

    @Override
    public CharSequence getSource() {
        return null;
    }

    @JsonLdNode("description")
    @Override
    public CharSequence getSubHeadline() {
        // Plain text
        return model.getSubHeadline();
    }
}
