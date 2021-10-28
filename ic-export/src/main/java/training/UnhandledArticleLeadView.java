package training;

import com.psddev.cms.view.JsonView;
import com.psddev.cms.view.ViewInterface;
import com.psddev.dari.db.Recordable;

@JsonView
@ViewInterface
public class UnhandledArticleLeadView extends ExportViewModel<Recordable> implements ArticleLeadView {

@Override
protected boolean shouldCreate() {
    throw new IllegalArgumentException("Unhandled type for ArticleLeadView: " + model.getClass().getName());
    }
}
