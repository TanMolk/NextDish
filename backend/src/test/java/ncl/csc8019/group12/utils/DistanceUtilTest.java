package ncl.csc8019.group12.utils;

import ncl.csc8019.group12.pojo.Location;
import org.junit.Assert;
import org.junit.Test;

import java.security.InvalidParameterException;

/**
 * @author wei tan
 */

public class DistanceUtilTest {

    @Test
    public void testConvertMilesToMetersApproximately() {
        int meters = DistanceUtil.convertMilesToMetersApproximately(1);
        Assert.assertEquals(1600, meters);

        meters = DistanceUtil.convertMilesToMetersApproximately(0);
        Assert.assertEquals(0, meters);

        Assert.assertThrows(InvalidParameterException.class, () -> {
            int i = DistanceUtil.convertMilesToMetersApproximately(-1);
        });
    }

    @Test
    public void testCalculateApproximateDistanceOfTwoLocation() {
        Location location1 = new Location(54.973549, -1.6297207);
        Location location2 = new Location(54.9787591, -1.6158987);

        int distance = DistanceUtil.calculateApproximateDistanceOfTwoLocation(location1, location2);
        Assert.assertEquals(1056, distance);

        Assert.assertThrows(InvalidParameterException.class, () -> {
            int i = DistanceUtil.calculateApproximateDistanceOfTwoLocation(null, location2);
        });

        Assert.assertThrows(InvalidParameterException.class, () -> {
            int i = DistanceUtil.calculateApproximateDistanceOfTwoLocation(location1, null);
        });

        Assert.assertThrows(InvalidParameterException.class, () -> {
            int i = DistanceUtil.calculateApproximateDistanceOfTwoLocation(null, null);
        });
    }
}
