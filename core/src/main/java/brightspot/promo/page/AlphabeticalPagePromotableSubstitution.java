package brightspot.promo.page;

import brightspot.search.sortalphabetical.AlphabeticallySortable;
import brightspot.util.RichTextUtils;
import com.psddev.dari.db.Record;
import com.psddev.dari.util.Substitution;
import com.psddev.dari.util.SubstitutionTarget;

@SubstitutionTarget(PagePromotable.class)
public class AlphabeticalPagePromotableSubstitution extends Record implements Substitution, AlphabeticallySortable {

    @Override
    public String getAlphabeticallySortableIndexValue() {
        return RichTextUtils.richTextToPlainText(((PagePromotable) this).getPagePromotableTitle());
    }
}
