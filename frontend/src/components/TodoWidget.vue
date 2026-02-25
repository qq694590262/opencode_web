<template>
  <div class="todo-widget">
    <div class="widget-header">
      <h3>✅ 待办事项</h3>
      <button class="add-btn" @click="showAddDialog = true">+</button>
    </div>
    
    <div class="todo-list">
      <div 
        v-for="todo in todos" 
        :key="todo.id" 
        class="todo-item"
        :class="{ completed: todo.status === 'completed' }"
      >
        <el-checkbox 
          :model-value="todo.status === 'completed'"
          @change="toggleStatus(todo)"
        />
        <div class="todo-content" @click="editTodo(todo)">
          <div class="todo-title">{{ todo.title }}</div>
          <div class="todo-meta">
            <span :class="['priority', getPriorityClass(todo.priority)]">
              {{ getPriorityText(todo.priority) }}
            </span>
            <span v-if="todo.dueDate" class="due-date">
              {{ formatDate(todo.dueDate) }}
            </span>
          </div>
        </div>
        <button class="delete-btn" @click.stop="handleDelete(todo.id)">🗑️</button>
      </div>
      
      <div v-if="todos.length === 0" class="empty-state">
        <span>暂无待办事项，点击 + 添加</span>
      </div>
    </div>

    <!-- 添加/编辑对话框 -->
    <el-dialog 
      v-model="showAddDialog" 
      :title="editingTodo ? '编辑待办' : '添加待办'"
      width="450px"
    >
      <el-form :model="todoForm" label-width="70px">
        <el-form-item label="标题" required>
          <el-input v-model="todoForm.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input 
            v-model="todoForm.content" 
            type="textarea" 
            :rows="3" 
            placeholder="请输入描述" 
          />
        </el-form-item>
        <el-form-item label="优先级">
          <el-radio-group v-model="todoForm.priority">
            <el-radio :label="3">紧急</el-radio>
            <el-radio :label="2">高</el-radio>
            <el-radio :label="1">普通</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="截止日期">
          <el-date-picker
            v-model="todoForm.dueDate"
            type="datetime"
            placeholder="选择日期时间"
            style="width: 100%"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, onMounted, computed } from 'vue'
import { dashboardApi } from '../api'
import { ElMessage } from 'element-plus'

export default {
  name: 'TodoWidget',
  setup() {
    const todos = ref([])
    const showAddDialog = ref(false)
    const editingTodo = ref(null)
    
    const todoForm = ref({
      title: '',
      content: '',
      priority: 1,
      status: 'pending',
      dueDate: null
    })
    
    const loadTodos = async () => {
      try {
        const res = await dashboardApi.getTodos()
        if (res.code === 200) {
          todos.value = res.data || []
        }
      } catch (error) {
        console.error('加载待办失败:', error)
      }
    }
    
    const handleSave = async () => {
      if (!todoForm.value.title) {
        ElMessage.warning('请输入标题')
        return
      }
      try {
        if (editingTodo.value) {
          await dashboardApi.updateTodo(editingTodo.value.id, todoForm.value)
          ElMessage.success('更新成功')
        } else {
          await dashboardApi.saveTodo(todoForm.value)
          ElMessage.success('添加成功')
        }
        showAddDialog.value = false
        editingTodo.value = null
        todoForm.value = { title: '', content: '', priority: 1, status: 'pending', dueDate: null }
        loadTodos()
      } catch (error) {
        ElMessage.error('操作失败')
      }
    }
    
    const editTodo = (todo) => {
      editingTodo.value = todo
      todoForm.value = { 
        title: todo.title, 
        content: todo.content || '',
        priority: todo.priority || 1,
        status: todo.status || 'pending',
        dueDate: todo.dueDate 
      }
      showAddDialog.value = true
    }
    
    const toggleStatus = async (todo) => {
      const newStatus = todo.status === 'completed' ? 'pending' : 'completed'
      try {
        await dashboardApi.updateTodo(todo.id, { ...todo, status: newStatus })
        loadTodos()
      } catch (error) {
        ElMessage.error('操作失败')
      }
    }
    
    const handleDelete = async (id) => {
      try {
        await dashboardApi.deleteTodo(id)
        ElMessage.success('删除成功')
        loadTodos()
      } catch (error) {
        ElMessage.error('删除失败')
      }
    }
    
    const getPriorityClass = (priority) => {
      const classes = { 3: 'urgent', 2: 'high', 1: 'normal' }
      return classes[priority] || 'normal'
    }
    
    const getPriorityText = (priority) => {
      const texts = { 3: '紧急', 2: '高', 1: '普通' }
      return texts[priority] || '普通'
    }
    
    const formatDate = (date) => {
      if (!date) return ''
      const d = new Date(date)
      return `${d.getMonth() + 1}/${d.getDate()}`
    }
    
    onMounted(() => {
      loadTodos()
    })
    
    return {
      todos,
      showAddDialog,
      editingTodo,
      todoForm,
      handleSave,
      editTodo,
      toggleStatus,
      handleDelete,
      getPriorityClass,
      getPriorityText,
      formatDate
    }
  }
}
</script>

<style scoped>
.todo-widget {
  background: #fff;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
}

.widget-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.widget-header h3 {
  font-size: 16px;
  font-weight: 600;
  color: #1e293b;
  margin: 0;
}

.add-btn {
  width: 28px;
  height: 28px;
  border-radius: 8px;
  border: none;
  background: #f1f5f9;
  color: #64748b;
  cursor: pointer;
  font-size: 18px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s;
}

.add-btn:hover {
  background: #0ea5e9;
  color: #fff;
}

.todo-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
  max-height: 300px;
  overflow-y: auto;
}

.todo-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  background: #f8fafc;
  border-radius: 10px;
  transition: all 0.2s;
}

.todo-item:hover {
  background: #f1f5f9;
}

.todo-item.completed {
  opacity: 0.6;
}

.todo-item.completed .todo-title {
  text-decoration: line-through;
  color: #94a3b8;
}

.todo-content {
  flex: 1;
  cursor: pointer;
}

.todo-title {
  font-size: 14px;
  color: #1e293b;
  margin-bottom: 4px;
}

.todo-meta {
  display: flex;
  gap: 8px;
  font-size: 12px;
}

.priority {
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 11px;
}

.priority.urgent {
  background: #fee2e2;
  color: #dc2626;
}

.priority.high {
  background: #ffedd5;
  color: #ea580c;
}

.priority.normal {
  background: #e0f2fe;
  color: #0284c7;
}

.due-date {
  color: #94a3b8;
}

.delete-btn {
  width: 28px;
  height: 28px;
  border: none;
  background: transparent;
  cursor: pointer;
  font-size: 14px;
  opacity: 0;
  transition: opacity 0.2s;
}

.todo-item:hover .delete-btn {
  opacity: 1;
}

.empty-state {
  text-align: center;
  padding: 20px;
  color: #94a3b8;
  font-size: 13px;
}
</style>
