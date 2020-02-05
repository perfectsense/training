package brightspot.core.person;

import brightspot.core.image.ImageOption;
import brightspot.core.page.opengraph.profile.OpenGraphProfile;
import brightspot.core.tool.MediumRichTextToolbar;
import brightspot.core.tool.RichTextUtils;
import brightspot.core.tool.SmallRichTextToolbar;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.util.ObjectUtils;

/**
 * The {@link AbstractPerson} class provides basic characteristics of a person, such as {@code names}, {@code image},
 * {@code title}, {@code email}, and {@code biographies}.
 */
@ToolUi.FieldDisplayOrder({
    "name",
    "firstName",
    "lastName",
    "image",
    "title",
    "email",
    "shortBiography",
    "fullBiography" })
public abstract class AbstractPerson extends Content implements
    OpenGraphProfile {

    @Required
    @Indexed
    protected String name;

    @Indexed
    protected String firstName;

    @Indexed
    protected String lastName;

    protected ImageOption image;

    protected String title;

    @Indexed
    protected String email;

    @ToolUi.RichText(inline = false, toolbar = MediumRichTextToolbar.class)
    protected String fullBiography;

    @ToolUi.RichText(toolbar = SmallRichTextToolbar.class)
    @ToolUi.Placeholder(dynamicText = "${content.shortBiographyPlaceholder}", editable = true)
    protected String shortBiography;

    /**
     * Returns the {@code name}.
     *
     * @return a plain text {@link String} (required).
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the {@code first name}.
     *
     * @return a plain text {@link String} (optional).
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the {@code first name}.
     *
     * @param firstName a plain text {@link String} (optional).
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the {@code last name}.
     *
     * @return a plain text {@link String} (optional).
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the {@code last name}.
     *
     * @param lastName a plain text {@link String} (optional).
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the {@code full biography}.
     *
     * @return a RichText {@link String} with block-level elements (optional).
     */
    public String getFullBiography() {
        return fullBiography;
    }

    /**
     * Returns the {@code short biography}.
     *
     * @return an inline RichText {@link String} (optional).
     */
    public String getShortBiography() {
        return ObjectUtils.isBlank(shortBiography)
            ? getShortBiographyPlaceholder()
            : shortBiography;
    }

    /**
     * Returns the {@code email address}.
     *
     * @return a plain text {@link String} (optional).
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the {@code image}.
     *
     * @return an {@link ImageOption} (optional).
     */
    public ImageOption getImage() {
        return image;
    }

    /**
     * Returns the {@code job title}.
     *
     * @return a plain text {@link String} (optional).
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the {@code job title}.
     *
     * @param title a plain text {@link String} (optional).
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets the {@code name}.
     *
     * @param name a plain text {@link String} (required).
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the {@code image}.
     *
     * @param image an {@link ImageOption} (optional).
     */
    public void setImage(ImageOption image) {
        this.image = image;
    }

    /**
     * Sets the {@code email address}.
     *
     * @param email a plain text {@link String} (optional).
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets the {@code full biography}.
     *
     * @param fullBiography a RichText {@link String} with block-level elements (optional).
     */
    public void setFullBiography(String fullBiography) {
        this.fullBiography = fullBiography;
    }

    /**
     * Sets the {@code short biography}.
     *
     * @param shortBiography an inline RichText {@link String} (optional).
     */
    public void setShortBiography(String shortBiography) {
        this.shortBiography = shortBiography;
    }

    /**
     * Returns the fallback text for the {@code #shortBiography}, which is the first paragraph of the {@code
     * #fullBiography}.
     *
     * @return a plain text {@link String}.
     */
    public String getShortBiographyPlaceholder() {
        return ObjectUtils.isBlank(fullBiography) ? "" : RichTextUtils.getFirstBodyParagraph(fullBiography);
    }

    @Override
    public String getOpenGraphUsername() {
        return getName();
    }

    @Override
    public String getOpenGraphFirstName() {
        return getFirstName();
    }

    @Override
    public String getOpenGraphLastName() {
        return getLastName();
    }

    @Override
    public String getOpenGraphGender() {
        return null;
    }
}
