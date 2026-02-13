# 前后端功能完整对照表

## ✅ 已完整实现的功能

### 1. 用户认证 (Auth)
| 功能 | 前端 | 后端API | 数据库 |
|------|------|---------|--------|
| 登录 | Login.vue | POST /api/auth/login | sys_user |
| 登出 | Login.vue/Dashboard.vue | POST /api/auth/logout | - |
| 获取用户信息 | Dashboard.vue | GET /api/auth/info | sys_user |

### 2. 用户管理 (Users)
| 功能 | 前端 | 后端API | 数据库 |
|------|------|---------|--------|
| 用户列表 | Users.vue | GET /api/users | sys_user |
| 新增用户 | Users.vue | POST /api/users | sys_user |
| 编辑用户 | Users.vue | PUT /api/users/{id} | sys_user |
| 删除用户 | Users.vue | DELETE /api/users/{id} | sys_user |
| 搜索用户 | Users.vue | 前端过滤 | - |

### 3. 角色管理 (Roles)
| 功能 | 前端 | 后端API | 数据库 |
|------|------|---------|--------|
| 角色列表 | Roles.vue | GET /api/roles | sys_role |
| 新增角色 | Roles.vue | POST /api/roles | sys_role |
| 编辑角色 | Roles.vue | PUT /api/roles/{id} | sys_role |
| 删除角色 | Roles.vue | DELETE /api/roles/{id} | sys_role |

### 4. 项目管理 (Projects)
| 功能 | 前端 | 后端API | 数据库 |
|------|------|---------|--------|
| 项目列表 | Projects.vue | GET /api/projects | sys_project |
| 新增项目 | Projects.vue | POST /api/projects | sys_project |
| 编辑项目 | Projects.vue | PUT /api/projects/{id} | sys_project |
| 删除项目 | Projects.vue | DELETE /api/projects/{id} | sys_project |

### 5. 任务管理 (Tasks)
| 功能 | 前端 | 后端API | 数据库 |
|------|------|---------|--------|
| 任务列表 | Tasks.vue | GET /api/tasks | sys_task |
| 按状态筛选 | Tasks.vue | GET /api/tasks/status/{status} | sys_task |
| 新增任务 | Tasks.vue | POST /api/tasks | sys_task |
| 编辑任务 | Tasks.vue | PUT /api/tasks/{id} | sys_task |
| 删除任务 | Tasks.vue | DELETE /api/tasks/{id} | sys_task |

### 6. 文档管理 (Documents)
| 功能 | 前端 | 后端API | 数据库 |
|------|------|---------|--------|
| 文档列表 | Documents.vue | GET /api/documents | sys_document |
| 新增文档 | Documents.vue | POST /api/documents | sys_document |
| 编辑文档 | Documents.vue | PUT /api/documents/{id} | sys_document |
| 删除文档 | Documents.vue | DELETE /api/documents/{id} | sys_document |

### 7. 日志管理 (Logs)
| 功能 | 前端 | 后端API | 数据库 |
|------|------|---------|--------|
| 日志列表 | Logs.vue | GET /api/logs | sys_log |
| 最近日志 | Logs.vue | GET /api/logs/recent | sys_log |

### 8. 知识库 (Wiki)
| 功能 | 前端 | 后端API | 数据库 |
|------|------|---------|--------|
| 分类列表 | Wiki.vue | GET /api/wiki/categories | sys_wiki_category |
| 新增分类 | - | POST /api/wiki/categories | sys_wiki_category |
| 编辑分类 | - | PUT /api/wiki/categories/{id} | sys_wiki_category |
| 删除分类 | - | DELETE /api/wiki/categories/{id} | sys_wiki_category |

### 9. 系统设置 (Settings)
| 功能 | 前端 | 后端API | 数据库 |
|------|------|---------|--------|
| 获取设置 | Prefs.vue/Profile.vue | GET /api/settings | sys_settings |
| 更新设置 | Prefs.vue/Profile.vue | PUT /api/settings | sys_settings |

### 10. 日程管理 (Calendar)
| 功能 | 前端 | 后端API | 数据库 |
|------|------|---------|--------|
| 日程列表 | Calendar.vue | GET /api/calendar | sys_calendar |
| 用户日程 | Calendar.vue | GET /api/calendar/user | sys_calendar |
| 新增日程 | - | POST /api/calendar | sys_calendar |
| 编辑日程 | - | PUT /api/calendar/{id} | sys_calendar |
| 删除日程 | - | DELETE /api/calendar/{id} | sys_calendar |

### 11. 报表管理 (Reports)
| 功能 | 前端 | 后端API | 数据库 |
|------|------|---------|--------|
| 报表列表 | Reports.vue | GET /api/reports | sys_report |
| 新增报表 | - | POST /api/reports | sys_report |
| 编辑报表 | - | PUT /api/reports/{id} | sys_report |
| 删除报表 | - | DELETE /api/reports/{id} | sys_report |

### 12. 仪表盘 (Dashboard)
| 功能 | 前端 | 后端API | 数据库 |
|------|------|---------|--------|
| 数据概览 | Overview.vue | GET /api/dashboard/overview | 多表统计 |
| 活动动态 | Dashboard.vue | GET /api/dashboard/activities | sys_log |
| 快捷链接 | Dashboard.vue | GET /api/dashboard/quick-links | 静态数据 |
| 图表数据 | Charts.vue | GET /api/dashboard/charts | 静态数据 |

## 📊 数据库表结构

### 核心表
1. **sys_user** - 用户表
2. **sys_role** - 角色表
3. **sys_project** - 项目表
4. **sys_task** - 任务表
5. **sys_document** - 文档表
6. **sys_log** - 日志表
7. **sys_wiki_category** - 知识库分类表
8. **sys_settings** - 设置表
9. **sys_calendar** - 日程表
10. **sys_report** - 报表表

## 🔌 前端API模块

- `authApi` - 认证相关
- `userApi` - 用户管理
- `roleApi` - 角色管理
- `projectApi` - 项目管理
- `taskApi` - 任务管理
- `documentApi` - 文档管理
- `logApi` - 日志管理
- `wikiApi` - 知识库
- `settingsApi` - 系统设置
- `calendarApi` - 日程管理（需要添加）
- `reportApi` - 报表管理（需要添加）
- `dashboardApi` - 仪表盘

## ⚠️ 需要前端补充的API调用

1. **Calendar.vue** - 需要接入 calendarApi
2. **Reports.vue** - 需要接入 reportApi
3. **Charts.vue** - 可使用 dashboardApi.getCharts()
4. **System.vue** - 纯展示页面，无需API

## 📈 初始数据

数据库初始化脚本已包含：
- 1个管理员用户
- 4个示例用户
- 4个示例角色
- 4个示例项目
- 4个示例任务
- 4个示例文档
- 4条示例日志
- 6个知识库分类
- 3个示例日程
- 6个示例报表
