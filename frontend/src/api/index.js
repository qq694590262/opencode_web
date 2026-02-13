const API_BASE = 'http://localhost:8080/api'

async function request(url, options = {}) {
  const defaultOptions = {
    credentials: 'include',
    headers: {
      'Content-Type': 'application/json',
    },
  }
  
  const response = await fetch(`${API_BASE}${url}`, { ...defaultOptions, ...options })
  const data = await response.json()
  
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
  getAll: () => request('/logs'),
  getRecent: (limit = 10) => request(`/logs/recent?limit=${limit}`),
}

export const wikiApi = {
  getCategories: () => request('/wiki/categories'),
  getCategoryById: (id) => request(`/wiki/categories/${id}`),
  createCategory: (data) => request('/wiki/categories', { method: 'POST', body: JSON.stringify(data) }),
  updateCategory: (id, data) => request(`/wiki/categories/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
  deleteCategory: (id) => request(`/wiki/categories/${id}`, { method: 'DELETE' }),
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
}
