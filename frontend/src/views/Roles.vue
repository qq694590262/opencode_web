<template>
  <div class="page-content">
    <div class="page-header">
      <h2>🛡️ 角色权限</h2>
      <p>管理系统角色与权限配置</p>
    </div>

    <div class="toolbar">
      <div class="search-box">
        <input v-model="searchQuery" placeholder="搜索角色名称..." @input="handleSearch">
      </div>
      <button class="btn-primary" @click="openAddModal">➕ 新增角色</button>
    </div>

    <div class="roles-grid">
      <div class="role-card" v-for="role in filteredRoles" :key="role.id">
        <div class="role-header">
          <span class="role-icon">{{ role.icon || '👤' }}</span>
          <h4>{{ role.name }}</h4>
        </div>
        <p class="role-desc">{{ role.desc || role.description || '暂无描述' }}</p>
        <div class="role-users">{{ role.count || 0 }} 人</div>
        <div class="role-actions">
          <button class="btn-edit" @click="openEditModal(role)">编辑</button>
          <button class="btn-delete" @click="handleDelete(role)">删除</button>
        </div>
      </div>
    </div>

    <!-- 新增/编辑弹窗 -->
    <div v-if="showModal" class="modal-overlay" @click="closeModal">
      <div class="modal-content" @click.stop>
        <h3>{{ isEditing ? '编辑角色' : '新增角色' }}</h3>
        <form @submit.prevent="handleSubmit">
          <div class="form-group">
            <label>角色名称 *</label>
            <input v-model="formData.name" required placeholder="请输入角色名称">
          </div>
          <div class="form-group">
            <label>角色图标</label>
            <input v-model="formData.icon" placeholder="例如: 👑">
          </div>
          <div class="form-group">
            <label>角色描述</label>
            <textarea v-model="formData.description" placeholder="请输入角色描述"></textarea>
          </div>
          <div class="form-actions">
            <button type="button" class="btn-cancel" @click="closeModal">取消</button>
            <button type="submit" class="btn-confirm">{{ isEditing ? '保存' : '创建' }}</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { roleApi } from '../api'
import { showToast } from '../components/Toast.vue'
import { showConfirm } from '../components/ConfirmDialog.vue'

const roles = ref([])
const searchQuery = ref('')
const showModal = ref(false)
const isEditing = ref(false)
const formData = ref({
  name: '',
  icon: '',
  description: ''
})
const editingId = ref(null)
const loading = ref(false)

const filteredRoles = computed(() => {
  if (!searchQuery.value) return roles.value
  const query = searchQuery.value.toLowerCase()
  return roles.value.filter(role => 
    role.name?.toLowerCase().includes(query) ||
    role.desc?.toLowerCase().includes(query) ||
    role.description?.toLowerCase().includes(query)
  )
})

