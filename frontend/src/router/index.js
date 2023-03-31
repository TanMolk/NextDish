import {createRouter, createWebHistory} from 'vue-router'
import WelcomePage from "@/views/WelcomePage.vue";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'home',
            component: WelcomePage
        },
        {
            path: '/app',
            name: 'app',
            //lazy-loaded
            component: () => import('../views/AppPage.vue')
        }
    ]
})

export default router
