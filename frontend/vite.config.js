import {fileURLToPath, URL} from 'node:url'

import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import Components from 'unplugin-vue-components/vite'
import NutUIResolver from '@nutui/nutui/dist/resolver'

// https://vitejs.dev/config/
export default defineConfig({
    server: {
        //server port
        port: 5173,
        strictPort: true,
        //only work in development, avoid cross-domain; After building, it doesn't work
        proxy: {
            '/map-api': {
                target: 'https://maps.googleapis.com/maps/api',
                changeOrigin: true,
                rewrite: (path) => path.replace(/^\/map-api/, '')
            }
        }
    },
    base: "./",
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
    // output setting
    build: {
        chunkSizeWarningLimit: 1500,
        rollupOptions: {
            output: {
                manualChunks(id) {
                    if (id.includes('node_modules')) {
                        return id.toString().split('node_modules/')[1].split('/')[0].toString();
                    }
                }
            }
        }
    }
});
