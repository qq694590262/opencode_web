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

    return {
      quickLinks
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
