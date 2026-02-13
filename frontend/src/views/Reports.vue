<template>
  <div class="page-content">
    <div class="page-header">
      <h2>📈 报表中心</h2>
      <p>查看和管理各类统计报表</p>
    </div>

    <div class="reports-grid">
      <div class="report-card" v-for="report in reports" :key="report.id">
        <div class="report-icon">{{ report.icon }}</div>
        <div class="report-info">
          <h4>{{ report.title }}</h4>
          <p>{{ report.desc }}</p>
        </div>
        <button class="report-btn">查看</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Reports',
  setup() {
    const reports = [
      { id: 1, icon: '📊', title: '销售报表', desc: '月度销售数据汇总' },
      { id: 2, icon: '👥', title: '用户报表', desc: '用户增长与留存分析' },
      { id: 3, icon: '💰', title: '财务报表', desc: '收入支出明细' },
      { id: 4, icon: '📦', title: '库存报表', desc: '库存周转情况' },
      { id: 5, icon: '🎯', title: '业绩报表', desc: '团队业绩排名' },
      { id: 6, icon: '📱', title: '流量报表', desc: '网站访问分析' }
    ]
    return { reports }
  }
}
</script>

<style scoped>
.page-content { animation: fadeIn 0.3s ease; }
@keyframes fadeIn { from { opacity: 0; transform: translateY(10px); } to { opacity: 1; transform: translateY(0); } }

.page-header { margin-bottom: 24px; }
.page-header h2 { font-size: 24px; font-weight: 700; color: #0c4a6e; margin: 0 0 8px 0; }
.page-header p { font-size: 14px; color: #64748b; margin: 0; }

.reports-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 20px; }

.report-card {
  background: #fff; border-radius: 16px; padding: 24px;
  display: flex; flex-direction: column; align-items: center;
  box-shadow: 0 4px 15px rgba(0,0,0,0.05); transition: all 0.3s ease;
}
.report-card:hover { transform: translateY(-4px); box-shadow: 0 8px 25px rgba(0,0,0,0.1); }

.report-icon { font-size: 40px; margin-bottom: 16px; }
.report-info { text-align: center; margin-bottom: 16px; }
.report-info h4 { font-size: 16px; font-weight: 600; color: #1e293b; margin: 0 0 8px 0; }
.report-info p { font-size: 13px; color: #64748b; margin: 0; }

.report-btn {
  width: 100%; padding: 10px; border: none; border-radius: 10px;
  background: linear-gradient(135deg, #0ea5e9, #0284c7); color: #fff;
  font-size: 14px; font-weight: 500; cursor: pointer; transition: all 0.3s ease;
}
.report-btn:hover { transform: scale(1.02); }

@media (max-width: 1024px) { .reports-grid { grid-template-columns: repeat(2, 1fr); } }
@media (max-width: 640px) { .reports-grid { grid-template-columns: 1fr; } }
</style>
