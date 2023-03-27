package ncl.csc8019.group12.service;

import ncl.csc8019.group12.pojo.Location;
import ncl.csc8019.group12.utils.DistanceUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wei tan
 */


public class CacheServiceTest {

    private static CacheService cacheService;

    @BeforeClass
    public static void setUp() throws Exception {
        //set service
        cacheService = new CacheService();

        //init data
        //read file
        BufferedReader br = new BufferedReader(new FileReader("./src/test/resources/restaurants_data.json"));
        String line;
        StringBuilder json = new StringBuilder();
        while ((line = br.readLine()) != null) {
            json.append(line);
        }

        //save to cacheService
        JSONArray places = new JSONArray(json.toString());
        for (int i = 0; i < places.length(); i++) {
            cacheService.saveOrUpdatePlaceCache(places.getJSONObject(i));
        }
    }

    @Test
    public void testGetPlaceDetailCacheWithPlaceId() {
        JSONObject placeInformation = cacheService.getPlaceDetailCacheWithPlaceId("ChIJJ0pNkHF3fkgRKFxvlnNKmZk");
        Assert.assertNotNull(placeInformation);
        Assert.assertEquals("Leonardo Hotel Newcastle - Formerly Jurys Inn", placeInformation.getString("name"));

        //doesn't exist
        placeInformation = cacheService.getPlaceDetailCacheWithPlaceId("asdasdm1k1kd12dnkcxz");
        Assert.assertNull(placeInformation);

        placeInformation = cacheService.getPlaceDetailCacheWithPlaceId(null);
        Assert.assertNull(placeInformation);

    }

    @Test
    public void testGetNearByPlaceDetailsWithLocation() {
        Location currentLocation = new Location(54.9733004, -1.6218502);
        double radius = 400;

        List<JSONObject> places = cacheService.getNearByPlaceDetailsWithLocation(currentLocation, radius);

        for (JSONObject place : places) {
            //get location from jsonObject
            JSONObject locationJsonObject = place.getJSONObject("geometry")
                    .getJSONObject("location");
            Location destination = new Location(
                    locationJsonObject.getDouble("lat"),
                    locationJsonObject.getDouble("lng"));

            //All filtered places are within "radius" meters
            Assert.assertTrue(
                    DistanceUtil.calculateApproximateDistanceOfTwoLocation(currentLocation, destination) <= radius);
        }
    }

    @Test
    public void testSavePlaceCache() {
        //test null
        Assert.assertFalse(cacheService.saveOrUpdatePlaceCache(null));

        //test without place_id
        JSONObject jsonObject = new JSONObject();
        Assert.assertFalse(cacheService.saveOrUpdatePlaceCache(jsonObject));

        //test with place_id
        jsonObject = new JSONObject();
        jsonObject.put("place_id", 12313);
        jsonObject.put("desc", "12312");
        Assert.assertTrue(cacheService.saveOrUpdatePlaceCache(jsonObject));

        //test not change
        jsonObject = new JSONObject();
        jsonObject.put("place_id", 12313);
        jsonObject.put("desc", "12312");
        Assert.assertFalse(cacheService.saveOrUpdatePlaceCache(jsonObject));

        //test information increase
        jsonObject = new JSONObject();
        jsonObject.put("place_id", 12313);
        jsonObject.put("desc", "123123");
        Assert.assertTrue(cacheService.saveOrUpdatePlaceCache(jsonObject));
    }

    @Test
    public void testSaveOrUpdatePlacesCache() {
        //contained cache
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("place_id", 12313);
        jsonObject.put("desc", "123123");

        List<JSONObject> places = new ArrayList<>();
        places.add(jsonObject);

        //a new cache
        jsonObject = new JSONObject();
        jsonObject.put("place_id", 123131);
        jsonObject.put("desc", "123123");
        places.add(jsonObject);

        Assert.assertEquals(1, cacheService.saveOrUpdatePlacesCache(places));

    }
}
