package ncl.csc8019.group12.service;


import ncl.csc8019.group12.enums.google.map.APIPathEnum;
import ncl.csc8019.group12.enums.google.map.RequestFieldEnum;
import ncl.csc8019.group12.enums.google.map.ResponseFieldEnum;
import ncl.csc8019.group12.exception.ExternalAPIException;
import ncl.csc8019.group12.exception.ExternalAPIParamsException;
import ncl.csc8019.group12.exception.ExternalAPIResponseException;
import ncl.csc8019.group12.pojo.Location;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Functions of Google map provides
 *
 * @author Wei tan & Rachel wu
 */

@Service
public class GoogleMapService {

    /**
     * Entrance url for google map api.
     */
    private static final String GOOGLE_APT_BASE_PATH = "https://maps.googleapis.com/maps/api";

    /**
     * When do a request to google map api, must attach this value with key.
     * Like key=abc123.
     * Default as null.
     */
    @Value("${google.cloud.map.key}")
    private String MAP_API_KEY;

    /**
     * For http request
     */
    @Resource
    private RestTemplate restTemplate;

    /**
     * Get detail information of a place
     *
     * @param placeID The place_id return by google place api, which is the identifier of a place
     * @author Rachel
     */

    /*
    * Create a hashmap to store placeID and sessionID input strings
    * Create a JSONObject to store baseRequest
    * Put the placeID and sessionID into the hashmap
    * Get the response
    * Return the response
    *
     */
    public JSONObject getPlaceDetail(String placeID, String sessionId) {
        Map<String, String> params = new HashMap<>();
        params.put(RequestFieldEnum.PLACE_ID.name, placeID);
        params.put(RequestFieldEnum.SESSION_TOKEN.name, sessionId);


        JSONObject result = baseRequest(APIPathEnum.DETAIL, params).getJSONObject("result");

        //handler additions
        /*
        * Create a JSONArray to store additions.
        * Use for loop to get the additions from ResponseFieldEnum.
        * If the result has the addition, then put the addition into the JSONArray.
        * Put the JSONArray into the result.
        * Return the result.
        *
         */
        JSONArray additions = new JSONArray();
        for (ResponseFieldEnum addition : ResponseFieldEnum.getAdditions()) {

            if (result.has(addition.name)) {
                String builder = addition.name.replace("_", " ")
                        + ": "
                        + (result.getBoolean(addition.name) ? "yes" : "no");
                additions.put(builder);
            }
        }
        result.put("additions", additions);

        return result;
    }

    /**
     * Get nearby places with location and fixed radius of 1 mile
     *
     * @param location the center location of circle
     * @return nearby places' brief information, max 60 places
     */
    public JSONObject getNearByPlaceWithLocation(Location location,
                                                 int radius,
                                                 String keyword) {
        String locationStr = location.getLatitude() + "," + location.getLongitude();

        Map<String, String> params = new HashMap<>();
        params.put(RequestFieldEnum.LOCATION.name, locationStr);
        //set radius as fixed, 1 miles
        params.put(RequestFieldEnum.RADIUS.name, String.valueOf(radius));
        //set type fixed as restaurant
        params.put(RequestFieldEnum.TYPE.name, "restaurant");
        params.put(RequestFieldEnum.KEYWORD.name, keyword == null ? "" : keyword);

        //get response and return
        return baseRequest(APIPathEnum.NEARBY, params);
    }

    /**
     * Get next page places' information
     *
     * @param nextPageToken the next page token Google returned
     * @return the original response
     */
    public JSONObject getNearByPlaceWithNextPageToken(String nextPageToken) {
        Map<String, String> params = new HashMap<>();
        params.put(RequestFieldEnum.PAGE_TOKEN.name, nextPageToken);
        return baseRequest(APIPathEnum.NEARBY, params);
    }

    /**
     * Get photo with photo reference
     *
     * @param photoReference photo reference, google api provided
     * @param maxheight      max height the picture is, just for request photo from Google platform
     * @param maxwidth       max with the picture is, just for request photo from Google platform
     * @return photo bytes
     */
    public byte[] getPhoto(String photoReference, int maxheight, int maxwidth) {
        Map<String, String> params = new HashMap<>();
        params.put("photo_reference", photoReference);
        params.put("maxheight", String.valueOf(maxheight));
        params.put("maxwidth", String.valueOf(maxwidth));

        String url = buildRequestUrl(APIPathEnum.PHOTO.path, params);

        return restTemplate.getForObject(url, byte[].class);
    }

    /**
     * Request to a google map api
     *
     * @param api           @see{@link APIPathEnum} which api to call
     * @param requestParams Query params
     * @return @see {@link JSONObject} The original response
     * @throws ExternalAPIParamsException   If required params don't contain in requestParams.
     * @throws ExternalAPIResponseException If response is not normal
     * @author Wei
     */

    /*
    * Create a JSONObject to store baseRequest
    * Check the required params
    * Build the request
    * Get the response
    * Check the response
    * Return the response
     */
    
    private JSONObject baseRequest(APIPathEnum api, Map<String, String> requestParams) throws ExternalAPIException {
        //check params
        for (RequestFieldEnum[] requiredParamList : api.requiredParams) {
            int needToValidNum = requiredParamList.length;
            for (RequestFieldEnum requestParam : requiredParamList) {
                if (requestParams.containsKey(requestParam.name)) {
                    needToValidNum--;
                }

                //if one set of required params passes, check successfully.
                if (needToValidNum == 0) {
                    break;
                }
            }
        }

        //select return fields only for Detail API
        if (APIPathEnum.DETAIL.equals(api)) {
            requestParams.put(
                    RequestFieldEnum.FIELDS.name,
                    RequestFieldEnum.FIELDS.defaultValue);
        }

        //Build the request
        String url = buildRequestUrl(api.path, requestParams);

        String json = restTemplate.getForObject(url, String.class);
        JSONObject jsonObject = new JSONObject(json);

        //if response is empty or status of response is not "ok", throw exception
        if (!"OK".equalsIgnoreCase(jsonObject.getString(ResponseFieldEnum.STATUS.name))) {
            throw new ExternalAPIResponseException(json);
        }

        return jsonObject;
    }

    /**
     * @param path          the uri
     * @param requestParams get query parameters
     * @return the request url
     */

    /*
    * Create a UriComponentsBuilder to build the request url
    * Put the requestParams into the UriComponentsBuilder
    * Return the request url
    *
     */
    private String buildRequestUrl(String path, Map<String, String> requestParams) {
        requestParams.put(RequestFieldEnum.KEY.name, MAP_API_KEY);
        UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(GOOGLE_APT_BASE_PATH + path);
        requestParams.forEach(urlBuilder::queryParam);
        return urlBuilder.build().encode().toString();
    }

}
