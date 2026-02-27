<template>
  <div class="page-content">
    <div class="page-header">
      <h2>📄 文档列表</h2>
      <p>管理和共享文档</p>
    </div>

    <div class="toolbar">
      <input type="text" v-model="searchQuery" placeholder="搜索文档..." class="search-input" @input="handleSearch" />
      <button class="btn-primary" @click="showAddModal = true">+ 上传文档</button>
    </div>

    <div class="docs-grid">
      <div class="doc-card" v-for="doc in filteredDocs" :key="doc.id" @click="editDoc(doc)">
        <div class="doc-icon">{{ getFileIcon(doc.type) }}</div>
        <div class="doc-info">
          <h4>{{ doc.name }}</h4>
          <p>{{ doc.description }}</p>
        </div>
        <div class="doc-meta">
          <span>{{ doc.size }}</span>
          <span>{{ formatDate(doc.createTime) }}</span>
        </div>
        <div class="doc-actions">
          <button class="btn-edit" @click.stop="editDoc(doc)">编辑</button>
          <button class="btn-delete" @click.stop="deleteDoc(doc)">删除</button>
        </div>
      </div>
    </div>

    <!-- 添加/编辑文档弹窗 -->
    <div v-if="showAddModal || showEditModal" class="modal-overlay" @click.self="closeModal">
      <div class="modal">
        <h3>{{ showEditModal ? '编辑文档' : '上传文档' }}</h3>
        <form @submit.prevent="saveDoc">
          <div class="form-group">
            <label>文档名称</label>
            <input v-model="formData.name" required />
          </div>
          <div class="form-group">
            <label>描述</label>
            <input v-model="formData.description" />
          </div>
          <div class="form-group">
            <label>文件类型</label>
            <select v-model="formData.type">
              <option value="docx">Word</option>
              <option value="pdf">PDF</option>
              <option value="md">Markdown</option>
              <option value="xlsx">Excel</option>
            </select>
          </div>
          <div class="form-group">
            <label>文件大小</label>
            <input v-model="formData.size" placeholder="例如: 2.3 MB" />
          </div>
          <div class="modal-actions">
            <button type="button" class="btn-cancel" @click="closeModal">取消</button>
            <button type="submit" class="btn-confirm">保存</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { documentApi } from '../api'
import { showToast } from '../components/Toast.vue'
import { showConfirm } from '../components/ConfirmDialog.vue'

export default {
  name: 'Documents',
  setup() {
    const docs = ref([])
    const searchQuery = ref('')
    const showAddModal = ref(false)
    const showEditModal = ref(false)
    const formData = ref({
      name: '',
      description: '',
      type: 'docx',
      size: ''
    })

    const fileIcons = {
      docx: '📕',
      pdf: '📗',
      md: '📘',
      xlsx: '📙'
    }

    const getFileIcon = (type) => fileIcons[type] || '📄'

    const formatDate = (dateStr) => {
      if (!dateStr) return ''
      const date = new Date(dateStr)
      return date.toLocaleDateString('zh-CN')
    }

    const filteredDocs = computed(() => {
      if (!searchQuery.value) return docs.value
      const query = searchQuery.value.toLowerCase()
      return docs.value.filter(doc => 
        (doc.name && doc.name.toLowerCase().includes(query)) ||
        (doc.description && doc.description.toLowerCase().includes(query))
      )
    })

    const loadDocs = async () => {
      try {
        const res = await documentApi.getAll()
        if (res.code === 200) {
          docs.value = res.data || []
        }
      } catch (error) {
        console.error('获取文档列表失败:', error)
        showToast({ type: 'error', message: '获取文档列表失败: ' + (error.message || '未知错误') })
      }
    }

    const handleSearch = () => {}

    const editDoc = (doc) => {
      formData.value = { ...doc }
      showEditModal.value = true
    }

    const saveDoc = async () => {
      try {
        let res
        if (showEditModal.value) {
          res = await documentApi.update(formData.value.id, formData.value)
        } else {
          res = await documentApi.create(formData.value)
        }
        if (res.code === 200) {
          showToast({ type: 'success', message: showEditModal.value ? '文档更新成功' : '文档创建成功' })
          closeModal()
          await loadDocs()
        }
      } catch (error) {
        console.error('保存文档失败:', error)
        showToast({ type: 'error', message: '保存失败: ' + (error.message || '未知错误') })
      }
    }

    const deleteDoc = async (doc) => {
      const confirmed = await showConfirm({
        title: '删除确认',
        message: `确定要删除文档 "${doc.name}" 吗？此操作不可恢复。`,
        type: 'danger',
        confirmText: '删除',
        cancelText: '取消'
      })
      
      if (!confirmed) return
      
      try {
        const res = await documentApi.delete(doc.id)
        if (res.code === 200) {
          showToast({ type: 'success', message: '删除成功' })
          await loadDocs()
        }
      } catch (error) {
        console.error('删除文档失败:', error)
        showToast({ type: 'error', message: '删除失败: ' + (error.message || '未知错误') })
      }
    }

    const closeModal = () => {
      showAddModal.value = false
      showEditModal.value = false
      formData.value = {
        name: '',
        description: '',
        type: 'docx',
        size: ''
      }
    }

    onMounted(loadDocs)

    return {
      docs,
      filteredDocs,
      searchQuery,
      showAddModal,
      showEditModal,
      formData,
      getFileIcon,
      formatDate,
      handleSearch,
      editDoc,
      saveDoc,
      deleteDoc,
      closeModal
    }
  }
}
</script>

<style scoped>
/* 文档卡片特定样式 */
.docs-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 20px; }

.doc-card {
  background: #fff; border-radius: 16px; padding: 20px;
  display: flex; flex-direction: column; box-shadow: 0 4px 15px rgba(0,0,0,0.05); transition: all 0.3s;
  cursor: pointer;
}
.doc-card:hover { transform: translateY(-4px); box-shadow: 0 8px 25px rgba(0,0,0,0.1); }

.doc-icon { font-size: 40px; margin-bottom: 12px; }
.doc-info h4 { font-size: 15px; font-weight: 600; color: #1e293b; margin: 0 0 6px 0; }
.doc-info p { font-size: 13px; color: #64748b; margin: 0 0 12px 0; }
.doc-meta { display: flex; justify-content: space-between; font-size: 12px; color: #94a3b8; margin-top: auto; margin-bottom: 12px; }

.doc-actions { display: flex; gap: 8px; }
.btn-edit { flex: 1; padding: 8px; border: none; border-radius: 8px; background: #e0f2fe; color: #0284c7; font-size: 13px; cursor: pointer; transition: all 0.2s; }
.btn-edit:hover { background: #0ea5e9; color: white; }
.btn-delete { padding: 8px 12px; border: none; border-radius: 8px; background: #fee2e2; color: #dc2626; font-size: 13px; cursor: pointer; transition: all 0.2s; }
.btn-delete:hover { background: #dc2626; color: white; }

/* 搜索框 */
.search-input { flex: 1; padding: 12px 16px; border: 2px solid #e2e8f0; border-radius: 10px; font-size: 14px; outline: none; }
.search-input:focus { border-color: #0ea5e9; }

/* 弹窗调整 */
.modal { background: #fff; border-radius: 16px; padding: 24px; width: 500px; max-width: 90%; max-height: 85vh; overflow-y: auto; }
.modal h3 { margin: 0 0 20px 0; font-size: 18px; color: #1e293b; }

@media (max-width: 1024px) { .docs-grid { grid-template-columns: repeat(2, 1fr); } }
@media (max-width: 640px) { .docs-grid { grid-template-columns: 1fr; } }
</style>

