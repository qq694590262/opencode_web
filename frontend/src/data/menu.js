export const MENU = [
  { id: 'dashboard', label: '工作台', route: '/dashboard/workplace', icon: 'Odometer' },
  
  // 组织架构
  {
    id: 'organization', label: '组织架构', icon: 'OfficeBuilding', children: [
      { id: 'departments', label: '部门管理', route: '/dashboard/departments', icon: 'School' },
      { id: 'users', label: '用户管理', route: '/dashboard/users', icon: 'UserFilled' },
      { id: 'roles', label: '角色权限', route: '/dashboard/roles', icon: 'Key' }
    ]
  },
  
  // 项目任务
  {
    id: 'project', label: '项目任务', icon: 'Folder', children: [
      { id: 'projects', label: '项目管理', route: '/dashboard/projects', icon: 'FolderOpened' },
      { id: 'tasks', label: '任务管理', route: '/dashboard/tasks', icon: 'Checked' },
      { id: 'calendar', label: '日程日历', route: '/dashboard/calendar', icon: 'Calendar' }
    ]
  },
  
  // 知识库
  {
    id: 'docs', label: '知识库', icon: 'Notebook', children: [
      { id: 'documents', label: '文档管理', route: '/dashboard/documents', icon: 'Document' },
      { id: 'wiki', label: '知识库', route: '/dashboard/wiki', icon: 'Reading' }
    ]
  },
  
  // 消息中心
  {
    id: 'message', label: '消息中心', icon: 'Bell', children: [
      { id: 'notices', label: '系统公告', route: '/dashboard/notices', icon: 'BellFilled' },
      { id: 'messages', label: '站内消息', route: '/dashboard/messages', icon: 'Message' }
    ]
  },
  
  // 数据分析
  {
    id: 'analytics', label: '数据分析', icon: 'TrendCharts', children: [
      { id: 'overview', label: '数据概览', route: '/dashboard/overview', icon: 'DataAnalysis' },
      { id: 'reports', label: '报表中心', route: '/dashboard/reports', icon: 'Document' }
    ]
  },
  
  // 系统设置
  {
    id: 'system', label: '系统设置', icon: 'Setting', children: [
      { id: 'profile', label: '个人信息', route: '/dashboard/profile', icon: 'UserFilled' },
      { id: 'system', label: '系统配置', route: '/dashboard/system', icon: 'Tools' },
      { id: 'logs', label: '操作日志', route: '/dashboard/logs', icon: 'List' }
    ]
  }
]
