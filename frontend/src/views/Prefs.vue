<template>
  <div class="page-content">
    <div class="page-header">
      <div class="header-title">
        <h2>⚙️ 偏好设置</h2>
        <p>自定义您的个人偏好和体验</p>
      </div>
      <div class="header-actions">
        <button class="btn-reset" @click="resetSettings">
          <span>↩️</span> 恢复默认
        </button>
        <button class="btn-save" @click="saveSettings" :disabled="saving">
          <span v-if="saving">💾 保存中...</span>
          <span v-else>💾 保存设置</span>
        </button>
      </div>
    </div>

    <div class="settings-layout">
      <!-- 左侧：设置导航 -->
      <div class="settings-nav">
        <div 
          v-for="section in settingSections" 
          :key="section.id"
          class="nav-item"
          :class="{ active: currentSection === section.id }"
          @click="currentSection = section.id"
        >
          <span class="nav-icon">{{ section.icon }}</span>
          <div class="nav-info">
            <span class="nav-label">{{ section.label }}</span>
            <span class="nav-desc">{{ section.desc }}</span>
          </div>
        </div>
      </div>

      <!-- 右侧：设置内容 -->
      <div class="settings-content">
        <!-- 外观设置 -->
        <div v-if="currentSection === 'appearance'" class="settings-section">
          <h3 class="section-title">
            <span>🎨</span> 外观设置
          </h3>
          
          <div class="setting-group">
            <h4>主题模式</h4>
            <div class="theme-options">
              <div 
                class="theme-card" 
                :class="{ active: settings.theme === 'light' }"
                @click="settings.theme = 'light'"
              >
                <div class="theme-preview light">
                  <div class="preview-header"></div>
                  <div class="preview-content">
                    <div class="preview-line"></div>
                    <div class="preview-line short"></div>
                  </div>
                </div>
                <span class="theme-label">浅色模式</span>
              </div>
              <div 
                class="theme-card" 
                :class="{ active: settings.theme === 'dark' }"
                @click="settings.theme = 'dark'"
              >
                <div class="theme-preview dark">
                  <div class="preview-header"></div>
                  <div class="preview-content">
                    <div class="preview-line"></div>
                    <div class="preview-line short"></div>
                  </div>
                </div>
                <span class="theme-label">深色模式</span>
              </div>
              <div 
                class="theme-card" 
                :class="{ active: settings.theme === 'auto' }"
                @click="settings.theme = 'auto'"
              >
                <div class="theme-preview auto">
                  <div class="preview-header"></div>
                  <div class="preview-content">
                    <div class="preview-line"></div>
                    <div class="preview-line short"></div>
                  </div>
                </div>
                <span class="theme-label">跟随系统</span>
              </div>
            </div>
          </div>

          <div class="setting-group">
            <h4>主题色</h4>
            <div class="color-options">
              <div 
                v-for="color in themeColors" 
                :key="color.value"
                class="color-option"
                :class="{ active: settings.primaryColor === color.value }"
                @click="settings.primaryColor = color.value"
              >
                <div class="color-circle" :style="{ background: color.color }"></div>
                <span class="color-name">{{ color.name }}</span>
              </div>
            </div>
          </div>

          <div class="setting-group">
            <h4>界面密度</h4>
            <div class="density-options">
              <label class="radio-option">
                <input type="radio" v-model="settings.density" value="compact" />
                <span class="radio-label">紧凑</span>
                <span class="radio-desc">更小的间距，显示更多内容</span>
              </label>
              <label class="radio-option">
                <input type="radio" v-model="settings.density" value="default" />
                <span class="radio-label">默认</span>
                <span class="radio-desc">平衡的间距和可读性</span>
              </label>
              <label class="radio-option">
                <input type="radio" v-model="settings.density" value="comfortable" />
                <span class="radio-label">舒适</span>
                <span class="radio-desc">更大的间距，更好的阅读体验</span>
              </label>
            </div>
          </div>
        </div>

        <!-- 通知设置 -->
        <div v-if="currentSection === 'notifications'" class="settings-section">
          <h3 class="section-title">
            <span>🔔</span> 通知设置
          </h3>
          
          <div class="setting-group">
            <div class="switch-list">
              <div class="switch-item">
                <div class="switch-info">
                  <span class="switch-label">邮件通知</span>
                  <span class="switch-desc">接收重要更新和提醒的邮件</span>
                </div>
                <label class="switch">
                  <input type="checkbox" v-model="settings.emailNotify" />
                  <span class="slider"></span>
                </label>
              </div>
              
              <div class="switch-item">
                <div class="switch-info">
                  <span class="switch-label">桌面通知</span>
                  <span class="switch-desc">在桌面显示系统通知</span>
                </div>
                <label class="switch">
                  <input type="checkbox" v-model="settings.desktopNotify" />
                  <span class="slider"></span>
                </label>
              </div>
              
              <div class="switch-item">
                <div class="switch-info">
                  <span class="switch-label">任务提醒</span>
                  <span class="switch-desc">任务到期前发送提醒</span>
                </div>
                <label class="switch">
                  <input type="checkbox" v-model="settings.taskReminder" />
                  <span class="slider"></span>
                </label>
              </div>
              
              <div class="switch-item">
                <div class="switch-info">
                  <span class="switch-label">系统公告</span>
                  <span class="switch-desc">接收系统维护和更新公告</span>
                </div>
                <label class="switch">
                  <input type="checkbox" v-model="settings.systemAnnouncements" />
                  <span class="slider"></span>
                </label>
              </div>
            </div>
          </div>

          <div class="setting-group" v-if="settings.taskReminder">
            <h4>提醒时间</h4>
            <div class="reminder-options">
              <label class="checkbox-option">
                <input type="checkbox" v-model="settings.reminderTimes.before1Day" />
                <span>提前1天</span>
              </label>
              <label class="checkbox-option">
                <input type="checkbox" v-model="settings.reminderTimes.before1Hour" />
                <span>提前1小时</span>
              </label>
              <label class="checkbox-option">
                <input type="checkbox" v-model="settings.reminderTimes.before15Min" />
                <span>提前15分钟</span>
              </label>
            </div>
          </div>
        </div>

        <!-- 语言与地区 -->
        <div v-if="currentSection === 'language'" class="settings-section">
          <h3 class="section-title">
            <span>🌐</span> 语言与地区
          </h3>
          
          <div class="setting-group">
            <h4>界面语言</h4>
            <div class="select-wrapper">
              <select v-model="settings.language">
                <option value="zh-CN">简体中文</option>
                <option value="zh-TW">繁體中文</option>
                <option value="en-US">English</option>
                <option value="ja-JP">日本語</option>
              </select>
            </div>
          </div>

          <div class="setting-group">
            <h4>时区</h4>
            <div class="select-wrapper">
              <select v-model="settings.timezone">
                <option value="Asia/Shanghai">北京时间 (UTC+8)</option>
                <option value="Asia/Tokyo">东京时间 (UTC+9)</option>
                <option value="America/New_York">纽约时间 (UTC-5)</option>
                <option value="Europe/London">伦敦时间 (UTC+0)</option>
                <option value="Australia/Sydney">悉尼时间 (UTC+10)</option>
              </select>
            </div>
          </div>

          <div class="setting-group">
            <h4>日期格式</h4>
            <div class="radio-group">
              <label class="radio-option">
                <input type="radio" v-model="settings.dateFormat" value="YYYY-MM-DD" />
                <span>2024-03-20</span>
              </label>
              <label class="radio-option">
                <input type="radio" v-model="settings.dateFormat" value="YYYY/MM/DD" />
                <span>2024/03/20</span>
              </label>
              <label class="radio-option">
                <input type="radio" v-model="settings.dateFormat" value="DD/MM/YYYY" />
                <span>20/03/2024</span>
              </label>
              <label class="radio-option">
                <input type="radio" v-model="settings.dateFormat" value="MM/DD/YYYY" />
                <span>03/20/2024</span>
              </label>
            </div>
          </div>
        </div>

        <!-- 隐私设置 -->
        <div v-if="currentSection === 'privacy'" class="settings-section">
          <h3 class="section-title">
            <span>🔒</span> 隐私设置
          </h3>
          
          <div class="setting-group">
            <div class="switch-list">
              <div class="switch-item">
                <div class="switch-info">
                  <span class="switch-label">在线状态</span>
                  <span class="switch-desc">允许其他人看到我的在线状态</span>
                </div>
                <label class="switch">
                  <input type="checkbox" v-model="settings.showOnlineStatus" />
                  <span class="slider"></span>
                </label>
              </div>
              
              <div class="switch-item">
                <div class="switch-info">
                  <span class="switch-label">个人资料可见</span>
                  <span class="switch-desc">允许其他人查看我的详细资料</span>
                </div>
                <label class="switch">
                  <input type="checkbox" v-model="settings.showProfile" />
                  <span class="slider"></span>
                </label>
              </div>
              
              <div class="switch-item">
                <div class="switch-info">
                  <span class="switch-label">活动记录</span>
                  <span class="switch-desc">保存我的操作历史记录</span>
                </div>
                <label class="switch">
                  <input type="checkbox" v-model="settings.saveActivityLog" />
                  <span class="slider"></span>
                </label>
              </div>
            </div>
          </div>

          <div class="setting-group">
            <h4>数据管理</h4>
            <div class="data-actions">
              <button class="data-btn" @click="exportData">
                <span>📤</span> 导出我的数据
              </button>
              <button class="data-btn danger" @click="clearHistory">
                <span>🗑️</span> 清除历史记录
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { settingsApi } from '../api'
import { showToast } from '../components/Toast.vue'
import { showConfirm } from '../components/ConfirmDialog.vue'

