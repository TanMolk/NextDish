import {fileURLToPath, URL} from 'node:url'

import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import Components from 'unplugin-vue-components/vite'
import NutUIResolver from '@nutui/nutui/dist/resolver'

// https://vitejs.dev/config/
export default defineConfig({
    plugins: [
        vue(),
        // import un-plugin auto import
        Components({
            resolvers: [NutUIResolver()],
        }),
    ],
    resolve: {
        alias: {
            '@': fileURLToPath(new URL('./src', import.meta.url))
        }
    },
    css: {
        preprocessorOptions: {
            scss: {
                additionalData: `@import "@nutui/nutui/dist/styles/variables.scss";`
            }
        }
    },
    base: "./",
    server: {
        proxy: {
            '/map-api': {
                target: 'https://maps.googleapis.com/maps/api',
                changeOrigin: true,
                rewrite: (path) => path.replace(/^\/map-api/, '')
            }
        }
    }
})
