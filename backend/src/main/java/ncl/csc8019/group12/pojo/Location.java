package ncl.csc8019.group12.pojo;

/**
 * This class defines a location class containing a location's latitude and longitude coordinates.
 *
 * @author wei tan
 */
public class Location {

    private final double latitude;
    private final double longitude;

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
