const API_BASE = 'http://localhost:8080/api'

// 跳转登录页面的函数
function redirectToLogin() {
  // 清除本地存储的用户信息
  localStorage.removeItem('userInfo')
  localStorage.removeItem('token')
  // 跳转到登录页面
  window.location.href = '#/login'
  // 如果当前不是登录页，则刷新页面
  if (!window.location.hash.includes('/login')) {
    window.location.reload()
  }
}

async function request(url, options = {}) {
  const defaultOptions = {
    credentials: 'include',
    headers: {
      'Content-Type': 'application/json',
    },
  }
  
  const response = await fetch(`${API_BASE}${url}`, { ...defaultOptions, ...options })
  
  // 处理401未授权响应
  if (response.status === 401) {
    const data = await response.json()
    console.warn('登录已过期，请重新登录')
    redirectToLogin()
    throw new Error(data.message || '请重新登录')
  }
  
  const data = await response.json()
  
  // 处理业务层面的未登录错误码
  if (data.code === 401) {
    console.warn('登录已过期，请重新登录')
    redirectToLogin()
    throw new Error(data.message || '请重新登录')
  }
  
  if (!response.ok) {
    throw new Error(data.message || '请求失败')
  }
  
  return data
}

export const authApi = {
  login: (username, password) => 
    request('/auth/login', {
      method: 'POST',
      body: JSON.stringify({ username, password }),
    }),
  
  logout: () => 
    request('/auth/logout', { method: 'POST' }),
  
  getInfo: () => 
    request('/auth/info'),
  
  changePassword: (oldPassword, newPassword) => 
    request('/auth/password', {
      method: 'POST',
      body: JSON.stringify({ oldPassword, newPassword }),
    }),
}

export const userApi = {
  getAll: () => request('/users'),
  getById: (id) => request(`/users/${id}`),
  create: (data) => request('/users', { method: 'POST', body: JSON.stringify(data) }),
  update: (id, data) => request(`/users/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
  delete: (id) => request(`/users/${id}`, { method: 'DELETE' }),
}

export const roleApi = {
  getAll: () => request('/roles'),
  getById: (id) => request(`/roles/${id}`),
  create: (data) => request('/roles', { method: 'POST', body: JSON.stringify(data) }),
  update: (id, data) => request(`/roles/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
  delete: (id) => request(`/roles/${id}`, { method: 'DELETE' }),
}

export const projectApi = {
  getAll: () => request('/projects'),
  getById: (id) => request(`/projects/${id}`),
  create: (data) => request('/projects', { method: 'POST', body: JSON.stringify(data) }),
  update: (id, data) => request(`/projects/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
  delete: (id) => request(`/projects/${id}`, { method: 'DELETE' }),
}

export const taskApi = {
  getAll: () => request('/tasks'),
  getByStatus: (status) => request(`/tasks/status/${status}`),
  getById: (id) => request(`/tasks/${id}`),
  create: (data) => request('/tasks', { method: 'POST', body: JSON.stringify(data) }),
  update: (id, data) => request(`/tasks/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
  delete: (id) => request(`/tasks/${id}`, { method: 'DELETE' }),
}

export const documentApi = {
  getAll: () => request('/documents'),
  getById: (id) => request(`/documents/${id}`),
  create: (data) => request('/documents', { method: 'POST', body: JSON.stringify(data) }),
  update: (id, data) => request(`/documents/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
  delete: (id) => request(`/documents/${id}`, { method: 'DELETE' }),
}

export const logApi = {
  getAll: () => request('/logs/all'),
  getRecent: (limit = 10) => request(`/logs/recent?limit=${limit}`),
  getPage: (pageNum = 1, pageSize = 10, params = {}) => {
    const queryParams = new URLSearchParams({
      pageNum,
      pageSize,
      ...params
    }).toString()
    return request(`/logs?${queryParams}`)
  },
  search: (keyword) => request(`/logs/search?keyword=${encodeURIComponent(keyword)}`),
  getStatistics: () => request('/logs/statistics'),
  deleteLogs: (ids) => request(`/logs/${ids.join(',')}`, { method: 'DELETE' }),
  clearLogs: () => request('/logs/clear', { method: 'DELETE' }),
}

export const wikiApi = {
  getCategories: () => request('/wiki/categories'),
  getCategoryById: (id) => request(`/wiki/categories/${id}`),
  createCategory: (data) => request('/wiki/categories', { method: 'POST', body: JSON.stringify(data) }),
  updateCategory: (id, data) => request(`/wiki/categories/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
  deleteCategory: (id) => request(`/wiki/categories/${id}`, { method: 'DELETE' }),
  // 文章相关API
  getArticles: () => request('/wiki/articles'),
  getArticleById: (id) => request(`/wiki/articles/${id}`),
  createArticle: (data) => request('/wiki/articles', { method: 'POST', body: JSON.stringify(data) }),
  updateArticle: (id, data) => request(`/wiki/articles/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
  deleteArticle: (id) => request(`/wiki/articles/${id}`, { method: 'DELETE' }),
}

export const settingsApi = {
  get: () => request('/settings'),
  update: (data) => request('/settings', { method: 'PUT', body: JSON.stringify(data) }),
}

export const dashboardApi = {
  getOverview: () => request('/dashboard/overview'),
  getActivities: () => request('/dashboard/activities'),
  getQuickLinks: () => request('/dashboard/quick-links'),
  getCharts: () => request('/dashboard/charts'),
  getSystemInfo: () => request('/dashboard/system-info'),
}

export const calendarApi = {
  getAll: () => request('/calendar'),
  getByUser: () => request('/calendar/user'),
  getById: (id) => request(`/calendar/${id}`),
  create: (data) => request('/calendar', { method: 'POST', body: JSON.stringify(data) }),
  update: (id, data) => request(`/calendar/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
  delete: (id) => request(`/calendar/${id}`, { method: 'DELETE' }),
}

export const reportApi = {
  getAll: () => request('/reports'),
  getById: (id) => request(`/reports/${id}`),
  create: (data) => request('/reports', { method: 'POST', body: JSON.stringify(data) }),
  update: (id, data) => request(`/reports/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
  delete: (id) => request(`/reports/${id}`, { method: 'DELETE' }),
}

export const uploadApi = {
  uploadImage: (file) => {
    const formData = new FormData()
    formData.append('file', file)
    return request('/upload/image', {
      method: 'POST',
      body: formData,
      headers: {} // 让浏览器自动设置 Content-Type
    })
  },
  uploadDocument: (file) => {
    const formData = new FormData()
    formData.append('file', file)
    return request('/upload/document', {
      method: 'POST',
      body: formData,
      headers: {}
    })
  },
  uploadAvatar: (file) => {
    const formData = new FormData()
    formData.append('file', file)
    return request('/upload/avatar', {
      method: 'POST',
      body: formData,
      headers: {}
    })
  },
}

export const systemApi = {
  getConfig: () => request('/system/config'),
  saveConfig: (data) => request('/system/config', { method: 'PUT', body: JSON.stringify(data) }),
}

export const departmentApi = {
  getAll: () => request('/departments'),
  getTree: () => request('/departments/tree'),
  getById: (id) => request(`/departments/${id}`),
  create: (data) => request('/departments', { method: 'POST', body: JSON.stringify(data) }),
  update: (id, data) => request(`/departments/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
  delete: (id) => request(`/departments/${id}`, { method: 'DELETE' }),
}
