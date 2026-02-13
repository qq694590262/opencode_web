<template>
  <div class="page-content">
    <div class="page-header">
      <h2>🔧 系统配置</h2>
      <p>系统全局设置</p>
    </div>

    <div class="system-info">
      <div class="info-card">
        <div class="info-header">
          <span class="info-icon">🖥️</span>
          <h4>系统信息</h4>
        </div>
        <div class="info-list">
          <div class="info-item"><span>系统版本</span><span>v1.0.0</span></div>
          <div class="info-item"><span>运行环境</span><span>Node.js 18.x</span></div>
          <div class="info-item"><span>数据库</span><span>PostgreSQL 15</span></div>
          <div class="info-item"><span>服务器时间</span><span>2024-03-20 14:30:00</span></div>
        </div>
      </div>

      <div class="info-card">
        <div class="info-header">
          <span class="info-icon">📊</span>
          <h4>运行状态</h4>
        </div>
        <div class="status-grid">
          <div class="status-item">
            <span class="status-value">98.5%</span>
            <span class="status-label">CPU 使用率</span>
          </div>
          <div class="status-item">
            <span class="status-value">4.2 GB</span>
            <span class="status-label">内存使用</span>
          </div>
          <div class="status-item">
            <span class="status-value">256 GB</span>
            <span class="status-label">磁盘空间</span>
          </div>
          <div class="status-item">
            <span class="status-value">99.9%</span>
            <span class="status-label">在线率</span>
          </div>
        </div>
      </div>
    </div>

    <div class="actions-card">
      <h4>系统操作</h4>
      <div class="action-buttons">
        <button class="action-btn">🔄 清除缓存</button>
        <button class="action-btn">📤 导出配置</button>
        <button class="action-btn">📥 导入配置</button>
        <button class="action-btn danger">🗑️ 数据备份</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'System'
}
</script>

<style scoped>
.page-content { animation: fadeIn 0.3s ease; }
@keyframes fadeIn { from { opacity: 0; transform: translateY(10px); } to { opacity: 1; transform: translateY(0); } }

.page-header { margin-bottom: 24px; }
.page-header h2 { font-size: 24px; font-weight: 700; color: #0c4a6e; margin: 0 0 8px 0; }
.page-header p { font-size: 14px; color: #64748b; margin: 0; }

.system-info { display: grid; grid-template-columns: repeat(2, 1fr); gap: 20px; margin-bottom: 24px; }

.info-card { background: #fff; border-radius: 16px; padding: 24px; box-shadow: 0 4px 15px rgba(0,0,0,0.05); }

.info-header { display: flex; align-items: center; gap: 12px; margin-bottom: 20px; }
.info-icon { font-size: 24px; }
.info-header h4 { font-size: 16px; font-weight: 600; color: #1e293b; margin: 0; }

.info-list { display: flex; flex-direction: column; gap: 12px; }
.info-item { display: flex; justify-content: space-between; padding: 10px 0; border-bottom: 1px solid #f1f5f9; font-size: 14px; }
.info-item span:first-child { color: #64748b; }
.info-item span:last-child { color: #1e293b; font-weight: 500; }

.status-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 16px; }
.status-item { text-align: center; padding: 16px; background: #f8fafc; border-radius: 12px; }
.status-value { display: block; font-size: 24px; font-weight: 700; color: #0ea5e9; }
.status-label { font-size: 12px; color: #64748b; }

.actions-card { background: #fff; border-radius: 16px; padding: 24px; box-shadow: 0 4px 15px rgba(0,0,0,0.05); }
.actions-card h4 { font-size: 16px; font-weight: 600; color: #1e293b; margin: 0 0 16px 0; }

.action-buttons { display: flex; gap: 12px; flex-wrap: wrap; }
.action-btn { padding: 12px 20px; border: none; border-radius: 10px; background: #f1f5f9; color: #475569; font-size: 14px; cursor: pointer; transition: all 0.2s; }
.action-btn:hover { background: #e0f2fe; color: #0284c7; }
.action-btn.danger:hover { background: #fee2e2; color: #dc2626; }

@media (max-width: 768px) { .system-info { grid-template-columns: 1fr; } }
</style>
