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
            // route level code-splitting
            // this generates a separate chunk (About.[hash].js) for this route
            // which is lazy-loaded when the route is visited.
            component: () => import('../views/AppPage.vue')
        }
    ]
})

export default router
