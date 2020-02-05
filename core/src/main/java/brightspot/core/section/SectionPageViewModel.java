package brightspot.core.section;

import java.util.ArrayList;
import java.util.List;

import brightspot.core.listmodule.AbstractListModuleViewModel;
import brightspot.core.listmodule.ListModule;
import brightspot.core.listmodule.ListModuleItemStream;
import brightspot.core.module.ModuleType;
import brightspot.core.page.AbstractPageViewModel;
import brightspot.core.tool.RichTextUtils;
import com.psddev.cms.view.PageEntryView;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Query;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.styleguide.core.page.PageViewMainField;
import com.psddev.styleguide.core.page.PageViewPageLeadField;
import com.psddev.styleguide.core.section.SectionPageView;

public class SectionPageViewModel extends AbstractPageViewModel<Section> implements SectionPageView, PageEntryView {

    protected ModuleType defaultMainModule() {

        ListModule listModule = (ListModule) ObjectType.getInstance(ListModule.class).createObject(null);
        AbstractListModuleViewModel.setPaginationId(listModule, model.getId());

        ListModuleItemStream itemStream = ListModuleItemStream.createDynamic();

        itemStream.as(SectionDynamicQueryModifier.class)
            .getSections()
            .add(Query.from(CurrentSection.class).first());

        listModule.setItemStream(itemStream);

        return listModule;
    }

    @Override
    public Iterable<? extends PageViewMainField> getMain() {

        List<ModuleType> contents = new ArrayList<>(model.getContents());

        if (contents.isEmpty()) {
            contents.add(defaultMainModule());
        }

        return createViews(PageViewMainField.class, contents);
    }

    @Override
    public CharSequence getPageHeading() {
        if (!model.isHideDisplayName()) {
            return model.getDisplayName();
        }
        return null;
    }

    @Override
    public Iterable<? extends PageViewPageLeadField> getPageLead() {
        return createViews(PageViewPageLeadField.class, model.getLead());
    }

    @Override
    public CharSequence getPageSubHeading() {
        return RichTextUtils.buildInlineHtml(model.getState().getDatabase(), model.getDescription(), this::createView);
    }

    @Override
    public Boolean getCustomContent() {
        return !ObjectUtils.isBlank(model.getContents());
    }
}