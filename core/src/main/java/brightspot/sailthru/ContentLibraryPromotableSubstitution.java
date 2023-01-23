package brightspot.sailthru;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import brightspot.api.client.ApiClientManager;
import brightspot.author.AuthoringPageViewModel;
import brightspot.image.WebImageAsset;
import brightspot.permalink.Permalink;
import brightspot.promo.page.PagePromotable;
import brightspot.sailthru.api.SailthruClient;
import brightspot.sailthru.api.StandardSailthruClientConfiguration;
import brightspot.tag.HasTags;
import brightspot.tag.Tag;
import brightspot.util.RichTextUtils;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.ui.ToolRequest;
import com.psddev.dari.db.Record;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.Substitution;
import com.psddev.dari.util.SubstitutionTarget;
import com.psddev.dari.web.WebRequest;

@SubstitutionTarget(PagePromotable.class)
public class ContentLibraryPromotableSubstitution extends Record implements Substitution, SailthruContentLibraryItem {

    @Override
    public SailthruClient getSailthruClient() {
        PagePromotable pagePromotable = (PagePromotable) this;

        Site site = WebRequest.isAvailable()
                ? WebRequest.getCurrent().as(ToolRequest.class).getCurrentSite()
                : pagePromotable.as(Site.ObjectModification.class).getOwner();
        SailthruApiSettings settings = SiteSettings.get(
                site,
                siteSettings -> siteSettings.as(SailthruSiteSettings.class).getSettings());
        Date lastChanged = SiteSettings.get(
                site,
                siteSettings -> siteSettings.as(SailthruSiteSettings.class).getLastChanged());
        if (settings == null || lastChanged == null) {

            return null;
        }

        StandardSailthruClientConfiguration configuration = new StandardSailthruClientConfiguration(
                pagePromotable.getState().getId(),
                "https://api.sailthru.com",
                settings.getApiKey(),
                settings.getApiSecret(),
                lastChanged.toInstant());
        return ApiClientManager.getApiClient(configuration);
    }

    @Override
    public SailthruContentLibraryItemDto getSailthruContentMetadata(Site site) {
        PagePromotable pagePromotable = (PagePromotable) this;
        return ObjectUtils.build(new SailthruContentLibraryItemDto(Permalink.getPermalink(site, this)), b -> {
            b.setTitle(RichTextUtils.richTextToPlainText(pagePromotable.getPagePromotableTitle()));
            b.setDescription(RichTextUtils.richTextToPlainText(pagePromotable.getPagePromotableDescription()));
            b.setDate(pagePromotable.getPagePromotableDate());
            b.setAuthor(AuthoringPageViewModel.getPrimaryAuthorName(pagePromotable));
            b.setFullImage(Optional.ofNullable(pagePromotable.getPagePromotableImage())
                    .map(WebImageAsset::getWebImageAssetFile)
                    .map(SailthruImageSizes.FULL.getImageSize()::toAttributes)
                    .map(attr -> attr.get("src"))
                    .orElse(null));
            b.setThumbnailImage(Optional.ofNullable(pagePromotable.getPagePromotableImage())
                    .map(WebImageAsset::getWebImageAssetFile)
                    .map(SailthruImageSizes.THUMBNAIL.getImageSize()::toAttributes)
                    .map(attr -> attr.get("src"))
                    .orElse(null));
            b.setSiteName(Optional.ofNullable(site).map(Site::getSeoDisplayName).orElse(null));
            List<String> tags = new ArrayList<>();
            Optional.ofNullable(pagePromotable.getPagePromotableCategory()).ifPresent(tags::add);
            Optional.of(pagePromotable)
                    .filter(HasTags.class::isInstance)
                    .map(HasTags.class::cast)
                    .map(HasTags::getTags)
                    .ifPresent(allTags -> tags.addAll(allTags.stream()
                            .map(Tag::getTagDisplayNamePlainText)
                            .collect(Collectors.toList())));
            b.setTags(tags);
        });
    }
}
