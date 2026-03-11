<template>
  <div class="dashboard">
    <el-row :gutter="16">
      <el-col :span="24">
        <el-card class="welcome-card">
          <h2>欢迎进入后台管理系统</h2>
          <p>当前用户：{{ userInfo?.username || '未登录' }}</p>
          <p>当前昵称：{{ userInfo?.nickname || '-' }}</p>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" style="margin-top: 16px;">
      <el-col :span="8">
        <el-card>
          <div class="stat-title">权限数量</div>
          <div class="stat-value">{{ permissions.length }}</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div class="stat-title">菜单数量</div>
          <div class="stat-value">{{ menus.length }}</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div class="stat-title">登录状态</div>
          <div class="stat-value">正常</div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" style="margin-top: 16px;">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>快捷入口</span>
          </template>
          <div class="quick-links">
            <router-link to="/users">用户管理</router-link>
            <router-link to="/roles">角色管理</router-link>
            <router-link to="/permissions">权限管理</router-link>
            <router-link to="/menus">菜单管理</router-link>
            <router-link to="/orders">订单管理</router-link>
          </div>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card>
          <template #header>
            <span>系统说明</span>
          </template>
          <ul class="desc-list">
            <li>本系统基于 RBAC 权限模型。</li>
            <li>用户通过角色获得权限和菜单。</li>
            <li>订单模块支持数据权限控制。</li>
            <li>系统支持登录日志和操作日志审计。</li>
          </ul>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { getMenus, getPermissions, getUserInfo } from '../utils/auth'

const userInfo = getUserInfo()

const permissions = computed(() => getPermissions() || [])
const menus = computed(() => getMenus() || [])
</script>

<style scoped>
.dashboard {
  min-height: 100%;
}
.welcome-card h2 {
  margin: 0 0 12px 0;
}
.welcome-card p {
  margin: 6px 0;
}
.stat-title {
  color: #666;
  margin-bottom: 12px;
}
.stat-value {
  font-size: 28px;
  font-weight: bold;
}
.quick-links {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
}
.quick-links a {
  color: #409eff;
  text-decoration: none;
}
.desc-list {
  padding-left: 18px;
  margin: 0;
}
.desc-list li {
  margin-bottom: 10px;
}
</style>