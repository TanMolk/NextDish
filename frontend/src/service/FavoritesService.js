import BaseService from "@/service/BaseService";
import UserUtil from "@/utils/UserUtil";

/**
 * This service provides functions to add, remove, get user favorites from backend
 * @author Ashton Chow
 */

class FavoritesService extends BaseService {

    /**
     * To add a restaurant to user's favorite
     * @param placeId restaurant's placeId
     * @returns {Promise<axios.AxiosResponse<any>>} http request
     */
    add(placeId) {
        return this.$http.post("/favorite", {
            placeId,
        }).catch((err) => {
            UserUtil.tokenExpired(err);
        });
    }

    /**
     * To remove a favorite restaurant
     * @param id restaurant's id
     * @returns {Promise<axios.AxiosResponse<any>>} http request
     */
    remove(id) {
        return this.$http.delete("/favorite/" + id)
            .catch((err) => {
                UserUtil.tokenExpired(err);
            });
    }

    /**
     * To get user's favourite restaurants
     * @returns {Promise<axios.AxiosResponse<any>>} http request
     */
    get() {
        return this.$http.get("/favorite")
            .catch((err) => {
                UserUtil.tokenExpired(err);
            });
    }
}


export default new FavoritesService();