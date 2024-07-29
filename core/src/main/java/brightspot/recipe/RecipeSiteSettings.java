package brightspot.recipe;

import brightspot.section.Section;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;

@Recordable.FieldInternalNamePrefix("recipe.")
public class RecipeSiteSettings extends Modification<SiteSettings> {

    @ToolUi.Cluster("Recipes")
    @ToolUi.Tab("CMS")
    private Section defaultRecipeSection;

    public Section getDefaultRecipeSection() {
        return defaultRecipeSection;
    }

    public void setDefaultRecipeSection(Section defaultRecipeSection) {
        this.defaultRecipeSection = defaultRecipeSection;
    }
}
