package brightspot.module.recipe;

import java.util.Optional;

import brightspot.image.ImagePreviewHtml;
import brightspot.image.WebImage;
import brightspot.image.WebImageAsset;
import brightspot.recipe.Recipe;
import com.psddev.cms.db.Content;
import com.psddev.cms.ui.form.DynamicNoteMethod;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.html.Node;
import com.psddev.dari.html.Nodes;

@Recordable.DisplayName("Recipe")
public abstract class AbstractRecipeModule extends Content {

    @Required
    private Recipe recipe;

    @DisplayName("Image")
    @DynamicNoteMethod("getImagePlaceholderHtml")
    private WebImage imageOverride;

    // --- Getters/setters ---

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public WebImage getImageOverride() {
        return imageOverride;
    }

    public void setImageOverride(WebImage imageOverride) {
        this.imageOverride = imageOverride;
    }

    // --- API methods ---

    public WebImageAsset getImage() {
        return Optional.ofNullable(getImageOverride())
            .orElseGet(this::getImageFallback);
    }

    // --- Fallbacks ---

    public WebImage getImageFallback() {
        return Optional.ofNullable(getRecipe())
            .map(Recipe::getImage)
            .orElse(null);
    }

    // --- Placeholders ---

    public Node getImagePlaceholderHtml() {
        if (imageOverride != null) {
            return null;
        }

        return Optional.ofNullable(getImageFallback())
            .map(WebImage::getFile)
            .map(ImagePreviewHtml::createPreviewImageHtml)
            .map(img -> Nodes.P.with(
                img,
                Nodes.BR,
                Nodes.text("Default Recipe Image")))
            .orElse(null);
    }

    // --- Recordable support ---

    @Override
    public String getLabel() {
        return Optional.ofNullable(getRecipe())
            .map(Recipe::getLabel)
            .orElseGet(super::getLabel);
    }
}
