import {ElMessageBox} from "element-plus";

/**
 * create an alert message to notify user
 * @param title message title
 * @param msg message
 * @param confirmButtonText
 * @param confirmEvent
 */
function alert(title, msg, confirmButtonText, confirmEvent) {
    let option = {
        customClass: "custom-message-box",
        showClose: false,
    };

    if (confirmButtonText) {
        option.confirmButtonText = confirmButtonText;
    }

    ElMessageBox
        .alert(msg, title, option)
        .then(confirmEvent)
}

export default {
    alert,
}