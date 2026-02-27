<template>
  <div class="page-content">
    <div class="page-header">
      <h2>📁 项目列表</h2>
      <p>管理所有项目</p>
    </div>

    <div class="toolbar">
      <div class="search-box">
        <input v-model="searchQuery" placeholder="搜索项目名称..." @input="handleSearch">
      </div>
      <button class="btn-primary" @click="openAddModal">➕ 新增项目</button>
    </div>

    <div class="projects-grid">
      <div class="project-card" v-for="p in filteredProjects" :key="p.id">
        <div class="project-header">
          <span class="project-icon" :style="{ background: p.color || '#0ea5e9' }">{{ p.icon || '📁' }}</span>
          <div class="status-actions">
            <span class="project-status" :class="p.status">{{ getStatusText(p.status) }}</span>
            <div class="card-actions">
              <button class="btn-icon" @click="openEditModal(p)">✏️</button>
              <button class="btn-icon delete" @click="handleDelete(p)">🗑️</button>
            </div>
          </div>
        </div>
        <h4>{{ p.name }}</h4>
        <p>{{ p.desc || p.description || '暂无描述' }}</p>
        <div class="project-meta">
          <span>👥 {{ p.members || 0 }}</span>
          <span>📅 {{ p.date || p.endDate || '-' }}</span>
        </div>
        <div class="progress-bar">
          <div class="progress" :style="{ width: (p.progress || 0) + '%' }"></div>
        </div>
        <div class="progress-text">{{ p.progress || 0 }}%</div>
      </div>
    </div>

    <!-- 新增/编辑弹窗 -->
    <div v-if="showModal" class="modal-overlay" @click="closeModal">
      <div class="modal-content" @click.stop>
        <h3>{{ isEditing ? '编辑项目' : '新增项目' }}</h3>
        <form @submit.prevent="handleSubmit">
          <div class="form-row">
            <div class="form-group">
              <label>项目名称 *</label>
              <input v-model="formData.name" required placeholder="请输入项目名称">
            </div>
            <div class="form-group">
              <label>项目图标</label>
              <input v-model="formData.icon" placeholder="例如: 💼">
            </div>
          </div>
          <div class="form-group">
            <label>项目描述</label>
            <textarea v-model="formData.description" placeholder="请输入项目描述"></textarea>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label>项目颜色</label>
              <input v-model="formData.color" type="color" class="color-picker">
            </div>
            <div class="form-group">
              <label>项目状态</label>
              <select v-model="formData.status">
                <option value="active">进行中</option>
                <option value="pending">待开始</option>
                <option value="done">已完成</option>
              </select>
            </div>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label>成员数量</label>
              <input v-model.number="formData.members" type="number" min="0" placeholder="成员数量">
            </div>
            <div class="form-group">
              <label>进度 (%)</label>
              <input v-model.number="formData.progress" type="number" min="0" max="100" placeholder="0-100">
            </div>
          </div>
          <div class="form-group">
            <label>截止日期</label>
            <input v-model="formData.endDate" type="date">
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
import { projectApi } from '../api'
import { showToast } from '../components/Toast.vue'
import { showConfirm } from '../components/ConfirmDialog.vue'

const projects = ref([])
const searchQuery = ref('')
const showModal = ref(false)
const isEditing = ref(false)
const formData = ref({
  name: '',
  icon: '',
  description: '',
  color: '#0ea5e9',
  status: 'active',
  members: 0,
  progress: 0,
  endDate: ''
})
const editingId = ref(null)
const loading = ref(false)

const getStatusText = (status) => {
  const map = { active: '进行中', pending: '待开始', done: '已完成' }
  return map[status] || status
}

const filteredProjects = computed(() => {
  if (!searchQuery.value) return projects.value
  const query = searchQuery.value.toLowerCase()
  return projects.value.filter(p => 
    p.name?.toLowerCase().includes(query) ||
    (p.desc || p.description)?.toLowerCase().includes(query)
  )
})

