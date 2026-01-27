import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    username: localStorage.getItem('username') || '',
    role: localStorage.getItem('role') || 'USER' // 新增角色存储
  }),

  actions: {
    setUserInfo(data) {
      // 这里的 data 对应后端返回的 Result.data
      this.token = data.token
      this.username = data.username
      this.role = data.role
      
      localStorage.setItem('token', data.token)
      localStorage.setItem('username', data.username)
      localStorage.setItem('role', data.role)
    },

    logout() {
      this.token = ''
      this.username = ''
      this.role = 'USER'
      localStorage.clear()
    }
  }
})