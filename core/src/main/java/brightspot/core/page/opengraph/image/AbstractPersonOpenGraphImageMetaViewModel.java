package brightspot.core.page.opengraph.image;

import java.util.Optional;

import brightspot.core.image.ImageOption;
import brightspot.core.person.AbstractPerson;

public class AbstractPersonOpenGraphImageMetaViewModel extends OpenGraphImageMetaViewModel<AbstractPerson> {

    @Override
    protected ImageOption getImageOption() {
        return Optional.ofNullable(model.getImage())
            .orElse(super.getImageOption());
    }
}
