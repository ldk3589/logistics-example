<template>
  <div class="layout">
    <aside class="sidebar">
      <h3>权限系统</h3>

      <el-menu
        :default-active="activeMenu"
        router
        background-color="#2d3a4b"
        text-color="#ffffff"
        active-text-color="#ffd04b"
      >
        <el-menu-item index="/dashboard">
          <span>首页</span>
        </el-menu-item>

        <template v-for="menu in renderMenus" :key="menu.id">
          <el-sub-menu
            v-if="menu.children && menu.children.length > 0"
            :index="String(menu.id)"
          >
            <template #title>
              <span>{{ menu.menuName }}</span>
            </template>

            <el-menu-item
              v-for="child in menu.children"
              :key="child.id"
              :index="normalizePath(child.path)"
            >
              <span>{{ child.menuName }}</span>
            </el-menu-item>
          </el-sub-menu>

          <el-menu-item
            v-else
            :index="normalizePath(menu.path)"
          >
            <span>{{ menu.menuName }}</span>
          </el-menu-item>
        </template>
      </el-menu>
    </aside>

    <main class="main">
      <div class="topbar">
        <span>{{ userName }}</span>
        <el-button size="small" type="danger" @click="logout">退出</el-button>
      </div>
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { clearAuth, getMenus, getUserInfo } from '../utils/auth'

const router = useRouter()
const route = useRoute()

const user = getUserInfo()

const userName = computed(() => {
  return user?.username || '未登录'
})

const activeMenu = computed(() => route.path)

function normalizePath(path) {
  if (!path) return '/dashboard'
  return path.startsWith('/') ? path : `/${path}`
}

function filterMenuTree(list) {
  if (!Array.isArray(list)) return []

  return list
    .filter(item => item && item.menuType !== 'BUTTON')
    .map(item => {
      const children = Array.isArray(item.children)
        ? item.children.filter(child => child && child.menuType !== 'BUTTON')
        : []

      return {
        ...item,
        children
      }
    })
    .filter(item => {
      return !!item.path || (item.children && item.children.length > 0)
    })
}

const renderMenus = computed(() => {
  const menus = getMenus()
  const result = filterMenuTree(menus)
  return result
})

function logout() {
  clearAuth()
  router.push('/login')
}
</script>

<style scoped>
.layout {
  display: flex;
  min-height: 100vh;
}
.sidebar {
  width: 240px;
  background: #2d3a4b;
  color: #fff;
  padding: 16px 0;
  box-sizing: border-box;
}
.sidebar h3 {
  margin: 0;
  padding: 0 20px 16px;
}
.main {
  flex: 1;
  padding: 20px;
  box-sizing: border-box;
  background: #f5f7fa;
}
.topbar {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}
:deep(.el-menu) {
  border-right: none;
}
</style>