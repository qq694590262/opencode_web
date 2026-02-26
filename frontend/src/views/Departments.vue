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

    <!-- 部门树 -->
    <div class="dept-tree-wrapper">
      <el-tree
        :data="filteredDepartments"
        :props="treeProps"
        node-key="id"
        :expand-on-click-node="false"
        :default-expand-all="true"
        v-loading="loading"
        class="dept-tree"
      >
        <template #default="{ node, data }">
          <div class="dept-node">
            <div class="dept-info">
              <el-icon class="dept-icon"><OfficeBuilding /></el-icon>
              <span class="dept-name">{{ data.name }}</span>
              <span class="dept-code">({{ data.code }})</span>
            </div>
            <div class="dept-actions">
              <el-tag v-if="data.status === 1" type="success" size="small">启用</el-tag>
              <el-tag v-else type="danger" size="small">禁用</el-tag>
              <el-button type="primary" size="small" link @click.stop="addChild(data)" v-if="data.status === 1">
                <el-icon><Plus /></el-icon>添加
              </el-button>
              <el-button type="primary" size="small" link @click.stop="editDept(data)">
                <el-icon><Edit /></el-icon>编辑
              </el-button>
              <el-button type="danger" size="small" link @click.stop="deleteDept(data)">
                <el-icon><Delete /></el-icon>删除
              </el-button>
            </div>
          </div>
        </template>
      </el-tree>
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
    
    const treeProps = {
      children: 'children',
      label: 'name'
    }
    
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
      treeProps,
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
      Delete
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

.dept-tree-wrapper {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
}

.dept-tree {
  background: transparent;
}

.dept-tree :deep(.el-tree-node__content) {
  height: 48px;
  border-radius: 8px;
  margin-bottom: 4px;
}

.dept-tree :deep(.el-tree-node__content:hover) {
  background: #f5f7fa;
}

.dept-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 8px;
  width: 100%;
}

.dept-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.dept-icon {
  color: #E6A23C;
  font-size: 16px;
}

.dept-name {
  color: #303133;
  font-weight: 500;
}

.dept-code {
  color: #909399;
  font-size: 12px;
}

.dept-actions {
  display: flex;
  align-items: center;
  gap: 8px;
}

.dept-form :deep(.el-form-item__label) {
  font-weight: 500;
}
</style>
