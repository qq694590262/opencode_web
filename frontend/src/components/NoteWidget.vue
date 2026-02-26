<template>
  <div class="note-widget">
    <div class="widget-header">
      <h3>📝 便签</h3>
      <button class="add-btn" @click="showAddDialog = true">+</button>
    </div>
    
    <div class="notes-grid">
      <div 
        v-for="note in notes" 
        :key="note.id" 
        class="note-card"
        :style="{ backgroundColor: note.color || '#fef3c7' }"
      >
        <div class="note-content">
          <div class="note-title">{{ note.title || '无标题' }}</div>
          <div class="note-text">{{ note.content }}</div>
        </div>
        <div class="note-actions">
          <button @click="editNote(note)" title="编辑">✏️</button>
          <button @click="handleDelete(note.id)" title="删除">🗑️</button>
        </div>
      </div>
      
      <div v-if="notes.length === 0" class="empty-state">
        <span>暂无便签，点击 + 添加</span>
      </div>
    </div>

    <!-- 添加/编辑对话框 -->
    <el-dialog 
      v-model="showAddDialog" 
      :title="editingNote ? '编辑便签' : '添加便签'"
      width="400px"
    >
      <el-form :model="noteForm" label-width="60px">
        <el-form-item label="标题">
          <el-input v-model="noteForm.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="内容">
          <el-input 
            v-model="noteForm.content" 
            type="textarea" 
            :rows="4" 
            placeholder="请输入内容" 
          />
        </el-form-item>
        <el-form-item label="颜色">
          <div class="color-options">
            <button 
              v-for="color in colors" 
              :key="color"
              class="color-btn"
              :class="{ active: noteForm.color === color }"
              :style="{ backgroundColor: color }"
              @click="noteForm.color = color"
            ></button>
          </div>
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
import { ref, onMounted } from 'vue'
import { dashboardApi } from '../api'
import { getUserInfo } from '../auth'
import { ElMessage } from 'element-plus'

export default {
  name: 'NoteWidget',
  setup() {
    const notes = ref([])
    const showAddDialog = ref(false)
    const editingNote = ref(null)
    
    const noteForm = ref({
      title: '',
      content: '',
      color: '#fef3c7'
    })
    
    const colors = [
      '#fef3c7', // 黄色
      '#dcfce7', // 绿色
      '#dbeafe', // 蓝色
      '#fce7f3', // 粉色
      '#f3e8ff', // 紫色
      '#ffedd5', // 橙色
    ]
    
    const getCurrentUserId = () => {
      const user = getUserInfo()
      return user?.id || 1
    }
    
    const loadNotes = async () => {
      try {
        const userId = getCurrentUserId()
        const res = await dashboardApi.getNotes(userId)
        if (res.code === 200) {
          notes.value = res.data || []
        }
      } catch (error) {
        console.error('加载便签失败:', error)
      }
    }
    
    const handleSave = async () => {
      try {
        const userId = getCurrentUserId()
        if (editingNote.value) {
          await dashboardApi.updateNote(editingNote.value.id, noteForm.value)
          ElMessage.success('更新成功')
        } else {
          await dashboardApi.saveNote({ ...noteForm.value, userId })
          ElMessage.success('添加成功')
        }
        showAddDialog.value = false
        editingNote.value = null
        noteForm.value = { title: '', content: '', color: '#fef3c7' }
        loadNotes()
      } catch (error) {
        ElMessage.error('操作失败')
      }
    }
    
    const editNote = (note) => {
      editingNote.value = note
      noteForm.value = { ...note }
      showAddDialog.value = true
    }
    
    const handleDelete = async (id) => {
      try {
        await dashboardApi.deleteNote(id)
        ElMessage.success('删除成功')
        loadNotes()
      } catch (error) {
        ElMessage.error('删除失败')
      }
    }
    
    onMounted(() => {
      loadNotes()
    })
    
    return {
      notes,
      showAddDialog,
      editingNote,
      noteForm,
      colors,
      handleSave,
      editNote,
      handleDelete
    }
  }
}
</script>

<style scoped>
.note-widget {
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

.notes-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}

.note-card {
  padding: 12px;
  border-radius: 12px;
  min-height: 100px;
  position: relative;
  cursor: pointer;
  transition: transform 0.2s;
}

.note-card:hover {
  transform: translateY(-2px);
}

.note-title {
  font-size: 14px;
  font-weight: 600;
  color: #1e293b;
  margin-bottom: 6px;
}

.note-text {
  font-size: 12px;
  color: #475569;
  line-height: 1.5;
}

.note-actions {
  position: absolute;
  top: 8px;
  right: 8px;
  display: none;
  gap: 4px;
}

.note-card:hover .note-actions {
  display: flex;
}

.note-actions button {
  width: 24px;
  height: 24px;
  border: none;
  background: rgba(255, 255, 255, 0.8);
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
}

.empty-state {
  grid-column: span 2;
  text-align: center;
  padding: 20px;
  color: #94a3b8;
  font-size: 13px;
}

.color-options {
  display: flex;
  gap: 8px;
}

.color-btn {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  border: 2px solid transparent;
  cursor: pointer;
  transition: all 0.2s;
}

.color-btn.active {
  border-color: #1e293b;
  transform: scale(1.1);
}
</style>
