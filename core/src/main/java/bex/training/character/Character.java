package bex.training.character;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import bex.training.alphasort.AlphaSortable;
import bex.training.util.TrainingUtils;
import brightspot.core.image.ImageOption;
import brightspot.core.link.Linkable;
import brightspot.core.promo.PromotableWithOverrides;
import brightspot.core.share.Shareable;
import brightspot.core.slug.Sluggable;
import brightspot.core.tag.Tag;
import brightspot.core.tag.Taggable;
import brightspot.core.tool.MediumRichTextToolbar;
import brightspot.core.tool.RichTextUtils;
import brightspot.core.tool.SmallRichTextToolbar;
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
@Recordable.PreviewField("image/image/file")
public abstract class Character extends Content implements AlphaSortable,
                                                           Linkable,
                                                           PromotableWithOverrides,
                                                           Shareable,
                                                           Sluggable,
                                                           Taggable {

    // Main.

    @Indexed
    @ToolUi.DisplayFirst
    private String alterEgo;

    @Indexed
    @Required
    private String firstName;

    @Indexed
    private String lastName;

    @ToolUi.ReadOnly
    @ToolUi.Placeholder(dynamicText = "${content.getNamePlaceholder()}")
    private String name;

    private ImageOption image;

    @ToolUi.Placeholder(dynamicText = "${content.getBiographyFallback()}")
    @ToolUi.RichText(toolbar = SmallRichTextToolbar.class)
    private String shortBiography;

    @Required
    @ToolUi.RichText(toolbar = MediumRichTextToolbar.class)
    private String fullBiography;

    private List<String> abilities;

    // Getters and Setters.

    public String getAlterEgo() {
        return alterEgo;
    }

    public void setAlterEgo(String alterEgo) {
        this.alterEgo = alterEgo;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Indexed
    @ToolUi.Hidden
    public String getName() {
        return ObjectUtils.firstNonBlank(name, getNamePlaceholder());
    }

    public void setName(String name) {
        this.name = name;
    }

    public ImageOption getImage() {
        return image;
    }

    public void setImage(ImageOption image) {
        this.image = image;
    }

    public String getShortBiography() {
        return ObjectUtils.firstNonBlank(shortBiography, getBiographyFallback());
    }

    public void setShortBiography(String shortBiography) {
        this.shortBiography = shortBiography;
    }

    public String getFullBiography() {
        return fullBiography;
    }

    public void setFullBiography(String fullBiography) {
        this.fullBiography = fullBiography;
    }

    public List<String> getAbilities() {
        if (abilities == null) {
            abilities = new ArrayList<>();
        }

        return abilities;
    }

    public void setAbilities(List<String> abilities) {
        this.abilities = abilities;
    }

    // Record Support.

    @Override
    public String getLabel() {
        return ObjectUtils.firstNonBlank(alterEgo, getName());
    }

    // AlphaSortable Support.

    @Override
    public String getAlphaSortValue() {
        return ObjectUtils.firstNonBlank(getAlterEgo(), getLastName(), getName());
    }

    // Linkable Support.

    @Override
    public String getLinkableText() {
        return getLabel();
    }

    // Promotable Support.

    @Override
    public String getPromotableTitleFallback() {
        return getLabel();
    }

    @Override
    public String getPromotableDescriptionFallback() {
        return getShortBiography();
    }

    @Override
    public ImageOption getPromotableImageFallback() {
        return getImage();
    }

    // SEO Support.

    @Ignored(false)
    @ToolUi.Hidden
    public String getSeoTitle() {
        return getLabel();
    }

    @Ignored(false)
    @ToolUi.Hidden
    public String getSeoDescription() {
        return getShortBiography();
    }

    @Ignored(false)
    @ToolUi.Hidden
    public Set<String> getSeoKeywords() {

        Set<String> keywords = new HashSet<>();

        keywords.add(getAlterEgo());
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
        return getLabel();
    }

    @Override
    public String getShareableDescriptionFallback() {
        return getShortBiography();
    }

    @Override
    public ImageOption getShareableImageFallback() {
        return getImage();
    }

    // Sluggable Support.

    @Override
    public String getSluggableSlugFallback() {
        return StringUtils.toNormalized(getLabel());
    }

    // Helpers.

    public String getNamePlaceholder() {
        return TrainingUtils.joinNonBlankStrings(" ",
                firstName,
                lastName);
    }

    public String getBiographyFallback() {
        return Optional.ofNullable(getFullBiography())
                .map(RichTextUtils::getFirstBodyParagraph)
                .orElse(null);
    }
}
