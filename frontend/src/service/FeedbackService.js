import BaseService from "@/service/BaseService";

/**
 * This service provides function to add user feedback
 */
class FeedbackService extends BaseService {
    /**
     * To add user feedback to backend
     * @param content feedback
     * @returns {Promise<axios.AxiosResponse<any>>}
     */
    add(content) {
        return this.$http.post(
            "/feedback",
            {content});
    }

}


export default new FeedbackService();