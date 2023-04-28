package ncl.csc8019.group12.enums.google.map;

/**
 * @author Wei
 * @see APIPathEnum
 * Field name of google api response
 */
public enum ResponseFieldEnum {
    /**
     * To judge if a request is fine.
     * "OK" means normal
     */
    STATUS("status"),

    NEXT_PAGE_TOKEN("next_page_token"),


    //for place detail
    NAME("name"),
    GEOMETRY("geometry"),
    PHOTO("photo"),
    PLACE_ID("place_id"),
    OPENING_HOURS("opening_hours"),
    TYPES("types"),

    //detail addition information
    CURBSIDE_PICKUP("curbside_pickup"),
    DELIVERY("delivery"),
    DINE_IN("dine_in"),
    RESERVABLE("reservable"),
    SERVES_BEER("serves_beer"),
    SERVES_BREAKFAST("serves_breakfast"),
    SERVES_BRUNCH("serves_brunch"),
    SERVES_DINNER("serves_dinner"),
    SERVES_LUNCH("serves_lunch"),
    SERVES_VEGETARIAN_FOOD("serves_vegetarian_food"),
    SERVES_WINE("serves_wine"),
    TAKEOUT("takeout"),
    ;

    public final String name;

    ResponseFieldEnum(String name) {
        this.name = name;
    }

    public static ResponseFieldEnum[] getAdditions() {
        return new ResponseFieldEnum[]{
                CURBSIDE_PICKUP,
                DELIVERY,
                DINE_IN,
                RESERVABLE,
                SERVES_BEER,
                SERVES_BREAKFAST,
                SERVES_BRUNCH,
                SERVES_DINNER,
                SERVES_LUNCH,
                SERVES_VEGETARIAN_FOOD,
                SERVES_WINE,
                TAKEOUT,
        };
    }
}
