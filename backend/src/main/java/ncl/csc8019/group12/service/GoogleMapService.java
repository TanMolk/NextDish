package ncl.csc8019.group12.service;


import ncl.csc8019.group12.exception.ExternalAPIException;
import ncl.csc8019.group12.exception.ExternalAPIParamsException;
import ncl.csc8019.group12.exception.ExternalAPIResponseException;
import ncl.csc8019.group12.pojo.Location;
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
 * @author Pulei, Rachel & Wei
 */
@Service
public class GoogleMapService {

    /**
     * Entrance url for google map api.
     */
    private static final String GOOGLE_APT_BASE_PATH = "https://maps.googleapis.com/maps/api";

    /**
     * When do a request to google map api, must attach this value with key.
     * Like key=abc123
     * default as null
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
    public JSONObject getPlaceDetail(String placeID) {
        Map<String, String> params = new HashMap<>();
        params.put(RequestFieldEnum.PLACE_ID.name, placeID);
        return baseRequest(APIPathEnum.DETAIL, params).getJSONObject("result");
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
     * Request to a google map api
     *
     * @param api           @see{@link APIPathEnum} which api to call
     * @param requestParams Query params
     * @return @see {@link JSONObject} The original response
     * @throws ExternalAPIParamsException   If required params don't contain in requestParams.
     * @throws ExternalAPIResponseException If response is not normal
     * @author Wei
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

        //Build the request
        requestParams.put(RequestFieldEnum.KEY.name, MAP_API_KEY);
        UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(GOOGLE_APT_BASE_PATH + api.path);
        requestParams.forEach(urlBuilder::queryParam);

        String json = restTemplate.getForObject(urlBuilder.build().encode().toString(), String.class);
        JSONObject jsonObject = new JSONObject(json);

        //if response is empty or status of response is not "ok", throw exception
        if (!"OK".equalsIgnoreCase(jsonObject.getString(ResponseFieldEnum.STATUS.name))) {
            throw new ExternalAPIResponseException(json);
        }

        return jsonObject;
    }

    /**
     * @author Wei
     * Enum for possible used api
     * <a href="https://developers.google.com/maps/documentation/places/web-service">API Document</a>
     */
    private enum APIPathEnum {
        /**
         * <a href="https://developers.google.com/maps/documentation/places/web-service/search-nearby">Nearby Place Search</a>
         */
        NEARBY("/place/nearbysearch/json",
                new RequestFieldEnum[][]{
                        new RequestFieldEnum[]{
                                RequestFieldEnum.RADIUS,
                                RequestFieldEnum.LOCATION
                        },
                        new RequestFieldEnum[]{
                                RequestFieldEnum.PAGE_TOKEN,
                        }}),

        /**
         * <a href="https://developers.google.com/maps/documentation/places/web-service/details">Place Detail</a>
         */
        DETAIL("/place/details/json",
                new RequestFieldEnum[][]{
                        new RequestFieldEnum[]{
                                RequestFieldEnum.PLACE_ID,
                        }});

        /**
         * Path for this api
         */
        private final String path;

        /**
         * Required query params for this api
         */
        private final RequestFieldEnum[][] requiredParams;

        APIPathEnum(String path, RequestFieldEnum[][] requiredParam) {
            this.path = path;
            this.requiredParams = requiredParam;
        }
    }

    /**
     * @author Wei
     * Field name of google api request
     */
    private enum RequestFieldEnum {


        KEY("key"),

        //for nearby place api
        //like -1.123,11.123
        LOCATION("location"),
        RADIUS("radius"),
        TYPE("type"),
        KEYWORD("keyword"),

        PAGE_TOKEN("pagetoken"),


        //for place detail api
        PLACE_ID("place_id"),

        ;

        private final String name;

        RequestFieldEnum(String name) {
            this.name = name;
        }
    }

    /**
     * @author Wei
     * Field name of google api response
     */
    private enum ResponseFieldEnum {
        /**
         * To judge if a request is fine.
         * "OK" means normal
         */
        STATUS("status"),
        NEXT_PAGE_TOKEN("next_page_token"),
        ;

        private final String name;

        ResponseFieldEnum(String name) {
            this.name = name;
        }
    }
}
