<template>
  <div class="page-content">
    <div class="page-header">
      <h2>📈 报表中心</h2>
      <p>查看和管理各类统计报表</p>
    </div>

    <div class="toolbar">
      <input type="text" v-model="searchQuery" placeholder="搜索报表..." class="search-input" @input="handleSearch" />
      <button class="btn-primary" @click="showAddModal = true">+ 新建报表</button>
    </div>

    <div class="reports-grid">
      <div class="report-card" v-for="report in filteredReports" :key="report.id" @click="editReport(report)">
        <div class="report-icon">{{ report.icon || '📊' }}</div>
        <div class="report-info">
          <h4>{{ report.title }}</h4>
          <p>{{ report.description }}</p>
        </div>
        <div class="report-actions">
          <button class="btn-view" @click.stop="viewReport(report)">查看</button>
          <button class="btn-delete" @click.stop="deleteReport(report)">删除</button>
        </div>
      </div>
    </div>

    <!-- 添加/编辑报表弹窗 -->
    <div v-if="showAddModal || showEditModal" class="modal-overlay" @click.self="closeModal">
      <div class="modal">
        <h3>{{ showEditModal ? '编辑报表' : '新建报表' }}</h3>
        <form @submit.prevent="saveReport">
          <div class="form-group">
            <label>报表名称</label>
            <input v-model="formData.title" required />
          </div>
          <div class="form-group">
            <label>描述</label>
            <input v-model="formData.description" />
          </div>
          <div class="form-group">
            <label>图标</label>
            <input v-model="formData.icon" placeholder="例如: 📊" />
          </div>
          <div class="form-group">
            <label>类型</label>
            <select v-model="formData.type">
              <option value="sales">销售</option>
              <option value="user">用户</option>
              <option value="finance">财务</option>
              <option value="inventory">库存</option>
              <option value="performance">业绩</option>
              <option value="traffic">流量</option>
            </select>
          </div>
          <div class="modal-actions">
            <button type="button" class="btn-cancel" @click="closeModal">取消</button>
            <button type="submit" class="btn-confirm">保存</button>
          </div>
        </form>
      </div>
    </div>
    <!-- 查看报表弹窗 -->
    <div v-if="showViewModal" class="modal-overlay" @click.self="showViewModal = false">
      <div class="modal">
        <h3>📊 {{ currentReport.title }}</h3>
        <div class="view-content">
          <div class="form-group">
            <label>描述</label>
            <p class="view-value">{{ currentReport.description || '暂无描述' }}</p>
          </div>
          <div class="form-group">
            <label>类型</label>
            <p class="view-value">{{ getTypeText(currentReport.type) }}</p>
          </div>
          <div class="form-group">
            <label>图标</label>
            <p class="view-value">{{ currentReport.icon || '📊' }}</p>
          </div>
        </div>
        <div class="modal-actions">
          <button type="button" class="btn-cancel" @click="showViewModal = false">关闭</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { reportApi } from '../api'
import { showToast } from '../components/Toast.vue'
import { showConfirm } from '../components/ConfirmDialog.vue'

export default {
  name: 'Reports',
  setup() {
    const reports = ref([])
    const searchQuery = ref('')
    const showAddModal = ref(false)
    const showEditModal = ref(false)
    const showViewModal = ref(false)
    const currentReport = ref({})
    const formData = ref({
      title: '',
      description: '',
      icon: '📊',
      type: 'sales'
    })

    const filteredReports = computed(() => {
      if (!searchQuery.value) return reports.value
      const query = searchQuery.value.toLowerCase()
      return reports.value.filter(report => 
        (report.title && report.title.toLowerCase().includes(query)) ||
        (report.description && report.description.toLowerCase().includes(query))
      )
    })

    const loadReports = async () => {
      try {
        const res = await reportApi.getAll()
        if (res.code === 200) {
          reports.value = res.data || []
        }
      } catch (error) {
        console.error('获取报表列表失败:', error)
        showToast({ type: 'error', message: '获取报表列表失败: ' + (error.message || '未知错误') })
      }
    }

    const handleSearch = () => {}

    const getTypeText = (type) => {
      const typeMap = {
        'sales': '销售报表',
        'user': '用户报表',
        'finance': '财务报表',
        'inventory': '库存报表',
        'performance': '业绩报表',
        'traffic': '流量报表'
      }
      return typeMap[type] || type || '未知类型'
    }

    const viewReport = (report) => {
      currentReport.value = report
      showViewModal.value = true
    }

    const editReport = (report) => {
      formData.value = { ...report }
      showEditModal.value = true
    }

    const deleteReport = async (report) => {
      const confirmed = await showConfirm({
        title: '删除确认',
        message: `确定要删除报表 "${report.title}" 吗？此操作不可恢复。`,
        type: 'danger',
        confirmText: '删除',
        cancelText: '取消'
      })
      
      if (!confirmed) return
      
      try {
        const res = await reportApi.delete(report.id)
        if (res.code === 200) {
          showToast({ type: 'success', message: '删除成功' })
          await loadReports()
        }
      } catch (error) {
        console.error('删除报表失败:', error)
        showToast({ type: 'error', message: '删除失败: ' + (error.message || '未知错误') })
      }
    }

    const saveReport = async () => {
      try {
        let res
        if (showEditModal.value) {
          res = await reportApi.update(formData.value.id, formData.value)
        } else {
          res = await reportApi.create(formData.value)
        }
        if (res.code === 200) {
          showToast({ type: 'success', message: showEditModal.value ? '报表更新成功' : '报表创建成功' })
          closeModal()
          await loadReports()
        }
      } catch (error) {
        console.error('保存报表失败:', error)
        showToast({ type: 'error', message: '保存失败: ' + (error.message || '未知错误') })
      }
    }

    const closeModal = () => {
      showAddModal.value = false
      showEditModal.value = false
      formData.value = {
        title: '',
        description: '',
        icon: '📊',
        type: 'sales'
      }
    }

    onMounted(loadReports)

    return {
      reports,
      filteredReports,
      searchQuery,
      showAddModal,
      showEditModal,
      showViewModal,
      currentReport,
      formData,
      handleSearch,
      getTypeText,
      viewReport,
      editReport,
      deleteReport,
      saveReport,
      closeModal
    }
  }
}
</script>

