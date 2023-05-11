import {ElMessageBox} from "element-plus";

/**
 * This utility provides function to create an alert message to notify user
 * @author Ashton Chow
 */

/**
 * To use an alert message to notify user
 * @param title message title
 * @param msg message
 * @param confirmButtonText confirm button text
 * @param confirmEvent confirm event
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