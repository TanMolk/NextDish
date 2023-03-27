package ncl.csc8019.group12.utils;

import ncl.csc8019.group12.pojo.Location;

import java.security.InvalidParameterException;

/**
 * @author wei tan
 */
public class DistanceUtil {

    /**
     * Use the factor 1600 to convert miles to meters
     *
     * @param miles the number of miles
     * @return Approximate integer value of meters
     */
    public static int convertMilesToMetersApproximately(double miles) {
        if (miles < 0) {
            throw new InvalidParameterException("The parameters can't be negative");
        }

        return (int) (miles * 1600);
    }

    /**
     * Calculate distance between two location
     * <br>
     * Theory: <a href="https://www.movable-type.co.uk/scripts/latlong.html">Click me</a>
     *
     * @param source      the location of source
     * @param destination the location of destination
     * @return Floor integer of  distance, meters
     */
    public static int calculateApproximateDistanceOfTwoLocation(Location source, Location destination) {
        if (source == null || destination == null) {
            throw new InvalidParameterException("The parameters can't be null");
        }

        double radLat1 = rad(source.getLatitude());
        double radLat2 = rad(destination.getLatitude());

        double latDiffer = radLat1 - radLat2;
        double lngDiffer = rad(source.getLongitude()) - rad(destination.getLongitude());

        double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(latDiffer / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(lngDiffer / 2), 2)));

        distance = distance * 6378.137 * 1000;
        return (int) Math.floor(distance);
    }

    /**
     * Convert location to rad
     *
     * @param d latitude or longitude
     * @return rad
     */
    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }
}
