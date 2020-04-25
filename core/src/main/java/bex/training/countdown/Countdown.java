package bex.training.countdown;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import bex.training.character.Villain;
import brightspot.core.image.Image;
import brightspot.core.tool.SmallRichTextToolbar;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.tool.ContentEditWidgetDisplay;
import com.psddev.cms.tool.content.UrlsWidget;
import com.psddev.dari.db.State;

public class Countdown extends Content implements
        ContentEditWidgetDisplay {

    private static final List<String> HIDDEN_WIDGETS = Arrays.asList(
            UrlsWidget.class.getName());

    @Indexed
    @Required
    private String name;

    private Double gammaDistributionShape;

    @ToolUi.RichText(toolbar = SmallRichTextToolbar.class)
    private String description;

    private Image image;

    @Indexed
    private List<Villain> villains;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getGammaDistributionShape() {
        return gammaDistributionShape;
    }

    public void setGammaDistributionShape(Double gammaDistributionShape) {
        this.gammaDistributionShape = gammaDistributionShape;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public List<Villain> getVillains() {
        if (villains == null) {
            villains = new ArrayList<>();
        }
        return villains;
    }

    public void setVillains(List<Villain> villains) {
        this.villains = villains;
    }

    // --- ContentEditWidgetDisplay implementation ---

    @Override
    public boolean shouldDisplayContentEditWidget(String widgetName) {
        return !HIDDEN_WIDGETS.contains(widgetName);
    }

    // --- Record implementation ---

    @Override
    protected void onValidate() {
        if (Optional.ofNullable(getGammaDistributionShape()).filter(d -> d <= 0).isPresent()) {
            State state = getState();
            state.addError(state.getField("gammaDistributionShape"), "Must be greater than zero!");
        }
    }
}