const loadRoles = async () => {
  try {
    loading.value = true
    const res = await roleApi.getAll()
    if (res.code === 200) {
      roles.value = res.data || []
    }
  } catch (error) {
    console.error('加载角色失败:', error)
    showToast({ type: 'error', message: '加载角色失败: ' + (error.message || '未知错误') })
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  // 搜索通过 computed 自动处理
}

const openAddModal = () => {
  isEditing.value = false
  editingId.value = null
  formData.value = { name: '', icon: '', description: '' }
  showModal.value = true
}

const openEditModal = (role) => {
  isEditing.value = true
  editingId.value = role.id
  formData.value = {
    name: role.name || '',
    icon: role.icon || '',
    description: role.desc || role.description || ''
  }
  showModal.value = true
}

const closeModal = () => {
  showModal.value = false
  formData.value = { name: '', icon: '', description: '' }
  editingId.value = null
}

const handleSubmit = async () => {
  try {
    loading.value = true
    const submitData = {
      ...formData.value,
      desc: formData.value.description
    }
    
    if (isEditing.value) {
      await roleApi.update(editingId.value, submitData)
      showToast({ type: 'success', message: '角色更新成功' })
    } else {
      await roleApi.create(submitData)
      showToast({ type: 'success', message: '角色创建成功' })
    }
    
    closeModal()
    await loadRoles()
  } catch (error) {
    console.error('保存角色失败:', error)
    showToast({ type: 'error', message: '保存失败: ' + (error.message || '未知错误') })
  } finally {
    loading.value = false
  }
}

const handleDelete = async (role) => {
  const confirmed = await showConfirm({
    title: '删除确认',
    message: `确定要删除角色 "${role.name}" 吗？此操作不可恢复。`,
    type: 'danger',
    confirmText: '删除',
    cancelText: '取消'
  })
  
  if (!confirmed) return
  
  try {
    loading.value = true
    await roleApi.delete(role.id)
    showToast({ type: 'success', message: '删除成功' })
    await loadRoles()
  } catch (error) {
    console.error('删除角色失败:', error)
    showToast({ type: 'error', message: '删除失败: ' + (error.message || '未知错误') })
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadRoles()
})
</script>

<style scoped>
.page-content { animation: fadeIn 0.3s ease; }
@keyframes fadeIn { from { opacity: 0; transform: translateY(10px); } to { opacity: 1; transform: translateY(0); } }

.page-header { margin-bottom: 24px; }
.page-header h2 { font-size: 24px; font-weight: 700; color: #0c4a6e; margin: 0 0 8px 0; }
.page-header p { font-size: 14px; color: #64748b; margin: 0; }

.toolbar { display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; }
.search-box input {
  padding: 10px 16px;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  font-size: 14px;
  width: 280px;
  outline: none;
}
.search-box input:focus { border-color: #0ea5e9; }

.btn-primary {
  padding: 10px 20px;
  background: linear-gradient(135deg, #0ea5e9, #38bdf8);
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s;
}
.btn-primary:hover { transform: translateY(-2px); box-shadow: 0 4px 12px rgba(14, 165, 233, 0.3); }

.roles-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 20px; }

.role-card {
  background: #fff; border-radius: 16px; padding: 24px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.05); transition: all 0.3s;
}
.role-card:hover { transform: translateY(-4px); box-shadow: 0 8px 25px rgba(0,0,0,0.1); }

.role-header { display: flex; align-items: center; gap: 12px; margin-bottom: 12px; }
.role-icon { font-size: 28px; }
.role-header h4 { font-size: 16px; font-weight: 600; color: #1e293b; margin: 0; }

.role-desc { font-size: 13px; color: #64748b; margin: 0 0 16px 0; }
.role-users { font-size: 14px; color: #0ea5e9; font-weight: 500; margin-bottom: 16px; }

.role-actions { display: flex; gap: 10px; }
.btn-edit, .btn-delete, .btn-view { flex: 1; padding: 8px; border: none; border-radius: 8px; font-size: 13px; cursor: pointer; transition: all 0.2s; }
.btn-edit { background: #e0f2fe; color: #0284c7; }
.btn-edit:hover { background: #bae6fd; }
.btn-delete { background: #fee2e2; color: #dc2626; }
.btn-delete:hover { background: #fecaca; }
.btn-view { background: #f1f5f9; color: #475569; }
.btn-view:hover { background: #e2e8f0; }

/* 弹窗样式 */
.modal-overlay {
  position: fixed; top: 0; left: 0; right: 0; bottom: 0;
  background: rgba(0,0,0,0.5); display: flex; align-items: center; justify-content: center;
  z-index: 1000; animation: fadeIn 0.2s ease;
}
.modal-content {
  background: white; border-radius: 16px; padding: 32px;
  width: 100%; max-width: 480px; box-shadow: 0 20px 60px rgba(0,0,0,0.3);
  animation: slideUp 0.3s ease;
}
@keyframes slideUp { from { opacity: 0; transform: translateY(20px); } to { opacity: 1; transform: translateY(0); } }
.modal-content h3 { margin: 0 0 24px 0; font-size: 20px; color: #1e293b; }
.form-group { margin-bottom: 20px; }
.form-group label { display: block; margin-bottom: 8px; font-size: 14px; color: #64748b; font-weight: 500; }
.form-group input, .form-group textarea {
  width: 100%; padding: 12px 16px; border: 1px solid #e2e8f0; border-radius: 10px;
  font-size: 15px; box-sizing: border-box; outline: none;
}
.form-group input:focus, .form-group textarea:focus { border-color: #0ea5e9; }
.form-group textarea { min-height: 80px; resize: vertical; }
.form-actions { display: flex; gap: 12px; margin-top: 24px; }
.btn-cancel, .btn-confirm { flex: 1; padding: 12px; border-radius: 10px; font-size: 15px; cursor: pointer; transition: all 0.2s; }
.btn-cancel { background: #f1f5f9; color: #64748b; border: none; }
.btn-cancel:hover { background: #e2e8f0; }
.btn-confirm { background: linear-gradient(135deg, #0ea5e9, #38bdf8); color: white; border: none; }
.btn-confirm:hover { transform: translateY(-2px); box-shadow: 0 4px 12px rgba(14, 165, 233, 0.3); }

@media (max-width: 1200px) { .roles-grid { grid-template-columns: repeat(2, 1fr); } }
@media (max-width: 640px) { .roles-grid { grid-template-columns: 1fr; } }
@media (max-width: 768px) {
  .toolbar { flex-direction: column; gap: 12px; align-items: stretch; }
  .search-box input { width: 100%; }
}
</style>
