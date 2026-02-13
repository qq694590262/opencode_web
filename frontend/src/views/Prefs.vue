<template>
  <div class="page-content">
    <div class="page-header">
      <h2>⚙️ 偏好设置</h2>
      <p>自定义个人偏好</p>
    </div>

    <div class="settings-card">
      <div class="setting-item">
        <div class="setting-info">
          <h4>深色模式</h4>
          <p>切换深色/浅色主题</p>
        </div>
        <label class="switch">
          <input type="checkbox" />
          <span class="slider"></span>
        </label>
      </div>

      <div class="setting-item">
        <div class="setting-info">
          <h4>邮件通知</h4>
          <p>接收重要邮件通知</p>
        </div>
        <label class="switch">
          <input type="checkbox" checked />
          <span class="slider"></span>
        </label>
      </div>

      <div class="setting-item">
        <div class="setting-info">
          <h4>桌面通知</h4>
          <p>启用浏览器桌面通知</p>
        </div>
        <label class="switch">
          <input type="checkbox" />
          <span class="slider"></span>
        </label>
      </div>

      <div class="setting-item">
        <div class="setting-info">
          <h4>语言</h4>
          <p>选择界面显示语言</p>
        </div>
        <select class="select-input">
          <option>简体中文</option>
          <option>English</option>
        </select>
      </div>

      <div class="setting-item">
        <div class="setting-info">
          <h4>时区</h4>
          <p>设置本地时区</p>
        </div>
        <select class="select-input">
          <option>Asia/Shanghai (UTC+8)</option>
          <option>America/New_York (UTC-5)</option>
        </select>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Prefs'
}
</script>

<style scoped>
.page-content { animation: fadeIn 0.3s ease; }
@keyframes fadeIn { from { opacity: 0; transform: translateY(10px); } to { opacity: 1; transform: translateY(0); } }

.page-header { margin-bottom: 24px; }
.page-header h2 { font-size: 24px; font-weight: 700; color: #0c4a6e; margin: 0 0 8px 0; }
.page-header p { font-size: 14px; color: #64748b; margin: 0; }

.settings-card { background: #fff; border-radius: 16px; padding: 24px; box-shadow: 0 4px 15px rgba(0,0,0,0.05); }

.setting-item { display: flex; justify-content: space-between; align-items: center; padding: 20px 0; border-bottom: 1px solid #f1f5f9; }
.setting-item:last-child { border-bottom: none; }

.setting-info h4 { font-size: 15px; font-weight: 600; color: #1e293b; margin: 0 0 4px 0; }
.setting-info p { font-size: 13px; color: #64748b; margin: 0; }

.switch { position: relative; width: 52px; height: 28px; }
.switch input { opacity: 0; width: 0; height: 0; }
.slider { position: absolute; cursor: pointer; inset: 0; background: #e2e8f0; border-radius: 28px; transition: 0.3s; }
.slider:before { content: ''; position: absolute; height: 22px; width: 22px; left: 3px; bottom: 3px; background: #fff; border-radius: 50%; transition: 0.3s; }
.switch input:checked + .slider { background: #0ea5e9; }
.switch input:checked + .slider:before { transform: translateX(24px); }

.select-input { padding: 10px 16px; border: 2px solid #e2e8f0; border-radius: 10px; font-size: 14px; outline: none; cursor: pointer; background: #fff; }
.select-input:focus { border-color: #0ea5e9; }
</style>