export default {
  name: 'Prefs',
  setup() {
    const currentSection = ref('appearance')
    const saving = ref(false)

    const settingSections = [
      { id: 'appearance', label: '外观设置', desc: '主题、颜色、界面密度', icon: '🎨' },
      { id: 'notifications', label: '通知设置', desc: '邮件、桌面、任务提醒', icon: '🔔' },
      { id: 'language', label: '语言与地区', desc: '语言、时区、日期格式', icon: '🌐' },
      { id: 'privacy', label: '隐私设置', desc: '在线状态、数据管理', icon: '🔒' }
    ]

    const themeColors = [
      { name: '天空蓝', value: '#0ea5e9', color: 'linear-gradient(135deg, #0ea5e9, #0284c7)' },
      { name: '翡翠绿', value: '#10b981', color: 'linear-gradient(135deg, #34d399, #10b981)' },
      { name: '玫瑰粉', value: '#ec4899', color: 'linear-gradient(135deg, #f472b6, #ec4899)' },
      { name: '琥珀橙', value: '#f59e0b', color: 'linear-gradient(135deg, #fbbf24, #f59e0b)' },
      { name: '紫罗兰', value: '#8b5cf6', color: 'linear-gradient(135deg, #a78bfa, #8b5cf6)' },
      { name: '日落红', value: '#ef4444', color: 'linear-gradient(135deg, #f87171, #ef4444)' }
    ]

    const settings = reactive({
      theme: 'light',
      primaryColor: '#0ea5e9',
      density: 'default',
      emailNotify: true,
      desktopNotify: false,
      taskReminder: true,
      systemAnnouncements: true,
      reminderTimes: {
        before1Day: true,
        before1Hour: true,
        before15Min: false
      },
      language: 'zh-CN',
      timezone: 'Asia/Shanghai',
      dateFormat: 'YYYY-MM-DD',
      showOnlineStatus: true,
      showProfile: true,
      saveActivityLog: true
    })

    const loadSettings = async () => {
      try {
        const res = await settingsApi.get()
        if (res.code === 200 && res.data) {
          Object.assign(settings, res.data)
        }
      } catch (error) {
        console.error('获取设置失败:', error)
        // 使用默认设置
      }
    }

    const saveSettings = async () => {
      saving.value = true
      try {
        const res = await settingsApi.update(settings)
        if (res.code === 200) {
          showToast({ type: 'success', message: '设置保存成功' })
          // 应用设置
          applySettings()
        }
      } catch (error) {
        console.error('保存设置失败:', error)
        showToast({ type: 'error', message: '保存失败' })
      } finally {
        saving.value = false
      }
    }

    const applySettings = () => {
      // 应用主题
      document.documentElement.setAttribute('data-theme', settings.theme)
      // 应用主题色
      document.documentElement.style.setProperty('--primary-color', settings.primaryColor)
    }

    const resetSettings = async () => {
      const confirmed = await showConfirm({
        title: '恢复默认',
        message: '确定要恢复所有设置为默认值吗？',
        type: 'warning'
      })
      
      if (confirmed) {
        Object.assign(settings, {
          theme: 'light',
          primaryColor: '#0ea5e9',
          density: 'default',
          emailNotify: true,
          desktopNotify: false,
          taskReminder: true,
          systemAnnouncements: true,
          reminderTimes: { before1Day: true, before1Hour: true, before15Min: false },
          language: 'zh-CN',
          timezone: 'Asia/Shanghai',
          dateFormat: 'YYYY-MM-DD',
          showOnlineStatus: true,
          showProfile: true,
          saveActivityLog: true
        })
        showToast({ type: 'success', message: '已恢复默认设置' })
      }
    }

    const exportData = () => {
      const dataStr = JSON.stringify(settings, null, 2)
      const dataBlob = new Blob([dataStr], { type: 'application/json' })
      const url = URL.createObjectURL(dataBlob)
      const link = document.createElement('a')
      link.href = url
      link.download = `user-settings-${new Date().toISOString().split('T')[0]}.json`
      link.click()
      URL.revokeObjectURL(url)
      showToast({ type: 'success', message: '数据已导出' })
    }

    const clearHistory = async () => {
      const confirmed = await showConfirm({
        title: '清除历史',
        message: '确定要清除所有历史记录吗？此操作不可恢复。',
        type: 'danger'
      })
      
      if (confirmed) {
        showToast({ type: 'success', message: '历史记录已清除' })
      }
    }

    onMounted(() => {
      loadSettings()
    })

    return {
      currentSection,
      settingSections,
      themeColors,
      settings,
      saving,
      saveSettings,
      resetSettings,
      exportData,
      clearHistory
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
  gap: 12px;
}

.btn-reset {
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

.btn-reset:hover {
  background: #e2e8f0;
}

.btn-save {
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

.btn-save:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(14, 165, 233, 0.4);
}

.btn-save:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* 设置布局 */
.settings-layout {
  display: grid;
  grid-template-columns: 280px 1fr;
  gap: 24px;
}

/* 设置导航 */
.settings-nav {
  background: #fff;
  border-radius: 16px;
  padding: 12px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.05);
  height: fit-content;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 14px;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s;
  margin-bottom: 4px;
}

.nav-item:hover {
  background: #f8fafc;
}

.nav-item.active {
  background: linear-gradient(135deg, #0ea5e9, #0284c7);
  color: white;
}

.nav-icon {
  font-size: 20px;
}

.nav-info {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.nav-label {
  font-size: 14px;
  font-weight: 600;
}

.nav-desc {
  font-size: 12px;
  opacity: 0.7;
}

/* 设置内容 */
.settings-content {
  background: #fff;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.05);
}

.settings-section {
  animation: sectionFadeIn 0.3s ease;
}

@keyframes sectionFadeIn {
  from { opacity: 0; transform: translateX(10px); }
  to { opacity: 1; transform: translateX(0); }
}

.section-title {
  font-size: 18px;
  font-weight: 600;
  color: #1e293b;
  margin: 0 0 24px 0;
  display: flex;
  align-items: center;
  gap: 10px;
  padding-bottom: 16px;
  border-bottom: 1px solid #f1f5f9;
}

.section-title span {
  font-size: 22px;
}

/* 设置组 */
.setting-group {
  margin-bottom: 32px;
}

.setting-group:last-child {
  margin-bottom: 0;
}

.setting-group h4 {
  font-size: 14px;
  font-weight: 600;
  color: #64748b;
  margin: 0 0 16px 0;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

/* 主题选项 */
.theme-options {
  display: flex;
  gap: 16px;
}

.theme-card {
  flex: 1;
  padding: 16px;
  background: #f8fafc;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s;
  border: 2px solid transparent;
  text-align: center;
}

.theme-card:hover {
  background: #f1f5f9;
}

.theme-card.active {
  border-color: #0ea5e9;
  background: #f0f9ff;
}

.theme-preview {
  width: 100%;
  aspect-ratio: 4/3;
  border-radius: 8px;
  margin-bottom: 12px;
  overflow: hidden;
}

.theme-preview.light {
  background: #fff;
  border: 1px solid #e2e8f0;
}

.theme-preview.dark {
  background: #1e293b;
}

.theme-preview.auto {
  background: linear-gradient(135deg, #fff 50%, #1e293b 50%);
  border: 1px solid #e2e8f0;
}

.preview-header {
  height: 20%;
  background: rgba(14, 165, 233, 0.2);
}

.theme-preview.dark .preview-header {
  background: rgba(56, 189, 248, 0.3);
}

.preview-content {
  padding: 12px;
}

.preview-line {
  height: 8px;
  background: #e2e8f0;
  border-radius: 4px;
  margin-bottom: 8px;
}

.preview-line.short {
  width: 60%;
}

.theme-preview.dark .preview-line {
  background: #334155;
}

.theme-label {
  font-size: 14px;
  font-weight: 500;
  color: #1e293b;
}

/* 颜色选项 */
.color-options {
  display: flex;
  gap: 16px;
  flex-wrap: wrap;
}

.color-option {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 8px;
  border-radius: 12px;
  transition: all 0.3s;
}

.color-option:hover {
  background: #f8fafc;
}

.color-circle {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  border: 3px solid transparent;
  transition: all 0.3s;
}

.color-option.active .color-circle {
  border-color: #1e293b;
  transform: scale(1.1);
}

.color-name {
  font-size: 12px;
  color: #64748b;
}

/* 密度选项 */
.density-options {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.radio-option {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  background: #f8fafc;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s;
}

.radio-option:hover {
  background: #f1f5f9;
}

.radio-label {
  font-size: 14px;
  font-weight: 600;
  color: #1e293b;
  min-width: 60px;
}

.radio-desc {
  font-size: 13px;
  color: #64748b;
}

/* 开关列表 */
.switch-list {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.switch-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  background: #f8fafc;
  border-radius: 12px;
}

.switch-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.switch-label {
  font-size: 15px;
  font-weight: 500;
  color: #1e293b;
}

.switch-desc {
  font-size: 13px;
  color: #64748b;
}

/* 开关样式 */
.switch {
  position: relative;
  width: 52px;
  height: 28px;
  cursor: pointer;
}

.switch input {
  opacity: 0;
  width: 0;
  height: 0;
}

.slider {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: #e2e8f0;
  border-radius: 28px;
  transition: 0.3s;
}

.slider:before {
  content: '';
  position: absolute;
  height: 22px;
  width: 22px;
  left: 3px;
  bottom: 3px;
  background: white;
  border-radius: 50%;
  transition: 0.3s;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.switch input:checked + .slider {
  background: #0ea5e9;
}

.switch input:checked + .slider:before {
  transform: translateX(24px);
}

/* 提醒选项 */
.reminder-options {
  display: flex;
  gap: 16px;
}

.checkbox-option {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 16px;
  background: #f8fafc;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.3s;
}

.checkbox-option:hover {
  background: #f1f5f9;
}

.checkbox-option span {
  font-size: 14px;
  color: #475569;
}

/* 选择框 */
.select-wrapper {
  position: relative;
}

.select-wrapper select {
  width: 100%;
  padding: 12px 16px;
  border: 2px solid #e2e8f0;
  border-radius: 10px;
  font-size: 14px;
  color: #334155;
  background: #fff;
  cursor: pointer;
  outline: none;
  transition: all 0.3s;
  appearance: none;
}

.select-wrapper::after {
  content: '▼';
  position: absolute;
  right: 16px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 10px;
  color: #64748b;
  pointer-events: none;
}

.select-wrapper select:focus {
  border-color: #0ea5e9;
}

/* 单选组 */
.radio-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.radio-group .radio-option {
  padding: 12px 16px;
}

/* 数据管理 */
.data-actions {
  display: flex;
  gap: 12px;
}

.data-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 20px;
  background: #f8fafc;
  border: 1px solid #e2e8f0;
  border-radius: 10px;
  font-size: 14px;
  color: #475569;
  cursor: pointer;
  transition: all 0.3s;
}

.data-btn:hover {
  background: #f1f5f9;
  border-color: #cbd5e1;
}

.data-btn.danger {
  color: #dc2626;
  border-color: #fecaca;
  background: #fef2f2;
}

.data-btn.danger:hover {
  background: #fee2e2;
  border-color: #fca5a5;
}

/* 响应式 */
@media (max-width: 1024px) {
  .settings-layout {
    grid-template-columns: 1fr;
  }
  
  .settings-nav {
    display: flex;
    overflow-x: auto;
    padding: 8px;
  }
  
  .nav-item {
    flex-shrink: 0;
    margin-bottom: 0;
    margin-right: 8px;
  }
  
  .nav-desc {
    display: none;
  }
  
  .theme-options {
    flex-direction: column;
  }
  
  .reminder-options {
    flex-wrap: wrap;
  }
}
</style>