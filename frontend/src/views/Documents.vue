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
          closeModal()
          await loadDocs()
        }
      } catch (error) {
        console.error('保存文档失败:', error)
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
      closeModal
    }
  }
}
</script>

<style scoped>
.page-content { animation: fadeIn 0.3s ease; }
@keyframes fadeIn { from { opacity: 0; transform: translateY(10px); } to { opacity: 1; transform: translateY(0); } }

.page-header { margin-bottom: 24px; display: flex; justify-content: space-between; align-items: center; }
.page-header h2 { font-size: 24px; font-weight: 700; color: #0c4a6e; margin: 0; }
.page-header p { font-size: 14px; color: #64748b; margin: 8px 0 0 0; }

.toolbar { display: flex; gap: 16px; margin-bottom: 20px; }
.search-input { flex: 1; padding: 12px 16px; border: 2px solid #e2e8f0; border-radius: 10px; font-size: 14px; outline: none; }
.search-input:focus { border-color: #0ea5e9; }

.btn-primary { padding: 12px 24px; border: none; border-radius: 10px; background: linear-gradient(135deg, #0ea5e9, #0284c7); color: #fff; font-size: 14px; font-weight: 500; cursor: pointer; }

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
.doc-meta { display: flex; justify-content: space-between; font-size: 12px; color: #94a3b8; margin-top: auto; }

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal {
  background: #fff;
  border-radius: 16px;
  padding: 24px;
  width: 500px;
  max-width: 90%;
  max-height: 85vh;
  overflow-y: auto;
}

.modal h3 {
  margin: 0 0 20px 0;
  font-size: 18px;
  color: #1e293b;
}

.form-group {
  margin-bottom: 16px;
}

.form-group label {
  display: block;
  font-size: 13px;
  color: #64748b;
  margin-bottom: 6px;
}

.form-group input,
.form-group select {
  width: 100%;
  box-sizing: border-box;
  padding: 10px 12px;
  border: 2px solid #e2e8f0;
  border-radius: 8px;
  font-size: 14px;
  outline: none;
}

.form-group input:focus,
.form-group select:focus {
  border-color: #0ea5e9;
}

.modal-actions {
  display: flex;
  gap: 12px;
  justify-content: flex-end;
  margin-top: 20px;
}

.btn-cancel {
  padding: 10px 20px;
  border: none;
  border-radius: 8px;
  background: #f1f5f9;
  color: #475569;
  font-size: 14px;
  cursor: pointer;
}

.btn-confirm {
  padding: 10px 20px;
  border: none;
  border-radius: 8px;
  background: linear-gradient(135deg, #0ea5e9, #0284c7);
  color: #fff;
  font-size: 14px;
  cursor: pointer;
}

@media (max-width: 1024px) { .docs-grid { grid-template-columns: repeat(2, 1fr); } }
@media (max-width: 640px) { .docs-grid { grid-template-columns: 1fr; } }
</style>
