package ncl.csc8019.group12.service;


import ncl.csc8019.group12.BackendApplication;
import ncl.csc8019.group12.pojo.Location;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author Pulei, Rachel & Wei
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BackendApplication.class)
public class GoogleMapServiceTest {

    @Resource
    private GoogleMapService googleMapService;

    @Test
    public void testGetPlaceDetail() {
        String placeId = "ChIJE-u-MMpwfkgRe5igEnexTN0";
        JSONObject jsonObject = googleMapService.getPlaceDetail(placeId, "test");
        Assert.assertNotNull(jsonObject);

        System.out.println(jsonObject);
    }

    @Test
    public void testGetNearByPlaceWithLocation() {
        Location currentLocation = new Location(54.9733004, -1.6218502);

        JSONObject places = googleMapService.getNearByPlaceWithLocation(currentLocation, 1600, "");
        Assert.assertNotNull(places);

        System.out.println(places);
        System.out.println(places.length());
    }

    @Test
    public void testGetPhoto() {
        String ref = "change_to_your_photo_reference";
        Assert.assertNotNull(googleMapService.getPhoto(ref, 1600, 1600));
    }
}
