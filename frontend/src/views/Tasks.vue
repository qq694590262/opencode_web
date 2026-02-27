<template>
  <div class="page-content">
    <div class="page-header">
      <h2>✅ 任务管理</h2>
      <p>查看和跟踪任务进度</p>
    </div>

    <div class="toolbar">
      <div class="search-box">
        <input v-model="searchQuery" placeholder="搜索任务..." @input="handleSearch">
      </div>
      <div class="filter-box">
        <select v-model="statusFilter" @change="handleFilter">
          <option value="">全部状态</option>
          <option value="todo">待办</option>
          <option value="inprogress">进行中</option>
          <option value="done">已完成</option>
        </select>
      </div>
      <button class="btn-primary" @click="openAddModal">➕ 新增任务</button>
    </div>

    <div class="tasks-board">
      <div class="task-column" v-for="col in filteredColumns" :key="col.id">
        <div class="column-header">
          <h4>{{ col.title }}</h4>
          <span class="count">{{ col.tasks.length }}</span>
        </div>
        <div class="task-list">
          <div class="task-card" v-for="task in col.tasks" :key="task.id" @click="openEditModal(task)">
            <div class="task-priority" :class="task.priority"></div>
            <div class="card-header">
              <h5>{{ task.title }}</h5>
              <div class="card-actions" @click.stop>
                <button class="btn-icon" @click="openEditModal(task)">✏️</button>
                <button class="btn-icon delete" @click="handleDelete(task)">🗑️</button>
              </div>
            </div>
            <p>{{ task.desc || task.description || '暂无描述' }}</p>
            <div class="task-footer">
              <span class="task-user">👤 {{ task.user || task.assignee || '未分配' }}</span>
              <span class="task-date">{{ task.date || task.dueDate || '-' }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 新增/编辑弹窗 -->
    <div v-if="showModal" class="modal-overlay" @click="closeModal">
      <div class="modal-content" @click.stop>
        <h3>{{ isEditing ? '编辑任务' : '新增任务' }}</h3>
        <form @submit.prevent="handleSubmit">
          <div class="form-group">
            <label>任务标题 *</label>
            <input v-model="formData.title" required placeholder="请输入任务标题">
          </div>
          <div class="form-group">
            <label>任务描述</label>
            <textarea v-model="formData.description" placeholder="请输入任务描述"></textarea>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label>优先级</label>
              <select v-model="formData.priority">
                <option value="high">高</option>
                <option value="medium">中</option>
                <option value="low">低</option>
              </select>
            </div>
            <div class="form-group">
              <label>状态</label>
              <select v-model="formData.status">
                <option value="todo">待办</option>
                <option value="inprogress">进行中</option>
                <option value="done">已完成</option>
              </select>
            </div>
          </div>
          <div class="form-group">
            <label>负责人</label>
            <input v-model="formData.assignee" placeholder="请输入负责人">
          </div>
          <div class="form-group">
            <label>截止日期</label>
            <input v-model="formData.dueDate" type="date">
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
import { taskApi } from '../api'
import { showToast } from '../components/Toast.vue'
import { showConfirm } from '../components/ConfirmDialog.vue'

const tasks = ref([])
const searchQuery = ref('')
const statusFilter = ref('')
const showModal = ref(false)
const isEditing = ref(false)
const formData = ref({
  title: '',
  description: '',
  priority: 'medium',
  status: 'todo',
  assignee: '',
  dueDate: ''
})
const editingId = ref(null)
const loading = ref(false)

const columnConfig = [
  { id: 'todo', title: '待办', status: 'todo' },
  { id: 'inprogress', title: '进行中', status: 'inprogress' },
  { id: 'done', title: '已完成', status: 'done' }
]

const getStatusKey = (status) => {
  const map = { 
    'todo': 'todo', '待办': 'todo', 'pending': 'todo',
    'inprogress': 'inprogress', '进行中': 'inprogress', 'doing': 'inprogress',
    'done': 'done', '已完成': 'done', 'completed': 'done'
  }
  return map[status] || 'todo'
}

const columns = computed(() => {
  const cols = columnConfig.map(col => ({
    ...col,
    tasks: []
  }))
  
  tasks.value.forEach(task => {
    const statusKey = getStatusKey(task.status)
    const col = cols.find(c => c.status === statusKey)
    if (col) {
      col.tasks.push(task)
    }
  })
  
  return cols
})

const filteredColumns = computed(() => {
  if (!searchQuery.value && !statusFilter.value) return columns.value
  
  return columns.value.map(col => ({
    ...col,
    tasks: col.tasks.filter(task => {
      const matchesSearch = !searchQuery.value || 
        task.title?.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
        (task.desc || task.description)?.toLowerCase().includes(searchQuery.value.toLowerCase())
      const matchesStatus = !statusFilter.value || getStatusKey(task.status) === statusFilter.value
      return matchesSearch && matchesStatus
    })
  }))
})

const loadTasks = async () => {
  try {
    loading.value = true
    const res = await taskApi.getAll()
    if (res.code === 200) {
      tasks.value = res.data || []
    }
  } catch (error) {
    console.error('加载任务失败:', error)
    showToast({ type: 'error', message: '加载任务失败: ' + (error.message || '未知错误') })
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  // 搜索通过 computed 自动处理
}

const handleFilter = async () => {
  if (statusFilter.value) {
    try {
      loading.value = true
      const res = await taskApi.getByStatus(statusFilter.value)
      if (res.code === 200) {
        tasks.value = res.data || []
      }
    } catch (error) {
      console.error('按状态加载任务失败:', error)
      showToast({ type: 'error', message: '加载失败: ' + (error.message || '未知错误') })
      await loadTasks()
    } finally {
      loading.value = false
    }
  } else {
    await loadTasks()
  }
}

const openAddModal = () => {
  isEditing.value = false
  editingId.value = null
  formData.value = {
    title: '',
    description: '',
    priority: 'medium',
    status: 'todo',
    assignee: '',
    dueDate: ''
  }
  showModal.value = true
}

const openEditModal = (task) => {
  isEditing.value = true
  editingId.value = task.id
  formData.value = {
    title: task.title || '',
    description: task.desc || task.description || '',
    priority: task.priority || 'medium',
    status: getStatusKey(task.status),
    assignee: task.user || task.assignee || '',
    dueDate: task.date || task.dueDate || ''
  }
  showModal.value = true
}

const closeModal = () => {
  showModal.value = false
  formData.value = {
    title: '',
    description: '',
    priority: 'medium',
    status: 'todo',
    assignee: '',
    dueDate: ''
  }
  editingId.value = null
}

const handleSubmit = async () => {
  try {
    loading.value = true
    const submitData = {
      ...formData.value,
      desc: formData.value.description,
      user: formData.value.assignee,
      date: formData.value.dueDate
    }
    
    if (isEditing.value) {
      await taskApi.update(editingId.value, submitData)
      showToast({ type: 'success', message: '任务更新成功' })
    } else {
      await taskApi.create(submitData)
      showToast({ type: 'success', message: '任务创建成功' })
    }
    
    closeModal()
    await loadTasks()
  } catch (error) {
    console.error('保存任务失败:', error)
    showToast({ type: 'error', message: '保存失败: ' + (error.message || '未知错误') })
  } finally {
    loading.value = false
  }
}

const handleDelete = async (task) => {
  const confirmed = await showConfirm({
    title: '删除确认',
    message: `确定要删除任务 "${task.title}" 吗？此操作不可恢复。`,
    type: 'danger',
    confirmText: '删除',
    cancelText: '取消'
  })
  
  if (!confirmed) return
  
  try {
    loading.value = true
    await taskApi.delete(task.id)
    showToast({ type: 'success', message: '删除成功' })
    await loadTasks()
  } catch (error) {
    console.error('删除任务失败:', error)
    showToast({ type: 'error', message: '删除失败: ' + (error.message || '未知错误') })
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadTasks()
})
</script>

<style scoped>
/* 任务看板特定样式 */
.tasks-board { display: flex; gap: 20px; overflow-x: auto; padding-bottom: 20px; }

.task-column { min-width: 300px; background: #f8fafc; border-radius: 16px; padding: 16px; flex: 1; }

.column-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.column-header h4 { font-size: 16px; font-weight: 600; color: #1e293b; margin: 0; }
.count { padding: 2px 10px; background: #e2e8f0; border-radius: 10px; font-size: 12px; color: #64748b; }

.task-card { background: #fff; border-radius: 12px; padding: 16px; margin-bottom: 12px; box-shadow: 0 2px 8px rgba(0,0,0,0.05); position: relative; overflow: hidden; cursor: pointer; transition: all 0.2s; }
.task-card:hover { box-shadow: 0 4px 12px rgba(0,0,0,0.1); transform: translateY(-2px); }

.task-priority { position: absolute; left: 0; top: 0; bottom: 0; width: 4px; }
.task-priority.high { background: #ef4444; }
.task-priority.medium { background: #f59e0b; }
.task-priority.low { background: #22c55e; }

.card-header { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 8px; }
.card-header h5 { font-size: 14px; font-weight: 600; color: #1e293b; margin: 0; padding-left: 8px; flex: 1; }
.card-actions { display: flex; gap: 4px; }
.btn-icon {
  padding: 4px 6px;
  background: transparent;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
  transition: all 0.2s;
}
.btn-icon:hover { background: #f1f5f9; }
.btn-icon.delete:hover { background: #fee2e2; }

.task-card p { font-size: 12px; color: #64748b; margin: 0 0 12px 0; padding-left: 8px; }

.task-footer { display: flex; justify-content: space-between; font-size: 12px; color: #94a3b8; padding-left: 8px; }

/* 筛选框 */
.filter-box select {
  padding: 10px 16px;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  font-size: 14px;
  outline: none;
  background: white;
  cursor: pointer;
}
.search-box input { width: 240px; }
.search-box input:focus, .filter-box select:focus { border-color: #0ea5e9; }

@media (max-width: 768px) {
  .toolbar { flex-wrap: wrap; }
  .search-box, .filter-box { flex: 1; }
  .search-box input { width: 100%; }
  .tasks-board { flex-direction: column; }
  .task-column { min-width: auto; }
  .form-row { grid-template-columns: 1fr; }
}
</style>

