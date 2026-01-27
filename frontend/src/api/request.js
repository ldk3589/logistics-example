import axios from 'axios';
import { ElMessage } from 'element-plus';

const request = axios.create({
  baseURL: 'http://localhost:8080', // 请确保与后端端口一致
  timeout: 5000
});

// 请求拦截器
request.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) {
    // 必须匹配后端 JwtAuthenticationFilter 的 "Bearer " 前缀
    config.headers['Authorization'] = `Bearer ${token}`;
  }
  return config;
}, error => Promise.reject(error));

// 响应拦截器
request.interceptors.response.use(response => {
  // 你的接口在成功时直接返回 String (如"注册成功") 或对象
  return response.data;
}, error => {
  // 后端抛出的 RuntimeException 会进入这里
  const message = error.response?.data || '服务器异常';
  ElMessage.error(message);
  return Promise.reject(error);
});

export default request;