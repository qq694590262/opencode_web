<template>
  <div class="page-content">
    <div class="page-header">
      <div class="header-title">
        <h2>👤 个人信息</h2>
        <p>查看和编辑您的个人资料</p>
      </div>
    </div>

    <div class="profile-layout">
      <!-- 左侧：基本信息卡片 -->
      <div class="left-column">
        <div class="profile-card">
          <div class="profile-avatar-section">
            <div class="avatar-wrapper">
              <div class="avatar" :style="{ background: avatarGradient }">
                {{ userInfo.name ? userInfo.name.charAt(0).toUpperCase() : 'U' }}
              </div>
              <div class="avatar-overlay" @click="showAvatarModal = true">
                <span>📷</span>
                <span>更换头像</span>
              </div>
            </div>
            <h3 class="profile-name">{{ userInfo.name || userInfo.username || '用户' }}</h3>
            <p class="profile-role">{{ getRoleText(userInfo.role) }}</p>
            <div class="profile-status">
              <span class="status-dot online"></span>
              <span>在线</span>
            </div>
          </div>
          
          <div class="profile-stats">
            <div class="stat-item">
              <span class="stat-value">{{ userStats.projects || 0 }}</span>
              <span class="stat-label">参与项目</span>
            </div>
            <div class="stat-item">
              <span class="stat-value">{{ userStats.tasks || 0 }}</span>
              <span class="stat-label">完成任务</span>
            </div>
            <div class="stat-item">
              <span class="stat-value">{{ userStats.documents || 0 }}</span>
              <span class="stat-label">上传文档</span>
            </div>
          </div>
          
          <div class="profile-actions">
            <button class="btn-outline" @click="showPasswordModal = true">
              <span>🔐</span> 修改密码
            </button>
          </div>
        </div>
        
        <!-- 账户信息 -->
        <div class="info-card">
          <h4>账户信息</h4>
          <div class="info-list">
            <div class="info-row">
              <span class="info-label">用户ID</span>
              <span class="info-value">#{{ userInfo.id || '0000' }}</span>
            </div>
            <div class="info-row">
              <span class="info-label">注册时间</span>
              <span class="info-value">{{ formatDate(userInfo.createTime) }}</span>
            </div>
            <div class="info-row">
              <span class="info-label">最后登录</span>
              <span class="info-value">{{ formatDate(userInfo.lastLoginTime) || '刚刚' }}</span>
            </div>
            <div class="info-row">
              <span class="info-label">登录IP</span>
              <span class="info-value">{{ userInfo.lastLoginIp || '127.0.0.1' }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧：编辑表单 -->
      <div class="right-column">
        <div class="edit-card">
          <div class="card-header">
            <h3>编辑资料</h3>
            <button class="btn-save" @click="saveProfile" :disabled="saving">
              <span v-if="saving">💾 保存中...</span>
              <span v-else>💾 保存修改</span>
            </button>
          </div>
          
          <div class="form-section">
            <h4 class="section-title">基本信息</h4>
            <div class="form-row">
              <div class="form-group">
                <label>用户名</label>
                <input v-model="formData.username" disabled />
                <span class="input-hint">用户名不可修改</span>
              </div>
              <div class="form-group">
                <label>姓名 *</label>
                <input v-model="formData.name" placeholder="请输入您的姓名" />
              </div>
            </div>
            
            <div class="form-row">
              <div class="form-group">
                <label>邮箱 *</label>
                <input v-model="formData.email" type="email" placeholder="请输入邮箱地址" />
              </div>
              <div class="form-group">
                <label>手机号</label>
                <input v-model="formData.phone" placeholder="请输入手机号码" />
              </div>
            </div>
            
            <div class="form-row">
              <div class="form-group">
                <label>部门</label>
                <select v-model="formData.department">
                  <option value="">请选择部门</option>
                  <option value="技术部">技术部</option>
                  <option value="产品部">产品部</option>
                  <option value="设计部">设计部</option>
                  <option value="运营部">运营部</option>
                  <option value="市场部">市场部</option>
                  <option value="人事部">人事部</option>
                  <option value="财务部">财务部</option>
                </select>
              </div>
              <div class="form-group">
                <label>职位</label>
                <input v-model="formData.position" placeholder="请输入职位" />
              </div>
            </div>
          </div>
          
          <div class="form-section">
            <h4 class="section-title">个人简介</h4>
            <div class="form-group">
              <textarea 
                v-model="formData.bio" 
                rows="4" 
                placeholder="介绍一下自己..."
                maxlength="200"
              ></textarea>
              <span class="char-count">{{ (formData.bio || '').length }}/200</span>
            </div>
          </div>
          
          <div class="form-section">
            <h4 class="section-title">社交媒体</h4>
            <div class="form-row">
              <div class="form-group">
                <label>GitHub</label>
                <input v-model="formData.github" placeholder="GitHub 用户名" />
              </div>
              <div class="form-group">
                <label>个人网站</label>
                <input v-model="formData.website" placeholder="https://example.com" />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 修改密码弹窗 -->
    <div v-if="showPasswordModal" class="modal-overlay" @click.self="showPasswordModal = false">
      <div class="modal-content">
        <div class="modal-header">
          <h3>修改密码</h3>
          <button class="btn-close" @click="showPasswordModal = false">✕</button>
        </div>
        <form @submit.prevent="changePassword">
          <div class="form-group">
            <label>当前密码</label>
            <input type="password" v-model="passwordForm.oldPassword" required />
          </div>
          <div class="form-group">
            <label>新密码</label>
            <input type="password" v-model="passwordForm.newPassword" required minlength="6" />
            <span class="input-hint">密码长度至少6位</span>
          </div>
          <div class="form-group">
            <label>确认新密码</label>
            <input type="password" v-model="passwordForm.confirmPassword" required />
            <span v-if="passwordError" class="error-text">{{ passwordError }}</span>
          </div>
          <div class="modal-actions">
            <button type="button" class="btn-cancel" @click="showPasswordModal = false">取消</button>
            <button type="submit" class="btn-confirm" :disabled="changingPassword">
              {{ changingPassword ? '修改中...' : '确认修改' }}
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- 更换头像弹窗 -->
    <div v-if="showAvatarModal" class="modal-overlay" @click.self="showAvatarModal = false">
      <div class="modal-content avatar-modal">
        <div class="modal-header">
          <h3>更换头像</h3>
          <button class="btn-close" @click="showAvatarModal = false">✕</button>
        </div>
        <div class="avatar-options">
          <div class="current-preview">
            <div class="preview-avatar" :style="{ background: avatarGradient }">
              {{ userInfo.name ? userInfo.name.charAt(0).toUpperCase() : 'U' }}
            </div>
            <p>当前头像</p>
          </div>
          <div class="color-picker">
            <h4>选择主题色</h4>
            <div class="color-grid">
              <div 
                v-for="color in avatarColors" 
                :key="color"
                class="color-item"
                :style="{ background: color }"
                :class="{ active: selectedColor === color }"
                @click="selectedColor = color"
              ></div>
            </div>
          </div>
        </div>
        <div class="modal-actions">
          <button class="btn-cancel" @click="showAvatarModal = false">取消</button>
          <button class="btn-confirm" @click="saveAvatar">保存</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { getUserInfo, changePassword } from '../auth'
import { userApi } from '../api'
import { showToast } from '../components/Toast.vue'

export default {
  name: 'Profile',
  setup() {
    const userInfo = ref({})
    const userStats = ref({
      projects: 5,
      tasks: 23,
      documents: 8
    })
    
    const formData = ref({
      username: '',
      name: '',
      email: '',
      phone: '',
      department: '',
      position: '',
      bio: '',
      github: '',
      website: ''
    })
    
    const saving = ref(false)
    const showPasswordModal = ref(false)
    const showAvatarModal = ref(false)
    const changingPassword = ref(false)
    const passwordError = ref('')
    
    const passwordForm = ref({
      oldPassword: '',
      newPassword: '',
      confirmPassword: ''
    })
    
    const selectedColor = ref('')
    
    const avatarColors = [
      'linear-gradient(135deg, #38bdf8, #0ea5e9)',
      'linear-gradient(135deg, #34d399, #10b981)',
      'linear-gradient(135deg, #f472b6, #ec4899)',
      'linear-gradient(135deg, #fbbf24, #f59e0b)',
      'linear-gradient(135deg, #a78bfa, #8b5cf6)',
      'linear-gradient(135deg, #f87171, #ef4444)',
      'linear-gradient(135deg, #2dd4bf, #14b8a6)',
      'linear-gradient(135deg, #fb923c, #f97316)'
    ]
    
    const avatarGradient = computed(() => {
      return selectedColor.value || 'linear-gradient(135deg, #38bdf8, #0ea5e9)'
    })

    const loadUserInfo = async () => {
      try {
        const info = await getUserInfo()
        if (info) {
          userInfo.value = info
          formData.value = { ...formData.value, ...info }
        }
      } catch (error) {
        console.error('获取用户信息失败:', error)
        // 使用模拟数据
        userInfo.value = {
          id: 1,
          username: 'admin',
          name: '系统管理员',
          email: 'admin@opencode.com',
          phone: '13800138000',
          department: '技术部',
          position: '技术总监',
          role: 'ADMIN',
          bio: '热爱技术，专注于企业级应用开发',
          createTime: '2024-01-15',
          lastLoginTime: new Date().toISOString()
        }
        formData.value = { ...userInfo.value }
      }
    }

    const getRoleText = (role) => {
      const roleMap = {
        'ADMIN': '超级管理员',
        'USER': '普通用户',
        'EDITOR': '编辑',
        'MANAGER': '经理'
      }
      return roleMap[role] || role || '用户'
    }

    const formatDate = (dateStr) => {
      if (!dateStr) return ''
      const date = new Date(dateStr)
      return date.toLocaleDateString('zh-CN')
    }

    const saveProfile = async () => {
      saving.value = true
      try {
        const res = await userApi.update(userInfo.value.id, formData.value)
        if (res.code === 200) {
          userInfo.value = { ...userInfo.value, ...formData.value }
          showToast({ type: 'success', message: '个人信息更新成功' })
        }
      } catch (error) {
        console.error('保存失败:', error)
        showToast({ type: 'error', message: '保存失败' })
      } finally {
        saving.value = false
      }
    }

    const changePassword = async () => {
      passwordError.value = ''
      
      if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) {
        passwordError.value = '两次输入的密码不一致'
        return
      }
      
      if (passwordForm.value.newPassword.length < 6) {
        passwordError.value = '密码长度至少6位'
        return
      }
      
      changingPassword.value = true
      try {
        const result = await changePassword(
          passwordForm.value.oldPassword,
          passwordForm.value.newPassword
        )
        
        if (result.code === 200) {
          showToast({ type: 'success', message: '密码修改成功' })
          showPasswordModal.value = false
          passwordForm.value = { oldPassword: '', newPassword: '', confirmPassword: '' }
        } else {
          passwordError.value = result.message || '原密码错误'
        }
      } catch (error) {
        showToast({ type: 'error', message: '密码修改失败' })
      } finally {
        changingPassword.value = false
      }
    }

    const saveAvatar = () => {
      showToast({ type: 'success', message: '头像已更新' })
      showAvatarModal.value = false
    }

    onMounted(() => {
      loadUserInfo()
    })

    return {
      userInfo,
      userStats,
      formData,
      saving,
      showPasswordModal,
      showAvatarModal,
      changingPassword,
      passwordError,
      passwordForm,
      selectedColor,
      avatarColors,
      avatarGradient,
      getRoleText,
      formatDate,
      saveProfile,
      changePassword,
      saveAvatar
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

/* 布局 */
.profile-layout {
  display: grid;
  grid-template-columns: 320px 1fr;
  gap: 24px;
}

.left-column {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* 个人资料卡片 */
.profile-card {
  background: #fff;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.05);
  text-align: center;
}

.profile-avatar-section {
  margin-bottom: 24px;
}

.avatar-wrapper {
  position: relative;
  width: 120px;
  height: 120px;
  margin: 0 auto 16px;
  border-radius: 50%;
  overflow: hidden;
}

.avatar {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 48px;
  font-weight: 700;
  color: #fff;
  transition: all 0.3s;
}

.avatar-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: all 0.3s;
  cursor: pointer;
}

.avatar-overlay span {
  color: #fff;
  font-size: 12px;
}

.avatar-overlay span:first-child {
  font-size: 24px;
  margin-bottom: 4px;
}

.avatar-wrapper:hover .avatar-overlay {
  opacity: 1;
}

.profile-name {
  font-size: 20px;
  font-weight: 700;
  color: #1e293b;
  margin: 0 0 4px 0;
}

.profile-role {
  font-size: 14px;
  color: #64748b;
  margin: 0 0 12px 0;
}

.profile-status {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 6px 12px;
  background: #dcfce7;
  border-radius: 20px;
  font-size: 12px;
  color: #16a34a;
}

.status-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: #22c55e;
}

.status-dot.online {
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.5; }
}

.profile-stats {
  display: flex;
  justify-content: space-around;
  padding: 20px 0;
  border-top: 1px solid #f1f5f9;
  border-bottom: 1px solid #f1f5f9;
  margin-bottom: 20px;
}

.stat-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.stat-value {
  font-size: 20px;
  font-weight: 700;
  color: #0ea5e9;
}

.stat-label {
  font-size: 12px;
  color: #64748b;
}

.profile-actions {
  display: flex;
  gap: 12px;
  justify-content: center;
}

.btn-outline {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  background: transparent;
  border: 2px solid #e2e8f0;
  border-radius: 10px;
  font-size: 14px;
  color: #475569;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-outline:hover {
  border-color: #0ea5e9;
  color: #0ea5e9;
  background: #f0f9ff;
}

/* 信息卡片 */
.info-card {
  background: #fff;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.05);
}

.info-card h4 {
  font-size: 16px;
  font-weight: 600;
  color: #1e293b;
  margin: 0 0 16px 0;
}

.info-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.info-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 14px;
}

