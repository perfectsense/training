package brightspot.core.requestextras.headelement;

import java.util.ArrayList;
import java.util.List;

import brightspot.core.requestextras.AbstractRequestExtra;

/**
 * Custom {@link HeadElement}s to be added to a matching request.
 */
public class CustomHeadElements extends AbstractRequestExtra {

    private List<HeadElement> elements;

    public List<HeadElement> getElements() {
        if (elements == null) {
            elements = new ArrayList<>();
        }
        return elements;
    }
}
