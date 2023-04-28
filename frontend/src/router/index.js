import {createRouter, createWebHistory} from 'vue-router'
import WelcomePage from "@/views/WelcomePage.vue";
import SharePage from "@/views/SharePage.vue";

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
        },
        {
            path: '/share',
            name: 'share',
            component: SharePage
        }
    ]
})

export default router
