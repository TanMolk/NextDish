package ncl.csc8019.group12.controller;

import ncl.csc8019.group12.pojo.Location;
import ncl.csc8019.group12.service.CacheService;
import ncl.csc8019.group12.service.GoogleMapService;
import ncl.csc8019.group12.utils.DistanceUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Place Apis.
 * Use the CacheService and GoogleMap API to find places near the user and get detailed information about the place.
 *
 * @author  Rachel wu & Wei tan & Pulei chen
 */

@CrossOrigin
@RestController
@RequestMapping("/place")
public class PlaceController {

    private static final Logger log = LoggerFactory.getLogger(PlaceController.class);

    private static final AtomicInteger API_CALL_TOTAL_AMOUNT = new AtomicInteger();
    private static final AtomicInteger CACHE_HIT_AMOUNT = new AtomicInteger();
    private static final AtomicInteger GOOGLE_API_REQUEST_AMOUNT = new AtomicInteger();

    @Resource
    private CacheService cacheService;

    @Resource
    private GoogleMapService googleMapService;

    /**
     * Get nearby places with location and keyword or nextPageToken.
     * Must contain location or nextPageToken.
     * If the priority of params is nextPageToken > location and keyword.
     * If the hash calculated by all request params is contained in cache, will return the cached response.
     *
     * @param location      location format is like 11.1111,-12,123
     * @param keyword       keyword of nearby places
     * @param nextPageToken the nextPageToken returned
     * @return Places data
     */


    @GetMapping("/nearby")
    public String nearByPlaces(@RequestHeader("c8019-client-id") String clientId,
                               @RequestParam String location,
                               @RequestParam(required = false) String keyword,
                               @RequestParam(required = false) String nextPageToken) {
        //produce response
        /*
        * Create a boolean variable to check if the response is from cache.
        * Create a JSONObject variable to store the response.
        *
        * Split the location string into latitude and longitude.
        * Create a Location object to store the latitude and longitude.
        * Convert the required radius (1 mile) to meters and create a variable to store the value.
        */

        boolean fromCache = false;
        JSONObject responseObject;
        String[] locations = location.split(",");
        Location currentLocation = new Location(Double.parseDouble(locations[0]), Double.parseDouble(locations[1]));
        int radius = DistanceUtil.convertMilesToMetersApproximately(1);

        //get data
        /*
         * If there is nextPageToken, get the cached response with the token.
         *   If the response is null, get a new response via googleMapService api.
         *   Else, set fromCache to true.
         *
         * If the nextPageToken is null, get cached response with the location and keyword.
         *  If the response is null, get a new response via googleMapService api.
         *  Else, set fromCache to true.
         */


        if (nextPageToken != null) {
            responseObject = cacheService.getCachedResponse(nextPageToken);
            if (responseObject == null) {
                responseObject = googleMapService.getNearByPlaceWithNextPageToken(nextPageToken);
            } else {
                fromCache = true;
            }
        } else {
            responseObject = cacheService.getCachedResponse(location, keyword);
            if (responseObject == null) {
                responseObject = googleMapService.getNearByPlaceWithLocation(
                        currentLocation,
                        radius,
                        keyword);
            } else {
                fromCache = true;
            }
        }

        //log and count request
        /*
        * Log request information, including incrementing API_CALL_TOTAL_AMOUNT,
        * whether the request is from cache, the location, keyword, nextPageToken and clientID.
         */
        
        log.info("[{}-Nearby] cache:{}; location:{}, keyword:{}, nextPageToken:{}; clientId:{}",
                API_CALL_TOTAL_AMOUNT.addAndGet(1),
                fromCache,
                location, keyword, nextPageToken,
                clientId
        );

        //if it gets cache, return.
        /*
        * If from cache is true, increment the CACHE_HIT_AMOUNT.
        * Turn the responseObject to string and return it.
         */

        if (fromCache) {
            CACHE_HIT_AMOUNT.addAndGet(1);
            return responseObject.toString();
        }

        //filter places within 1 mile
        /*
         * If fromCache is false,
         * Create an array to store all places in the response.
         * Create another array to store the filtered places within 1 mile of current location.
         *
         * Get the corresponding locations of the places using a for-loop.
         * Get the latitude and longitude of each location to create a new Location object.
         * Calculate the distance between the current location and the new Location object.
         * If the distance is within 1 mile, append this place JSONObject to the filteredPlaces JSONArray.
         * Repeat until the loop ends.
         *
         * Finally, append the JSONArray of filteredPlaces to the responseObject.
         */
        JSONArray places = responseObject.getJSONArray("results");
        JSONArray filteredPlaces = new JSONArray();

        for (int i = 0; i < places.length(); i++) {
            JSONObject place = places.getJSONObject(i);
            JSONObject placeLocationObj = place.getJSONObject("geometry").getJSONObject("location");

            Location placeLocation = new Location(
                    placeLocationObj.getDouble("lat"),
                    placeLocationObj.getDouble("lng"));

            if (DistanceUtil.calculateApproximateDistanceOfTwoLocation(currentLocation, placeLocation) <= radius) {
                filteredPlaces.put(place);
            }
        }
        responseObject.put("results", filteredPlaces);

        //cache
        /*
        * If there is nextPageToken, add the responseObject and the token to the cache storage.
        * Else, add the responseObject, location and keyword to the cache storage.
        * Increment the GOOGLE_API_REQUEST_AMOUNT.
        * Turn the responseObject to string and return it.
        *
        * */
        if (nextPageToken != null) {
            cacheService.cacheResponse(responseObject, nextPageToken);
        } else {
            cacheService.cacheResponse(responseObject, location, keyword);
        }

        GOOGLE_API_REQUEST_AMOUNT.addAndGet(1);
        return responseObject.toString();
    }

