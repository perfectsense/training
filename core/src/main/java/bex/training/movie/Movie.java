package bex.training.movie;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import bex.training.alphasort.AlphaSortOverride;
import bex.training.character.Character;
import bex.training.release.Releasable;
import brightspot.core.image.ImageOption;
import brightspot.core.link.Linkable;
import brightspot.core.promo.PromotableWithOverrides;
import brightspot.core.share.Shareable;
import brightspot.core.slug.Sluggable;
import brightspot.core.tag.Tag;
import brightspot.core.tag.Taggable;
import brightspot.core.tool.MediumRichTextToolbar;
import brightspot.core.tool.RichTextUtils;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Seo;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StringUtils;

@Seo.TitleFields("getSeoTitle")
@Seo.DescriptionFields("getSeoDescription")
@Seo.KeywordsFields("getSeoKeywords")
@ToolUi.Main
@Recordable.PreviewField("cover/file")
public class Movie extends Content implements AlphaSortOverride,
                                              Linkable,
                                              PromotableWithOverrides,
                                              Releasable,
                                              Shareable,
                                              Sluggable,
                                              Taggable {

    // Main.

    @Required
    @Indexed
    @ToolUi.DisplayFirst
    private String name;

    private ImageOption cover;

    @ToolUi.Placeholder(dynamicText = "${content.getSummaryFallback()}")
    @ToolUi.RichText(toolbar = MediumRichTextToolbar.class)
    private String summary;

    @Required
    @ToolUi.RichText(toolbar = MediumRichTextToolbar.class)
    private String plot;

    @CollectionMinimum(1)
    private Set<Character> featuredCharacters;

    @Required
    @Indexed
    @ToolUi.Filterable
    private Phase phase;

    // Getters and Setters.

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ImageOption getCover() {
        return cover;
    }

    public void setCover(ImageOption cover) {
        this.cover = cover;
    }

    public String getSummary() {
        return ObjectUtils.firstNonBlank(summary, getSummaryFallback());
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    @Indexed
    @ToolUi.Filterable
    @ToolUi.Hidden
    public Set<Character> getFeaturedCharacters() {
        if (featuredCharacters == null) {
            featuredCharacters = new HashSet<>();
        }

        return featuredCharacters;
    }

    public void setFeaturedCharacters(Set<Character> featuredCharacters) {
        this.featuredCharacters = featuredCharacters;
    }

    // AlphaSortOverride Support.

    @Override
    public String getAlphaSortValueFallback() {
        return getName();
    }

    // Linkable Support.

    @Override
    public String getLinkableText() {
        return getName();
    }

    // Promotable Support.

    @Override
    public String getPromotableTitleFallback() {
        return getName();
    }

    @Override
    public String getPromotableDescriptionFallback() {
        return getSummary();
    }

    @Override
    public ImageOption getPromotableImageFallback() {
        return getCover();
    }

    // SEO Support.

    @Ignored(false)
    @ToolUi.Hidden
    public String getSeoTitle() {
        return getName();
    }

    @Ignored(false)
    @ToolUi.Hidden
    public String getSeoDescription() {
        return getSummary();
    }

    @Ignored(false)
    @ToolUi.Hidden
    public Set<String> getSeoKeywords() {

        Set<String> keywords = new HashSet<>();

        keywords.add(getName());

        // Get a set of all tags name keywords.
        keywords.addAll(asTaggableData().getTags().stream()
            .map(Tag::getDisplayName)
            .flatMap(tagDisplayName -> Arrays.stream(tagDisplayName.split(" ")))
            .collect(Collectors.toSet()));

        return keywords;
    }

    // Shareable Support.

    @Override
    public String getShareableTitleFallback() {
        return getName();
    }

    @Override
    public String getShareableDescriptionFallback() {
        return getSummary();
    }

    @Override
    public ImageOption getShareableImageFallback() {
        return getCover();
    }

    // Sluggable Support.

    @Override
    public String getSluggableSlugFallback() {
        return StringUtils.toNormalized(getName());
    }

    // Helpers.

    public String getSummaryFallback() {
        return Optional.ofNullable(getPlot())
                .map(RichTextUtils::getFirstBodyParagraph)
                .orElse(null);
    }
}
