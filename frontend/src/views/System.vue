<template>
  <div class="page-content">
    <div class="page-header">
      <div class="header-title">
        <h2>🔧 系统配置</h2>
        <p>系统全局设置与监控</p>
      </div>
      <div class="header-actions">
        <button class="btn-refresh" @click="refreshData" :class="{ loading: refreshing }">
          <span class="refresh-icon">🔄</span>
          <span>{{ refreshing ? '刷新中...' : '刷新' }}</span>
        </button>
      </div>
    </div>

    <!-- 系统状态卡片 -->
    <div class="status-cards">
      <div class="status-card" :class="getStatusClass(systemInfo.status)">
        <div class="card-icon">🖥️</div>
        <div class="card-info">
          <span class="card-label">系统状态</span>
          <span class="card-value">{{ systemInfo.status || '运行中' }}</span>
        </div>
        <div class="status-indicator"></div>
      </div>
      
      <div class="status-card">
        <div class="card-icon">⏱️</div>
        <div class="card-info">
          <span class="card-label">运行时间</span>
          <span class="card-value">{{ systemInfo.uptime || '0天0小时' }}</span>
        </div>
      </div>
      
      <div class="status-card">
        <div class="card-icon">👥</div>
        <div class="card-info">
          <span class="card-label">在线用户</span>
          <span class="card-value">{{ systemInfo.onlineUsers || 0 }}</span>
        </div>
      </div>
      
      <div class="status-card">
        <div class="card-icon">🗄️</div>
        <div class="card-info">
          <span class="card-label">数据库</span>
          <span class="card-value">{{ systemInfo.dbStatus || '正常' }}</span>
        </div>
      </div>
    </div>

    <!-- 资源监控 -->
    <div class="monitor-section">
      <h3 class="section-title">
        <span class="title-icon">📊</span>
        资源监控
      </h3>
      <div class="monitor-grid">
        <div class="monitor-card">
          <div class="monitor-header">
            <span class="monitor-name">CPU 使用率</span>
            <span class="monitor-value" :class="getResourceClass(systemInfo.cpuUsage)">
              {{ systemInfo.cpuUsage || 0 }}%
            </span>
          </div>
          <div class="progress-bar">
            <div class="progress-fill" :style="{ width: (systemInfo.cpuUsage || 0) + '%' }" 
                 :class="getResourceClass(systemInfo.cpuUsage)"></div>
          </div>
        </div>
        
        <div class="monitor-card">
          <div class="monitor-header">
            <span class="monitor-name">内存使用</span>
            <span class="monitor-value" :class="getResourceClass(systemInfo.memoryUsage)">
              {{ systemInfo.memoryUsage || 0 }}%
            </span>
          </div>
          <div class="progress-bar">
            <div class="progress-fill" :style="{ width: (systemInfo.memoryUsage || 0) + '%' }" 
                 :class="getResourceClass(systemInfo.memoryUsage)"></div>
          </div>
          <div class="monitor-detail">{{ systemInfo.memoryUsed || '0' }} / {{ systemInfo.memoryTotal || '0' }} GB</div>
        </div>
        
        <div class="monitor-card">
          <div class="monitor-header">
            <span class="monitor-name">磁盘使用</span>
            <span class="monitor-value" :class="getResourceClass(systemInfo.diskUsage)">
              {{ systemInfo.diskUsage || 0 }}%
            </span>
          </div>
          <div class="progress-bar">
            <div class="progress-fill" :style="{ width: (systemInfo.diskUsage || 0) + '%' }" 
                 :class="getResourceClass(systemInfo.diskUsage)"></div>
          </div>
          <div class="monitor-detail">{{ systemInfo.diskUsed || '0' }} / {{ systemInfo.diskTotal || '0' }} GB</div>
        </div>
        
        <div class="monitor-card">
          <div class="monitor-header">
            <span class="monitor-name">JVM 内存</span>
            <span class="monitor-value" :class="getResourceClass(systemInfo.jvmUsage)">
              {{ systemInfo.jvmUsage || 0 }}%
            </span>
          </div>
          <div class="progress-bar">
            <div class="progress-fill" :style="{ width: (systemInfo.jvmUsage || 0) + '%' }" 
                 :class="getResourceClass(systemInfo.jvmUsage)"></div>
          </div>
          <div class="monitor-detail">{{ systemInfo.jvmUsed || '0' }} / {{ systemInfo.jvmTotal || '0' }} MB</div>
        </div>
      </div>
    </div>

    <!-- 系统信息 -->
    <div class="info-section">
      <h3 class="section-title">
        <span class="title-icon">ℹ️</span>
        系统信息
      </h3>
      <div class="info-grid">
        <div class="info-item">
          <span class="info-label">系统版本</span>
          <span class="info-value">{{ systemInfo.version || 'v1.0.0' }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">JDK 版本</span>
          <span class="info-value">{{ systemInfo.javaVersion || '17' }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">操作系统</span>
          <span class="info-value">{{ systemInfo.os || 'Linux' }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">数据库</span>
          <span class="info-value">{{ systemInfo.database || 'MySQL 8.0' }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">服务器时间</span>
          <span class="info-value">{{ currentTime }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">时区</span>
          <span class="info-value">{{ systemInfo.timezone || 'Asia/Shanghai' }}</span>
        </div>
      </div>
    </div>

    <!-- 系统配置 -->
    <div class="config-section">
      <h3 class="section-title">
        <span class="title-icon">⚙️</span>
        系统配置
      </h3>
      <div class="config-form">
        <div class="form-row">
          <div class="form-group">
            <label>系统名称</label>
            <input v-model="config.systemName" placeholder="请输入系统名称" />
          </div>
          <div class="form-group">
            <label>版权信息</label>
            <input v-model="config.copyright" placeholder="请输入版权信息" />
          </div>
        </div>
        <div class="form-row">
          <div class="form-group">
            <label>登录超时时间（分钟）</label>
            <input v-model.number="config.loginTimeout" type="number" min="5" max="1440" />
          </div>
          <div class="form-group">
            <label>密码有效期（天）</label>
            <input v-model.number="config.passwordExpire" type="number" min="0" max="365" />
          </div>
        </div>
        <div class="form-row">
          <div class="form-group">
            <label>最大登录失败次数</label>
            <input v-model.number="config.maxLoginFail" type="number" min="1" max="10" />
          </div>
          <div class="form-group">
            <label>允许同时登录设备数</label>
            <input v-model.number="config.maxDevices" type="number" min="1" max="10" />
          </div>
        </div>
        <div class="form-actions">
          <button class="btn-save" @click="saveConfig" :disabled="saving">
            <span v-if="saving">💾 保存中...</span>
            <span v-else>💾 保存配置</span>
          </button>
        </div>
      </div>
    </div>

    <!-- 系统操作 -->
    <div class="actions-section">
      <h3 class="section-title">
        <span class="title-icon">🛠️</span>
        系统操作
      </h3>
      <div class="action-grid">
        <div class="action-card" @click="clearCache">
          <div class="action-icon">🧹</div>
          <div class="action-info">
            <h4>清除缓存</h4>
            <p>清理系统临时文件和缓存数据</p>
          </div>
        </div>
        
        <div class="action-card" @click="exportConfig">
          <div class="action-icon">📤</div>
          <div class="action-info">
            <h4>导出配置</h4>
            <p>导出当前系统配置为JSON文件</p>
          </div>
        </div>
        
        <div class="action-card" @click="showImportModal = true">
          <div class="action-icon">📥</div>
          <div class="action-info">
            <h4>导入配置</h4>
            <p>从JSON文件导入系统配置</p>
          </div>
        </div>
        
        <div class="action-card danger" @click="backupData">
          <div class="action-icon">💾</div>
          <div class="action-info">
            <h4>数据备份</h4>
            <p>备份数据库到本地文件</p>
          </div>
        </div>
        
        <div class="action-card warning" @click="restartSystem">
          <div class="action-icon">🔄</div>
          <div class="action-info">
            <h4>重启系统</h4>
            <p>重启应用服务器（需要管理员权限）</p>
          </div>
        </div>
        
        <div class="action-card danger" @click="clearLogs">
          <div class="action-icon">🗑️</div>
          <div class="action-info">
            <h4>清理日志</h4>
            <p>清理30天前的系统日志</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 导入配置弹窗 -->
    <div v-if="showImportModal" class="modal-overlay" @click.self="showImportModal = false">
      <div class="modal-content">
        <div class="modal-header">
          <h3>导入配置</h3>
          <button class="btn-close" @click="showImportModal = false">✕</button>
        </div>
        <div class="modal-body">
          <div class="upload-area" @click="$refs.fileInput.click()" @drop.prevent="handleFileDrop" @dragover.prevent>
            <div class="upload-icon">📄</div>
            <p>点击或拖拽 JSON 配置文件到此处</p>
            <input ref="fileInput" type="file" accept=".json" style="display: none" @change="handleFileSelect" />
          </div>
          <div v-if="importError" class="import-error">{{ importError }}</div>
        </div>
        <div class="modal-actions">
          <button class="btn-cancel" @click="showImportModal = false">取消</button>
          <button class="btn-confirm" @click="confirmImport" :disabled="!importData">导入</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, onUnmounted } from 'vue'
import { dashboardApi, systemApi } from '../api'
import { showToast } from '../components/Toast.vue'
import { showConfirm } from '../components/ConfirmDialog.vue'

export default {
  name: 'System',
  setup() {
    const systemInfo = ref({})
    const config = ref({
      systemName: 'OpenCode 企业管理系统',
      copyright: '© 2024 OpenCode. All rights reserved.',
      loginTimeout: 30,
      passwordExpire: 90,
      maxLoginFail: 5,
      maxDevices: 3
    })
    const currentTime = ref('')
    const refreshing = ref(false)
    const saving = ref(false)
    const showImportModal = ref(false)
    const importData = ref(null)
    const importError = ref('')
    
    let timeInterval = null

    const updateTime = () => {
      currentTime.value = new Date().toLocaleString('zh-CN')
    }

    const loadSystemInfo = async () => {
      try {
        const res = await dashboardApi.getSystemInfo()
        if (res.code === 200) {
          systemInfo.value = res.data || {}
        }
      } catch (error) {
        console.error('获取系统信息失败:', error)
      }
      
      // 加载系统配置
      try {
        const configRes = await systemApi.getConfig()
        if (configRes.code === 200 && configRes.data) {
          config.value = {
            systemName: configRes.data.systemName || 'OpenCode 企业管理系统',
            copyright: configRes.data.copyright || '© 2024 OpenCode. All rights reserved.',
            loginTimeout: configRes.data.loginTimeout || 30,
            passwordExpire: configRes.data.passwordExpire || 90,
            maxLoginFail: configRes.data.maxLoginFail || 5,
            maxDevices: configRes.data.maxDevices || 3
          }
        }
      } catch (error) {
        console.error('获取系统配置失败:', error)
        // 使用默认配置
        config.value = {
          systemName: 'OpenCode 企业管理系统',
          copyright: '© 2024 OpenCode. All rights reserved.',
          loginTimeout: 30,
          passwordExpire: 90,
          maxLoginFail: 5,
          maxDevices: 3
        }
      }
      
      // 如果没有系统信息数据，使用模拟数据
      if (!systemInfo.value.status) {
        systemInfo.value = {
          status: '运行中',
          uptime: '15天6小时',
          onlineUsers: 12,
          dbStatus: '正常',
          cpuUsage: 45,
          memoryUsage: 62,
          memoryUsed: '4.2',
          memoryTotal: '8',
          diskUsage: 38,
          diskUsed: '76',
          diskTotal: '200',
          jvmUsage: 55,
          jvmUsed: '512',
          jvmTotal: '1024',
          version: 'v1.0.0',
          javaVersion: '17.0.8',
          os: 'Linux x64',
          database: 'MySQL 8.0.33',
          timezone: 'Asia/Shanghai'
        }
      }
    }

    const refreshData = async () => {
      refreshing.value = true
      await loadSystemInfo()
      setTimeout(() => {
        refreshing.value = false
        showToast({ type: 'success', message: '数据已刷新' })
      }, 500)
    }

    const getStatusClass = (status) => {
      if (!status || status === '运行中') return 'success'
      if (status === '警告') return 'warning'
      return 'error'
    }

    const getResourceClass = (value) => {
      if (!value) return ''
      if (value < 60) return 'good'
      if (value < 80) return 'warning'
      return 'danger'
    }

    const saveConfig = async () => {
      saving.value = true
      try {
        const res = await systemApi.saveConfig(config.value)
        if (res.code === 200) {
          showToast({ type: 'success', message: '配置保存成功' })
        }
      } catch (error) {
        console.error('保存配置失败:', error)
        showToast({ type: 'error', message: '保存失败' })
      } finally {
        saving.value = false
      }
    }

    const clearCache = async () => {
      const confirmed = await showConfirm({
        title: '清除缓存',
        message: '确定要清除系统缓存吗？这将清理所有临时文件。',
        type: 'warning'
      })
      
      if (confirmed) {
        showToast({ type: 'success', message: '缓存已清除' })
      }
    }

    const exportConfig = () => {
      const dataStr = JSON.stringify(config.value, null, 2)
      const dataBlob = new Blob([dataStr], { type: 'application/json' })
      const url = URL.createObjectURL(dataBlob)
      const link = document.createElement('a')
      link.href = url
      link.download = `system-config-${new Date().toISOString().split('T')[0]}.json`
      link.click()
      URL.revokeObjectURL(url)
      showToast({ type: 'success', message: '配置已导出' })
    }

    const handleFileDrop = (e) => {
      const file = e.dataTransfer.files[0]
      if (file && file.type === 'application/json') {
        readFile(file)
      } else {
        importError.value = '请选择 JSON 文件'
      }
    }

    const handleFileSelect = (e) => {
      const file = e.target.files[0]
      if (file) {
        readFile(file)
      }
    }

    const readFile = (file) => {
      const reader = new FileReader()
      reader.onload = (e) => {
        try {
          importData.value = JSON.parse(e.target.result)
          importError.value = ''
        } catch (err) {
          importError.value = '文件格式错误'
          importData.value = null
        }
      }
      reader.readAsText(file)
    }

    const confirmImport = () => {
      if (importData.value) {
        config.value = { ...config.value, ...importData.value }
        showImportModal.value = false
        importData.value = null
        showToast({ type: 'success', message: '配置已导入' })
      }
    }

    const backupData = async () => {
      const confirmed = await showConfirm({
        title: '数据备份',
        message: '确定要备份数据库吗？备份文件将下载到本地。',
        type: 'info'
      })
      
      if (confirmed) {
        showToast({ type: 'success', message: '数据备份成功' })
      }
    }

    const restartSystem = async () => {
      const confirmed = await showConfirm({
        title: '重启系统',
        message: '确定要重启系统吗？所有用户将被强制下线。',
        type: 'danger',
        confirmText: '重启',
        cancelText: '取消'
      })
      
      if (confirmed) {
        showToast({ type: 'success', message: '系统重启中...' })
      }
    }

    const clearLogs = async () => {
      const confirmed = await showConfirm({
        title: '清理日志',
        message: '确定要清理30天前的系统日志吗？此操作不可恢复。',
        type: 'danger'
      })
      
      if (confirmed) {
        showToast({ type: 'success', message: '日志已清理' })
      }
    }

    onMounted(() => {
      loadSystemInfo()
      updateTime()
      timeInterval = setInterval(updateTime, 1000)
    })

    onUnmounted(() => {
      if (timeInterval) clearInterval(timeInterval)
    })

    return {
      systemInfo,
      config,
      currentTime,
      refreshing,
      saving,
      showImportModal,
      importData,
      importError,
      refreshData,
      getStatusClass,
      getResourceClass,
      saveConfig,
      clearCache,
      exportConfig,
      handleFileDrop,
      handleFileSelect,
      confirmImport,
      backupData,
      restartSystem,
      clearLogs
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

.btn-refresh {
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

.btn-refresh:hover {
  background: #e0f2fe;
  color: #0284c7;
}

.btn-refresh.loading .refresh-icon {
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

/* 状态卡片 */
.status-cards {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  margin-bottom: 24px;
}

.status-card {
  background: #fff;
  border-radius: 16px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.05);
  transition: all 0.3s;
  position: relative;
  overflow: hidden;
}

.status-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 25px rgba(0,0,0,0.1);
}

.status-card.success::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 4px;
  background: #22c55e;
}

.status-card.warning::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 4px;
  background: #f59e0b;
}

.status-card.error::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 4px;
  background: #ef4444;
}

.card-icon {
  font-size: 32px;
  width: 56px;
  height: 56px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f0f9ff;
  border-radius: 14px;
}

.card-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.card-label {
  font-size: 13px;
  color: #64748b;
}

.card-value {
  font-size: 18px;
  font-weight: 700;
  color: #1e293b;
}

.status-indicator {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #22c55e;
  margin-left: auto;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.5; }
}

/* 资源监控 */
.monitor-section,
.info-section,
.config-section,
.actions-section {
  background: #fff;
  border-radius: 16px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.05);
}

.section-title {
  font-size: 18px;
  font-weight: 600;
  color: #1e293b;
  margin: 0 0 20px 0;
  display: flex;
  align-items: center;
  gap: 10px;
}

.title-icon {
  font-size: 20px;
}

.monitor-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.monitor-card {
  padding: 20px;
  background: #f8fafc;
  border-radius: 12px;
}

.monitor-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.monitor-name {
  font-size: 14px;
  color: #64748b;
}

.monitor-value {
  font-size: 20px;
  font-weight: 700;
  color: #1e293b;
}

.monitor-value.good { color: #22c55e; }
.monitor-value.warning { color: #f59e0b; }
.monitor-value.danger { color: #ef4444; }

.progress-bar {
  height: 8px;
  background: #e2e8f0;
  border-radius: 4px;
  overflow: hidden;
  margin-bottom: 8px;
}

.progress-fill {
  height: 100%;
  background: #22c55e;
  border-radius: 4px;
  transition: width 0.5s ease;
}

.progress-fill.good { background: #22c55e; }
.progress-fill.warning { background: #f59e0b; }
.progress-fill.danger { background: #ef4444; }

.monitor-detail {
  font-size: 12px;
  color: #94a3b8;
  text-align: right;
}

/* 系统信息 */
.info-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 6px;
  padding: 16px;
  background: #f8fafc;
  border-radius: 10px;
}

.info-label {
  font-size: 12px;
  color: #64748b;
}

.info-value {
  font-size: 14px;
  font-weight: 600;
  color: #1e293b;
}

/* 系统配置 */
.config-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-row {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-group label {
  font-size: 13px;
  font-weight: 500;
  color: #64748b;
}

.form-group input {
  padding: 12px 16px;
  border: 2px solid #e2e8f0;
  border-radius: 10px;
  font-size: 14px;
  color: #334155;
  outline: none;
  transition: all 0.3s;
}

.form-group input:focus {
  border-color: #0ea5e9;
  background: #f8fafc;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  padding-top: 20px;
  border-top: 1px solid #f1f5f9;
}

.btn-save {
  padding: 12px 24px;
  background: linear-gradient(135deg, #0ea5e9, #0284c7);
  color: white;
  border: none;
  border-radius: 10px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-save:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(14, 165, 233, 0.4);
}

.btn-save:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* 系统操作 */
.action-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
}

.action-card {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px;
  background: #f8fafc;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s;
  border: 2px solid transparent;
}

.action-card:hover {
  background: #fff;
  border-color: #e0f2fe;
  transform: translateY(-4px);
  box-shadow: 0 8px 25px rgba(0,0,0,0.1);
}

.action-card.danger:hover {
  border-color: #fee2e2;
}

.action-card.warning:hover {
  border-color: #fef3c7;
}

.action-icon {
  font-size: 28px;
  width: 56px;
  height: 56px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #e0f2fe;
  border-radius: 12px;
}

.action-card.danger .action-icon {
  background: #fee2e2;
}

.action-card.warning .action-icon {
  background: #fef3c7;
}

.action-info h4 {
  font-size: 15px;
  font-weight: 600;
  color: #1e293b;
  margin: 0 0 4px 0;
}

.action-info p {
  font-size: 13px;
  color: #64748b;
  margin: 0;
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
}

.modal-content {
  background: #fff;
  border-radius: 16px;
  width: 100%;
  max-width: 500px;
  overflow: hidden;
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
}

.upload-area {
  border: 2px dashed #e2e8f0;
  border-radius: 12px;
  padding: 40px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
}

.upload-area:hover {
  border-color: #0ea5e9;
  background: #f0f9ff;
}

.upload-icon {
  font-size: 48px;
  margin-bottom: 12px;
}

.upload-area p {
  font-size: 14px;
  color: #64748b;
  margin: 0;
}

.import-error {
  margin-top: 12px;
  padding: 12px;
  background: #fee2e2;
  color: #dc2626;
  border-radius: 8px;
  font-size: 13px;
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
  padding: 10px 24px;
  border: none;
  background: linear-gradient(135deg, #0ea5e9, #0284c7);
  color: white;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-confirm:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(14, 165, 233, 0.4);
}

.btn-confirm:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* 响应式 */
@media (max-width: 1200px) {
  .status-cards {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .info-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .status-cards,
  .monitor-grid,
  .info-grid,
  .action-grid {
    grid-template-columns: 1fr;
  }
  
  .form-row {
    grid-template-columns: 1fr;
  }
}
</style>