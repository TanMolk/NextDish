package ncl.csc8019.group12.service;

import ncl.csc8019.group12.pojo.Location;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@SpringBootApplication
@RestController
@RequestMapping("/nearbyrestaurants")
public class NearbyRestaurantsAPI {

    // Constants for the API request
    private static final String baseUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";
    private static final String restaurantType = "restaurant";

        // radius - the search radius is set within 1000 meters
    private static final int radius = 1000;

    // API key (replace with your own key)
    private static final String apiKey = "YOUR_API_KEY_HERE";



    /**
     * Performs a nearby search for restaurants using the Google Places API.
     *
     * @param latitude  the latitude of the search location
     * @param longitude the longitude of the search location
     * @return a JSON string containing the search results
     *
     * author: Rachel Wu
     * @see <a href="https://developers.google.com/maps/documentation/places/web-service/search-nearby#maps_http_places_nearbysearch-txt"> Nearby Search API</a>
     * @see <a href="https://www.baeldung.com/java-http-request"> http request </a>
     *
     */
    public static String nearbySearch(double latitude, double longitude) {
        try {
            // Build the API request URL
            String url = baseUrl + "?location=" + latitude + "," + longitude + "&radius=" + radius + "&type=" + restaurantType + "&key=" + apiKey;

            // Create a new HTTP connection
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

            // Set the request method to GET
            connection.setRequestMethod("GET");

            // Get the response code
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // If the response code is OK, read the response
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder responseBuilder = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line);
                }

                reader.close();
                connection.disconnect();

                // Return the response as a JSON string
                return responseBuilder.toString();
            } else {
                // If the response code is not OK, return an error message
                return "Error: " + responseCode + " " + connection.getResponseMessage();
            }

        } catch (Exception e) {
            // If an exception is thrown, return an error message
            return "Error: " + e.getMessage();
        }
    }
}

