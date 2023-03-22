//application constans
const MODE = import.meta.env.MODE;
const API_GATEWAY = import.meta.env.VITE_API_GATEWAY;

//google map constants
let GOOGLE_MAP_ID;
let GOOGLE_MAP_API_KEY;

if (MODE === "production") {
    //production environment

} else {
    //not production
    GOOGLE_MAP_ID = import.meta.env.VITE_GOOGLE_MAP_ID;
    GOOGLE_MAP_API_KEY = import.meta.env.VITE_GOOGLE_MAP_API_KEY;
}


//local storage constants
const STORAGE_USER_LOCATION_LATITUDE = "currentPositionLat"
const STORAGE_USER_LOCATION_LONGITUDE = "currentPositionLng"
//exist means is not the first time, don't exist means it is the first time
const STORAGE_IS_USER_FIRST_USER_STAT = "userFirstUseState"

export default {
    MODE,
    API_GATEWAY,
    GOOGLE_MAP_ID,
    GOOGLE_MAP_API_KEY,
    STORAGE_USER_LOCATION_LATITUDE,
    STORAGE_USER_LOCATION_LONGITUDE,
    STORAGE_IS_USER_FIRST_USER_STAT,
}