const loadProjects = async () => {
  try {
    loading.value = true
    const res = await projectApi.getAll()
    if (res.code === 200) {
      projects.value = res.data || []
    }
  } catch (error) {
    console.error('加载项目失败:', error)
    showToast({ type: 'error', message: '加载项目失败: ' + (error.message || '未知错误') })
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
  formData.value = {
    name: '',
    icon: '',
    description: '',
    color: '#0ea5e9',
    status: 'active',
    members: 0,
    progress: 0,
    endDate: ''
  }
  showModal.value = true
}

const openEditModal = (project) => {
  isEditing.value = true
  editingId.value = project.id
  formData.value = {
    name: project.name || '',
    icon: project.icon || '',
    description: project.desc || project.description || '',
    color: project.color || '#0ea5e9',
    status: project.status || 'active',
    members: project.members || 0,
    progress: project.progress || 0,
    endDate: project.date || project.endDate || ''
  }
  showModal.value = true
}

const closeModal = () => {
  showModal.value = false
  formData.value = {
    name: '',
    icon: '',
    description: '',
    color: '#0ea5e9',
    status: 'active',
    members: 0,
    progress: 0,
    endDate: ''
  }
  editingId.value = null
}

const handleSubmit = async () => {
  try {
    loading.value = true
    const submitData = {
      ...formData.value,
      desc: formData.value.description,
      date: formData.value.endDate
    }
    
    if (isEditing.value) {
      await projectApi.update(editingId.value, submitData)
      showToast({ type: 'success', message: '项目更新成功' })
    } else {
      await projectApi.create(submitData)
      showToast({ type: 'success', message: '项目创建成功' })
    }
    
    closeModal()
    await loadProjects()
  } catch (error) {
    console.error('保存项目失败:', error)
    showToast({ type: 'error', message: '保存失败: ' + (error.message || '未知错误') })
  } finally {
    loading.value = false
  }
}

const handleDelete = async (project) => {
  const confirmed = await showConfirm({
    title: '删除确认',
    message: `确定要删除项目 "${project.name}" 吗？此操作不可恢复。`,
    type: 'danger',
    confirmText: '删除',
    cancelText: '取消'
  })
  
  if (!confirmed) return
  
  try {
    loading.value = true
    await projectApi.delete(project.id)
    showToast({ type: 'success', message: '删除成功' })
    await loadProjects()
  } catch (error) {
    console.error('删除项目失败:', error)
    showToast({ type: 'error', message: '删除失败: ' + (error.message || '未知错误') })
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadProjects()
})
</script>

<style scoped>
/* 项目卡片特定样式 */
.projects-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 20px; }

.project-card {
  background: #fff; border-radius: 16px; padding: 24px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.05); transition: all 0.3s;
}
.project-card:hover { transform: translateY(-4px); box-shadow: 0 8px 25px rgba(0,0,0,0.1); }

.project-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.project-icon { width: 48px; height: 48px; border-radius: 12px; display: flex; align-items: center; justify-content: center; font-size: 24px; }

.status-actions { display: flex; align-items: center; gap: 10px; }
.project-status { padding: 4px 12px; border-radius: 20px; font-size: 12px; font-weight: 500; }
.project-status.active { background: #dcfce7; color: #16a34a; }
.project-status.pending { background: #fef3c7; color: #d97706; }
.project-status.done { background: #e0f2fe; color: #0284c7; }

.card-actions { display: flex; gap: 6px; }
.btn-icon {
  padding: 6px 10px;
  background: #f1f5f9;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 12px;
  transition: all 0.2s;
}
.btn-icon:hover { background: #e2e8f0; }
.btn-icon.delete:hover { background: #fee2e2; }

.project-card h4 { font-size: 18px; font-weight: 600; color: #1e293b; margin: 0 0 8px 0; }
.project-card p { font-size: 13px; color: #64748b; margin: 0 0 16px 0; }

.project-meta { display: flex; gap: 16px; font-size: 13px; color: #64748b; margin-bottom: 16px; }

.progress-bar { height: 6px; background: #f1f5f9; border-radius: 3px; overflow: hidden; }
.progress { height: 100%; background: linear-gradient(90deg, #0ea5e9, #38bdf8); border-radius: 3px; transition: width 0.5s ease; }
.progress-text { font-size: 12px; color: #64748b; margin-top: 8px; text-align: right; }

.color-picker { height: 44px; padding: 4px !important; cursor: pointer; }

@media (max-width: 768px) {
  .projects-grid { grid-template-columns: 1fr; }
  .toolbar { flex-direction: column; gap: 12px; align-items: stretch; }
  }
</style>
