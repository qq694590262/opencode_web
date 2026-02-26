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
    <el-table 
      :data="filteredDepartments" 
      row-key="id"
      stripe 
      class="dept-table"
      v-loading="loading"
      :expand-row-keys="expandedKeys"
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
    >
      <el-table-column prop="name" label="部门名称" min-width="200">
        <template #default="{ row, treeNode }">
          <div class="dept-name" :style="{ paddingLeft: treeNode.level * 20 + 'px' }">
            <el-icon v-if="row.children && row.children.length > 0" class="arrow-icon">
              <ArrowRight />
            </el-icon>
            <el-icon v-else class="folder-icon">
              <OfficeBuilding />
            </el-icon>
            <span class="dept-text">{{ row.name }}</span>
          </div>
        </template>
      </el-table-column>
      
      <el-table-column prop="code" label="部门编码" width="140">
        <template #default="{ row }">
          <span class="dept-code">{{ row.code }}</span>
        </template>
      </el-table-column>
      
      <el-table-column prop="leader" label="负责人" width="140" align="center">
        <template #default="{ row }">
          <div class="leader-cell" v-if="row.leader">
            <span class="leader-avatar">{{ row.leader.charAt(0) }}</span>
            <span>{{ row.leader }}</span>
          </div>
          <span v-else class="no-data">-</span>
        </template>
      </el-table-column>
      
      <el-table-column prop="phone" label="联系电话" width="150" align="center">
        <template #default="{ row }">
          <span class="phone-text">{{ row.phone || '-' }}</span>
        </template>
      </el-table-column>
      
      <el-table-column prop="status" label="状态" width="100" align="center">
        <template #default="{ row }">
          <span :class="['status-tag', row.status === 1 ? 'active' : 'inactive']">
            {{ row.status === 1 ? '启用' : '禁用' }}
          </span>
        </template>
      </el-table-column>
      
      <el-table-column prop="sort" label="排序" width="80" align="center">
        <template #default="{ row }">
          <span class="sort-num">{{ row.sort }}</span>
        </template>
      </el-table-column>
      
      <el-table-column label="操作" width="180" align="center" fixed="right">
        <template #default="{ row }">
          <div class="action-buttons">
            <el-button type="primary" size="small" text bg @click="addChild(row)" v-if="row.status === 1">
              <el-icon><Plus /></el-icon>
              添加
            </el-button>
            <el-button type="primary" size="small" text bg @click="editDept(row)">
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
            <el-button type="danger" size="small" text bg @click="deleteDept(row)">
              <el-icon><Delete /></el-icon>
              删除
            </el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>

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
import { OfficeBuilding, Plus, Search, Edit, Delete, ArrowRight } from '@element-plus/icons-vue'
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
    
    const formData = ref({
      id: null,
      parentId: null,
      name: '',
      code: '',
      leader: '',
      phone: '',
      sort: 0,
      status: 1
    })
    
    const rules = {
      name: [
        { required: true, message: '请输入部门名称', trigger: 'blur' }
      ],
      code: [
        { required: true, message: '请输入部门编码', trigger: 'blur' }
      ]
    }
    
    // 递归构建树形结构
    const buildTree = (list, parentId = null) => {
      return list
        .filter(item => {
          // 处理根节点：parentId 为 null 或 0
          if (parentId === null || parentId === 0) {
            return item.parentId === null || item.parentId === 0 || item.parentId === undefined
          }
          return item.parentId === parentId
        })
        .map(item => ({
          ...item,
          children: buildTree(list, item.id)
        }))
        .sort((a, b) => (a.sort || 0) - (b.sort || 0))
    }
    
    const filteredDepartments = computed(() => {
      if (!searchQuery.value) {
        return buildTree(departments.value)
      }
      const query = searchQuery.value.toLowerCase()
      const filtered = departments.value.filter(dept => 
        dept.name?.toLowerCase().includes(query) ||
        dept.code?.toLowerCase().includes(query)
      )
      return buildTree(filtered)
    })
    
    const loadDepartments = async () => {
      loading.value = true
      try {
        const res = await departmentApi.getAll()
        if (res.data) {
          departments.value = res.data
        }
      } catch (error) {
        console.error('获取部门列表失败:', error)
        ElMessage.error('获取部门列表失败')
      } finally {
        loading.value = false
      }
    }
    
    const openAddDialog = () => {
      isEdit.value = false
      formData.value = {
        id: null,
        parentId: null,
        name: '',
        code: '',
        leader: '',
        phone: '',
        sort: 0,
        status: 1
      }
      parentName.value = ''
      dialogVisible.value = true
    }
    
    const addChild = (row) => {
      isEdit.value = false
      formData.value = {
        id: null,
        parentId: row.id,
        name: '',
        code: '',
        leader: '',
        phone: '',
        sort: 0,
        status: 1
      }
      parentName.value = row.name
      dialogVisible.value = true
    }
    
    const editDept = (dept) => {
      isEdit.value = true
      formData.value = { ...dept }
      parentName.value = ''
      // 查找父部门名称
      if (dept.parentId) {
        const parent = departments.value.find(d => d.id === dept.parentId)
        parentName.value = parent ? parent.name : ''
      }
      dialogVisible.value = true
    }
    
    const deleteDept = async (dept) => {
      try {
        await ElMessageBox.confirm(
          `确定要删除部门 "${dept.name}" 吗？此操作不可恢复。`,
          '删除确认',
          { confirmButtonText: '删除', cancelButtonText: '取消', type: 'warning' }
        )
        
        await departmentApi.delete(dept.id)
        // 重新加载数据
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
          // 重新加载数据
          await loadDepartments()
        } catch (error) {
          console.error('保存失败:', error)
          ElMessage.error('保存失败')
        } finally {
          saving.value = false
        }
      })
    }
    
    const closeDialog = () => {
      dialogVisible.value = false
    }
    
    onMounted(loadDepartments)
    
    return {
      loading,
      saving,
      departments,
      searchQuery,
      filteredDepartments,
      dialogVisible,
      isEdit,
      formRef,
      formData,
      rules,
      parentName,
      openAddDialog,
      addChild,
      editDept,
      deleteDept,
      saveDept,
      closeDialog,
      OfficeBuilding,
      Plus,
      Search,
      Edit,
      Delete,
      ArrowRight
    }
  }
}
</script>

