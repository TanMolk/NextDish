package ncl.csc8019.group12.enums.google.map;

/**
 * @author Wei
 * Enum for possible used api
 * <a href="https://developers.google.com/maps/documentation/places/web-service">API Document</a>
 */
public enum APIPathEnum {
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
                    }}),

    /**
     * <a href="https://developers.google.com/maps/documentation/places/web-service/photos">Photo</a>
     */
    PHOTO("/place/photo", null),

    ;

    /**
     * Path for this api
     */
    public final String path;

    /**
     * Required query params for this api
     */
    public final RequestFieldEnum[][] requiredParams;

    APIPathEnum(String path, RequestFieldEnum[][] requiredParam) {
        this.path = path;
        this.requiredParams = requiredParam;
    }
}
