package ncl.csc8019.group12.service;

import ncl.csc8019.group12.BackendApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author wei tan
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BackendApplication.class)
public class GoogleMapServiceTest {

    @Resource
    private GoogleMapService googleMapService;

    @Test
    public void testFindRestaurantWithLocation() throws Exception {
        googleMapService.findRestaurantWithLocation(54.974141, -1.618138);
    }
}
