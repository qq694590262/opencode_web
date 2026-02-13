<template>
  <div class="page-content">
    <div class="page-header">
      <h2>📉 图表分析</h2>
      <p>可视化数据展示与分析</p>
    </div>

    <div class="charts-grid">
      <div class="chart-card">
        <h3>饼图 - 用户分布</h3>
        <div class="pie-chart">
          <div class="pie-segment" v-for="(item, i) in pieData" :key="i" :style="{ '--deg': item.deg, '--color': item.color }"></div>
        </div>
        <div class="pie-legend">
          <div class="legend-item" v-for="(item, i) in pieData" :key="i">
            <span class="legend-color" :style="{ background: item.color }"></span>
            <span>{{ item.label }}</span>
          </div>
        </div>
      </div>

      <div class="chart-card">
        <h3>柱状图 - 月度对比</h3>
        <div class="bar-chart">
          <div class="bar-item" v-for="(item, i) in barData" :key="i">
            <div class="bar" :style="{ height: item.value + '%' }"></div>
            <span class="bar-label">{{ item.label }}</span>
          </div>
        </div>
      </div>

      <div class="chart-card full">
        <h3>折线图 - 趋势变化</h3>
        <div class="line-chart">
          <svg viewBox="0 0 800 200" class="chart-svg">
            <polyline fill="none" stroke="#0ea5e9" stroke-width="3" points="0,150 100,120 200,140 300,80 400,100 500,60 600,90 700,40 800,70" />
            <circle v-for="(p, i) in linePoints" :key="i" :cx="p.x" :cy="p.y" r="5" fill="#fff" stroke="#0ea5e9" stroke-width="2" />
          </svg>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Charts',
  setup() {
    const pieData = [
      { label: '华东区', value: 35, deg: '126deg', color: '#0ea5e9' },
      { label: '华南区', value: 25, deg: '216deg', color: '#34d399' },
      { label: '华北区', value: 20, deg: '288deg', color: '#fbbf24' },
      { label: '西南区', value: 15, deg: '342deg', color: '#f472b6' },
      { label: '其他', value: 5, deg: '360deg', color: '#94a3b8' }
    ]
    const barData = [
      { label: '1月', value: 65 }, { label: '2月', value: 78 }, { label: '3月', value: 90 },
      { label: '4月', value: 85 }, { label: '5月', value: 95 }, { label: '6月', value: 80 }
    ]
    const linePoints = [
      { x: 0, y: 150 }, { x: 100, y: 120 }, { x: 200, y: 140 }, { x: 300, y: 80 },
      { x: 400, y: 100 }, { x: 500, y: 60 }, { x: 600, y: 90 }, { x: 700, y: 40 }, { x: 800, y: 70 }
    ]
    return { pieData, barData, linePoints }
  }
}
</script>

<style scoped>
.page-content { animation: fadeIn 0.3s ease; }
@keyframes fadeIn { from { opacity: 0; } to { opacity: 1; } }
.page-header { margin-bottom: 24px; }
.page-header h2 { font-size: 24px; font-weight: 700; color: #0c4a6e; margin: 0 0 8px 0; }
.page-header p { font-size: 14px; color: #64748b; margin: 0; }

.charts-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 20px; }

.chart-card {
  background: #fff; border-radius: 16px; padding: 24px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.05);
}
.chart-card.full { grid-column: span 2; }
.chart-card h3 { font-size: 16px; font-weight: 600; color: #1e293b; margin: 0 0 20px 0; }

.pie-chart {
  width: 160px; height: 160px; border-radius: 50%;
  background: conic-gradient(#0ea5e9 0deg 126deg, #34d399 126deg 216deg, #fbbf24 216deg 288deg, #f472b6 288deg 342deg, #94a3b8 342deg 360deg);
  margin: 0 auto 20px;
}

.pie-legend { display: flex; flex-wrap: wrap; gap: 12px; justify-content: center; }
.legend-item { display: flex; align-items: center; gap: 6px; font-size: 13px; color: #475569; }
.legend-color { width: 12px; height: 12px; border-radius: 3px; }

.bar-chart { display: flex; justify-content: space-around; align-items: flex-end; height: 200px; }
.bar-item { display: flex; flex-direction: column; align-items: center; gap: 8px; flex: 1; }
.bar { width: 40px; background: linear-gradient(180deg, #38bdf8, #0ea5e9); border-radius: 6px 6px 0 0; transition: height 0.5s ease; }
.bar-label { font-size: 12px; color: #64748b; }

.line-chart { height: 200px; }
.chart-svg { width: 100%; height: 100%; }

@media (max-width: 768px) { .charts-grid { grid-template-columns: 1fr; } .chart-card.full { grid-column: span 1; } }
</style>
