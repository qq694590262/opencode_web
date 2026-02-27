<template>
  <div class="page-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <h2 class="page-title">
          <el-icon><OfficeBuilding /></el-icon>
          部门管理
        </h2>
        <p class="page-desc">管理组织架构和部门信息</p>
      </div>
      <el-button type="primary" :icon="Plus" @click="openAddDialog">新增部门</el-button>
    </div>

    <!-- 搜索栏 -->
    <div class="search-bar">
      <el-input
        v-model="searchQuery"
        placeholder="搜索部门名称..."
        :prefix-icon="Search"
        clearable
        class="search-input"
      />
    </div>

    <!-- 部门树形表格 -->
    <div class="oc-dept-table-wrapper oc-dept-card">
      <el-table
        :data="filteredDepartments"
        row-key="id"
        stripe
        border
        v-loading="loading"
        :tree-props="{ children: 'children' }"
        class="dept-table"
      >
        <!-- 第一列：部门名称，使用树形列，箭头与名称同行 -->
        <el-table-column prop="name" label="部门名称" min-width="240" type="tree" class-name="oc-dept-name-cell"></el-table-column>

        <el-table-column prop="code" label="部门编码" width="140" align="center" />

        <el-table-column prop="leader" label="负责人" width="120" align="center">
          <template #default="{ row }">
            <span v-if="row.leader">{{ row.leader }}</span>
            <span v-else class="text-gray">-</span>
          </template>
        </el-table-column>

        <el-table-column prop="phone" label="联系电话" width="140" align="center">
          <template #default="{ row }">
            <span v-if="row.phone">{{ row.phone }}</span>
            <span v-else class="text-gray">-</span>
          </template>
        </el-table-column>

        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="{ row }">
            <span :class="['status-tag', row.status === 1 ? 'active' : 'inactive']">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </span>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="140" align="center" fixed="right">
          <template #default="{ row }">
            <div class="action-btns">
              <el-tooltip content="添加子部门" placement="top">
                <el-button type="primary" :icon="Plus" circle size="small" @click="addChild(row)" v-if="row.status === 1" />
              </el-tooltip>
              <el-tooltip content="编辑" placement="top">
                <el-button type="warning" :icon="Edit" circle size="small" @click="editDept(row)" />
              </el-tooltip>
              <el-tooltip content="删除" placement="top">
                <el-button type="danger" :icon="Delete" circle size="small" @click="deleteDept(row)" />
              </el-tooltip>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 部门表单弹窗 -->
    <el-dialog 
      v-model="dialogVisible" 
      :title="isEdit ? '编辑部门' : '新增部门'"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form 
        ref="formRef"
        :model="formData" 
        :rules="rules"
        label-width="100px"
        class="dept-form"
      >
        <el-form-item label="上级部门" v-if="formData.parentId">
          <el-input :value="parentName" disabled />
        </el-form-item>
        
        <el-form-item label="部门名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入部门名称" />
        </el-form-item>
        
        <el-form-item label="部门编码" prop="code">
          <el-input v-model="formData.code" placeholder="请输入部门编码" />
        </el-form-item>
        
        <el-form-item label="负责人">
          <el-input v-model="formData.leader" placeholder="请输入负责人" />
        </el-form-item>
        
        <el-form-item label="联系电话">
          <el-input v-model="formData.phone" placeholder="请输入联系电话" />
        </el-form-item>
        
        <el-form-item label="排序">
          <el-input-number v-model="formData.sort" :min="0" :max="9999" />
        </el-form-item>
        
        <el-form-item label="状态">
          <el-switch
            v-model="formData.status"
            :active-value="1"
            :inactive-value="0"
            active-text="启用"
            inactive-text="禁用"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="closeDialog">取消</el-button>
        <el-button type="primary" :loading="saving" @click="saveDept">
          {{ saving ? '保存中...' : '保存' }}
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { OfficeBuilding, Plus, Search, Edit, Delete } from '@element-plus/icons-vue'
import { departmentApi } from '../api'

