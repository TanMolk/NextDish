import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import VueTour from 'v3-tour'

import 'element-plus/dist/index.css'
import 'v3-tour/dist/vue-tour.css'
import './assets/main.css'


const app = createApp(App)

//router
app.use(router)
app.use(ElementPlus)
app.use(VueTour)

app.mount('#app')
