import BaseService from "@/service/BaseService";

class FeedbackService extends BaseService {

    add(content) {
        return this.$http.post(
            "/feedback",
            {content});
    }

}


export default new FeedbackService();