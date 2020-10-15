// Declare a package
package bex.training.hud;
// Set that we are creating a ViewModel
import com.psddev.cms.view.ViewModel;
// Set up the ViewModel that we created below named HudView
import com.psddev.styleguide.training.hud.HudView;
// Set up ViewModel related extensions for Heroes, Images, and Villains that will be used on this module
import com.psddev.styleguide.training.hud.HudViewHeroesField;
import com.psddev.styleguide.training.hud.HudViewImageField;
import com.psddev.styleguide.training.hud.HudViewVillainsField;

public class HudViewModel extends ViewModel<Hud> implements HudView {

    // Access the image field we created in the CMS
    @Override
    public Iterable<? extends HudViewImageField> getImage() {
        return createViews(HudViewImageField.class, model.getImage());
    }

    // Access the battle name input we created in the CMS
    @Override
    public CharSequence getBattleName() {
        return model.getBattleName();
    }

    // Access the PDF report we uploaded in the CMS
    @Override
    public CharSequence getHref() {
        return model.getLinkUrl();
    }

    //  Access the array of villains that we added
    @Override
    public Iterable<? extends HudViewVillainsField> getVillains() {
        return createViews(HudViewVillainsField.class, model.getVillains());
    }

    //  Access the array of heroes that we added
    @Override
    public Iterable<? extends HudViewHeroesField> getHeroes() {
        return createViews(HudViewHeroesField.class, model.getHeroes());
    }
}