export default {
  name: 'Departments',
  setup() {
    const loading = ref(false)
    const saving = ref(false)
    const departments = ref([])
    const searchQuery = ref('')
    const dialogVisible = ref(false)
    const isEdit = ref(false)
    const formRef = ref(null)
    const parentName = ref('')
    const formData = ref({ id: null, parentId: null, name: '', code: '', leader: '', phone: '', sort: 0, status: 1 })
    const rules = {
      name: [{ required: true, message: '请输入部门名称', trigger: 'blur' }],
      code: [{ required: true, message: '请输入部门编码', trigger: 'blur' }]
    }
    const treeProps = { children: 'children', hasChildren: 'hasChildren' }

    // 递归构建树形结构
    const buildTree = (list, parentId = null) => {
      return list
        .filter(item => {
          if (parentId === null || parentId === 0) {
            return item.parentId === null || item.parentId === 0 || item.parentId === undefined
          }
          return item.parentId === parentId
        })
        .map(item => ({ ...item, children: buildTree(list, item.id) }))
        .sort((a, b) => (a.sort || 0) - (b.sort || 0))
    }

    const filteredDepartments = computed(() => {
      const list = departments.value || []
      if (!searchQuery.value) {
        return buildTree(list, null)
      }
      const q = searchQuery.value.toLowerCase()
      const filtered = list.filter(d => (d.name?.toLowerCase().includes(q)) || (d.code?.toLowerCase().includes(q)))
      return buildTree(filtered, null)
    })

    const loadDepartments = async () => {
      loading.value = true
      try {
        const res = await departmentApi.getAll()
        if (res.data) departments.value = res.data
      } catch (error) {
        console.error('获取部门列表失败:', error)
        ElMessage.error('获取部门列表失败')
      } finally {
        loading.value = false
      }
    }

    const openAddDialog = () => {
      isEdit.value = false
      formData.value = { id: null, parentId: null, name: '', code: '', leader: '', phone: '', sort: 0, status: 1 }
      parentName.value = ''
      dialogVisible.value = true
    }

    const addChild = (row) => {
      isEdit.value = false
      formData.value = { id: null, parentId: row.id, name: '', code: '', leader: '', phone: '', sort: 0, status: 1 }
      parentName.value = row.name
      dialogVisible.value = true
    }

    const editDept = (dept) => {
      isEdit.value = true
      formData.value = { ...dept }
      parentName.value = ''
      if (dept.parentId) {
        const parent = departments.value.find(d => d.id === dept.parentId)
        parentName.value = parent ? parent.name : ''
      }
      dialogVisible.value = true
    }

    const deleteDept = async (dept) => {
      try {
        await ElMessageBox.confirm(`确定要删除部门 "${dept.name}" 吗？此操作不可恢复。`, '删除确认', { confirmButtonText: '删除', cancelButtonText: '取消', type: 'warning' })
        await departmentApi.delete(dept.id)
        await loadDepartments()
        ElMessage.success('删除成功')
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除失败:', error)
          ElMessage.error('删除失败')
        }
      }
    }

    const saveDept = async () => {
      if (!formRef.value) return
      await formRef.value.validate(async (valid) => {
        if (!valid) return
        saving.value = true
        try {
          if (isEdit.value) {
            await departmentApi.update(formData.value.id, formData.value)
            ElMessage.success('更新成功')
          } else {
            await departmentApi.create(formData.value)
            ElMessage.success('创建成功')
          }
          closeDialog()
          await loadDepartments()
        } catch (error) {
          console.error('保存失败:', error)
          ElMessage.error('保存失败')
        } finally {
          saving.value = false
        }
      })
    }

    const closeDialog = () => { dialogVisible.value = false }

    onMounted(loadDepartments)

    return {
      loading, saving, departments, searchQuery, filteredDepartments, dialogVisible, isEdit,
      formRef, formData, rules, parentName, openAddDialog, addChild, editDept, deleteDept, saveDept, closeDialog,
      OfficeBuilding, Plus, Search, Edit, Delete, treeProps
    }
  }
}
</script>

<style scoped>
.page-container { padding: 0 20px; background: #f4f6f9; min-height: 100vh; }
.dept-table { border-radius: 12px; overflow: hidden; }
.dept-table thead { background: linear-gradient(135deg, #f8fbff 0%, #ffffff 100%); }
.dept-table tbody tr:hover { background: #f5f7fb; }
.dept-icon { color: #3c8dbc; font-size: 16px; margin-right: 6px; }
.dept-name { font-weight: 700; }
.text-gray { color: #888; }
.status-tag { padding: 4px 12px; border-radius: 12px; font-size: 12px; font-weight: 500; }
.action-btns { display: flex; gap: 6px; justify-content: center; }
.page-container { animation: fadeIn 0.3s ease; }
@keyframes fadeIn { from { opacity: 0; transform: translateY(0); } to { opacity: 1; transform: translateY(0); } }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; }
.header-content { display: flex; flex-direction: column; }
.page-title { display: flex; align-items: center; gap: 10px; font-size: 22px; font-weight: 600; color: #303133; margin: 0; }
.page-title .el-icon { color: #409EFF; }
.page-desc { font-size: 14px; color: #909399; margin: 6px 0 0 0; }
.search-bar { margin-bottom: 20px; }
.search-input { max-width: 320px; }
.dept-table-wrapper { padding: 12px; background: #fff; border-radius: 8px; }
.dept-table { width: 100%; }
.dept-name-cell { display: flex; align-items: center; gap: 8px; }
.dept-icon { color: #E6A23C; font-size: 16px; }
.dept-name { font-weight: 500; color: #303133; }
.dept-code { color: #606266; font-family: monospace; }
.status-tag { padding: 4px 12px; border-radius: 12px; font-size: 12px; font-weight: 500; }
.action-btns { display: flex; gap: 6px; justify-content: center; }
.text-gray { color: #c0c4cc; }
</style>
