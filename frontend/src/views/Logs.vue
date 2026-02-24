<template>
  <div class="page-content">
    <div class="page-header">
      <div class="header-title">
        <h2>📋 操作日志</h2>
        <p>系统操作记录审计与追踪</p>
      </div>
      <div class="header-actions">
        <button class="btn-export" @click="exportLogs">
          <span>📤</span> 导出日志
        </button>
        <button class="btn-clear" @click="clearOldLogs">
          <span>🧹</span> 清理旧日志
        </button>
      </div>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-bar">
      <div class="stat-item">
        <span class="stat-icon">📊</span>
        <div class="stat-info">
          <span class="stat-value">{{ totalLogs }}</span>
          <span class="stat-label">总记录</span>
        </div>
      </div>
      <div class="stat-item">
        <span class="stat-icon">👤</span>
        <div class="stat-info">
          <span class="stat-value">{{ uniqueUsers }}</span>
          <span class="stat-label">操作用户</span>
        </div>
      </div>
      <div class="stat-item">
        <span class="stat-icon">📅</span>
        <div class="stat-info">
          <span class="stat-value">{{ todayLogs }}</span>
          <span class="stat-label">今日记录</span>
        </div>
      </div>
      <div class="stat-item">
        <span class="stat-icon">⚠️</span>
        <div class="stat-info">
          <span class="stat-value">{{ errorLogs }}</span>
          <span class="stat-label">异常记录</span>
        </div>
      </div>
    </div>

    <!-- 筛选栏 -->
    <div class="filter-bar">
      <div class="filter-group">
        <div class="search-box">
          <span class="search-icon">🔍</span>
          <input 
            v-model="searchQuery" 
            placeholder="搜索用户名、操作内容..."
            @input="handleSearch"
          />
        </div>
        
        <select v-model="filterType" class="filter-select">
          <option value="">全部类型</option>
          <option value="LOGIN">登录</option>
          <option value="CREATE">创建</option>
          <option value="UPDATE">更新</option>
          <option value="DELETE">删除</option>
          <option value="EXPORT">导出</option>
          <option value="SYSTEM">系统</option>
        </select>
        
        <select v-model="filterStatus" class="filter-select">
          <option value="">全部状态</option>
          <option value="SUCCESS">成功</option>
          <option value="FAILED">失败</option>
        </select>
        
        <input 
          type="date" 
          v-model="filterDate"
          class="filter-date"
        />
      </div>
      
      <button class="btn-filter" @click="applyFilters">
        <span>🔍</span> 筛选
      </button>
    </div>

    <!-- 日志列表 -->
    <div class="logs-container">
      <div class="logs-table-wrapper">
        <table class="logs-table">
          <thead>
            <tr>
              <th class="col-time">时间</th>
              <th class="col-user">用户</th>
              <th class="col-type">类型</th>
              <th class="col-operation">操作内容</th>
              <th class="col-ip">IP地址</th>
              <th class="col-status">状态</th>
              <th class="col-duration">耗时</th>
              <th class="col-action">操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="log in paginatedLogs" :key="log.id" class="log-row">
              <td class="col-time">
                <div class="time-info">
                  <span class="time-date">{{ formatDate(log.createTime) }}</span>
                  <span class="time-hour">{{ formatTime(log.createTime) }}</span>
                </div>
              </td>
              <td class="col-user">
                <div class="user-info">
                  <span class="user-avatar">{{ log.username?.charAt(0) || 'U' }}</span>
                  <span class="user-name">{{ log.username || '系统' }}</span>
                </div>
              </td>
              <td class="col-type">
                <span class="type-badge" :class="getTypeClass(log.operation)">
                  {{ getOperationType(log.operation) }}
                </span>
              </td>
              <td class="col-operation">
                <span class="operation-text" :title="log.operation">
                  {{ log.operation }}
                </span>
              </td>
              <td class="col-ip">
                <span class="ip-text">{{ log.ip || '-' }}</span>
              </td>
              <td class="col-status">
                <span class="status-badge" :class="log.status === 1 ? 'success' : 'failed'">
                  {{ log.status === 1 ? '✓ 成功' : '✕ 失败' }}
                </span>
              </td>
              <td class="col-duration">
                <span class="duration-text" :class="getDurationClass(log.duration)">
                  {{ log.duration || 0 }}ms
                </span>
              </td>
              <td class="col-action">
                <button class="btn-view" @click="viewDetail(log)">查看</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- 分页 -->
      <div class="pagination">
        <div class="page-info">
          显示 {{ (currentPage - 1) * pageSize + 1 }} - {{ Math.min(currentPage * pageSize, filteredLogs.length) }} 条，
          共 {{ filteredLogs.length }} 条
        </div>
        <div class="page-controls">
          <button 
            class="page-btn" 
            :disabled="currentPage === 1"
            @click="currentPage--"
          >
            ← 上一页
          </button>
          <div class="page-numbers">
            <button 
              v-for="page in visiblePages" 
              :key="page"
              class="page-number"
              :class="{ active: page === currentPage }"
              @click="currentPage = page"
            >
              {{ page }}
            </button>
          </div>
          <button 
            class="page-btn" 
            :disabled="currentPage === totalPages"
            @click="currentPage++"
          >
            下一页 →
          </button>
        </div>
        <select v-model="pageSize" class="page-size">
          <option :value="10">10条/页</option>
          <option :value="20">20条/页</option>
          <option :value="50">50条/页</option>
        </select>
      </div>
    </div>

    <!-- 详情弹窗 -->
    <div v-if="showDetailModal" class="modal-overlay" @click.self="showDetailModal = false">
      <div class="modal-content">
        <div class="modal-header">
          <h3>日志详情</h3>
          <button class="btn-close" @click="showDetailModal = false">✕</button>
        </div>
        <div class="modal-body">
          <div class="detail-grid">
            <div class="detail-item">
              <span class="detail-label">日志ID</span>
              <span class="detail-value">#{{ currentLog.id }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">操作时间</span>
              <span class="detail-value">{{ formatDateTime(currentLog.createTime) }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">操作用户</span>
              <span class="detail-value">{{ currentLog.username || '系统' }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">操作类型</span>
              <span class="detail-value">{{ getOperationType(currentLog.operation) }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">IP地址</span>
              <span class="detail-value">{{ currentLog.ip || '-' }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">请求方法</span>
              <span class="detail-value method-badge">{{ currentLog.method || 'GET' }}</span>
            </div>
            <div class="detail-item full">
              <span class="detail-label">操作内容</span>
              <span class="detail-value">{{ currentLog.operation }}</span>
            </div>
            <div class="detail-item full">
              <span class="detail-label">请求参数</span>
              <pre class="detail-code">{{ formatJson(currentLog.params) }}</pre>
            </div>
            <div class="detail-item full" v-if="currentLog.errorMsg">
              <span class="detail-label">错误信息</span>
              <pre class="detail-code error">{{ currentLog.errorMsg }}</pre>
            </div>
            <div class="detail-item">
              <span class="detail-label">执行耗时</span>
              <span class="detail-value">{{ currentLog.duration || 0 }}ms</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">执行状态</span>
              <span class="detail-value" :class="currentLog.status === 1 ? 'success' : 'failed'">
                {{ currentLog.status === 1 ? '成功' : '失败' }}
              </span>
            </div>
          </div>
        </div>
        <div class="modal-actions">
          <button class="btn-cancel" @click="showDetailModal = false">关闭</button>
          <button class="btn-confirm" @click="copyDetail">📋 复制详情</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { logApi } from '../api'
import { showToast } from '../components/Toast.vue'
import { showConfirm } from '../components/ConfirmDialog.vue'

export default {
  name: 'Logs',
  setup() {
    const logs = ref([])
    const searchQuery = ref('')
    const filterType = ref('')
    const filterStatus = ref('')
    const filterDate = ref('')
    const currentPage = ref(1)
    const pageSize = ref(20)
    const showDetailModal = ref(false)
    const currentLog = ref({})

    // 统计数据
    const totalLogs = computed(() => logs.value.length)
    const uniqueUsers = computed(() => new Set(logs.value.map(l => l.username)).size)
    const todayLogs = computed(() => {
      const today = new Date().toDateString()
      return logs.value.filter(l => new Date(l.createTime).toDateString() === today).length
    })
    const errorLogs = computed(() => logs.value.filter(l => l.status !== 1).length)

    // 筛选后的日志
    const filteredLogs = computed(() => {
      let result = logs.value

      // 搜索筛选
      if (searchQuery.value) {
        const query = searchQuery.value.toLowerCase()
        result = result.filter(log =>
          log.username?.toLowerCase().includes(query) ||
          log.operation?.toLowerCase().includes(query) ||
          log.ip?.includes(query)
        )
      }

      // 类型筛选
      if (filterType.value) {
        result = result.filter(log => getOperationType(log.operation) === filterType.value)
      }

      // 状态筛选
      if (filterStatus.value) {
        const status = filterStatus.value === 'SUCCESS' ? 1 : 0
        result = result.filter(log => log.status === status)
      }

      // 日期筛选
      if (filterDate.value) {
        const filterDateStr = new Date(filterDate.value).toDateString()
        result = result.filter(log => new Date(log.createTime).toDateString() === filterDateStr)
      }

      return result.sort((a, b) => new Date(b.createTime) - new Date(a.createTime))
    })

    // 分页数据
    const paginatedLogs = computed(() => {
      const start = (currentPage.value - 1) * pageSize.value
      const end = start + pageSize.value
      return filteredLogs.value.slice(start, end)
    })

    const totalPages = computed(() => Math.ceil(filteredLogs.value.length / pageSize.value))

    const visiblePages = computed(() => {
      const pages = []
      const maxVisible = 5
      let start = Math.max(1, currentPage.value - Math.floor(maxVisible / 2))
      let end = Math.min(totalPages.value, start + maxVisible - 1)
      
      if (end - start < maxVisible - 1) {
        start = Math.max(1, end - maxVisible + 1)
      }
      
      for (let i = start; i <= end; i++) {
        pages.push(i)
      }
      return pages
    })

    const loadLogs = async () => {
      try {
        const res = await logApi.getAll()
        if (res.code === 200) {
          logs.value = res.data || []
        }
      } catch (error) {
        console.error('获取日志失败:', error)
        showToast({ type: 'error', message: '获取日志失败' })
      }
    }

    const getOperationType = (operation) => {
      if (!operation) return 'OTHER'
      const op = operation.toLowerCase()
      if (op.includes('login') || op.includes('auth')) return 'LOGIN'
      if (op.includes('create') || op.includes('save') || op.includes('add')) return 'CREATE'
      if (op.includes('update') || op.includes('edit')) return 'UPDATE'
      if (op.includes('delete') || op.includes('remove')) return 'DELETE'
      if (op.includes('get') || op.includes('list') || op.includes('query') || op.includes('find')) return 'QUERY'
      if (op.includes('export')) return 'EXPORT'
      if (op.includes('system')) return 'SYSTEM'
      return 'OTHER'
    }

    const getTypeClass = (operation) => {
      const type = getOperationType(operation)
      const classMap = {
        'LOGIN': 'login',
        'CREATE': 'create',
        'UPDATE': 'update',
        'DELETE': 'delete',
        'EXPORT': 'export',
        'SYSTEM': 'system',
        'OTHER': 'other'
      }
      return classMap[type] || 'other'
    }

    const getDurationClass = (duration) => {
      if (!duration) return ''
      if (duration < 100) return 'fast'
      if (duration < 500) return 'normal'
      return 'slow'
    }

    const formatDate = (dateStr) => {
      if (!dateStr) return ''
      const date = new Date(dateStr)
      return date.toLocaleDateString('zh-CN', { month: '2-digit', day: '2-digit' })
    }

    const formatTime = (dateStr) => {
      if (!dateStr) return ''
      const date = new Date(dateStr)
      return date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
    }

    const formatDateTime = (dateStr) => {
      if (!dateStr) return ''
      const date = new Date(dateStr)
      return date.toLocaleString('zh-CN')
    }

    const formatJson = (json) => {
      if (!json) return '{}'
      try {
        return JSON.stringify(JSON.parse(json), null, 2)
      } catch {
        return json
      }
    }

    const handleSearch = () => {
      currentPage.value = 1
    }

    const applyFilters = () => {
      currentPage.value = 1
      showToast({ type: 'success', message: '筛选已应用' })
    }

    const viewDetail = (log) => {
      currentLog.value = log
      showDetailModal.value = true
    }

    const copyDetail = () => {
      const detail = JSON.stringify(currentLog.value, null, 2)
      navigator.clipboard.writeText(detail).then(() => {
        showToast({ type: 'success', message: '已复制到剪贴板' })
      })
    }

    const exportLogs = () => {
      const dataStr = JSON.stringify(filteredLogs.value, null, 2)
      const dataBlob = new Blob([dataStr], { type: 'application/json' })
      const url = URL.createObjectURL(dataBlob)
      const link = document.createElement('a')
      link.href = url
      link.download = `logs-${new Date().toISOString().split('T')[0]}.json`
      link.click()
      URL.revokeObjectURL(url)
      showToast({ type: 'success', message: '日志已导出' })
    }

    const clearOldLogs = async () => {
      const confirmed = await showConfirm({
        title: '清理旧日志',
        message: '确定要清理30天前的日志记录吗？此操作不可恢复。',
        type: 'warning'
      })
      
      if (confirmed) {
        try {
          const res = await logApi.clearLogs()
          if (res.code === 200) {
            showToast({ type: 'success', message: '旧日志已清理' })
            await loadLogs()
          }
        } catch (error) {
          console.error('清理日志失败:', error)
          showToast({ type: 'error', message: '清理失败: ' + (error.message || '未知错误') })
        }
      }
    }

    onMounted(() => {
      loadLogs()
    })

    return {
      logs,
      searchQuery,
      filterType,
      filterStatus,
      filterDate,
      currentPage,
      pageSize,
      showDetailModal,
      currentLog,
      totalLogs,
      uniqueUsers,
      todayLogs,
      errorLogs,
      filteredLogs,
      paginatedLogs,
      totalPages,
      visiblePages,
      getOperationType,
      getTypeClass,
      getDurationClass,
      formatDate,
      formatTime,
      formatDateTime,
      formatJson,
      handleSearch,
      applyFilters,
      viewDetail,
      copyDetail,
      exportLogs,
      clearOldLogs
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
  margin-bottom: 20px;
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
  gap: 12px;
}

.btn-export,
.btn-clear {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 16px;
  background: #f1f5f9;
  border: none;
  border-radius: 10px;
  font-size: 14px;
  color: #475569;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-export:hover {
  background: #e0f2fe;
  color: #0284c7;
}

.btn-clear:hover {
  background: #fee2e2;
  color: #dc2626;
}

/* 统计栏 */
.stats-bar {
  display: flex;
  gap: 16px;
  margin-bottom: 20px;
  padding: 16px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.05);
}

.stat-item {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  background: #f8fafc;
  border-radius: 10px;
}

.stat-icon {
  font-size: 24px;
}

.stat-info {
  display: flex;
  flex-direction: column;
}

.stat-value {
  font-size: 20px;
  font-weight: 700;
  color: #1e293b;
}

.stat-label {
  font-size: 12px;
  color: #64748b;
}

/* 筛选栏 */
.filter-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 16px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.05);
}

.filter-group {
  display: flex;
  gap: 12px;
  align-items: center;
}

.search-box {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 16px;
  background: #f1f5f9;
  border-radius: 10px;
  border: 2px solid transparent;
  transition: all 0.3s;
}

.search-box:focus-within {
  background: #fff;
  border-color: #0ea5e9;
}

.search-box input {
  border: none;
  background: transparent;
  outline: none;
  font-size: 14px;
  width: 200px;
  color: #334155;
}

.filter-select,
.filter-date {
  padding: 10px 16px;
  border: 2px solid #e2e8f0;
  border-radius: 10px;
  font-size: 14px;
  color: #334155;
  background: #fff;
  outline: none;
  cursor: pointer;
  transition: all 0.3s;
}

.filter-select:focus,
.filter-date:focus {
  border-color: #0ea5e9;
}

.btn-filter {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  background: linear-gradient(135deg, #0ea5e9, #0284c7);
  color: white;
  border: none;
  border-radius: 10px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-filter:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(14, 165, 233, 0.4);
}

/* 日志容器 */
.logs-container {
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.05);
  overflow: hidden;
}

.logs-table-wrapper {
  overflow-x: auto;
}

.logs-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 14px;
}

.logs-table th {
  text-align: left;
  padding: 14px 16px;
  font-weight: 600;
  color: #64748b;
  background: #f8fafc;
  border-bottom: 1px solid #e2e8f0;
  white-space: nowrap;
}

.logs-table td {
  padding: 14px 16px;
  border-bottom: 1px solid #f1f5f9;
  vertical-align: middle;
}

.log-row:hover {
  background: #f8fafc;
}

.col-time { width: 100px; }
.col-user { width: 120px; }
.col-type { width: 80px; }
.col-operation { min-width: 200px; }
.col-ip { width: 120px; }
.col-status { width: 80px; }
.col-duration { width: 80px; }
.col-action { width: 80px; }

.time-info {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.time-date {
  font-size: 13px;
  color: #334155;
}

.time-hour {
  font-size: 12px;
  color: #94a3b8;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.user-avatar {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  background: linear-gradient(135deg, #38bdf8, #0ea5e9);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: 600;
  color: #fff;
}

.user-name {
  font-size: 13px;
  color: #334155;
}

.type-badge {
  display: inline-block;
  padding: 4px 10px;
  border-radius: 20px;
  font-size: 11px;
  font-weight: 500;
}

.type-badge.login { background: #dbeafe; color: #1d4ed8; }
.type-badge.create { background: #dcfce7; color: #15803d; }
.type-badge.update { background: #fef3c7; color: #a16207; }
.type-badge.delete { background: #fee2e2; color: #b91c1c; }
.type-badge.export { background: #f3e8ff; color: #7c3aed; }
.type-badge.system { background: #f1f5f9; color: #475569; }
.type-badge.other { background: #f1f5f9; color: #64748b; }

.operation-text {
  display: block;
  max-width: 250px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  color: #334155;
}

.ip-text {
  font-family: monospace;
  font-size: 13px;
  color: #64748b;
}

.status-badge {
  display: inline-block;
  padding: 4px 10px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;
}

.status-badge.success {
  background: #dcfce7;
  color: #15803d;
}

.status-badge.failed {
  background: #fee2e2;
  color: #b91c1c;
}

.duration-text {
  font-family: monospace;
  font-size: 13px;
}

.duration-text.fast { color: #22c55e; }
.duration-text.normal { color: #f59e0b; }
.duration-text.slow { color: #ef4444; }

.btn-view {
  padding: 6px 12px;
  background: #e0f2fe;
  color: #0284c7;
  border: none;
  border-radius: 6px;
  font-size: 12px;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-view:hover {
  background: #0ea5e9;
  color: white;
}

/* 分页 */
.pagination {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  border-top: 1px solid #f1f5f9;
  background: #fafafa;
}

.page-info {
  font-size: 13px;
  color: #64748b;
}

.page-controls {
  display: flex;
  align-items: center;
  gap: 8px;
}

.page-btn {
  padding: 8px 16px;
  background: #fff;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  font-size: 13px;
  color: #475569;
  cursor: pointer;
  transition: all 0.2s;
}

.page-btn:hover:not(:disabled) {
  background: #f0f9ff;
  border-color: #0ea5e9;
  color: #0ea5e9;
}

.page-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-numbers {
  display: flex;
  gap: 4px;
}

.page-number {
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fff;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  font-size: 13px;
  color: #475569;
  cursor: pointer;
  transition: all 0.2s;
}

.page-number:hover {
  background: #f0f9ff;
  border-color: #0ea5e9;
  color: #0ea5e9;
}

.page-number.active {
  background: linear-gradient(135deg, #0ea5e9, #0284c7);
  color: white;
  border-color: #0ea5e9;
}

.page-size {
  padding: 8px 12px;
  background: #fff;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  font-size: 13px;
  color: #475569;
  cursor: pointer;
  outline: none;
}

/* 弹窗 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.5);
  backdrop-filter: blur(4px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  padding: 20px;
}

.modal-content {
  background: #fff;
  border-radius: 16px;
  width: 100%;
  max-width: 600px;
  max-height: 90vh;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  animation: modalSlideIn 0.3s ease;
}

@keyframes modalSlideIn {
  from {
    opacity: 0;
    transform: translateY(-20px) scale(0.95);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  border-bottom: 1px solid #f1f5f9;
}

.modal-header h3 {
  margin: 0;
  font-size: 18px;
  color: #1e293b;
}

.btn-close {
  width: 32px;
  height: 32px;
  border: none;
  background: #f1f5f9;
  border-radius: 8px;
  cursor: pointer;
  font-size: 14px;
  color: #64748b;
  transition: all 0.2s;
}

.btn-close:hover {
  background: #e2e8f0;
  color: #475569;
}

.modal-body {
  padding: 24px;
  overflow-y: auto;
}

.detail-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.detail-item {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.detail-item.full {
  grid-column: span 2;
}

.detail-label {
  font-size: 12px;
  color: #94a3b8;
  font-weight: 500;
}

.detail-value {
  font-size: 14px;
  color: #1e293b;
  font-weight: 500;
}

.detail-value.success {
  color: #22c55e;
}

.detail-value.failed {
  color: #ef4444;
}

.method-badge {
  display: inline-block;
  padding: 4px 8px;
  background: #f1f5f9;
  border-radius: 4px;
  font-family: monospace;
  font-size: 12px;
}

.detail-code {
  background: #1e293b;
  color: #e2e8f0;
  padding: 16px;
  border-radius: 10px;
  font-family: monospace;
  font-size: 13px;
  overflow-x: auto;
  white-space: pre-wrap;
  word-break: break-word;
  margin: 0;
}

.detail-code.error {
  background: #fef2f2;
  color: #dc2626;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding: 20px 24px;
  border-top: 1px solid #f1f5f9;
  background: #fafafa;
}

.btn-cancel {
  padding: 10px 20px;
  border: none;
  background: #f1f5f9;
  color: #64748b;
  border-radius: 8px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-cancel:hover {
  background: #e2e8f0;
}

.btn-confirm {
  padding: 10px 20px;
  border: none;
  background: linear-gradient(135deg, #0ea5e9, #0284c7);
  color: white;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-confirm:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(14, 165, 233, 0.4);
}

/* 响应式 */
@media (max-width: 1024px) {
  .stats-bar {
    flex-wrap: wrap;
  }
  
  .stat-item {
    flex: 1 1 calc(50% - 8px);
  }
  
  .filter-bar {
    flex-direction: column;
    gap: 12px;
  }
  
  .filter-group {
    flex-wrap: wrap;
  }
  
  .pagination {
    flex-direction: column;
    gap: 12px;
  }
}

@media (max-width: 768px) {
  .stat-item {
    flex: 1 1 100%;
  }
  
  .detail-grid {
    grid-template-columns: 1fr;
  }
  
  .detail-item.full {
    grid-column: span 1;
  }
}
</style>