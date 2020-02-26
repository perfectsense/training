package brightspot.core.background;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Color")
public class ColorBackground extends Background {

    @Required
    @ToolUi.ColorPicker
    private String color;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String getCssValue() {
        return getColor();
    }
}
