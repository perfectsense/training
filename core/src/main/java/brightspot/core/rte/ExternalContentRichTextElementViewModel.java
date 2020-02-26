package brightspot.core.rte;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Map;

import com.psddev.cms.db.ExternalContent;
import com.psddev.cms.rte.ExternalContentCache;
import com.psddev.cms.rte.ExternalContentRichTextElement;
import com.psddev.cms.view.ViewModel;
import com.psddev.dari.util.HtmlWriter;
import com.psddev.styleguide.RawHtml;
import com.psddev.styleguide.core.enhancement.EnhancementView;
import com.psddev.styleguide.core.enhancement.EnhancementViewItemField;

public class ExternalContentRichTextElementViewModel extends ViewModel<ExternalContentRichTextElement>
    implements EnhancementView {

    @Override
    public Map<String, ?> getExternalContent() {
        return ExternalContentCache.get(model.getUrl(), model.getMaximumWidth(), model.getMaximumHeight());
    }

    @Override
    public Boolean getInline() {
        return Boolean.FALSE;
    }

    @Override
    public Iterable<? extends EnhancementViewItemField> getItem() {
        ExternalContent ec = new ExternalContent();

        ec.setUrl(model.getUrl());
        ec.setMaximumWidth(model.getMaximumWidth());
        ec.setMaximumHeight(model.getMaximumHeight());

        StringWriter sw = new StringWriter();
        HtmlWriter hw = new HtmlWriter(sw);

        try {
            ec.renderObject(null, null, hw);

        } catch (IOException error) {
            // Should never happen.
        }

        return Collections.singletonList(RawHtml.of(sw.toString()));
    }
}
