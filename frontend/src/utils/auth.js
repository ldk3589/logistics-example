const TOKEN_KEY = 'TOKEN'
const USER_KEY = 'USER_INFO'
const PERMISSIONS_KEY = 'PERMISSIONS'
const MENUS_KEY = 'MENUS'

export function getToken() {
  return localStorage.getItem(TOKEN_KEY) || ''
}

export function setToken(token) {
  localStorage.setItem(TOKEN_KEY, token)
}

export function removeToken() {
  localStorage.removeItem(TOKEN_KEY)
}

export function getUserInfo() {
  const raw = localStorage.getItem(USER_KEY)
  return raw ? JSON.parse(raw) : null
}

export function setUserInfo(user) {
  localStorage.setItem(USER_KEY, JSON.stringify(user))
}

export function setPermissions(list) {
  localStorage.setItem(PERMISSIONS_KEY, JSON.stringify(list || []))
}

export function getPermissions() {
  const raw = localStorage.getItem(PERMISSIONS_KEY)
  return raw ? JSON.parse(raw) : []
}

export function setMenus(list) {
  localStorage.setItem(MENUS_KEY, JSON.stringify(list || []))
}

export function getMenus() {
  const raw = localStorage.getItem(MENUS_KEY)
  return raw ? JSON.parse(raw) : []
}

export function hasPermission(code) {
  return getPermissions().includes(code)
}

export function removeUserInfo() {
  localStorage.removeItem(USER_KEY)
}

export function clearAuth() {
  removeToken()
  removeUserInfo()
  localStorage.removeItem(PERMISSIONS_KEY)
  localStorage.removeItem(MENUS_KEY)
}