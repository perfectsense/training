package brightspot.module.list.attachment;

import brightspot.itemstream.MemoizationDynamicQueryModifiable;
import brightspot.itemstream.SiteItemsQueryModifiableWithField;
import brightspot.l10n.LocaleDynamicQueryModifiable;
import com.psddev.dari.util.Substitution;

public class DynamicAttachmentItemStreamSubstitution extends DynamicAttachmentItemStream implements
        Substitution,
        LocaleDynamicQueryModifiable,
        MemoizationDynamicQueryModifiable,
        SiteItemsQueryModifiableWithField {

}
