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
    public String nearByPlaces(@RequestParam String location,
                               @RequestParam(required = false) String keyword,
                               @RequestParam(required = false) String nextPageToken) {
        //log and count request
        log.info("[Nearby-{}] location:{}, keyword:{}, nextPageToken:{}",
                API_CALL_TOTAL_AMOUNT.addAndGet(1),
                location, keyword, nextPageToken);

        //produce response
        boolean fromCache = false;
        JSONObject responseObject;
        String[] locations = location.split(",");
        Location currentLocation = new Location(Double.parseDouble(locations[0]), Double.parseDouble(locations[1]));
        int radius = DistanceUtil.convertMilesToMetersApproximately(1);

        //get data
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

        //if it gets cache, return.
        if (fromCache) {
            return responseObject.toString();
        }

        //filter places within 1 mile
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
        if (nextPageToken != null) {
            cacheService.cacheResponse(responseObject, nextPageToken);
        } else {
            cacheService.cacheResponse(responseObject, location, keyword);
        }

        GOOGLE_API_REQUEST_AMOUNT.addAndGet(1);
        return responseObject.toString();
    }

    @GetMapping("/detail")
    public String detail(@RequestParam String placeId) {
        log.info("[Detail-{}] placeId:{}", API_CALL_TOTAL_AMOUNT.addAndGet(1), placeId);

        JSONObject response;
        //get from cache
        response = cacheService.getCachedResponse(placeId);

        if (response == null) {
            response = googleMapService.getPlaceDetail(placeId);
            GOOGLE_API_REQUEST_AMOUNT.addAndGet(1);
            cacheService.cacheResponse(response, placeId);
        }

        return response.toString();
    }

    @GetMapping(
            value = "/photo",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] photo(@RequestParam String photoReference,
                        @RequestParam int width,
                        @RequestParam int height) {
        log.info("[Photo-{}] photoReference:{}", API_CALL_TOTAL_AMOUNT.addAndGet(1), photoReference);
        byte[] photoBytes = cacheService.getPhotoCache(photoReference);
        if (photoBytes == null) {
            photoBytes = googleMapService.getPhoto(photoReference, width, height);
            GOOGLE_API_REQUEST_AMOUNT.addAndGet(1);
            cacheService.cachePhoto(photoReference, photoBytes);
        }
        return photoBytes;
    }
}
