package brightspot.core.lead;

import java.util.Map;
import java.util.Optional;

import brightspot.core.image.ImageOption;
import com.psddev.cms.image.ImageSize;
import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.core.figure.FigureView;

public class LeadViewModel extends ViewModel<Lead> implements FigureView {

    @Override
    public Map<String, ?> getImage() {
        return Optional.ofNullable(model.getLeadImage())
            .map(ImageOption::getImageOptionFile)
            .map(ImageSize::getAttributes)
            .orElse(null);
    }

    @Override
    public CharSequence getAlt() {
        return Optional.ofNullable(model.getLeadImage())
            .map(ImageOption::getImageOptionAltText)
            .orElse(null);
    }

    @Override
    public CharSequence getCaption() {
        return null;
    }

    @Override
    public CharSequence getCredit() {
        return null;
    }
}
