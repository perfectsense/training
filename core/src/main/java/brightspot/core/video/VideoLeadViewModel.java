package brightspot.core.video;

import java.util.Optional;

import com.psddev.cms.db.Site;
import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.servlet.CurrentSite;
import com.psddev.styleguide.core.video.VideoLeadView;
import com.psddev.styleguide.core.video.VideoLeadViewPlayerField;

public class VideoLeadViewModel extends ViewModel<VideoLead> implements VideoLeadView {

    @CurrentSite
    Site site;

    @Override
    public CharSequence getCategory() {
        return Optional.ofNullable(model.getVideo())
            .map(Video::getPromotableCategory)
            .orElse(null);
    }

    @Override
    public CharSequence getCategoryUrl() {
        return Optional.ofNullable(model.getVideo())
            .map(video -> video.getPromotableCategoryUrl(site))
            .orElse(null);
    }

    @Override
    public CharSequence getDuration() {
        return Optional.ofNullable(model.getVideo())
            .map(Video::getPromotableDuration)
            .orElse(null);
    }

    @Override
    public CharSequence getHeadline() {
        return model.getHeadline();
    }

    @Override
    public CharSequence getSubHeadline() {
        return model.getSubHeadline();
    }

    @Override
    public Iterable<? extends VideoLeadViewPlayerField> getPlayer() {
        return createViews(VideoLeadViewPlayerField.class, Optional.ofNullable(model.getVideo())
            .map(Video::getVideoProvider)
            .orElse(null));
    }
}
