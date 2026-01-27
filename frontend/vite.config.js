import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5173,
    proxy: {
      // 统一拦截以 /api 开头的请求
      '/api': {
        target: 'http://localhost:8080', 
        changeOrigin: true,
        // 如果后端接口本身没有 /api 前缀，则需要替换掉
        rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  },
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src')
    }
  }
})