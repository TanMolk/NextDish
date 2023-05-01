package ncl.csc8019.group12.controller;

import ncl.csc8019.group12.BackendApplication;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

/**
 * @author wei tan
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {BackendApplication.class})
public class PlaceControllerTest {

    @Resource
    private WebApplicationContext webApplicationContext;

    //mock request
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testNearby() throws Exception {
        String url = "/place/nearby?location=54.9733004, -1.6218502";


        String responseString = mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse().getContentAsString();
        Assert.assertNotNull(responseString);

        String responseString2 = mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assert.assertEquals(responseString, responseString2);
    }

    @Test
    public void testDetail() throws Exception {
        String url = "/place/detail?placeId=ChIJE-u-MMpwfkgRe5igEnexTN0";


        String responseString = mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse().getContentAsString();
        Assert.assertNotNull(responseString);

        String responseString2 = mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assert.assertEquals(responseString, responseString2);
    }
}
