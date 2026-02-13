export const MENU = [
  { id: 'dash', label: '仪表盘', route: '/dashboard', icon: '/images/image1.png', children: [] },
  {
    id: 'analytics', label: '数据分析', icon: '/images/image2.png', children: [
      { id: 'overview', label: '数据概览', route: '/dashboard/overview', icon: '/images/image3.png', children: [] },
      { id: 'reports', label: '报表中心', route: '/dashboard/reports', icon: '/images/image4.png', children: [] },
      { id: 'charts', label: '图表分析', route: '/dashboard/charts', icon: '/images/image1.png', children: [] }
    ]
  },
  {
    id: 'management', label: '用户管理', icon: '/images/image3.png', children: [
      { id: 'users', label: '用户列表', route: '/dashboard/users', icon: '/images/image4.png', children: [] },
      { id: 'roles', label: '角色权限', route: '/dashboard/roles', icon: '/images/image1.png', children: [] },
      { id: 'logs', label: '操作日志', route: '/dashboard/logs', icon: '/images/image2.png', children: [] }
    ]
  },
  {
    id: 'project', label: '项目管理', icon: '/images/image4.png', children: [
      { id: 'projects', label: '项目列表', route: '/dashboard/projects', icon: '/images/image1.png', children: [] },
      { id: 'tasks', label: '任务管理', route: '/dashboard/tasks', icon: '/images/image2.png', children: [] },
      { id: 'calendar', label: '日程日历', route: '/dashboard/calendar', icon: '/images/image3.png', children: [] }
    ]
  },
  {
    id: 'docs', label: '文档中心', icon: '/images/image1.png', children: [
      { id: 'documents', label: '文档列表', route: '/dashboard/documents', icon: '/images/image2.png', children: [] },
      { id: 'wiki', label: '知识库', route: '/dashboard/wiki', icon: '/images/image3.png', children: [] }
    ]
  },
  {
    id: 'settings', label: '系统设置', icon: '/images/image2.png', children: [
      { id: 'profile', label: '个人信息', route: '/dashboard/profile', icon: '/images/image3.png', children: [] },
      { id: 'prefs', label: '偏好设置', route: '/dashboard/prefs', icon: '/images/image4.png', children: [] },
      { id: 'system', label: '系统配置', route: '/dashboard/system', icon: '/images/image1.png', children: [] }
    ]
  }
]
