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
 * Place Apis
 *
 * @author Pulei & Rachel & Wei
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
         * If fromCache is false:
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

    @GetMapping("/detail")
    public String detail(@RequestHeader("c8019-client-id") String clientId,
                         @RequestParam String placeId) {

        JSONObject response;
        //get from cache
        response = cacheService.getCachedResponse(placeId);

        log.info("[{}-Detail] cache:{}; placeId:{}; clientId:{}",
                API_CALL_TOTAL_AMOUNT.addAndGet(1),
                response != null,
                placeId,
                clientId
        );

        if (response != null) {
            CACHE_HIT_AMOUNT.addAndGet(1);
            return response.toString();
        }

        response = googleMapService.getPlaceDetail(placeId, clientId);
        GOOGLE_API_REQUEST_AMOUNT.addAndGet(1);
        cacheService.cacheResponse(response, placeId);
        return response.toString();
    }

    @GetMapping(
            value = "/photo",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] photo(@RequestHeader("c8019-client-id") String clientId,
                        @RequestParam String photoReference,
                        @RequestParam int width,
                        @RequestParam int height) {
        byte[] photoBytes = cacheService.getPhotoCache(photoReference);

        log.info("[{}-Photo] cache:{}; photoReference:{}; clientId:{}",
                API_CALL_TOTAL_AMOUNT.addAndGet(1),
                photoBytes != null,
                photoReference,
                clientId
        );


        if (photoBytes != null) {
            CACHE_HIT_AMOUNT.addAndGet(1);
            return photoBytes;
        }

        photoBytes = googleMapService.getPhoto(photoReference, width, height);
        GOOGLE_API_REQUEST_AMOUNT.addAndGet(1);
        cacheService.cachePhoto(photoReference, photoBytes);
        return photoBytes;
    }
}
