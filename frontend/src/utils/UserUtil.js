import StorageUtil from "@/utils/StorageUtil";
import Constants from "@/constants/Constants";
import {ElNotification} from "element-plus";

let userDialog;

/**
 * This utility provides functions for userDialog
 * @author Wei Tan
 */

/**
 * To show userDialog
 */
function show() {
    userDialog.currentStep = 0;
    userDialog.titleIndex = 0;
    userDialog.formData.email = "";
    userDialog.formData.password = "";
    userDialog.formData.verifyCode = "";
    userDialog.passwordInputShow = true;
    userDialog.verifyCodeInputShow = false;
    userDialog.buttonLoading = false;
    userDialog.openState = true;
}

/**
 * To hide userDialog
 */
function hide() {
    userDialog.openState = false;
}

/**
 * To handle token expired
 * @param err
 */
function tokenExpired(err) {
    StorageUtil.remove(Constants.STORAGE_TOKEN);

    console.log(err);
    ElNotification({
        type: "warning",
        message: "Login expired"
    });
    show();
}

/**
 * To setUserDialog
 * @param obj
 */
function setUserDialog(obj) {
    userDialog = obj;
}

export default {
    show,
    hide,
    tokenExpired,
    setUserDialog
}