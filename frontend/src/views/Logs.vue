<template>
  <div class="page-content">
    <div class="page-header">
      <h2>📋 操作日志</h2>
      <p>系统操作记录审计</p>
    </div>

    <div class="logs-list">
      <div class="log-item" v-for="log in logs" :key="log.id">
        <div class="log-time">{{ log.time }}</div>
        <div class="log-icon" :style="{ background: log.color }">{{ log.icon }}</div>
        <div class="log-content">
          <p><strong>{{ log.user }}</strong> {{ log.action }}</p>
          <span class="log-ip">IP: {{ log.ip }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Logs',
  setup() {
    const logs = [
      { id: 1, time: '14:32:15', icon: '👤', color: '#e0f2fe', user: '张三', action: '登录系统', ip: '192.168.1.101' },
      { id: 2, time: '14:30:22', icon: '✏️', color: '#dcfce7', user: '李四', action: '编辑用户资料', ip: '192.168.1.102' },
      { id: 3, time: '14:28:45', icon: '📤', color: '#fef3c7', user: '王五', action: '导出报表', ip: '192.168.1.103' },
      { id: 4, time: '14:25:11', icon: '🗑️', color: '#fee2e2', user: '赵六', action: '删除文档', ip: '192.168.1.104' },
      { id: 5, time: '14:20:05', icon: '➕', color: '#e0e7ff', user: '钱七', action: '创建新项目', ip: '192.168.1.105' }
    ]
    return { logs }
  }
}
</script>

<style scoped>
.page-content { animation: fadeIn 0.3s ease; }
@keyframes fadeIn { from { opacity: 0; transform: translateY(10px); } to { opacity: 1; transform: translateY(0); } }

.page-header { margin-bottom: 24px; }
.page-header h2 { font-size: 24px; font-weight: 700; color: #0c4a6e; margin: 0 0 8px 0; }
.page-header p { font-size: 14px; color: #64748b; margin: 0; }

.logs-list { background: #fff; border-radius: 16px; padding: 20px; box-shadow: 0 4px 15px rgba(0,0,0,0.05); }

.log-item { display: flex; align-items: center; gap: 16px; padding: 16px 0; border-bottom: 1px solid #f1f5f9; }
.log-item:last-child { border-bottom: none; }

.log-time { font-size: 12px; color: #94a3b8; font-family: monospace; width: 80px; }

.log-icon { width: 40px; height: 40px; border-radius: 10px; display: flex; align-items: center; justify-content: center; font-size: 18px; }

.log-content { flex: 1; }
.log-content p { margin: 0 0 4px 0; font-size: 14px; color: #1e293b; }
.log-ip { font-size: 12px; color: #94a3b8; }
</style>
