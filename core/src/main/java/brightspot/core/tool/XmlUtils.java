package brightspot.core.tool;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;

public class XmlUtils {

    public static Map<String, String> getAttributeMap(Element element) {

        if (element == null) {
            throw new IllegalArgumentException();
        }

        Map<String, String> attributeMap = new HashMap<>();
        for (Attribute attribute : element.attributes().asList()) {
            attributeMap.put(attribute.getKey(), attribute.getValue());
        }

        return attributeMap;
    }
}
