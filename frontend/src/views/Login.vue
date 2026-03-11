<template>
  <div class="login-page">
    <el-card class="login-card">
      <h2>{{ isRegister ? '用户注册' : '系统登录' }}</h2>

      <el-form :model="form" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" />
        </el-form-item>

        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" show-password />
        </el-form-item>

        <el-form-item v-if="isRegister" label="昵称">
          <el-input v-model="form.nickname" />
        </el-form-item>

        <el-form-item v-if="isRegister" label="真实姓名">
          <el-input v-model="form.realName" />
        </el-form-item>

        <el-form-item v-if="isRegister" label="手机号">
          <el-input v-model="form.phone" />
        </el-form-item>

        <el-form-item v-if="isRegister" label="邮箱">
          <el-input v-model="form.email" />
        </el-form-item>

        <el-form-item v-if="isRegister" label="部门ID">
          <el-input v-model.number="form.deptId" />
        </el-form-item>

        <el-button v-if="!isRegister" type="primary" style="width:100%" @click="handleLogin">
          登录
        </el-button>

        <el-button v-else type="success" style="width:100%" @click="handleRegister">
          注册
        </el-button>

        <el-button text style="margin-top: 12px; width:100%" @click="isRegister = !isRegister">
          {{ isRegister ? '已有账号？去登录' : '没有账号？去注册' }}
        </el-button>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '../utils/request'
import { setToken, setUserInfo, setPermissions, setMenus } from '../utils/auth'

const router = useRouter()
const isRegister = ref(false)

const form = reactive({
  username: '',
  password: '',
  nickname: '',
  realName: '',
  phone: '',
  email: '',
  deptId: 2
})

function toggleMode() {
  isRegister.value = !isRegister.value
}

async function handleLogin() {
  const res = await request.post('/auth/login', {
    username: form.username,
    password: form.password
  })
  const data = res.data
  setToken(data.token)
  setUserInfo(data.userInfo)
  setPermissions(data.permissions || [])
  setMenus(data.menus || [])
  ElMessage.success('登录成功')
  router.push('/dashboard')
}

async function handleRegister() {
  await request.post('/auth/register', {
    username: form.username,
    password: form.password,
    nickname: form.nickname,
    realName: form.realName,
    phone: form.phone,
    email: form.email,
    deptId: form.deptId
  })
  ElMessage.success('注册成功，请登录')
  isRegister.value = false
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #f5f7fa;
}
.login-card {
  width: 460px;
}
</style>