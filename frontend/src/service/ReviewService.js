import BaseService from "@/service/BaseService";
import UserUtil from "@/utils/UserUtil";

/**
 * add, remove, get user review to/from backend
 */
class ReviewService extends BaseService {
    add(placeId, content) {
        return this.$http.post("/review", {
            placeId,
            content
        }).catch((err) => {
            UserUtil.tokenExpired(err);
        });
    }

    remove(id) {
        return this.$http.delete("/review/" + id)
            .catch((err) => {
                UserUtil.tokenExpired(err);
            });
    }

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