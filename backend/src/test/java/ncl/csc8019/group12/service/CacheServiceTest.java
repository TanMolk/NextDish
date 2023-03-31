package ncl.csc8019.group12.service;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author wei tan
 */


public class CacheServiceTest {

    private static CacheService cacheService;

    @BeforeClass
    public static void setUp() {
        //set service
        cacheService = new CacheService();
        cacheService.cacheResponse("test", "1,1", "123123", null);
        cacheService.cacheResponse("test1", null, null, null);
        cacheService.cacheResponse("test2", "1,1", null, null);
        cacheService.cacheResponse("test3", null, null, "1233123");
    }

    @Test
    public void testGetNearByPlaceDetailsWithLocation() {
        Assert.assertEquals("test", cacheService.getCachedResponse("1,1", "123123", null));
        Assert.assertEquals("test2", cacheService.getCachedResponse("1,1", null, null));
        Assert.assertNotEquals("test2", cacheService.getCachedResponse(null, "1,1"));
        Assert.assertNotEquals("test2", cacheService.getCachedResponse(null, "1,1", null));
    }
}
