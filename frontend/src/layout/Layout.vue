<template>
  <el-container class="layout-wrapper">
    <el-aside width="220px" class="aside-menu">
      <div class="logo">订单管理系统</div>
      <el-menu
        :default-active="$route.path"
        router
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
      >
        <el-menu-item index="/orders">
          <el-icon><List /></el-icon>
          <span>订单管理</span>
        </el-menu-item>
        <el-menu-item index="/stats">
          <el-icon><PieChart /></el-icon>
          <span>业务统计</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="nav-header">
        <div class="header-left">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item>首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{ $route.name }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
          <el-dropdown @command="handleCommand">
            <span class="user-info">
              {{ userStore.username }} ({{ userStore.role }})
              <el-icon><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <el-main class="main-content">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
      <el-menu
  :default-active="$route.path"
  router
>
  <el-menu-item
    v-for="item in menus"
    :key="item.path"
    :index="item.path"
  >
    <el-icon>
      <component :is="icons[item.icon]" />
    </el-icon>
    <span>{{ item.title }}</span>
  </el-menu-item>
</el-menu>

    </el-container>
  </el-container>
</template>

<script setup>
import { useUserStore } from '@/store/user'
import { useRouter } from 'vue-router'
import { List, PieChart, ArrowDown } from '@element-plus/icons-vue'
import { computed } from 'vue'
import { menuMap } from '@/config/menu'
import * as icons from '@element-plus/icons-vue'

const userStore = useUserStore()

const menus = computed(() => menuMap[userStore.role] || [])
const router = useRouter()

const handleCommand = (command) => {
  if (command === 'logout') {
    userStore.logout()
    router.push('/login')
  }
}
</script>

<style scoped>
.layout-wrapper { height: 100vh; }
.aside-menu { background-color: #304156; transition: width 0.3s; }
.logo { height: 60px; line-height: 60px; color: #fff; text-align: center; font-weight: bold; font-size: 18px; background: #2b2f3a; }
.nav-header { background: #fff; border-bottom: 1px solid #e6e6e6; display: flex; justify-content: space-between; align-items: center; padding: 0 20px; }
.user-info { cursor: pointer; color: #409EFF; font-weight: 500; }
.main-content { background: #f0f2f5; padding: 20px; }
/* 页面切换动画 */
.fade-enter-active, .fade-leave-active { transition: opacity 0.2s ease; }
.fade-enter-from, .fade-leave-to { opacity: 0; }
</style>