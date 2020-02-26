package brightspot.core.page;

import java.util.Map;

import brightspot.core.creativework.CreativeWork;
import com.psddev.styleguide.core.page.CreativeWorkPageView;
import com.psddev.styleguide.core.page.CreativeWorkPageViewContributorsField;
import com.psddev.styleguide.core.page.CreativeWorkPageViewPeopleField;

public abstract class AbstractCreativeWorkPageViewModel<M extends CreativeWork> extends AbstractContentPageViewModel<M>
    implements
    CreativeWorkPageView {

    @Override
    public CharSequence getAuthorBiography() {
        return page.getAuthorBiography();
    }

    @Override
    public Map<String, ?> getAuthorImage() {
        return page.getAuthorImage();
    }

    @Override
    public CharSequence getAuthorName() {
        return page.getAuthorName();
    }

    @Override
    public CharSequence getAuthorUrl() {
        return page.getAuthorUrl();
    }

    @Override
    public Iterable<? extends CreativeWorkPageViewContributorsField> getContributors() {
        return page.getContributors(CreativeWorkPageViewContributorsField.class);
    }

    @Override
    public CharSequence getHeadline() {
        return model.getHeadline();
    }

    @Override
    public Iterable<? extends CreativeWorkPageViewPeopleField> getPeople() {
        return page.getPeople(CreativeWorkPageViewPeopleField.class);
    }

    @Override
    public CharSequence getSubHeadline() {
        return model.getSubHeadline();
    }
}
