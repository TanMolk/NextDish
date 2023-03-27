package ncl.csc8019.group12.service;

import ncl.csc8019.group12.pojo.Location;
import ncl.csc8019.group12.utils.DistanceUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This Cache Service is to store data in server memory for further use.
 * It helps with the speed of data request and decrease the number of requesting for Google place api.
 * And the cache is stored in a ConcurrentHashMap, which could make concurrency safety.
 *
 * @author wei tan
 */

@Service
public class CacheService {

    //Key: placeId
    //Value: the information of this place
    private final static Map<String, JSONObject> CACHE_STORAGE = new ConcurrentHashMap<>();

    /**
     * Get information about a place with the placeId
     *
     * @param placeId the placeId of the place,which is returned by google map api
     * @return null-if don't contain this place cache, or the input is null
     */
    public JSONObject getPlaceDetailCacheWithPlaceId(String placeId) {
        if (placeId == null) {
            return null;
        }

        return CACHE_STORAGE.get(placeId);
    }

    /**
     * Get nearby places information by location and radius.
     * This method will return nearby places' information based on a certain circle.
     * This circle is from input latitude, longitude and radius.
     * If the place is within this circle, it will be included in the return list.
     *
     * @param location The location of centre point
     * @param radius   the circle radius, meters
     * @return A list of places' information within the circle
     */
    public List<JSONObject> getNearByPlaceDetailsWithLocation(Location location, double radius) {
        List<JSONObject> results = new ArrayList<>();

        for (JSONObject place : CACHE_STORAGE.values()) {
            //get location in place information
            JSONObject locationJsonObject = place.getJSONObject("geometry")
                    .getJSONObject("location");
            Location destination = new Location(
                    locationJsonObject.getDouble("lat"),
                    locationJsonObject.getDouble("lng"));

            //calculate distance
            if (DistanceUtil.calculateApproximateDistanceOfTwoLocation(location, destination) <= radius) {
                results.add(place);
            }
        }

        return results;
    }

    /**
     * Save or update place information cache.
     * <br>
     * If the place don't have cache and the jsonObject have the key "place_id", it will save this information.
     * <br>
     * If the place has cache and the json string length is greater than the original one (it means the new one contain more information), it will update to the new one.
     * <br>
     * And one assumption is that in one day, the information of a place can only increase
     *
     * @return If update successfully:
     * true-successfully
     * false-do not save or update
     */
    public boolean saveOrUpdatePlaceCache(JSONObject placeInformation) {
        //Skip null
        if (placeInformation == null) {
            return false;
        }

        //get place_id
        Object placeIdObject;
        try {
            placeIdObject = placeInformation.get("place_id");
        } catch (JSONException e) {
            //can't find, return false
            return false;
        }

        String placeId = placeIdObject.toString();
        JSONObject cache = CACHE_STORAGE.get(placeId);
        //save
        if (cache == null) {
            CACHE_STORAGE.put(placeId, placeInformation);
            return true;
        } else {
            //update
            if (cache.toString().length() < placeInformation.toString().length()) {
                CACHE_STORAGE.put(placeId, placeInformation);
                return true;
            }
        }
        //don't contain key "place_id" or don't update
        return false;
    }

    /**
     * @param placeInformationList the information of places
     * @return how many cache saved or updated
     */
    public int saveOrUpdatePlacesCache(List<JSONObject> placeInformationList) {
        int count = 0;

        for (JSONObject jsonObject : placeInformationList) {
            if (saveOrUpdatePlaceCache(jsonObject)) {
                count++;
            }
        }
        return count;
    }
}
