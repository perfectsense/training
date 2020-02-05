package brightspot.core.search;

import brightspot.core.page.CurrentPageViewModel;
import brightspot.core.page.PageViewModel;
import com.psddev.cms.view.PreviewEntryView;
import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.PreviewPageView;
import com.psddev.styleguide.PreviewPageViewExtraBodyAttributesField;
import com.psddev.styleguide.PreviewPageViewExtraBodyItemsField;
import com.psddev.styleguide.PreviewPageViewExtraScriptsField;
import com.psddev.styleguide.PreviewPageViewExtraStylesField;
import com.psddev.styleguide.PreviewPageViewFaviconsField;
import com.psddev.styleguide.PreviewPageViewMainField;
import com.psddev.styleguide.PreviewPageViewMetaField;

public class SearchModulePreviewViewModel extends ViewModel<SearchModule> implements
    PreviewPageView,
    PreviewEntryView {

    @CurrentPageViewModel
    protected PageViewModel page;

    @Override
    public Iterable<? extends PreviewPageViewMainField> getMain() {
        return createViews(PreviewPageViewMainField.class, model);
    }

    // -- Page Support --

    @Override
    public CharSequence getLanguage() {
        return page.getLanguage();
    }

    @Override
    public Iterable<? extends PreviewPageViewExtraScriptsField> getExtraScripts() {
        return page.getExtraScripts(PreviewPageViewExtraScriptsField.class);
    }

    @Override
    public Iterable<? extends PreviewPageViewExtraBodyAttributesField> getExtraBodyAttributes() {
        return page.getExtraBodyAttributes(PreviewPageViewExtraBodyAttributesField.class);
    }

    @Override
    public Iterable<? extends PreviewPageViewExtraBodyItemsField> getExtraBodyItems() {
        return page.getExtraBodyItems(PreviewPageViewExtraBodyItemsField.class);
    }

    @Override
    public Iterable<? extends PreviewPageViewExtraStylesField> getExtraStyles() {
        return page.getStyles(PreviewPageViewExtraStylesField.class);
    }

    @Override
    public Iterable<? extends PreviewPageViewMetaField> getMeta() {
        return page.getMeta(PreviewPageViewMetaField.class);
    }

    @Override
    public CharSequence getCharset() {
        return page.getCharset();
    }

    @Override
    public CharSequence getKeywords() {
        return page.getKeywords();
    }

    @Override
    public CharSequence getViewport() {
        return page.getViewport();
    }

    @Override
    public CharSequence getTitle() {
        return page.getTitle();
    }

    @Override
    public CharSequence getManifestLink() {
        return page.getManifestLink();
    }

    @Override
    public Iterable<? extends PreviewPageViewFaviconsField> getFavicons() {
        return page.getFavicons();
    }

    @Override
    public CharSequence getDescription() {
        return page.getDescription();
    }

    @Override
    public CharSequence getCanonicalLink() {
        return page.getCanonicalLink();
    }
}
