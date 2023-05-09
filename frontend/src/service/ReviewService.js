import BaseService from "@/service/BaseService";
import UserUtil from "@/utils/UserUtil";

/**
 * This service provides functions to add, remove, get user review to/from backend
 */
class ReviewService extends BaseService {

    /**
     *To add user review to backend
     * @param placeId restaurant placeId
     * @param content review content
     * @returns {Promise<axios.AxiosResponse<any>>} http request
     */
    add(placeId, content) {
        return this.$http.post("/review", {
            placeId,
            content
        }).catch((err) => {
            UserUtil.tokenExpired(err);
        });
    }

    /**
     *To remove user review from database
     * @param id user's id
     * @returns {Promise<axios.AxiosResponse<any>>} http request
     */
    remove(id) {
        return this.$http.delete("/review/" + id)
            .catch((err) => {
                UserUtil.tokenExpired(err);
            });
    }

    /**
     *To get review of the restaurant
     * @param placeId restaurant's placeId
     * @returns {Promise<axios.AxiosResponse<any>>} http request
     */
    get(placeId) {
        return this.$http.get("/review", {
            params: {
                placeId
            }
        }).catch((err) => {
            UserUtil.tokenExpired(err);
        });
    }
}


export default new ReviewService();