package ncl.csc8019.group12.controller;

import ncl.csc8019.group12.pojo.Location;
import ncl.csc8019.group12.service.CacheService;
import ncl.csc8019.group12.service.GoogleMapService;
import ncl.csc8019.group12.utils.DistanceUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.security.InvalidParameterException;

/**
 * Place Apis
 *
 * @author Pulei & Rachel & Wei
 */

@RestController
@RequestMapping("/place")
public class PlaceController {

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
    public String nearByPlaces(@RequestParam(required = false) String location,
                               @RequestParam(required = false) String keyword,
                               @RequestParam(required = false) String nextPageToken) {
        //avoid the same request
        if (nextPageToken != null) {
            String cache = cacheService.getCachedResponse(nextPageToken);
            //get data from cache
            if (cache != null) {
                return cache;
            } else {
                // get data from Google map api
                String response = googleMapService.getNearByPlaceWithNextPageToken(nextPageToken).toString();
                //cache response
                cacheService.cacheResponse(response, nextPageToken);
                return response;
            }
        }

        if (location != null) {
            String cache = cacheService.getCachedResponse(location, keyword);
            if (cache != null) {
                return cache;
            } else {
                String[] locations = location.split(",");
                Location currentLocation = new Location(Double.parseDouble(locations[0]), Double.parseDouble(locations[1]));
                int radius = DistanceUtil.convertMilesToMetersApproximately(1);
                String response = googleMapService.getNearByPlaceWithLocation(currentLocation, radius, keyword).toString();
                cacheService.cacheResponse(response, location, keyword);
                return response;
            }
        }

        throw new InvalidParameterException("");
    }

    @GetMapping("/detail")
    public String detail(@RequestParam String placeId) {

        //get from cache
        String cache = cacheService.getCachedResponse(placeId);

        if (cache != null) {
            return cache;
        } else {
            String response = googleMapService.getPlaceDetail(placeId).toString();
            cacheService.cacheResponse(response, placeId);
            return response;
        }
    }

    @GetMapping(
            value = "/photo",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] photo(@RequestParam String photoReference,
                        @RequestParam int width,
                        @RequestParam int height) {
        byte[] photoBytes = cacheService.getPhotoCache(photoReference);
        if (photoBytes == null) {
            photoBytes = googleMapService.getPhoto(photoReference, width, height);
            cacheService.cachePhoto(photoReference, photoBytes);
        }
        return photoBytes;
    }
}
