import UserService from "@/service/UserService";

class UserData {

    reviews
    favorites
    nickname

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