<template>
  <el-card class="register-card">
    <h2 style="text-align: center">用户注册</h2>

    <el-form :model="form" label-width="110px">
      <!-- 用户名 -->
      <el-form-item label="用户名">
        <el-input v-model="form.username" />
      </el-form-item>

      <!-- 密码 -->
      <el-form-item label="密码">
        <el-input
          v-model="form.password"
          type="password"
          show-password
        />
      </el-form-item>

      <!-- 确认密码 -->
      <el-form-item label="确认密码">
        <el-input
          v-model="form.confirmPassword"
          type="password"
          show-password
        />
      </el-form-item>

      <!-- 管理员密码（非必填） -->
      <el-form-item label="管理员密码">
        <el-input
          v-model="form.adminPassword"
          type="password"
          show-password
          placeholder="不填写则注册为普通用户"
        />
      </el-form-item>

      <el-form-item>
        <el-button
          type="primary"
          style="width: 100%"
          @click="handleRegister"
        >
          注册
        </el-button>
      </el-form-item>

      <el-form-item>
        <el-button link style="width: 100%" @click="toLogin">
          返回登录
        </el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script setup>
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { register } from '@/api/auth'

const router = useRouter()


const ADMIN1_PASSWORD = 'admin1-123456'
const ADMIN2_PASSWORD = 'admin2-123456'

const form = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  adminPassword: ''
})

/**
 * 检查是否包含“中间空格”
 */
const hasInnerSpace = (str) => /\s/.test(str)

const handleRegister = async () => {
  // 自动清理首尾空格
  form.username = form.username.trim()
  form.password = form.password.trim()
  form.confirmPassword = form.confirmPassword.trim()
  form.adminPassword = form.adminPassword.trim()

  if (!form.username || !form.password) {
    ElMessage.warning('用户名和密码不能为空')
    return
  }

  if (hasInnerSpace(form.username)) {
    ElMessage.warning('用户名中间不能包含空格')
    return
  }

  if (hasInnerSpace(form.password)) {
    ElMessage.warning('密码中间不能包含空格')
    return
  }

  if (form.password !== form.confirmPassword) {
    ElMessage.warning('两次输入的密码不一致')
    return
  }

  // 根据管理员密码决定角色
  let role = 'USER'

  if (form.adminPassword) {
    if (form.adminPassword === ADMIN1_PASSWORD) {
      role = 'ADMIN1'
    } else if (form.adminPassword === ADMIN2_PASSWORD) {
      role = 'ADMIN2'
    } else {
      ElMessage.error('管理员密码错误')
      return
    }
  }

  try {
    await register({
      username: form.username,
      password: form.password,
      role
    })
    ElMessage.success('注册成功，请登录')
    router.push('/login')
  } catch (e) {
    // 统一由 axios 拦截器处理
  }
}

const toLogin = () => {
  router.push('/login')
}
</script>

<style scoped>
.register-card {
  width: 450px;
  margin: 100px auto;
}
</style>