<style scoped>
.page-container {
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.header-content {
  display: flex;
  flex-direction: column;
}

.page-title {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 22px;
  font-weight: 600;
  color: #303133;
  margin: 0;
}

.page-title .el-icon {
  color: #409EFF;
}

.page-desc {
  font-size: 14px;
  color: #909399;
  margin: 6px 0 0 0;
}

.search-bar {
  margin-bottom: 20px;
}

.search-input {
  max-width: 320px;
}

.dept-table {
  border-radius: 12px;
  overflow: hidden;
}

.dept-table :deep(.el-table__row) {
  height: 48px;
}

.dept-table :deep(.el-table__cell) {
  padding: 8px 0;
}

.dept-name {
  display: flex;
  align-items: center;
  gap: 8px;
}

.arrow-icon {
  color: #409EFF;
  font-size: 14px;
  flex-shrink: 0;
}

.folder-icon {
  color: #E6A23C;
  font-size: 16px;
  flex-shrink: 0;
}

.dept-text {
  color: #303133;
  font-weight: 500;
}

.dept-code {
  color: #606266;
  font-family: 'Monaco', 'Consolas', monospace;
}

.leader-cell {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.leader-avatar {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background: linear-gradient(135deg, #409EFF 0%, #67C23A 100%);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: 600;
}

.no-data {
  color: #cbd5e1;
}

.phone-text {
  color: #64748b;
}

.status-tag {
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.status-tag.active {
  background: #e8f5e9;
  color: #67C23A;
}

.status-tag.inactive {
  background: #ffebee;
  color: #F56C6C;
}

.sort-num {
  color: #909399;
  font-weight: 500;
}

.action-buttons {
  display: flex;
  justify-content: center;
  gap: 4px;
}

.dept-form :deep(.el-form-item__label) {
  font-weight: 500;
}
</style>
