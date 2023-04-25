package brightspot.event;

import com.psddev.cms.view.ViewModel;
import com.psddev.dari.db.Location;
import com.psddev.styleguide.event.AddressView;

public class AddressViewModel extends ViewModel<Address> implements AddressView {

    @Override
    public CharSequence getCity() {
        return model.getCity();
    }

    @Override
    public CharSequence getCountry() {
        return model.getCountry();
    }

    @Override
    public String getLocationCoords() {
        Location location = model.getLocation();
        return location.getX() + ", " + location.getY();
    }

    @Override
    public CharSequence getState() {
        return model.getUsState();
    }

    @Override
    public CharSequence getStreet() {
        return model.getStreet();
    }

    @Override
    public CharSequence getZip() {
        return model.getZip();
    }
}
