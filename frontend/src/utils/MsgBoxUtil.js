import {ElMessageBox} from "element-plus";

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