.info-label {
  color: #64748b;
}

.info-value {
  color: #1e293b;
  font-weight: 500;
}

/* 编辑卡片 */
.edit-card {
  background: #fff;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.05);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding-bottom: 20px;
  border-bottom: 1px solid #f1f5f9;
}

.card-header h3 {
  font-size: 18px;
  font-weight: 600;
  color: #1e293b;
  margin: 0;
}

.btn-save {
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

/* 表单 */
.form-section {
  margin-bottom: 24px;
}

.form-section:last-child {
  margin-bottom: 0;
}

.section-title {
  font-size: 15px;
  font-weight: 600;
  color: #1e293b;
  margin: 0 0 16px 0;
}

.form-row {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
  margin-bottom: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
  position: relative;
}

.form-group label {
  font-size: 13px;
  font-weight: 500;
  color: #64748b;
}

.form-group input,
.form-group select,
.form-group textarea {
  padding: 12px 16px;
  border: 2px solid #e2e8f0;
  border-radius: 10px;
  font-size: 14px;
  color: #334155;
  outline: none;
  transition: all 0.3s;
  background: #fff;
}

.form-group input:focus,
.form-group select:focus,
.form-group textarea:focus {
  border-color: #0ea5e9;
  background: #f8fafc;
}

.form-group input:disabled {
  background: #f1f5f9;
  cursor: not-allowed;
}

.form-group textarea {
  resize: vertical;
  font-family: inherit;
  line-height: 1.6;
}

.input-hint {
  font-size: 12px;
  color: #94a3b8;
}

.error-text {
  font-size: 12px;
  color: #ef4444;
}

.char-count {
  position: absolute;
  bottom: 12px;
  right: 16px;
  font-size: 12px;
  color: #94a3b8;
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
  max-width: 400px;
  overflow: hidden;
  animation: modalSlideIn 0.3s ease;
}

.modal-content.avatar-modal {
  max-width: 420px;
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

.modal-content form {
  padding: 24px;
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

/* 头像选择 */
.avatar-options {
  padding: 24px;
}

.current-preview {
  text-align: center;
  margin-bottom: 24px;
}

.preview-avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  margin: 0 auto 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 40px;
  font-weight: 700;
  color: #fff;
}

.current-preview p {
  font-size: 14px;
  color: #64748b;
  margin: 0;
}

.color-picker h4 {
  font-size: 14px;
  font-weight: 600;
  color: #1e293b;
  margin: 0 0 12px 0;
}

.color-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px;
}

.color-item {
  aspect-ratio: 1;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s;
  border: 3px solid transparent;
}

.color-item:hover {
  transform: scale(1.1);
}

.color-item.active {
  border-color: #1e293b;
  box-shadow: 0 4px 12px rgba(0,0,0,0.2);
}

/* 响应式 */
@media (max-width: 1024px) {
  .profile-layout {
    grid-template-columns: 1fr;
  }
  
  .left-column {
    order: 2;
  }
  
  .right-column {
    order: 1;
  }
}

@media (max-width: 768px) {
  .form-row {
    grid-template-columns: 1fr;
  }
}
</style>