package brightspot.pressrelease;

import brightspot.section.Section;
import com.psddev.cms.db.BoardSearchResultField;
import com.psddev.dari.db.Alteration;

public class PressReleaseAlteration extends Alteration<PressRelease> {

    @BoardSearchResultField
    @InternalName("hasSection.getSectionParent")
    private Section getSectionParent;
}
