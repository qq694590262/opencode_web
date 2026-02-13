# 前后端功能对照表

## 已完整对应的功能 ✅

| 前端页面 | 后端API | 状态 |
|---------|---------|------|
| Login.vue | AuthController | ✅ |
| Users.vue | UserController | ✅ |
| Roles.vue | RoleController | ✅ |
| Projects.vue | ProjectController | ✅ |
| Tasks.vue | TaskController | ✅ |
| Documents.vue | DocumentController | ✅ |
| Logs.vue | LogController | ✅ |
| Wiki.vue | WikiController | ✅ |
| Settings.vue | SettingsController | ✅ |
| Overview.vue | DashboardController.getOverview | ✅ |

## 需要完善的功能 ⚠️

| 前端页面 | 缺失的后端API | 需要的数据 |
|---------|--------------|-----------|
| Dashboard.vue | getActivities, getQuickLinks | 活动列表、快捷链接 |
| System.vue | SystemController | 系统信息、运行状态 |
| Prefs.vue | 需要完善settings表 | 个人偏好设置 |
| Profile.vue | ProfileController 或 UserController扩展 | 个人信息详情 |
| Calendar.vue | CalendarController | 日程、事件 |
| Charts.vue | ChartController 或 DashboardController扩展 | 图表数据 |
| Reports.vue | ReportController | 报表列表 |

## 需要完善的实体和表

1. **sys_settings** - 需要完善字段
2. **sys_calendar** - 需要创建（日程表）
3. **sys_report** - 需要创建（报表表）

## Dashboard需要完善的数据

当前DashboardController使用mock数据，需要改为：
1. 从数据库统计真实数据
2. 插入初始测试数据
