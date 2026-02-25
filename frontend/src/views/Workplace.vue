<template>
  <div class="page-content">
    <!-- 快捷入口 -->
    <div class="quick-access">
      <h3>⚡ 快捷入口</h3>
      <div class="quick-grid">
        <router-link 
          v-for="item in quickLinks" 
          :key="item.route"
          :to="item.route"
          class="quick-card"
        >
          <span class="quick-icon">{{ item.icon }}</span>
          <div class="quick-info">
            <span class="quick-name">{{ item.name }}</span>
            <span class="quick-desc">{{ item.desc }}</span>
          </div>
          <span class="quick-arrow">→</span>
        </router-link>
      </div>
    </div>

    <!-- 最近活动 -->
    <div class="activity-section">
      <h3>🔔 最近活动</h3>
      <div class="activity-list">
        <div class="activity-item" v-for="(activity, index) in recentActivities" :key="index">
          <div class="activity-icon" :style="{ background: activity.color }">
            {{ activity.icon }}
          </div>
          <div class="activity-content">
            <p class="activity-text">{{ activity.text }}</p>
            <span class="activity-time">{{ activity.time }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Workplace',
  setup() {
    const quickLinks = [
      { icon: '👥', name: '用户管理', desc: '管理用户账号', route: '/dashboard/users' },
      { icon: '📁', name: '项目列表', desc: '查看所有项目', route: '/dashboard/projects' },
      { icon: '✅', name: '任务管理', desc: '跟踪任务进度', route: '/dashboard/tasks' },
      { icon: '📄', name: '文档中心', desc: '管理文档资料', route: '/dashboard/documents' },
      { icon: '📅', name: '日程日历', desc: '安排工作计划', route: '/dashboard/calendar' },
      { icon: '📊', name: '报表中心', desc: '查看统计报表', route: '/dashboard/reports' }
    ]

    const recentActivities = [
      { icon: '✅', text: '张三完成了任务 "完成用户模块API开发"', time: '5分钟前', color: '#dcfce7' },
      { icon: '📁', text: '李四创建了项目 "电商平台重构"', time: '1小时前', color: '#e0f2fe' },
      { icon: '👤', text: '王五更新了个人资料', time: '2小时前', color: '#f3e8ff' },
      { icon: '📄', text: '赵六上传了文档 "API接口文档V1.0"', time: '3小时前', color: '#fef3c7' },
      { icon: '✏️', text: '钱七编辑了文章 "Vue3最佳实践"', time: '5小时前', color: '#fee2e2' }
    ]

    return {
      quickLinks,
      recentActivities
    }
  }
}
</script>

<style scoped>
.page-content {
  animation: fadeIn 0.3s ease;
  background: #fff;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

/* 快捷入口 */
.quick-access {
  background: #fff;
  border-radius: 16px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
}

.quick-access h3 {
  font-size: 18px;
  font-weight: 600;
  color: #1e293b;
  margin: 0 0 20px 0;
}

.quick-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
}

.quick-card {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  background: #f8fafc;
  border-radius: 12px;
  text-decoration: none;
  transition: all 0.3s;
  border: 2px solid transparent;
}

.quick-card:hover {
  background: #f0f9ff;
  border-color: #e0f2fe;
  transform: translateY(-2px);
}

.quick-icon {
  font-size: 24px;
}

.quick-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.quick-name {
  font-size: 14px;
  font-weight: 600;
  color: #1e293b;
}

.quick-desc {
  font-size: 12px;
  color: #94a3b8;
}

.quick-arrow {
  font-size: 16px;
  color: #94a3b8;
}

/* 活动列表 */
.activity-section {
  background: #fff;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
}

.activity-section h3 {
  font-size: 18px;
  font-weight: 600;
  color: #1e293b;
  margin: 0 0 20px 0;
}

.activity-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.activity-item {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 12px;
  background: #f8fafc;
  border-radius: 10px;
  transition: all 0.3s;
}

.activity-item:hover {
  background: #f1f5f9;
}

.activity-icon {
  width: 36px;
  height: 36px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  flex-shrink: 0;
}

.activity-content {
  flex: 1;
}

.activity-text {
  font-size: 14px;
  color: #334155;
  margin: 0 0 4px 0;
}

.activity-time {
  font-size: 12px;
  color: #94a3b8;
}

/* 响应式 */
@media (max-width: 1200px) {
  .quick-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .quick-grid {
    grid-template-columns: 1fr;
  }
}
</style>
