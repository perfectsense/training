package brightspot.core.listmodule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.psddev.cms.db.Site;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.State;
import org.apache.commons.lang3.StringUtils;

public interface DynamicQueryModifier extends Recordable {

    void updateQuery(Site site, Object mainObject, Query<?> query);

    String createLabel();

    /**
     * Find all {@link com.psddev.dari.db.Modification}s of the given class that implement {@link DynamicQueryModifier}
     * and invoke {@link DynamicQueryModifier#updateQuery(Site, Object, Query)} on the given query.
     */
    static void updateQueryWithModifications(Site site, Object mainObject, Recordable queryItemStream, Query<?> query) {
        for (Class<? extends DynamicQueryModifier> modificationClass : getDynamicQueryModifierClasses(queryItemStream)) {
            queryItemStream.as(modificationClass).updateQuery(site, mainObject, query);
        }
    }

    static List<String> createLabels(Recordable queryItemStream) {
        List<String> labels = new ArrayList<>();
        for (Class<? extends DynamicQueryModifier> modificationClass : getDynamicQueryModifierClasses(queryItemStream)) {
            String label = queryItemStream.as(modificationClass).createLabel();
            if (!StringUtils.isBlank(label)) {
                labels.add(label);
            }
        }
        return labels;
    }

    static Collection<Class<? extends DynamicQueryModifier>> getDynamicQueryModifierClasses(Recordable queryItemStream) {
        Collection<Class<? extends DynamicQueryModifier>> modifiers = new ArrayList<>();
        State queryItemStreamState = queryItemStream.getState();
        Set<String> modifications = queryItemStreamState.getType().getModificationClassNames();

        for (String modificationClassName : modifications) {
            ObjectType modification = ObjectType.getInstance(modificationClassName);
            Class<?> modificationClass = modification.getObjectClass();
            if (modificationClass != null
                && DynamicQueryModifier.class.isAssignableFrom(modification.getObjectClass())) {
                @SuppressWarnings("unchecked")
                Class<? extends DynamicQueryModifier> cls = (Class<? extends DynamicQueryModifier>) modificationClass;
                modifiers.add(cls);
            }
        }
        return modifiers;
    }
}
