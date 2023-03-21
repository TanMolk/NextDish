const MODE = import.meta.env.MODE;
const API_GATEWAY = import.meta.env.VITE_API_GATEWAY;

let GOOGLE_MAP_ID;
let GOOGLE_MAP_API_KEY;

if (MODE === "production") {
    //production environment

} else {
    //not production
    GOOGLE_MAP_ID = import.meta.env.VITE_GOOGLE_MAP_ID;
    GOOGLE_MAP_API_KEY = import.meta.env.VITE_GOOGLE_MAP_API_KEY;
}

export default {
    MODE,
    API_GATEWAY,
    GOOGLE_MAP_ID,
    GOOGLE_MAP_API_KEY
}
