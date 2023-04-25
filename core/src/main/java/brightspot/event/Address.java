package brightspot.event;

import java.util.Objects;
import java.util.stream.Stream;

import com.psddev.dari.db.Location;
import com.psddev.dari.db.Record;

public class Address extends Record {

    @Indexed
    @Required
    String street;
    @Indexed
    @Required
    String city;
    @Indexed
    @Required
    @DisplayName("State")
    String usState;
    @Indexed
    @Required
    String country;
    @Indexed(unique = true)
    @Required
    String zip;
    double x = 52.67157577829781;
    double y = -8.546506159149112;
    Location location = new Location(x, y);

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUsState() {
        return usState;
    }

    public void setUsState(String usState) {
        this.usState = usState;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public String getLabel() {
        if (Stream.of(getStreet(), getCity(), getCountry()).anyMatch(Objects::isNull)) {
            return "Insert Fields";
        } else {
            return getStreet() + ", " + getCity() + ", " + getCountry();
        }
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}