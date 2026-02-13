<template>
  <div class="page-content">
    <div class="page-header">
      <h2>📁 项目列表</h2>
      <p>管理所有项目</p>
    </div>

    <div class="projects-grid">
      <div class="project-card" v-for="p in projects" :key="p.id">
        <div class="project-header">
          <span class="project-icon" :style="{ background: p.color }">{{ p.icon }}</span>
          <span class="project-status" :class="p.status">{{ p.statusText }}</span>
        </div>
        <h4>{{ p.name }}</h4>
        <p>{{ p.desc }}</p>
        <div class="project-meta">
          <span>👥 {{ p.members }}</span>
          <span>📅 {{ p.date }}</span>
        </div>
        <div class="progress-bar">
          <div class="progress" :style="{ width: p.progress + '%' }"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Projects',
  setup() {
    const projects = [
      { id: 1, icon: '💼', color: '#0ea5e9', name: '企业管理系统', desc: '企业内部管理系统开发', members: 8, date: '2024-03-15', progress: 75, status: 'active', statusText: '进行中' },
      { id: 2, icon: '🛒', color: '#34d399', name: '电商平台', desc: 'B2C 电商网站', members: 12, date: '2024-02-20', progress: 90, status: 'active', statusText: '进行中' },
      { id: 3, icon: '📱', color: '#f472b6', name: '移动端APP', desc: 'iOS/Android 应用', members: 5, date: '2024-01-10', progress: 30, status: 'pending', statusText: '待开始' },
      { id: 4, icon: '🔒', color: '#fbbf24', name: '安全审计系统', desc: '系统安全检测工具', members: 3, date: '2024-03-01', progress: 100, status: 'done', statusText: '已完成' }
    ]
    return { projects }
  }
}
</script>

<style scoped>
.page-content { animation: fadeIn 0.3s ease; }
@keyframes fadeIn { from { opacity: 0; transform: translateY(10px); } to { opacity: 1; transform: translateY(0); } }

.page-header { margin-bottom: 24px; }
.page-header h2 { font-size: 24px; font-weight: 700; color: #0c4a6e; margin: 0 0 8px 0; }
.page-header p { font-size: 14px; color: #64748b; margin: 0; }

.projects-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 20px; }

.project-card {
  background: #fff; border-radius: 16px; padding: 24px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.05); transition: all 0.3s;
}
.project-card:hover { transform: translateY(-4px); box-shadow: 0 8px 25px rgba(0,0,0,0.1); }

.project-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.project-icon { width: 48px; height: 48px; border-radius: 12px; display: flex; align-items: center; justify-content: center; font-size: 24px; }

.project-status { padding: 4px 12px; border-radius: 20px; font-size: 12px; font-weight: 500; }
.project-status.active { background: #dcfce7; color: #16a34a; }
.project-status.pending { background: #fef3c7; color: #d97706; }
.project-status.done { background: #e0f2fe; color: #0284c7; }

.project-card h4 { font-size: 18px; font-weight: 600; color: #1e293b; margin: 0 0 8px 0; }
.project-card p { font-size: 13px; color: #64748b; margin: 0 0 16px 0; }

.project-meta { display: flex; gap: 16px; font-size: 13px; color: #64748b; margin-bottom: 16px; }

.progress-bar { height: 6px; background: #f1f5f9; border-radius: 3px; overflow: hidden; }
.progress { height: 100%; background: linear-gradient(90deg, #0ea5e9, #38bdf8); border-radius: 3px; transition: width 0.5s ease; }

@media (max-width: 768px) { .projects-grid { grid-template-columns: 1fr; } }
</style>