    /**
     * Get details of the location by placeId.
     * Must contain clientId and placeId.
     * Use the CacheService to reduce the frequency of use of Google APIs and thus improve efficiency.
     *
     * @param clientId     A unique id for each user
     * @param placeId      Unique id for each place
     * @return             Detailed about the place identified by placeID
     */
    @GetMapping("/detail")
    public String detail(@RequestHeader("c8019-client-id") String clientId,
                         @RequestParam String placeId) {

        JSONObject response;
        //get details of the place from cache
        response = cacheService.getCachedResponse(placeId);

        /*
        *This code uses the logger logs to record specific information about using the CacheService,
        * such as whether the target place's details are available in the cache and the count of API calls.
        */
        log.info("[{}-Detail] cache:{}; placeId:{}; clientId:{}",
                API_CALL_TOTAL_AMOUNT.addAndGet(1),
                response != null,
                placeId,
                clientId
        );


        /*
        * If there are target data in the Cache, the data is returned via the toString method,
        * if there is no target data in the Cache, the data is obtained by calling GoogleAPI and storing the new data in the cache.
        * also increase the number of Google API usage once.
        */
        if (response != null) {
            CACHE_HIT_AMOUNT.addAndGet(1);
            return response.toString();
        }

        response = googleMapService.getPlaceDetail(placeId, clientId);
        GOOGLE_API_REQUEST_AMOUNT.addAndGet(1);
        cacheService.cacheResponse(response, placeId);
        return response.toString();
    }


    /**
     * This code defines an API interface for fetching map photos. When the interface is called,
     * it fetches the specified photo from the cache or Google Maps API based on the parameters passed in and returns the result as a byte array to the caller.
     * Must contain clientId and placeId.
     * Use the CacheService to reduce the frequency of use of Google APIs and thus improve efficiency.
     *
     * @param clientId            A unique id for each user
     * @param photoReference      It is the identifier of the image in the Google Places API
     * @param width               It defines the width of the image returned by GoogleAPI
     * @param height              It defines the height of the image returned by GoogleAPI
     * @return                    Pictures in the form of byte arrays,but it will be converted to jepg format through the browser
     */
    @GetMapping(
            value = "/photo",
            //Define the response type of this interface as "JEPG" format
            produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] photo(@RequestHeader("c8019-client-id") String clientId,
                        @RequestParam String photoReference,
                        @RequestParam int width,
                        @RequestParam int height) {
            //Determine if the image already exists in the cache
        byte[] photoBytes = cacheService.getPhotoCache(photoReference);

            //The log records specific information about when the image was found and whether the CacheService service was used and counted.
        log.info("[{}-Photo] cache:{}; photoReference:{}; clientId:{}",
                API_CALL_TOTAL_AMOUNT.addAndGet(1),
                photoBytes != null,
                photoReference,
                clientId
        );

           /*
           * If the image is already stored in the Cache,
           * the data for that image is returned directly.
           */
        if (photoBytes != null) {
            CACHE_HIT_AMOUNT.addAndGet(1);
            return photoBytes;
        }

           /*
           * If the image is not in the Cache,
           * the GoogleAPI is called to get the data of the image while storing the data in the Cache and finally returning the data.
           */
        photoBytes = googleMapService.getPhoto(photoReference, width, height);
        GOOGLE_API_REQUEST_AMOUNT.addAndGet(1);
        cacheService.cachePhoto(photoReference, photoBytes);
        return photoBytes;
    }
}
