import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import {Dialog, Popup, Overlay} from '@nutui/nutui';

import './assets/main.css'


const app = createApp(App)

//router
app.use(router)

app.mount('#app')
