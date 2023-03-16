/**
 *Functions for Google map place api
 *@author wei tan
 */
import axios from 'axios';
import Constants from "@/constants/Constants";
import {mileToMeter} from "@/utils/MileUtil";

let http = axios.create({
    baseURL: "/map-api",
    params: {
        key: Constants.GOOGLE_MAP_API_KEY
    }
});

/**
 * Get restaurants in one mile with location and type
 * @param location longitude and latitude of center location, like {lat: 55,lng: -1.6}
 * @param keyword keyword of aim restaurants
 */
async function getRestaurantWithKeywordInOneMile(location, keyword) {
    return http.get("/place/nearbysearch/json",
        {
            params: {
                location: location.lat + "," + location.lng,
                keyword,
                type: "restaurant",
                radius: mileToMeter(1)
            }
        });

}

export default {
    getRestaurantWithKeywordInOneMile
}