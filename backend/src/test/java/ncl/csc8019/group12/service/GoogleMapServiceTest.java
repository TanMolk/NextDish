package ncl.csc8019.group12.service;


import ncl.csc8019.group12.BackendApplication;
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
    public void testExample() {
        String placeId = "ChIJE-u-MMpwfkgRe5igEnexTN0";
        JSONObject jsonObject = googleMapService.exampleHowToUse(placeId);
        Assert.assertNotNull(jsonObject);

        System.out.println(jsonObject);
    }
}
