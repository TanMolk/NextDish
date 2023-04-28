package ncl.csc8019.group12.enums.google.map;

/**
 * @author Wei
 * @see APIPathEnum
 * Field name of google api request
 */
public enum RequestFieldEnum {
    KEY("key", null),

    //for nearby place api
    //like -1.123,11.123
    LOCATION("location", null),
    RADIUS("radius", null),
    TYPE("type", null),
    KEYWORD("keyword", null),
    PAGE_TOKEN("pagetoken", null),

    //for place detail api
    PLACE_ID("place_id", null),
    FIELDS("fields", "name,geometry,photo,place_id,opening_hours,type,curbside_pickup,delivery,dine_in,reservable,serves_beer,serves_breakfast,serves_brunch,serves_dinner,serves_lunch,serves_vegetarian_food,serves_wine,takeout"),
    SESSION_TOKEN("sessiontoken",null),


    ;

    public final String name;

    public final String defaultValue;

    RequestFieldEnum(String name, String defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }
}
