<template>
  <div class="page-content">
    <div class="page-header">
      <h2>📋 操作日志</h2>
      <p>系统操作记录审计</p>
    </div>

    <div class="logs-list">
      <div class="log-item" v-for="log in logs" :key="log.id">
        <div class="log-time">{{ formatTime(log.createTime) }}</div>
        <div class="log-icon" :style="{ background: getLogColor(log.operation) }">{{ getLogIcon(log.operation) }}</div>
        <div class="log-content">
          <p><strong>{{ log.username }}</strong> {{ log.operation }}</p>
          <span class="log-ip">IP: {{ log.ip }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { logApi } from '../api'

export default {
  name: 'Logs',
  setup() {
    const logs = ref([])

    const logColors = {
      '登录': '#e0f2fe',
      '编辑': '#dcfce7',
      '导出': '#fef3c7',
      '删除': '#fee2e2',
      '创建': '#e0e7ff'
    }

    const logIcons = {
      '登录': '👤',
      '编辑': '✏️',
      '导出': '📤',
      '删除': '🗑️',
      '创建': '➕'
    }

    const getLogColor = (operation) => {
      for (const key in logColors) {
        if (operation && operation.includes(key)) return logColors[key]
      }
      return '#f1f5f9'
    }

    const getLogIcon = (operation) => {
      for (const key in logIcons) {
        if (operation && operation.includes(key)) return logIcons[key]
      }
      return '📝'
    }

    const formatTime = (dateStr) => {
      if (!dateStr) return ''
      const date = new Date(dateStr)
      return date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit', second: '2-digit' })
    }

    const loadLogs = async () => {
      try {
        const res = await logApi.getAll()
        if (res.code === 200) {
          logs.value = res.data || []
        }
      } catch (error) {
        console.error('获取日志失败:', error)
      }
    }

    onMounted(loadLogs)

    return {
      logs,
      getLogColor,
      getLogIcon,
      formatTime
    }
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
