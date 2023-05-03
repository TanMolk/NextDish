import BaseService from "@/service/BaseService";

class FeedbackService extends BaseService {
    /**
     * send user feedback to backend
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