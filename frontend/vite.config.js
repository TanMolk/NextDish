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
                        const arr = id.toString().split('node_modules/')[1].split('/')
                        switch (arr[0]) {
                            case 'vue':
                            case '@nutui/nutui':
                            case 'element-plus':
                            case 'vue3-google-map':
                            case 'vue3-infinite-list':
                                return '_' + arr[0]
                            default :
                                return '__vendor'
                        }
                    }
                },
                chunkFileNames: 'static/chunk/[name]-[hash].js',
                entryFileNames: 'static/main/[name]-[hash].js',
                assetFileNames: 'static/[ext]/[name]-[hash].[ext]'
            },
            brotliSize: false,
            target: 'esnext',
            minify: 'esbuild'
        }
    }
});
