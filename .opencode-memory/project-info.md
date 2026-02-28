# OpenCode Web 项目记忆

最后更新: 2026-02-28

## 项目信息
- 名称: OpenCode Web (企业管理系统)
- 技术栈: Vue 3 + Vite + Element Plus | Spring Boot + MyBatis-Plus
- 仓库: Gitee + GitHub

## 用户偏好 (本项目)
- 自动提交推送: 每次修改完成后自动 commit 并 push
- Git推送: 同时推送到 gitee 和 github

## 技术栈偏好
- 前端: Vue 3, Element Plus, Vite
- 后端: Spring Boot, MyBatis-Plus, MySQL

## 代码风格
- 前端样式: 使用全局 CSS 变量和公共类
- 后端导入: 避免通配符导入，使用具体导入
- 代码清理: 移除未使用的导入和代码

## 重要注意事项 ⚠️

### Departments.vue
- 禁止使用自定义 tree-arrow
- 必须使用 el-table 原生树形展开

### CSS 样式
- 优先使用 global.css 中的公共类

## 常见操作
- 启动前端: `cd frontend && npm run dev`
- 构建前端: `cd frontend && npm run build`
- Git推送: `git push origin master`

## 会话历史
- 2026-02-28: 代码优化 + 记忆系统创建
