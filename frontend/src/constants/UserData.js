import UserService from "@/service/UserService";

class UserData {

    reviews
    favorites
    nickname

    /**
     * get user data from backend
     * @returns {*} user's own reviews, favorites, nickname
     */
    freshUserData() {
        return UserService.freshUserData()
            .then(resp => {
                if (resp) {
                    let data = resp.data;
                    this.reviews = data.reviews;
                    this.favorites = data.favorites;
                    this.nickname = data.nickname;
                }
            });

    }

    /**
     * check if the review can be removed
     * @param id user's own id
     * @returns {boolean} true = remove successful, false = fail
     */
    ifReviewCanRemove(id) {
        if (this.reviews) {
            for (const review of this.reviews) {
                if (id === review.id) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * check if the place is favourited by user
     * @param placeId placeId of the restaurant
     * @returns {boolean} true = is favourited, false = not
     */
    isFavorite(placeId) {
        if (this.favorites) {
            for (const favorite of this.favorites) {
                if (placeId === favorite.placeId) {
                    return true;
                }
            }
        }
        return false;
    }
}

export default new UserData();