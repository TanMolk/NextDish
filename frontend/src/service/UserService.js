import BaseService from "@/service/BaseService";
import {ElNotification} from "element-plus";

class UserService extends BaseService {

    login(email, password) {
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

    signIn(email, password, code) {
        return this.$http({
            method: "post",
            url: "/user/sign-in",
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

    resetPassword(email, password, code) {
        return this.$http.get("/user/reset-password",
            {
                params: {
                    email, password, code
                }
            })
    }

    sendVerifyCode(email) {
        return this.$http.get("/user/send-verify-code",
            {
                params: {email}
            });
    }

    verifyCode(email, code) {
        return this.$http.get("/user/verify-code",
            {
                params: {email, code}
            });
    }

    changeName(name) {
        return this.$http({
            method: "post",
            url: "/user/change-name",
            params: {
                name
            }
        });
    }

    exist(email) {
        return this.$http.get("/user/exist",
            {
                params: {email}
            });
    }

    freshUserData() {
        return this.$http.get("/user");
    }
}


export default new UserService();