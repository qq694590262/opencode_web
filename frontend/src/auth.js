import { authApi } from './api'

export async function login(username, password) {
  try {
    const result = await authApi.login(username, password)
    if (result.code === 200 && result.data) {
      window.localStorage.setItem('token', result.data.token)
      // 保存用户信息
      if (result.data.user) {
        window.localStorage.setItem('userInfo', JSON.stringify(result.data.user))
      }
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
  // 清除所有本地存储
  window.localStorage.removeItem('token')
  window.localStorage.removeItem('userInfo')
  window.localStorage.removeItem('avatarColor')
  // 跳转到登录页
  window.location.href = '#/login'
  window.location.reload()
}

export function getUserInfo() {
  const userInfo = window.localStorage.getItem('userInfo')
  if (userInfo) {
    try {
      return JSON.parse(userInfo)
    } catch (e) {
      return null
    }
  }
  return null
}

export async function fetchUserInfo() {
  try {
    const result = await authApi.getInfo()
    if (result.code === 200 && result.data) {
      window.localStorage.setItem('userInfo', JSON.stringify(result.data))
      return result.data
    }
    return null
  } catch (error) {
    console.error('获取用户信息失败:', error)
    return null
  }
}

export async function changePassword(oldPassword, newPassword) {
  try {
    const result = await authApi.changePassword(oldPassword, newPassword)
    return result
  } catch (error) {
    console.error('修改密码失败:', error)
    return { code: 500, message: '修改密码失败' }
  }
}
