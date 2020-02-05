package brightspot.core.lead;

import java.util.Optional;

import brightspot.core.image.Image;
import brightspot.core.image.ImageOption;
import brightspot.core.image.OneOffImageOption;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.view.ModelWrapper;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Shared")
@Recordable.Embedded
public class SharedLead extends Record implements Lead, ModelWrapper {

    @Required
    @ToolUi.Unlabeled
    private LeadItem item;

    public LeadItem getItem() {
        return item;
    }

    public void setItem(LeadItem item) {
        this.item = item;
    }

    @Override
    public ImageOption getLeadImage() {
        return Optional.ofNullable(getItem())
            .map(LeadItem::getLeadItemImage)
            .orElse(null);
    }

    @Override
    public Object unwrap() {
        LeadItem item = getItem();

        if (item instanceof Image) {
            OneOffImageOption oneOff = new OneOffImageOption();
            Image image = (Image) item;

            oneOff.setFile(image.getFile());
            oneOff.setAltText(image.getAltText());
            return oneOff;

        } else {
            return item;
        }
    }
}
