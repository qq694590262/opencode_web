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
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
    >
      <el-table-column prop="name" label="部门名称" min-width="200">
        <template #default="{ row }">
          <div class="dept-name">
            <el-icon><OfficeBuilding /></el-icon>
            <span>{{ row.name }}</span>
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
          <span v-else style="color: #cbd5e1;">-</span>
        </template>
      </el-table-column>
      
      <el-table-column prop="phone" label="联系电话" width="150" align="center">
        <template #default="{ row }">
          <span style="color: #64748b;">{{ row.phone || '-' }}</span>
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
      
      <el-table-column label="操作" width="220" align="center" fixed="right">
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
    <el-table 
      :data="filteredDepartments" 
      row-key="id"
      stripe 
      border
      class="dept-table"
      v-loading="loading"
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
    >
      <el-table-column prop="name" label="部门名称" min-width="180">
        <template #default="{ row }">
          <div class="dept-name">
            <el-icon><OfficeBuilding /></el-icon>
            <span>{{ row.name }}</span>
          </div>
        </template>
      </el-table-column>
      
      <el-table-column prop="code" label="部门编码" width="150" />
      
      <el-table-column prop="leader" label="负责人" width="120" align="center">
        <template #default="{ row }">
          {{ row.leader || '-' }}
        </template>
      </el-table-column>
      
      <el-table-column prop="phone" label="联系电话" width="150" align="center" />
      
      <el-table-column prop="status" label="状态" width="100" align="center">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'" effect="dark" round>
            {{ row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      
      <el-table-column prop="sort" label="排序" width="80" align="center" />
      
      <el-table-column label="操作" width="200" align="center" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" size="small" text @click="addChild(row)" v-if="row.status === 1">
            <el-icon><Plus /></el-icon>
            添加子部门
          </el-button>
          <el-button type="primary" size="small" text @click="editDept(row)">
            <el-icon><Edit /></el-icon>
            编辑
          </el-button>
          <el-button type="danger" size="small" text @click="deleteDept(row)">
            <el-icon><Delete /></el-icon>
          </el-button>
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
import { OfficeBuilding, Plus, Search, Edit, Delete } from '@element-plus/icons-vue'

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
        .filter(item => item.parentId === parentId)
        .map(item => ({
          ...item,
          children: buildTree(list, item.id)
        }))
        .sort((a, b) => a.sort - b.sort)
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
    
    // 模拟数据
    const mockData = [
      { id: 1, parentId: null, name: '总公司', code: 'HQ', leader: '张三', phone: '13800138000', sort: 0, status: 1 },
      { id: 2, parentId: 1, name: '技术部', code: 'TECH', leader: '李四', phone: '13800138001', sort: 1, status: 1 },
      { id: 3, parentId: 1, name: '市场部', code: 'MARKET', leader: '王五', phone: '13800138002', sort: 2, status: 1 },
      { id: 4, parentId: 1, name: '财务部', code: 'FIN', leader: '赵六', phone: '13800138003', sort: 3, status: 1 },
      { id: 5, parentId: 2, name: '研发组', code: 'RD', leader: '钱七', phone: '13800138004', sort: 1, status: 1 },
      { id: 6, parentId: 2, name: '测试组', code: 'QA', leader: '孙八', phone: '13800138005', sort: 2, status: 1 },
      { id: 7, parentId: 3, name: '品牌组', code: 'BRAND', leader: '周九', phone: '13800138006', sort: 1, status: 1 },
      { id: 8, parentId: 3, name: '销售组', code: 'SALES', leader: '吴十', phone: '13800138007', sort: 2, status: 1 }
    ]
    
    const loadDepartments = async () => {
      loading.value = true
      try {
        // 模拟API调用
        await new Promise(resolve => setTimeout(resolve, 300))
        departments.value = mockData
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
        
        // 模拟删除
        const index = departments.value.findIndex(d => d.id === dept.id)
        if (index > -1) {
          departments.value.splice(index, 1)
        }
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
          await new Promise(resolve => setTimeout(resolve, 500))
          
          if (isEdit.value) {
            const index = departments.value.findIndex(d => d.id === formData.value.id)
            if (index > -1) {
              departments.value[index] = { ...departments.value[index], ...formData.value }
            }
            ElMessage.success('更新成功')
          } else {
            const newDept = {
              ...formData.value,
              id: Date.now(),
              children: []
            }
            departments.value.push(newDept)
            ElMessage.success('创建成功')
          }
          
          closeDialog()
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

.dept-table {
  border-radius: 12px;
  overflow: hidden;
}

.dept-name {
  display: flex;
  align-items: center;
  gap: 8px;
}

.dept-name .el-icon {
  color: #409EFF;
}

.dept-form :deep(.el-form-item__label) {
  font-weight: 500;
}
</style>
