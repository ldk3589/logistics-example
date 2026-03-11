import { createRouter, createWebHistory } from 'vue-router'
import { getToken } from '../utils/auth'
import Layout from '../layout/Layout.vue'
import Login from '../views/Login.vue'
import Dashboard from '../views/Dashboard.vue'
import UserList from '../views/UserList.vue'
import RoleList from '../views/RoleList.vue'
import PermissionList from '../views/PermissionList.vue'
import MenuList from '../views/MenuList.vue'
import OrderList from '../views/OrderList.vue'
import LoginLogList from '../views/LoginLogList.vue'
import OperationLogList from '../views/OperationLogList.vue'
import DeptList from '../views/DeptList.vue'

const routes = [
  {
    path: '/login',
    component: Login
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      { path: 'dashboard', component: Dashboard },

      { path: 'system/user', component: UserList },
      { path: 'system/role', component: RoleList },
      { path: 'system/permission', component: PermissionList },
      { path: 'system/menu', component: MenuList },
      { path: 'business/order', component: OrderList },
      { path: 'log/login', component: LoginLogList },
      { path: 'log/operation', component: OperationLogList },
      { path: 'system/dept', component: DeptList }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  if (to.path === '/login') {
    next()
    return
  }

  const token = getToken()
  if (!token) {
    next('/login')
    return
  }

  next()
})

export default router