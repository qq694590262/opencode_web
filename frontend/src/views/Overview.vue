<template>
  <div class="page-content">
    <div class="page-header">
      <h2>📊 数据概览</h2>
      <p>实时数据监控与分析</p>
    </div>
    
    <div class="stats-row">
      <div class="stat-item">
        <span class="stat-num">{{ stats.totalVisits || 0 }}</span>
        <span class="stat-desc">总访问</span>
      </div>
      <div class="stat-item">
        <span class="stat-num">{{ stats.activeUsers || 0 }}</span>
        <span class="stat-desc">活跃用户</span>
      </div>
      <div class="stat-item">
        <span class="stat-num">{{ stats.totalProjects || 0 }}</span>
        <span class="stat-desc">项目总数</span>
      </div>
      <div class="stat-item">
        <span class="stat-num">{{ stats.pendingTasks || 0 }}</span>
        <span class="stat-desc">待办事项</span>
      </div>
    </div>

    <div class="card">
      <h3>数据趋势图</h3>
      <div class="chart-area">
        <div class="chart-line">
          <div class="line-item" v-for="(item, index) in trends" :key="index" :style="{ '--delay': index * 0.1 + 's' }">
            <div class="line-bar" :style="{ height: item.value + '%' }"></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { dashboardApi } from '../api'

export default {
  name: 'Overview',
  setup() {
    const stats = ref({})
    const trends = ref([])

    onMounted(async () => {
      try {
        const res = await dashboardApi.getOverview()
        if (res.code === 200) {
          stats.value = res.data || {}
          trends.value = res.data?.trends || []
        }
      } catch (error) {
        console.error('获取数据概览失败:', error)
      }
    })

    return { stats, trends }
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
}

.page-header h2 {
  font-size: 24px;
  font-weight: 700;
  color: #0c4a6e;
  margin: 0 0 8px 0;
}

.page-header p {
  font-size: 14px;
  color: #64748b;
  margin: 0;
}

.stats-row {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 24px;
}

.stat-item {
  background: linear-gradient(135deg, #fff 0%, #f0f9ff 100%);
  border: 1px solid #e0f2fe;
  border-radius: 16px;
  padding: 24px;
  text-align: center;
}

.stat-num {
  display: block;
  font-size: 32px;
  font-weight: 700;
  color: #0ea5e9;
  margin-bottom: 8px;
}

.stat-desc {
  font-size: 14px;
  color: #64748b;
}

.card {
  background: #fff;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
}

.card h3 {
  font-size: 18px;
  font-weight: 600;
  color: #1e293b;
  margin: 0 0 20px 0;
}

.chart-area {
  height: 300px;
  display: flex;
  align-items: flex-end;
}

.chart-line {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  width: 100%;
  height: 100%;
  padding: 0 20px;
}

.line-item {
  flex: 1;
  height: 100%;
  display: flex;
  align-items: flex-end;
  padding: 0 10px;
}

.line-bar {
  width: 100%;
  background: linear-gradient(180deg, #38bdf8 0%, #0ea5e9 100%);
  border-radius: 8px 8px 0 0;
  animation: growUp 0.8s ease forwards;
  animation-delay: var(--delay);
  opacity: 0;
}

@keyframes growUp {
  from {
    height: 0;
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@media (max-width: 1024px) {
  .stats-row {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 640px) {
  .stats-row {
    grid-template-columns: 1fr;
  }
}
</style>
