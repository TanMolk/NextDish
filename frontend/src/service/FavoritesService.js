import BaseService from "@/service/BaseService";
import UserUtil from "@/utils/UserUtil";

/**
 * add, remove, get user favorites from backend
 */

class FavoritesService extends BaseService {
    add(placeId) {
        return this.$http.post("/favorite", {
            placeId,
        }).catch((err) => {
            UserUtil.tokenExpired(err);
        });
    }

    remove(id) {
        return this.$http.delete("/favorite/" + id)
            .catch((err) => {
                UserUtil.tokenExpired(err);
            });
    }

    get() {
        return this.$http.get("/favorite")
            .catch((err) => {
                UserUtil.tokenExpired(err);
            });
    }
}


export default new FavoritesService();