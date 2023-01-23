package brightspot.importtransformers.richtext;

import java.util.List;
import java.util.Optional;

import brightspot.importapi.ImportTransformer;
import com.psddev.dari.util.ObjectUtils;

public class BodyTransformUtil {

    public static String transform(List<BodyElement> bodyElements, ImportTransformer parentTransformer) {

        if (ObjectUtils.isBlank(bodyElements)) {
            return null;
        }

        StringBuilder builder = new StringBuilder();

        for (BodyElement element : bodyElements) {
            Optional.ofNullable(element)
                    .map(e -> e.transform(parentTransformer))
                    .ifPresent(builder::append);
        }

        return builder.toString();
    }

    static Integer parseInteger(String h) {
        return Optional.ofNullable(h).map(value -> value.replaceAll("\\D", "")).map(Integer::valueOf).orElse(null);
    }

    static String parseNumericString(String w) {
        return Optional.ofNullable(w).map(value -> value.replaceAll("\\D", "")).orElse(null);
    }
}
