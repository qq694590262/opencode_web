export const MENU = [
  { id: 'dash', label: '仪表盘', route: '/dashboard', icon: 'Odometer', children: [] },
  {
    id: 'analytics', label: '数据分析', icon: 'TrendCharts', children: [
      { id: 'overview', label: '数据概览', route: '/dashboard/overview', icon: 'DataAnalysis', children: [] },
      { id: 'reports', label: '报表中心', route: '/dashboard/reports', icon: 'Document', children: [] },
      { id: 'charts', label: '图表分析', route: '/dashboard/charts', icon: 'PieChart', children: [] }
    ]
  },
  {
    id: 'management', label: '用户管理', icon: 'User', children: [
      { id: 'users', label: '用户列表', route: '/dashboard/users', icon: 'UserFilled', children: [] },
      { id: 'roles', label: '角色权限', route: '/dashboard/roles', icon: 'Key', children: [] },
      { id: 'logs', label: '操作日志', route: '/dashboard/logs', icon: 'List', children: [] }
    ]
  },
  {
    id: 'project', label: '项目管理', icon: 'Folder', children: [
      { id: 'projects', label: '项目列表', route: '/dashboard/projects', icon: 'FolderOpened', children: [] },
      { id: 'tasks', label: '任务管理', route: '/dashboard/tasks', icon: 'Checked', children: [] },
      { id: 'calendar', label: '日程日历', route: '/dashboard/calendar', icon: 'Calendar', children: [] }
    ]
  },
  {
    id: 'docs', label: '文档中心', icon: 'Notebook', children: [
      { id: 'documents', label: '文档列表', route: '/dashboard/documents', icon: 'Document', children: [] },
      { id: 'wiki', label: '知识库', route: '/dashboard/wiki', icon: 'Reading', children: [] }
    ]
  },
  {
    id: 'settings', label: '系统设置', icon: 'Setting', children: [
      { id: 'profile', label: '个人信息', route: '/dashboard/profile', icon: 'Avatar', children: [] },
      { id: 'prefs', label: '偏好设置', route: '/dashboard/prefs', icon: 'Tools', children: [] },
      { id: 'system', label: '系统配置', route: '/dashboard/system', icon: 'Cpu', children: [] }
    ]
  }
]
