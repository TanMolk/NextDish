const MODE = import.meta.env.MODE;
const API_GATEWAY = import.meta.env.VITE_API_GATEWAY;
const GOOGLE_MAP_ID = import.meta.env.VITE_GOOGLE_MAP_ID;
const GOOGLE_MAP_API_KEY = import.meta.env.VITE_GOOGLE_MAP_API_KEY;

console.log(import.meta.env)
export default {
    MODE,
    API_GATEWAY,
    GOOGLE_MAP_ID,
    GOOGLE_MAP_API_KEY
}
