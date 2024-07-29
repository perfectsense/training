package brightspot.meal;

import brightspot.section.Section;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;

@Recordable.FieldInternalNamePrefix("meal.")
public class MealSiteSettings extends Modification<SiteSettings> {

    @ToolUi.Cluster("Recipes")
    @ToolUi.Tab("CMS")
    private Section defaultMealSection;

    public Section getDefaultMealSection() {
        return defaultMealSection;
    }

    public void setDefaultMealSection(Section defaultMealSection) {
        this.defaultMealSection = defaultMealSection;
    }
}