<style scoped>
.page-content { animation: fadeIn 0.3s ease; }
@keyframes fadeIn { from { opacity: 0; transform: translateY(10px); } to { opacity: 1; transform: translateY(0); } }

.page-header { margin-bottom: 24px; }
.page-header h2 { font-size: 24px; font-weight: 700; color: #0c4a6e; margin: 0 0 8px 0; }
.page-header p { font-size: 14px; color: #64748b; margin: 0; }

.toolbar { display: flex; gap: 16px; margin-bottom: 20px; }
.search-input { flex: 1; padding: 12px 16px; border: 2px solid #e2e8f0; border-radius: 10px; font-size: 14px; outline: none; }
.search-input:focus { border-color: #0ea5e9; }

.btn-primary { padding: 12px 24px; border: none; border-radius: 10px; background: linear-gradient(135deg, #0ea5e9, #0284c7); color: #fff; font-size: 14px; font-weight: 500; cursor: pointer; }

.reports-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 20px; }

.report-card {
  background: #fff; border-radius: 16px; padding: 24px;
  display: flex; flex-direction: column; align-items: center;
  box-shadow: 0 4px 15px rgba(0,0,0,0.05); transition: all 0.3s ease;
  cursor: pointer;
}
.report-card:hover { transform: translateY(-4px); box-shadow: 0 8px 25px rgba(0,0,0,0.1); }

.report-icon { font-size: 40px; margin-bottom: 16px; }
.report-info { text-align: center; margin-bottom: 16px; }
.report-info h4 { font-size: 16px; font-weight: 600; color: #1e293b; margin: 0 0 8px 0; }
.report-info p { font-size: 13px; color: #64748b; margin: 0; }

.report-actions { display: flex; gap: 8px; width: 100%; }
.btn-view { flex: 1; padding: 10px; border: none; border-radius: 10px; background: linear-gradient(135deg, #0ea5e9, #0284c7); color: #fff; font-size: 14px; font-weight: 500; cursor: pointer; }
.btn-delete { padding: 10px 16px; border: none; border-radius: 10px; background: #fee2e2; color: #dc2626; font-size: 14px; cursor: pointer; }

.modal-overlay { position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0,0,0,0.5); display: flex; align-items: center; justify-content: center; z-index: 1000; }
.modal { background: #fff; border-radius: 16px; padding: 24px; width: 500px; max-width: 90%; max-height: 85vh; overflow-y: auto; }
.modal h3 { margin: 0 0 20px 0; font-size: 18px; color: #1e293b; }
.view-content { padding: 16px 0; }
.view-value { font-size: 15px; color: #1e293b; margin: 0; padding: 10px 0; border-bottom: 1px solid #f1f5f9; }
.form-group { margin-bottom: 16px; }
.form-group label { display: block; font-size: 13px; color: #64748b; margin-bottom: 6px; }
.form-group input, .form-group select { width: 100%; box-sizing: border-box; padding: 10px 12px; border: 2px solid #e2e8f0; border-radius: 8px; font-size: 14px; outline: none; }
.form-group input:focus, .form-group select:focus { border-color: #0ea5e9; }
.modal-actions { display: flex; gap: 12px; justify-content: flex-end; margin-top: 20px; }
.btn-cancel { padding: 10px 20px; border: none; border-radius: 8px; background: #f1f5f9; color: #475569; font-size: 14px; cursor: pointer; }
.btn-confirm { padding: 10px 20px; border: none; border-radius: 8px; background: linear-gradient(135deg, #0ea5e9, #0284c7); color: #fff; font-size: 14px; cursor: pointer; }

@media (max-width: 1024px) { .reports-grid { grid-template-columns: repeat(2, 1fr); } }
@media (max-width: 640px) { .reports-grid { grid-template-columns: 1fr; } }
</style>
