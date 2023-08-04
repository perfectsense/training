package brightspot.seo;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import brightspot.article.Listicle;
import com.psddev.cms.db.Site;
import com.psddev.cms.ui.ToolLocalization;
import com.psddev.cms.ui.ToolRequest;
import com.psddev.cms.ui.form.DynamicNote;
import com.psddev.dari.db.ObjectField;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.html.Nodes;
import com.psddev.dari.html.content.FlowContent;
import com.psddev.dari.web.WebRequest;

public class EnhancedSeoListicleDynamicNote implements DynamicNote {

    @Override
    public Object get(Recordable recordable, ObjectField field) {
        if (!(recordable instanceof Listicle)) {
            return null;
        }
        Site site = WebRequest.getCurrent().as(ToolRequest.class).getCurrentSite();
        if (!EnhancedSeoSiteSettings.isEnableEnhancedSeoUi(site)) {
            return null;
        }

        String introObject = Optional.ofNullable(recordable.getState().getByPath(Listicle.class.getName() + "/intro"))
            .map(introStr -> introStr + "\n")
            .orElse("");
        Object itemsObject = Optional.ofNullable(recordable.getState().getByPath(Listicle.class.getName() + "/items"))
            .orElse(Collections.emptyList());

        StringBuilder collectionBuilder = new StringBuilder();

        collectionBuilder.append(introObject);

        if (itemsObject instanceof Collections) {
            for (Object collectionObj : (Collection) itemsObject) {
                if (collectionObj instanceof Recordable) {
                    if (collectionObj instanceof EnhancedSeoTextProvider) {
                        collectionBuilder.append(((EnhancedSeoTextProvider) collectionObj).getEnhancedSeoBodyText());
                    }
                } else if (collectionObj instanceof String) {
                    collectionBuilder.append((String) collectionObj);
                }
                collectionBuilder.append("\n");
            }
        }

        if (collectionBuilder.toString().isEmpty()) {
            return null;
        }

        EnhancedSeoData seoData = ((EnhancedSeoWithFields) recordable).asEnhancedSeoData();

        return Nodes.DIV.with(ToolLocalization.text(EnhancedSeoListicleDynamicNote.class, "seoNoteInfo"))
            .with(Nodes.BR)
            .with((FlowContent) seoData.getBodyFieldNoteHtml(collectionBuilder.toString()));
    }
}
