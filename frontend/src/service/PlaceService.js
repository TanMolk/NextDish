/**
 *Functions for Google map place api
 *@author wei tan
 */
import axios from 'axios';
import Constants from "@/constants/Constants";
import {mileToMeter} from "@/utils/MileUtil";

let http = axios.create({
    baseURL: Constants.API_GATEWAY
});

/**
 * Get restaurants in one mile with location and type
 * @param location longitude and latitude of center location, like {lat: 55,lng: -1.6}
 * @param keyword keyword of aim restaurants
 */
function getRestaurantWithKeywordInOneMile(location, keyword) {
    return http.get("/place/nearby",
        {
            params: {
                location: location.lat + "," + location.lng,
                keyword,
                type: "restaurant",
                radius: mileToMeter(1)
            }
        });
}

/**
 * Get more information of this place
 * @param placeReference place reference id that Google Api return
 */
function getPlaceDetail(placeReference) {
    return http.get("/place/detail",
        {
            params: {
                place_id: placeReference
            }
        });
}

/**
 * Get restaurant photo
 * @param photoInfo google returned photo's information, format like
 *             {
 *                 "height": 2268,
 *                 "photo_reference": "123",
 *                 "width": 4032
 *             }
 *
 */
function getPlaceImage(photoInfo) {
    return http.get("/place/photo",
        {
            responseType: "blob",
            params: {
                photo_reference: photoInfo.photo_reference,
                maxheight: photoInfo.height,
                maxwidth: photoInfo.width
            }
        });
}

export default {
    getRestaurantWithKeywordInOneMile,
    getPlaceImage,
    getPlaceDetail,

}