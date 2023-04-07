package ncl.csc8019.group12.service;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wei tan
 */


public class CacheServiceTest {

    private static CacheService cacheService;
    private static List<JSONObject> data;

    @BeforeClass
    public static void setUp() {
        data = new ArrayList<>();
        data.add(new JSONObject("{1:1}"));
        data.add(new JSONObject("{2:2}"));
        data.add(new JSONObject("{3:3}"));
        data.add(new JSONObject("{4:4}"));


        //set service
        cacheService = new CacheService();
        cacheService.cacheResponse(data.get(0), "1,1", "123123", null);
        cacheService.cacheResponse(data.get(1), null, null, null);
        cacheService.cacheResponse(data.get(2), "1,1", null, null);
        cacheService.cacheResponse(data.get(3), null, null, "1233123");
    }

    @Test
    public void testGetNearByPlaceDetailsWithLocation() {
        Assert.assertEquals(data.get(0), cacheService.getCachedResponse("1,1", "123123", null));
        Assert.assertEquals(data.get(2), cacheService.getCachedResponse("1,1", null, null));
        Assert.assertNotEquals(data.get(1), cacheService.getCachedResponse(null, "1,1"));
        Assert.assertNotEquals(data.get(1), cacheService.getCachedResponse(null, "1,1", null));
    }
}
