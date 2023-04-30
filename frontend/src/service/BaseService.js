import axios from 'axios';
import Constants from "@/constants/Constants";

let http = axios.create({
    baseURL: Constants.API_GATEWAY,
    headers: {
        "c8019-client-id": Constants.CLIENT_ID
    }
});

export default class BaseService {
    constructor() {
        this.$http = http;
    }
}