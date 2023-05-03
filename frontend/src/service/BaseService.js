import axios from 'axios';
import Constants from "@/constants/Constants";
import StorageUtil from "@/utils/StorageUtil";

/**
 * base function for communication between frontend and backend
 * @type {axios.AxiosInstance}
 */

let http = axios.create({
    baseURL: Constants.API_GATEWAY
});

http.interceptors.request.use(
    config => {
        config.headers['c8019-client-id'] = Constants.CLIENT_ID;


        let token = StorageUtil.get(Constants.STORAGE_TOKEN);
        if (token) {
            config.headers['c8019-token'] = token;
        }
        return config
    },
    error => {
        console.log(error);
    }
);

export default class BaseService {
    constructor() {
        this.$http = http;
    }
}