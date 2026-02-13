import { authApi } from './api'

export async function login(username, password) {
  try {
    const result = await authApi.login(username, password)
    if (result.code === 200 && result.data) {
      window.localStorage.setItem('token', result.data.token)
      return true
    }
    return false
  } catch (error) {
    console.error('登录失败:', error)
    return false
  }
}

export function isAuthenticated() {
  return !!window.localStorage.getItem('token')
}

export async function logout() {
  try {
    await authApi.logout()
  } catch (error) {
    console.error('登出失败:', error)
  }
  window.localStorage.removeItem('token')
}

export async function getUserInfo() {
  try {
    const result = await authApi.getInfo()
    return result.data
  } catch (error) {
    console.error('获取用户信息失败:', error)
    return null
  }
}
