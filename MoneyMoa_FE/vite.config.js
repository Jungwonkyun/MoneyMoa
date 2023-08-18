import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  // SCSS 전역 사용
  css: {
    preprocessorOptions: {
      scss: {
        additionalData: '@import "@/assets/scss/variables.scss";'
      }
    }
  }
  // server: {
  //   port: 5173,
  //   open: true,
  //   // 웹소켓용 url 지정
  //   hmr: {
  //     clientPort: 443,
  //     protocol: 'wss'
  //   }
  // }
})
