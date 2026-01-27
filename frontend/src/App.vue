<template>
  <router-view v-if="$route.name === 'Login'" />

  <el-container v-else class="layout-container">
    <el-aside width="220px">
      <div class="logo">订单管理系统</div>
      <el-menu
        router
        :default-active="$route.path"
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
          <span>数据统计</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header>
        <div class="header-right">
          <el-tag type="success" effect="dark">{{ roleName }}</el-tag>
          <span class="username">{{ username }}</span>
          <el-button type="danger" link @click="handleLogout">退出登录</el-button>
        </div>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { List, PieChart } from '@element-plus/icons-vue'

const router = useRouter()
const username = localStorage.getItem('username')
const role = localStorage.getItem('role')

const roleName = computed(() => {
  if (role === 'ADMIN_L1') return '超级管理员'
  if (role === 'ADMIN_L2') return '二级管理员'
  return '用户'
})

const handleLogout = () => {
  localStorage.clear()
  router.push('/login')
}
</script>

<style scoped>
.layout-container { height: 100vh; }
.el-aside { background-color: #304156; transition: width 0.3s; }
.logo { height: 60px; line-height: 60px; text-align: center; color: #fff; font-weight: bold; font-size: 18px; background: #2b2f3a; }
.el-header { background: #fff; border-bottom: 1px solid #e6e6e6; display: flex; align-items: center; justify-content: flex-end; padding: 0 20px; }
.header-right { display: flex; align-items: center; gap: 15px; }
.username { font-size: 14px; color: #666; }
</style>