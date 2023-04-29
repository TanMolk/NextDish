import {createApp} from "vue";
import {ElButton, ElDialog, ElInput, ElStep, ElSteps} from "element-plus";
import UserModal from "@/components/UserModal.vue";


const mountNode = document.createElement('div')
const userDialog = createApp(UserModal)
userDialog.use(ElDialog)
userDialog.use(ElInput)
userDialog.use(ElButton)
userDialog.use(ElSteps)
userDialog.use(ElStep)
userDialog.mount(mountNode)

function show() {
    userDialog._instance.data.currentStep = 0;
    userDialog._instance.data.titleIndex = 0;
    userDialog._instance.data.email = "";
    userDialog._instance.data.password = "";
    userDialog._instance.data.verifyCode = "";
    userDialog._instance.data.passwordInputShow = true;
    userDialog._instance.data.verifyCodeInputShow = false;
    userDialog._instance.data.openState = true;
}

function hide() {
    userDialog._instance.data.openState = false;
}

export default {
    show,
    hide
}