package ncl.csc8019.group12;
import ncl.csc8019.group12.service.NearbyRestaurantsAPI;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NearbyRestaurantsTest {

    // Test the nearbySearch() method
    @Test
    public void testNearbySearch() {
        // Test case 1 - search for restaurants near School of Computing
        double latitude1 = 55.8712287;
        double longitude1 = -4.2870229;
        String response1 = NearbyRestaurantsAPI.nearbySearch(latitude1, longitude1);
        assertNotNull(response1);  // Check that the response is not null
        assertFalse(response1.startsWith("Error"));  // Check that there are no errors in the response

        // Test case 2 - search for restaurants near Buckingham Palace
        double latitude2 = 51.501364;
        double longitude2 = -0.141889;
        String response2 = NearbyRestaurantsAPI.nearbySearch(latitude2, longitude2);
        assertNotNull(response2);  // Check that the response is not null
        assertFalse(response2.startsWith("Error"));  // Check that there are no errors in the response
    }
}