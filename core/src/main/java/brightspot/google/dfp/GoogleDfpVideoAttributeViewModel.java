package brightspot.google.dfp;

import java.util.List;
import java.util.stream.Collectors;

import brightspot.core.ad.AdsFrontEndSettingsModification;
import brightspot.core.section.Section;
import brightspot.core.tag.Tag;
import brightspot.core.video.Video;
import brightspot.core.video.VideoAttributes;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.servlet.CurrentSite;
import com.psddev.cms.view.servlet.HttpParameter;
import com.psddev.styleguide.dfp.GoogleDfpVideoAttributeView;
import org.apache.commons.lang3.StringUtils;

/**
 * Renders a {@link GoogleDfp} as a {@link GoogleDfpVideoAttributeView}.
 */
public class GoogleDfpVideoAttributeViewModel extends ViewModel<GoogleDfp>
    implements GoogleDfpVideoAttributeView, VideoAttributes {

    protected static final String DFP_HEADLINE_PARAMETER = "headline";
    protected static final String DFP_SUB_HEADLINE_PARAMETER = "subHeadline";
    protected static final String DFP_SECTION_PARAMETER = "section";
    protected static final String DFP_TAG_PARAMETER = "tag";
    protected static final String DFP_DURATION_PARAMETER = "duration";

    @CurrentSite
    protected Site site;

    @HttpParameter
    protected String disableAds;

    private Video video;

    /**
     * @return False if the {@link #disableAds} parameter is set to the value configured in {@link
     * AdsFrontEndSettingsModification} or {@link GoogleDfp#dfpVideoTagUrl} is blank.
     */
    @Override
    protected boolean shouldCreate() {
        return (StringUtils.isBlank(disableAds)
            || !StringUtils.equals(
            disableAds,
            SiteSettings.get(
                site,
                siteSettings -> siteSettings.as(AdsFrontEndSettingsModification.class).getDisableAdsParameterValue())))
            && !StringUtils.isBlank(model.getDfpVideoTagUrl());
    }

    /**
     * @return DFP Url, if available and {{@link #video}} is not null.
     */
    public CharSequence getDfpUrl() {
        String dfpUrl = model.getDfpVideoTagUrl();

        if (dfpUrl != null && video != null) {
            dfpUrl = getAdditionalDfpParameters(dfpUrl);
        }

        return dfpUrl;
    }

    private String getAdditionalDfpParameters(String dfpUrl) {
        Section section = video.asSectionableData().getSection();
        List<Tag> tags = video.asTaggableData().getTags();

        dfpUrl = com.psddev.dari.util.StringUtils.addQueryParameters(dfpUrl,
            DFP_HEADLINE_PARAMETER, video.getHeadline(),
            DFP_SUB_HEADLINE_PARAMETER, video.getSubHeadline(),
            DFP_DURATION_PARAMETER, video.getDuration()
        );

        if (section != null) {
            dfpUrl = com.psddev.dari.util.StringUtils.addQueryParameters(
                dfpUrl,
                DFP_SECTION_PARAMETER,
                section.getDisplayName());
        }

        if (tags.size() > 0) {
            dfpUrl = com.psddev.dari.util.StringUtils.addQueryParameters(dfpUrl, DFP_TAG_PARAMETER, tags.stream()
                .map(Tag::getDisplayName)
                .collect(Collectors.joining(","))
            );
        }

        return dfpUrl;
    }

    @Override
    public void setVideo(Video video) {
        this.video = video;
    }
}
