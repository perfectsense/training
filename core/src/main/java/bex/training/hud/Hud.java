// Declare a package
package bex.training.hud;
// Add in Java Utilities for ArrayList and List which are used to access Lists of Heroes and Villains
import java.util.ArrayList;
import java.util.List;
// Import Heroes and Villains from the Character Content Type
import bex.training.character.Villain;
import bex.training.character.Hero;

// Import Image from Brightspot core to add an Image to this Content Type
import brightspot.core.image.Image;
// Content is what is used for basic text fields
import com.psddev.cms.db.Content;
// The ToolUi will allow us to have Heads Up Display in the "Main Content Type" part of the left rail when creating a new HUD
import com.psddev.cms.db.ToolUi;
// Storage is a dependency that we need to add in order to allow for PDF Report to be uploaded and stored
import com.psddev.dari.util.StorageItem;

@ToolUi.Main

// A class of Heads Up Display needs to be declared and Content needs to be extended
public class Hud extends Content {
    // Create an indexable field for the Battle Name that is required to fill out by editors and will display first to editors
    // The Battle Name will be a string
    @Indexed
    @Required
    @ToolUi.DisplayFirst
    private String battleName;
    // Battles should have a background image this will create a field for the image to be input
    private Image image;
    // Here we can define a list of villains that will show up in the HUD for the battle and these villains will be indexed in the database
    @Indexed
    private List<Villain> villains;
    // Here we can define a list of heroes that will show up in the HUD and these heroes will be indexed in the database
    @Indexed
    private List<Hero> heroes;
    // Here we can have an editor upload and analysis report that will display the data of the battle
    @Required
    private StorageItem report;

    // Getters and Setters do just that they Get and Set the data that will be grabbed by the View Model and ultimately displayed on the Front End
    public String getBattleName() {
        return battleName;
    }

    public void setBattleName(String battleName) {
        this.battleName = battleName;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;

    }
    // We will allow for editors to set an array of multiple villains
    public List<Villain> getVillains() {
        if (villains == null) {
            villains = new ArrayList<>();
        }
        return villains;
    }

    public void setVillains(List<Villain> villains) {
        this.villains = villains;
    }

    // We will allow for editors to set an array of multiple heroes
    public List<Hero> getHeroes() {
        if (heroes == null) {
            heroes = new ArrayList<>();
        }
        return heroes;
    }

    public void setHeroes(List<Hero> Heroes) {
        this.heroes = Heroes;
    }

    // We will use the StorageItem Utility to gain access to PDF we will upload as our report
    public StorageItem getReport() {
        return report;
    }

    public void setReport(StorageItem report) {
        this.report = report;
    }
    // Using helper to get a secure public url to access the report we set
    public String getLinkUrl() {
        return report.getSecurePublicUrl();
    }
}
