package brightspot.core.tool;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.psddev.dari.db.Modification;
import com.psddev.dari.db.ObjectField;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ClassFinder;
import com.psddev.dari.util.HtmlWriter;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.TypeDefinition;

/**
 * Conditionally modify an object based on its state.
 *
 * A class that is being conditionally modified needs a method like:
 *
 * <pre>{@code
 *     public String getConditionalModificationNoteHtml() throws IOException {
 *         return ConditionalModification.generateConditionalModificationNoteHtml(this);
 *     }
 * }</pre>
 *
 * Additionally, one of its fields needs the annotation:
 *
 * <pre>{@code
 *     @ToolUi.NoteHtml("<span data-dynamic-html='${content.getConditionalModificationNoteHtml()}'></span>")
 * }</pre>
 *
 * Note that this implementation only hides fields in the CMS - use {@link com.psddev.dari.db.ConditionallyValidatable}
 * to skip validation.
 */
@Deprecated
public interface ConditionalModification extends Recordable {

    /**
     * Return true to modify the target object, false to hide all fields in this modification. Use {@code
     * getOriginalObject()} to inspect the state of the target class.
     */
    boolean shouldModify();

    static String generateConditionalModificationNoteHtml(Recordable content) throws IOException {

        if (content == null) {
            return "";
        }

        Collection<String> hiddenFieldNames = new ArrayList<>();

        for (Class<? extends ConditionalModification> modClass : ClassFinder.findClasses(ConditionalModification.class)) {

            if (Modification.class.isAssignableFrom(modClass)) {

                Class<?> genericClass = TypeDefinition.getInstance(modClass)
                    .getInferredGenericTypeArgumentClass(Modification.class, 0);

                if (genericClass.isAssignableFrom(content.getClass())) {

                    ConditionalModification mod = content.as(modClass);
                    if (!mod.shouldModify()) {

                        for (ObjectField field : ObjectType.getInstance(modClass).getFields()) {
                            if (modClass.getName().equals(field.getJavaDeclaringClassName())) {
                                hiddenFieldNames.add(field.getInternalName());
                            }
                        }
                    }
                }
            }
        }

        Map<String, Object> json = new HashMap<>();
        json.put("fieldNames", hiddenFieldNames);

        StringWriter stringWriter = new StringWriter();

        try (HtmlWriter html = new HtmlWriter(stringWriter)) {

            html.writeStart("span", "data-hidden-mods", ObjectUtils.toJson(json)).writeEnd();

            return stringWriter.toString();
        }
    }
}
