package brightspot.core.page.opengraph;

import brightspot.core.tool.DirectoryItemUtils;
import com.psddev.cms.db.Site;
import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.servlet.CurrentSite;
import com.psddev.dari.db.Recordable;
import com.psddev.styleguide.facebook.OpenGraphMetaView;
import com.psddev.styleguide.facebook.OpenGraphMetaViewImagesField;
import com.psddev.styleguide.facebook.OpenGraphMetaViewTypeField;

public class OpenGraphMetaViewModel<T extends Recordable> extends ViewModel<T> implements OpenGraphMetaView {

    @CurrentSite
    protected Site site;

    @Override
    public Iterable<? extends OpenGraphMetaViewImagesField> getImages() {
        return createViews(OpenGraphMetaViewImagesField.class, model);
    }

    @Override
    public CharSequence getDeterminer() {
        return "";
    }

    @Override
    public CharSequence getDescription() {
        return null;
    }

    @Override
    public Iterable<? extends CharSequence> getLocaleAlternates() {
        return null;
    }

    @Override
    public CharSequence getSiteName() {
        return site != null ? site.getName() : null;
    }

    @Override
    public CharSequence getTitle() {
        return null;
    }

    @Override
    public CharSequence getLocale() {
        return null;
    }

    @Override
    public Iterable<? extends OpenGraphMetaViewTypeField> getType() {
        return createViews(OpenGraphMetaViewTypeField.class, model);
    }

    @Override
    public CharSequence getUrl() {
        return DirectoryItemUtils.getCanonicalUrl(site, model);
    }
}
