import StorageUtil from "@/utils/StorageUtil";
import Constants from "@/constants/Constants";
import {ElNotification} from "element-plus";

let userDialog;

/**
 * user states associated to different function
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

function hide() {
    userDialog.openState = false;
}

function tokenExpired(err) {
    StorageUtil.remove(Constants.STORAGE_TOKEN);

    console.log(err);
    ElNotification({
        type: "warning",
        message: "Login expired"
    });
    show();
}

function setUserDialog(obj) {
    userDialog = obj;
}

export default {
    show,
    hide,
    tokenExpired,
    setUserDialog
}