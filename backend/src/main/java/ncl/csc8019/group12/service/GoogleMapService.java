package ncl.csc8019.group12.service;

import com.alibaba.fastjson.JSONObject;
import ncl.csc8019.group12.exception.ExternalAPIException;
import ncl.csc8019.group12.exception.ExternalAPIParamsException;
import ncl.csc8019.group12.exception.ExternalAPIResponseException;
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
 * @author wei tan
 */
@Service
public class GoogleMapService {

    /**
     * Entrance url for google map api.
     */
    private static final  String GOOGLE_APT_BASE_PATH = "https://maps.googleapis.com/maps/api";

    /**
     * When do a request to google map api, must attach this value with key.
     * Like key=abc123
     * default as null
     */
    @Value("${google.cloud.map.key}")
    private String MAP_KEY;

    /**
     * For http request
     */
    @Resource
    private RestTemplate restTemplate;

    /**
     * Get more infomation of a place
     *
     * @param placeID The place_id return by google place api, which is the identifier of a place
     */
    public JSONObject exampleHowToUse(String placeID) {
        Map<String, String> params = new HashMap<>();
        params.put(RequestFieldEnum.PLACE_ID.name, placeID);
        return baseRequest(APIPathEnum.DETAIL, params);
    }

    /**
     * Request to a google map api
     *
     * @param api           @see{@link APIPathEnum} which api to call
     * @param requestParams Query params
     * @return @see {@link JSONObject}
     * @throws ExternalAPIParamsException   If required params don't contain in requestParams.
     * @throws ExternalAPIResponseException If response is not normal
     */
    private JSONObject baseRequest(APIPathEnum api, Map<String, String> requestParams) throws ExternalAPIException {
        //check params
        for (RequestFieldEnum requiredParam : api.requiredParams) {
            //if it doesn't contain required param, throw exception
            if (!requestParams.containsKey(requiredParam.name)) {
                throw new ExternalAPIParamsException();
            }
        }

        //Build the request
        requestParams.put(RequestFieldEnum.KEY.name, MAP_KEY);
        UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(GOOGLE_APT_BASE_PATH + api.path);
        requestParams.forEach(urlBuilder::queryParam);

        String json = restTemplate.getForObject(urlBuilder.build().encode().toString(), String.class);
        JSONObject jsonObject = JSONObject.parseObject(json);

        //if response is empty or status of response is not "ok", throw exception
        if (jsonObject == null
                || !"OK".equalsIgnoreCase(jsonObject.getString(ResponseFieldEnum.STATUS.name))) {
            throw new ExternalAPIResponseException(json);
        }

        return jsonObject;
    }

    /**
     * Enum for possible used api
     * <a href="https://developers.google.com/maps/documentation/places/web-service">API Document</a>
     */
    private enum APIPathEnum {
        /**
         * <a href="https://developers.google.com/maps/documentation/places/web-service/search-nearby">Nearby Place Search</a>
         */
        NEARBY("/place/nearbysearch/json",
                new RequestFieldEnum[]{
                        RequestFieldEnum.RADIUS,
                        RequestFieldEnum.LOCATION
                }),

        /**
         * <a href="https://developers.google.com/maps/documentation/places/web-service/details">Place Detail</a>
         */
        DETAIL("/place/details/json",
                new RequestFieldEnum[]{
                        RequestFieldEnum.PLACE_ID,
                });

        /**
         * Path for this api
         */
        private final String path;

        /**
         * Required query params for this api
         */
        private final RequestFieldEnum[] requiredParams;

        APIPathEnum(String path, RequestFieldEnum[] requiredParam) {
            this.path = path;
            this.requiredParams = requiredParam;
        }
    }

    /**
     * Field name of google api request
     */
    private enum RequestFieldEnum {


        KEY("key"),

        //for nearby place api
        LOCATION("location"),
        RADIUS("radius"),

        //for place detail  api
        PLACE_ID("place_id"),

        ;

        private final String name;

        RequestFieldEnum(String name) {
            this.name = name;
        }
    }

    /**
     * Field name of google api response
     */
    private enum ResponseFieldEnum {
        /**
         * To judge if a request is fine.
         * "OK" means normal
         */
        STATUS("status"),
        ;

        private final String name;

        ResponseFieldEnum(String name) {
            this.name = name;
        }
    }
}
