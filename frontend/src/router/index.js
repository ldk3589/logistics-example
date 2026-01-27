import { createRouter, createWebHistory } from 'vue-router';
// 导入组件
import Login from '../views/Login.vue';
import Order from '../views/Order.vue';
import OrderStat from "../views/OrderStat.vue"

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta: { requiresAuth: false }
  },
  {
    path: '/orders',
    name: 'Orders',
    component: Order,
    meta: { requiresAuth: true }
  },
  {
    path: '/stats',
    name: 'OrderStat',
    component: OrderStat,
    meta: { requiresAuth: true }
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

// 路由守卫：如果没有 Token，强行拦截到登录页
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token');
  
  if (to.meta.requiresAuth && !token) {
    next('/login');
  } else {
    next();
  }
});

export default router;