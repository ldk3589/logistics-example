import { createApp } from 'vue'
import App from './App.vue'
import router from './router' // 这里的 router/index.js 内部会用到 store
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'


const app = createApp(App)
const pinia = createPinia()

app.use(pinia)      // 第一步：先挂载 Pinia
app.use(router)     // 第二步：再挂载 Router（此时守卫可以安全访问 Pinia）
app.use(ElementPlus)
app.mount('#app')
