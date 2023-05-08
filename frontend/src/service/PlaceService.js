/**
 *Functions for Google map place api
 *@author wei tan
 */
import BaseService from "@/service/BaseService";

class PlaceService extends BaseService {
    /**
     * To get restaurants in one mile with location and type
     * @param location longitude and latitude of center location, like {lat: 55,lng: -1.6}
     * @param keyword keyword of aim restaurants
     * @param nextPageToken the nextPageToken, for page turning
     */
    getRestaurantWithKeywordInOneMile(location, keyword, nextPageToken) {
        //use round to keep 3 decimal to make request, although it will cause location issue. But the error range it is within 100m.
        let lat = location.lat.toFixed(3);
        let lng = location.lng.toFixed(3);

        return this.$http.get("/place/nearby",
            {
                params: {
                    location: lat + "," + lng,
                    keyword,
                    nextPageToken
                }
            });
    }

    /**
     * To get more information of this place
     * @param placeReference place reference id that Google Api return
     */
    getPlaceDetail(placeReference) {
        return this.$http.get("/place/detail",
            {
                params: {
                    placeId: placeReference
                }
            });
    }

    /**
     * To get restaurant photo
     * @param photoInfo google returned photo's information, format like
     *             {
     *                 "height": 2268,
     *                 "photo_reference": "123",
     *                 "width": 4032
     *             }
     *
     */
    getPlaceImage(photoInfo) {
        return this.$http.get("/place/photo",
            {
                responseType: "blob",
                params: {
                    photoReference: photoInfo.photo_reference,
                    height: photoInfo.height,
                    width: photoInfo.width
                }
            });
    }
}


export default new PlaceService();