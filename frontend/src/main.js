import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus, {ElMessage} from 'element-plus'
import VueTour from 'v3-tour'
import VideoBackground from 'vue-responsive-video-background-player'

import 'element-plus/dist/index.css'
import 'v3-tour/dist/vue-tour.css'
import './assets/main.css'
import ClientVersionUtil from "@/utils/ClientVersionUtil";


const app = createApp(App)

//component
app.use(router)
app.use(ElementPlus)
app.use(VueTour)
app.component('video-background', VideoBackground);

// for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
//     app.component(key, component)
// }

//global error handle
app.config.errorHandler = (err, vm, info) => {
    console.log(err);
    console.log(vm);
    console.log(info);

    let msg = ClientVersionUtil.isMobile()
        ? "Server busy."
        : "Server busy. Please wait for a moment or refresh current page.";

    ElMessage({
        message: msg,
        type: 'warning',
    })
}

app.mount('#app')
