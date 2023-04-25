package brightspot.event;

import java.util.Date;

import brightspot.image.WebImage;
import brightspot.image.WebImageAsset;
import brightspot.page.Page;
import brightspot.permalink.AbstractPermalinkRule;
import brightspot.promo.page.PagePromotableWithOverrides;
import brightspot.rte.SmallRichTextToolbar;
import brightspot.section.HasSectionWithField;
import brightspot.share.Shareable;
import brightspot.tag.HasTagsWithField;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Directory.Item;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.ui.form.DynamicPlaceholderMethod;
import com.psddev.cms.ui.form.EditablePlaceholder;

public class Event extends Content implements LiveContent, Page, Item,
    HasSectionWithField, HasTagsWithField,
    PagePromotableWithOverrides, Shareable {

    @Indexed
    @Required
    private String name;
    @DynamicPlaceholderMethod("getInternalNameFallback")
    @EditablePlaceholder
    private String internalName;
    @ToolUi.RichText(toolbar = SmallRichTextToolbar.class)
    private String details;
    @ToolUi.Note("date format: dd/mm/yy")
    @Required
    private Date startDate;
    @ToolUi.Note("date format: dd/mm/yy")
    @Required
    private Date endDate;
    private WebImage image;
    private Address address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date date) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public WebImage getImage() {
        return image;
    }

    public void setImage(WebImage image) {
        this.image = image;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getInternalName() {
        return internalName;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    /*Fallbacks*/
    public String getInternalNameFallback() {
        return getName();
    }

    @Override
    public String getSeoTitle() {
        return getName();
    }

    @Override
    public String getSeoDescription() {
        return getDetails();
    }

    @Override
    public String createPermalink(Site site) {
        return
            AbstractPermalinkRule.create(site, this, EventPermaLinkRule.class);
    }

    public String getPagePromotableTitleFallback() {
        return this.getName();
    }

    public String getPagePromotableDescriptionFallback() {
        return this.getDetails();
    }

    public WebImageAsset getPagePromotableImageFallback() {
        return this.getImage();
    }

    @Override
    public String getLinkableText() {
        return getName();
    }
}