package brightspot.core.page;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;

import com.psddev.cms.view.servlet.ServletViewRequestAnnotationProcessor;
import com.psddev.dari.util.StringUtils;
import com.psddev.dari.util.UuidUtils;

/**
 * See {@link HttpIdPrefixedParameters}. Produces an {@link IdParameterMap} with all of the values accessible by ID.
 */
public class HttpIdPrefixedParametersProcessor
    implements ServletViewRequestAnnotationProcessor<HttpIdPrefixedParameters> {

    private static final String SEPARATOR = "-";

    private static final Pattern UUID_PATTERN = Pattern.compile(
        "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}",
        Pattern.CASE_INSENSITIVE);
    private static final int UUID_LENGTH = 36;

    /**
     * Return a properly formatted parameter name for usage with {@link HttpIdPrefixedParameters}.
     */
    public static String parameterName(String pageParameter, UUID id) {
        return id + SEPARATOR + pageParameter;
    }

    @Override
    public IdParameterMap getValue(HttpServletRequest request, String fieldName, HttpIdPrefixedParameters annotation) {

        String parameterName = annotation.value();
        if (StringUtils.isBlank(parameterName)) {
            parameterName = fieldName;
        }

        Map<UUID, List<String>> parameters = new HashMap<>();
        for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
            String key = entry.getKey();
            if (key.endsWith(SEPARATOR + parameterName) && key.length() > UUID_LENGTH) {
                String possibleIdPart = key.substring(0, UUID_LENGTH);
                if (UUID_PATTERN.matcher(possibleIdPart).matches()) {
                    UUID id = UuidUtils.fromString(possibleIdPart);
                    String[] value = entry.getValue();
                    if (value != null) {
                        parameters.put(id, Arrays.asList(value));
                    }
                }
            }
        }
        return new IdParameterMap(parameters);
    }
}
