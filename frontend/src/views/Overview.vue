<template>
  <div class="page-content">
    <div class="page-header">
      <div class="header-title">
        <h2>📊 数据概览</h2>
        <p>实时监控系统运行状态</p>
      </div>
      <div class="header-actions">
        <div class="date-range">
          <button 
            v-for="range in dateRanges" 
            :key="range.value"
            class="range-btn"
            :class="{ active: selectedRange === range.value }"
            @click="selectedRange = range.value"
          >
            {{ range.label }}
          </button>
        </div>
        <button class="btn-refresh" @click="refreshData" :class="{ loading: refreshing }">
          <span>🔄</span>
        </button>
      </div>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-grid">
      <div class="stat-card" v-for="stat in statistics" :key="stat.id">
        <div class="stat-icon" :style="{ background: stat.gradient }">
          <span>{{ stat.icon }}</span>
        </div>
        <div class="stat-info">
          <span class="stat-label">{{ stat.label }}</span>
          <div class="stat-value-row">
            <span class="stat-value">{{ stat.value }}</span>
            <span class="stat-trend" :class="stat.trend > 0 ? 'up' : 'down'">
              {{ stat.trend > 0 ? '↑' : '↓' }} {{ Math.abs(stat.trend) }}%
            </span>
          </div>
          <span class="stat-desc">较上期 {{ stat.trend > 0 ? '增长' : '下降' }}</span>
        </div>
      </div>
    </div>

    <!-- 图表区域 -->
    <div class="charts-grid">
      <!-- 访问趋势图 -->
      <div class="chart-card large">
        <div class="chart-header">
          <h3>📈 访问趋势</h3>
          <div class="chart-legend">
            <span class="legend-item">
              <span class="dot" style="background: #0ea5e9;"></span>
              页面访问
            </span>
            <span class="legend-item">
              <span class="dot" style="background: #10b981;"></span>
              独立访客
            </span>
          </div>
        </div>
        <div class="chart-body">
          <div class="line-chart">
            <div class="chart-y-axis">
              <span v-for="n in 5" :key="n">{{ (6 - n) * 200 }}</span>
            </div>
            <div class="chart-content">
              <div class="chart-lines">
                <svg viewBox="0 0 100 100" preserveAspectRatio="none">
                  <path class="line line-1" d="M0,70 Q20,60 40,50 T80,30 T100,20" />
                  <path class="line line-2" d="M0,80 Q20,70 40,65 T80,50 T100,40" />
                </svg>
              </div>
              <div class="chart-x-axis">
                <span v-for="(date, index) in chartDates" :key="index">{{ date }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 项目状态分布 -->
      <div class="chart-card">
        <div class="chart-header">
          <h3>📁 项目状态</h3>
        </div>
        <div class="chart-body">
          <div class="pie-chart">
            <div class="pie-container">
              <div class="pie" :style="pieStyle"></div>
              <div class="pie-center">
                <span class="pie-total">{{ totalProjects }}</span>
                <span class="pie-label">项目总数</span>
              </div>
            </div>
            <div class="pie-legend">
              <div class="legend-item" v-for="item in projectStatus" :key="item.name">
                <span class="dot" :style="{ background: item.color }"></span>
                <span class="name">{{ item.name }}</span>
                <span class="value">{{ item.value }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 任务完成率 -->
      <div class="chart-card">
        <div class="chart-header">
          <h3>✅ 任务统计</h3>
        </div>
        <div class="chart-body">
          <div class="task-stats">
            <div class="progress-ring">
              <svg viewBox="0 0 100 100">
                <circle class="ring-bg" cx="50" cy="50" r="40" />
                <circle 
                  class="ring-progress" 
                  cx="50" 
                  cy="50" 
                  r="40"
                  :style="{ strokeDashoffset: 251 - (251 * taskCompletionRate / 100) }"
                />
              </svg>
              <div class="ring-text">
                <span class="percentage">{{ taskCompletionRate }}%</span>
                <span class="label">完成率</span>
              </div>
            </div>
            <div class="task-breakdown">
              <div class="task-item">
                <span class="dot" style="background: #22c55e;"></span>
                <span>已完成: {{ taskStats.completed }}</span>
              </div>
              <div class="task-item">
                <span class="dot" style="background: #f59e0b;"></span>
                <span>进行中: {{ taskStats.inProgress }}</span>
              </div>
              <div class="task-item">
                <span class="dot" style="background: #94a3b8;"></span>
                <span>待办: {{ taskStats.todo }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 部门活跃度 -->
      <div class="chart-card">
        <div class="chart-header">
          <h3>👥 部门活跃度</h3>
        </div>
        <div class="chart-body">
          <div class="bar-chart">
            <div class="bar-item" v-for="dept in departmentStats" :key="dept.name">
              <div class="bar-label">{{ dept.name }}</div>
              <div class="bar-wrapper">
                <div class="bar" :style="{ width: dept.percentage + '%', background: dept.color }"></div>
                <span class="bar-value">{{ dept.value }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

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
import { ref, computed, onMounted } from 'vue'
import { dashboardApi } from '../api'
import { showToast } from '../components/Toast.vue'

export default {
  name: 'Overview',
  setup() {
    const refreshing = ref(false)
    const selectedRange = ref('week')

    const dateRanges = [
      { label: '今日', value: 'today' },
      { label: '本周', value: 'week' },
      { label: '本月', value: 'month' },
      { label: '本年', value: 'year' }
    ]

    const statistics = ref([
      { id: 1, icon: '👁️', label: '总访问量', value: '12,345', trend: 12.5, gradient: 'linear-gradient(135deg, #0ea5e9, #0284c7)' },
      { id: 2, icon: '👥', label: '活跃用户', value: '1,234', trend: 8.3, gradient: 'linear-gradient(135deg, #22c55e, #16a34a)' },
      { id: 3, icon: '📁', label: '项目总数', value: '56', trend: -2.1, gradient: 'linear-gradient(135deg, #f59e0b, #d97706)' },
      { id: 4, icon: '✅', label: '完成任务', value: '234', trend: 15.7, gradient: 'linear-gradient(135deg, #a78bfa, #8b5cf6)' }
    ])

    const chartDates = ['周一', '周二', '周三', '周四', '周五', '周六', '周日']

    const projectStatus = ref([
      { name: '进行中', value: 24, color: '#0ea5e9' },
      { name: '已完成', value: 18, color: '#22c55e' },
      { name: '待开始', value: 10, color: '#f59e0b' },
      { name: '已延期', value: 4, color: '#ef4444' }
    ])

    const totalProjects = computed(() => 
      projectStatus.value.reduce((sum, item) => sum + item.value, 0)
    )

    const pieStyle = computed(() => {
      let gradient = 'conic-gradient('
      let currentAngle = 0
      projectStatus.value.forEach((item, index) => {
        const angle = (item.value / totalProjects.value) * 360
        gradient += `${item.color} ${currentAngle}deg ${currentAngle + angle}deg`
        if (index < projectStatus.value.length - 1) gradient += ', '
        currentAngle += angle
      })
      gradient += ')'
      return { background: gradient }
    })

    const taskStats = ref({
      completed: 156,
      inProgress: 45,
      todo: 33
    })

    const taskCompletionRate = computed(() => {
      const total = taskStats.value.completed + taskStats.value.inProgress + taskStats.value.todo
      return Math.round((taskStats.value.completed / total) * 100)
    })

    const departmentStats = ref([
      { name: '技术部', value: 45, percentage: 90, color: '#0ea5e9' },
      { name: '产品部', value: 38, percentage: 76, color: '#22c55e' },
      { name: '设计部', value: 32, percentage: 64, color: '#f59e0b' },
      { name: '运营部', value: 28, percentage: 56, color: '#a78bfa' },
      { name: '市场部', value: 25, percentage: 50, color: '#ec4899' }
    ])

    const quickLinks = [
      { icon: '👥', name: '用户管理', desc: '管理用户账号', route: '/dashboard/users' },
      { icon: '📁', name: '项目列表', desc: '查看所有项目', route: '/dashboard/projects' },
      { icon: '✅', name: '任务管理', desc: '跟踪任务进度', route: '/dashboard/tasks' },
      { icon: '📄', name: '文档中心', desc: '管理文档资料', route: '/dashboard/documents' },
      { icon: '📅', name: '日程日历', desc: '安排工作计划', route: '/dashboard/calendar' },
      { icon: '📊', name: '报表中心', desc: '查看统计报表', route: '/dashboard/reports' }
    ]

    const recentActivities = ref([
      { icon: '✅', text: '张三完成了任务 "完成用户模块API开发"', time: '5分钟前', color: '#dcfce7' },
      { icon: '📁', text: '李四创建了项目 "电商平台重构"', time: '1小时前', color: '#e0f2fe' },
      { icon: '👤', text: '王五更新了个人资料', time: '2小时前', color: '#f3e8ff' },
      { icon: '📄', text: '赵六上传了文档 "API接口文档V1.0"', time: '3小时前', color: '#fef3c7' },
      { icon: '✏️', text: '钱七编辑了文章 "Vue3最佳实践"', time: '5小时前', color: '#fee2e2' }
    ])

    const refreshData = async (showNotification = true) => {
      refreshing.value = true
      try {
        const res = await dashboardApi.getOverview()
        if (res.code === 200 && res.data) {
          // 更新统计数据
          const data = res.data
          statistics.value[0].value = data.totalVisits?.toLocaleString() || '12,345'
          statistics.value[1].value = data.activeUsers?.toLocaleString() || '1,234'
          statistics.value[2].value = data.totalProjects?.toString() || '56'
          statistics.value[3].value = data.completedTasks?.toString() || '234'
        }
        if (showNotification) {
          showToast({ type: 'success', message: '数据已刷新' })
        }
      } catch (error) {
        console.error('刷新数据失败:', error)
        if (showNotification) {
          showToast({ type: 'error', message: '刷新失败' })
        }
      } finally {
        setTimeout(() => {
          refreshing.value = false
        }, 500)
      }
    }

    onMounted(() => {
      refreshData(false) // 初始加载不显示提示
    })

    return {
      refreshing,
      selectedRange,
      dateRanges,
      statistics,
      chartDates,
      projectStatus,
      totalProjects,
      pieStyle,
      taskStats,
      taskCompletionRate,
      departmentStats,
      quickLinks,
      recentActivities,
      refreshData
    }
  }
}
</script>

<style scoped>
.page-content {
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

.page-header {
  margin-bottom: 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-title h2 {
  font-size: 24px;
  font-weight: 700;
  color: #0c4a6e;
  margin: 0 0 8px 0;
}

.header-title p {
  font-size: 14px;
  color: #64748b;
  margin: 0;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 16px;
}

.date-range {
  display: flex;
  background: #f1f5f9;
  border-radius: 10px;
  padding: 4px;
}

.range-btn {
  padding: 8px 16px;
  border: none;
  background: transparent;
  border-radius: 8px;
  font-size: 13px;
  color: #64748b;
  cursor: pointer;
  transition: all 0.3s;
}

.range-btn.active {
  background: #fff;
  color: #0ea5e9;
  font-weight: 500;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.btn-refresh {
  width: 40px;
  height: 40px;
  border: none;
  background: #f1f5f9;
  border-radius: 10px;
  font-size: 18px;
  cursor: pointer;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  justify-content: center;
}

.btn-refresh:hover {
  background: #e0f2fe;
  color: #0ea5e9;
}

.btn-refresh.loading {
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

/* 统计卡片 */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 24px;
}

.stat-card {
  background: #fff;
  border-radius: 16px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.05);
  transition: all 0.3s;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 25px rgba(0,0,0,0.1);
}

.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
}

.stat-info {
  flex: 1;
}

.stat-label {
  display: block;
  font-size: 13px;
  color: #64748b;
  margin-bottom: 4px;
}

.stat-value-row {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 4px;
}

.stat-value {
  font-size: 24px;
  font-weight: 700;
  color: #1e293b;
}

.stat-trend {
  font-size: 12px;
  font-weight: 600;
  padding: 2px 6px;
  border-radius: 4px;
}

.stat-trend.up {
  background: #dcfce7;
  color: #16a34a;
}

.stat-trend.down {
  background: #fee2e2;
  color: #dc2626;
}

.stat-desc {
  font-size: 12px;
  color: #94a3b8;
}

/* 图表区域 */
.charts-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
  margin-bottom: 24px;
}

.chart-card {
  background: #fff;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.05);
}

.chart-card.large {
  grid-column: span 2;
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.chart-header h3 {
  font-size: 16px;
  font-weight: 600;
  color: #1e293b;
  margin: 0;
}

.chart-legend {
  display: flex;
  gap: 16px;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: #64748b;
}

.dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
}

/* 折线图 */
.line-chart {
  display: flex;
  height: 200px;
}

.chart-y-axis {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding-right: 12px;
  font-size: 11px;
  color: #94a3b8;
}

.chart-content {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.chart-lines {
  flex: 1;
  position: relative;
}

.chart-lines svg {
  width: 100%;
  height: 100%;
}

.line {
  fill: none;
  stroke-width: 3;
  stroke-linecap: round;
}

.line-1 {
  stroke: #0ea5e9;
  opacity: 0.8;
}

.line-2 {
  stroke: #10b981;
  opacity: 0.8;
}

.chart-x-axis {
  display: flex;
  justify-content: space-between;
  padding-top: 8px;
  font-size: 11px;
  color: #94a3b8;
}

/* 饼图 */
.pie-chart {
  display: flex;
  align-items: center;
  gap: 24px;
}

.pie-container {
  position: relative;
  width: 120px;
  height: 120px;
}

.pie {
  width: 100%;
  height: 100%;
  border-radius: 50%;
}

.pie-center {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: #fff;
  width: 70px;
  height: 70px;
  border-radius: 50%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.pie-total {
  font-size: 20px;
  font-weight: 700;
  color: #1e293b;
}

.pie-label {
  font-size: 10px;
  color: #94a3b8;
}

.pie-legend {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.pie-legend .legend-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
}

.pie-legend .name {
  color: #64748b;
  flex: 1;
}

.pie-legend .value {
  color: #1e293b;
  font-weight: 600;
}

/* 任务统计 */
.task-stats {
  display: flex;
  align-items: center;
  gap: 24px;
}

.progress-ring {
  position: relative;
  width: 100px;
  height: 100px;
}

.progress-ring svg {
  width: 100%;
  height: 100%;
  transform: rotate(-90deg);
}

.ring-bg {
  fill: none;
  stroke: #f1f5f9;
  stroke-width: 8;
}

.ring-progress {
  fill: none;
  stroke: #0ea5e9;
  stroke-width: 8;
  stroke-linecap: round;
  stroke-dasharray: 251;
  transition: stroke-dashoffset 0.5s ease;
}

.ring-text {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  text-align: center;
}

.percentage {
  display: block;
  font-size: 24px;
  font-weight: 700;
  color: #0ea5e9;
}

.label {
  font-size: 11px;
  color: #94a3b8;
}

.task-breakdown {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.task-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  color: #64748b;
}

/* 柱状图 */
.bar-chart {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.bar-item {
  display: flex;
  align-items: center;
  gap: 12px;
}

.bar-label {
  width: 60px;
  font-size: 13px;
  color: #64748b;
  text-align: right;
}

.bar-wrapper {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 8px;
}

.bar {
  height: 24px;
  border-radius: 12px;
  transition: width 0.5s ease;
}

.bar-value {
  font-size: 13px;
  color: #1e293b;
  font-weight: 500;
  min-width: 24px;
}

/* 快捷入口 */
.quick-access {
  background: #fff;
  border-radius: 16px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.05);
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
  box-shadow: 0 4px 15px rgba(0,0,0,0.05);
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
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .charts-grid {
    grid-template-columns: 1fr;
  }
  
  .chart-card.large {
    grid-column: span 1;
  }
  
  .quick-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }
  
  .quick-grid {
    grid-template-columns: 1fr;
  }
  
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }
}
</style>