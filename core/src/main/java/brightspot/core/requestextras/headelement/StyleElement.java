package brightspot.core.requestextras.headelement;

import com.psddev.cms.view.ModelWrapper;

public class StyleElement extends HeadElement implements ModelWrapper {

    @Required
    private StyleElementBody type;

    private String media;

    @Override
    public Object unwrap() {
        if (type != null) {
            type.setMedia(media);
        }
        return type;
    }
}
