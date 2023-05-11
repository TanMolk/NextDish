import BaseService from "@/service/BaseService";
import {ElNotification} from "element-plus";
import md5 from 'js-md5';

/**
 * This service provide functions for various user account related actions
 * @author Ashton Chow
 */
class UserService extends BaseService {

    /**
     * To login to user account
     * @param email user email
     * @param password user password
     * @returns {Promise<axios.AxiosResponse<any>>} http request
     */
    login(email, password) {
        password = md5(password)
        return this.$http.post("/user/login", {
            email,
            password
        })
            .catch((e) => {
                ElNotification({
                    type: 'error',
                    message: 'Login failed',
                })
            });
    }

    /**
     * To sign in user account
     * @param email user email
     * @param password user password
     * @param code verify code
     * @returns {Promise<axios.AxiosResponse<any>>} http request
     */
    signIn(email, password, code) {
        password = md5(password)
        return this.$http({
            method: "post",
            url: "/user/sign-up",
            params: {
                code
            },
            data: {
                email,
                password
            }
        }).catch(() => {
            ElNotification({
                type: 'error',
                message: 'SignIn failed',
            })
        });
    }

    /**
     * To reset user password
     * @param email user email
     * @param password user password
     * @param code verify code
     * @returns {Promise<axios.AxiosResponse<any>>} http request
     */
    resetPassword(email, password, code) {
        password = md5(password)
        return this.$http.get("/user/reset-password",
            {
                params: {
                    email, password, code
                }
            })
    }

    /**
     * To send verify code to user email
     * @param email user email
     * @returns {Promise<axios.AxiosResponse<any>>} http request
     */
    sendVerifyCode(email) {
        return this.$http.get("/user/send-verify-code",
            {
                params: {email}
            });
    }

    /**
     * To verify the code inputted by user
     * @param email user email
     * @param code verify code
     * @returns {Promise<axios.AxiosResponse<any>>} http request
     */
    verifyCode(email, code) {
        return this.$http.get("/user/verify-code",
            {
                params: {email, code}
            });
    }

    /**
     * To change user nickname
     * @param name nickname
     * @returns {Promise<axios.AxiosResponse<any>>} http request
     */
    changeName(name) {
        return this.$http({
            method: "post",
            url: "/user/change-name",
            params: {
                name
            }
        });
    }

    /**
     * To check whether this email existed in database
     * @param email user email
     * @returns {Promise<axios.AxiosResponse<any>>} http request
     */
    exist(email) {
        return this.$http.get("/user/exist",
            {
                params: {email}
            });
    }

    /**
     * To get user data
     * @returns {Promise<axios.AxiosResponse<any>>} http request
     */
    freshUserData() {
        return this.$http.get("/user");
    }
}


export default new UserService();