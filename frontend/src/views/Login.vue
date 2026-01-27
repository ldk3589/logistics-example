<template>
  <div class="auth-container">
    <el-card class="auth-card">
      <template #header>
        <h2 class="title">{{ isLogin ? '系统登录' : '用户注册' }}</h2>
      </template>

      <el-form :model="authForm" label-position="top">
        <!-- 用户名 -->
        <el-form-item label="用户名">
          <el-input
            v-model="authForm.username"
            placeholder="请输入用户名"
          />
        </el-form-item>

        <!-- 密码 -->
        <el-form-item label="密码">
          <el-input
            v-model="authForm.password"
            type="password"
            show-password
            placeholder="请输入密码"
          />
        </el-form-item>

        <!-- 注册：确认密码 -->
        <el-form-item v-if="!isLogin" label="确认密码">
          <el-input
            v-model="authForm.confirmPassword"
            type="password"
            show-password
            placeholder="请再次输入密码"
          />
        </el-form-item>

        <!-- 注册：管理员密码（非必填） -->
        <el-form-item v-if="!isLogin" label="管理员密码">
          <el-input
            v-model="authForm.adminPassword"
            type="password"
            show-password
            placeholder="不填写则注册为普通用户"
          />
        </el-form-item>

        <el-button
          type="primary"
          class="submit-btn"
          @click="handleAction"
        >
          {{ isLogin ? '立即登录' : '提交注册' }}
        </el-button>

        <div class="footer-links">
          <el-link type="primary" @click="toggleMode">
            {{ isLogin ? '没有账号？去注册' : '已有账号？去登录' }}
          </el-link>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { loginApi, registerApi } from '@/api/auth'

const router = useRouter()
const isLogin = ref(true)

const authForm = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  adminPassword: ''
})

const hasInnerSpace = (str) => /\s/.test(str)

const toggleMode = () => {
  isLogin.value = !isLogin.value
  // 切换时清空表单，防止状态污染
  Object.assign(authForm, {
    username: '',
    password: '',
    confirmPassword: '',
    adminPassword: ''
  })
}

const handleAction = async () => {
  // 自动 trim 首尾空格
  authForm.username = authForm.username.trim()
  authForm.password = authForm.password.trim()
  authForm.confirmPassword = authForm.confirmPassword.trim()
  authForm.adminPassword = authForm.adminPassword.trim()

  if (!authForm.username || !authForm.password) {
    return ElMessage.warning('用户名和密码不能为空')
  }

  if (hasInnerSpace(authForm.username)) {
    return ElMessage.warning('用户名中间不能包含空格')
  }

  if (hasInnerSpace(authForm.password)) {
    return ElMessage.warning('密码中间不能包含空格')
  }

  try {
    if (isLogin.value) {
      // 登录
      const token = await loginApi({
        username: authForm.username,
        password: authForm.password
      })
      localStorage.setItem('token', token)
      ElMessage.success('登录成功')
      router.push('/orders')
    } else {
      // 注册逻辑
      if (authForm.password !== authForm.confirmPassword) {
        return ElMessage.warning('两次输入的密码不一致')
      }

      await registerApi({
        username: authForm.username,
        password: authForm.password,
        adminPassword:authForm.adminPassword
      })

      ElMessage.success('注册成功，请登录')
      toggleMode()
    }
  } catch (err) {
    // 错误已由 axios 拦截器统一处理
  }
}
</script>

<style scoped>
.auth-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}
.auth-card {
  width: 400px;
  border-radius: 12px;
}
.title {
  text-align: center;
  margin: 0;
  color: #409eff;
}
.submit-btn {
  width: 100%;
  margin-top: 10px;
}
.footer-links {
  margin-top: 15px;
  text-align: center;
}
</style>
