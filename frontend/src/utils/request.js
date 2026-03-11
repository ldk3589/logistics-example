import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '../router'
import { getToken, clearAuth } from './auth'

const service = axios.create({
  timeout: 10000
})

service.interceptors.request.use(
  config => {
    const token = getToken()
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => Promise.reject(error)
)

service.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code !== 200) {
      ElMessage.error(res.message || '请求失败')
      return Promise.reject(res)
    }
    return res
  },
  error => {
    const status = error?.response?.status
    if (status === 401) {
      ElMessage.error('未登录或登录已过期')
      clearAuth()
      router.push('/login')
    } else if (status === 403) {
      ElMessage.error('没有权限')
    } else {
      ElMessage.error(error?.response?.data?.message || error.message || '请求失败')
    }
    return Promise.reject(error)
  }
)

export default service