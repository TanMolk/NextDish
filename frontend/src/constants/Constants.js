//application constants
import StorageUtil from "@/utils/StorageUtil";

//-----------------System setting---------------
const MODE = import.meta.env.MODE;
let API_GATEWAY = import.meta.env.VITE_API_GATEWAY;

//google map constants
let GOOGLE_MAP_ID;
let GOOGLE_MAP_API_KEY;
if (MODE === "production") {
    //production environment
    API_GATEWAY = "";
    GOOGLE_MAP_ID = ""
    GOOGLE_MAP_API_KEY = "";


} else {
    //not production
    GOOGLE_MAP_ID = import.meta.env.VITE_GOOGLE_MAP_ID;
    GOOGLE_MAP_API_KEY = import.meta.env.VITE_GOOGLE_MAP_API_KEY;
}

const SUPPORT_SHARE = !!navigator.share;

//-----------------System setting end---------------


//----------local storage constants start---------------
const STORAGE_USER_LOCATION_LATITUDE = "currentPositionLat";
const STORAGE_USER_LOCATION_LONGITUDE = "currentPositionLng";
//if show the welcome page, when access to root path
const STORAGE_IF_NEVER_SHOW = "ifNeverShowWelcomePage";
//if user has experienced user guidance
const STORAGE_IF_EXPERIENCE_USER_GUIDANCE = "ifExperiencedGuidance";
//if detail is from marker clicking
const STORAGE_IF_DETAIL_SHOW_BY_CLICK_MARKER = "detailShowByClickMarker";
//client uuid
const STORAGE_CLIENT_UUID = "clientId"
//----------local storage constants end---------------


//-------Assign Client ID---------------
let CLIENT_ID = StorageUtil.get(STORAGE_CLIENT_UUID);
//set client id
if (!CLIENT_ID) {
    CLIENT_ID = uuid();
    StorageUtil.set(STORAGE_CLIENT_UUID, CLIENT_ID);
}
console.log(CLIENT_ID);
//-------Assign Client ID stop----------

function uuid() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        var r = Math.random() * 16 | 0,
            v = c == 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
    });
}

export default {
    MODE,
    API_GATEWAY,
    GOOGLE_MAP_ID,
    GOOGLE_MAP_API_KEY,
    SUPPORT_SHARE,
    STORAGE_USER_LOCATION_LATITUDE,
    STORAGE_USER_LOCATION_LONGITUDE,
    STORAGE_IF_NEVER_SHOW,
    STORAGE_IF_EXPERIENCE_USER_GUIDANCE,
    STORAGE_IF_DETAIL_SHOW_BY_CLICK_MARKER,
    CLIENT_ID
}
