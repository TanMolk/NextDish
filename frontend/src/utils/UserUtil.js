import {createApp} from "vue";
import {ElButton, ElDialog, ElForm, ElFormItem, ElInput, ElNotification, ElStep, ElSteps} from "element-plus";
import UserModal from "@/components/UserModal.vue";
import StorageUtil from "@/utils/StorageUtil";
import Constants from "@/constants/Constants";


const mountNode = document.createElement('div')
const userDialog = createApp(UserModal)
userDialog.use(ElDialog)
userDialog.use(ElInput)
userDialog.use(ElButton)
userDialog.use(ElSteps)
userDialog.use(ElStep)
userDialog.use(ElForm)
userDialog.use(ElFormItem)
userDialog.use(ElNotification)
userDialog.mount(mountNode)

function show() {
    userDialog._instance.data.currentStep = 0;
    userDialog._instance.data.titleIndex = 0;
    userDialog._instance.data.formData.email = "";
    userDialog._instance.data.formData.password = "";
    userDialog._instance.data.formData.verifyCode = "";
    userDialog._instance.data.passwordInputShow = true;
    userDialog._instance.data.verifyCodeInputShow = false;
    userDialog._instance.data.buttonLoading = false;
    userDialog._instance.data.openState = true;
}

function hide() {
    userDialog._instance.data.openState = false;
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

export default {
    show,
    hide,
    tokenExpired
}