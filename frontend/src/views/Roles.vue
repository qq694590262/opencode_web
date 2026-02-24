<template>
  <div class="page-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <h2 class="page-title">
          <el-icon><Key /></el-icon>
          角色管理
        </h2>
        <p class="page-desc">管理系统角色与权限配置</p>
      </div>
      <el-button type="primary" :icon="Plus" @click="openAddModal">新增角色</el-button>
    </div>

    <!-- 搜索栏 -->
    <div class="search-bar">
      <el-input
        v-model="searchQuery"
        placeholder="搜索角色名称..."
        :prefix-icon="Search"
        clearable
        class="search-input"
      />
    </div>

    <!-- 角色卡片列表 -->
    <div class="roles-grid" v-loading="loading">
      <el-card 
        v-for="role in filteredRoles" 
        :key="role.id" 
        class="role-card"
        shadow="hover"
      >
        <div class="role-header">
          <div class="role-icon-wrap">
            <el-icon class="role-icon"><UserFilled /></el-icon>
          </div>
          <div class="role-info">
            <h4 class="role-name">{{ role.name }}</h4>
            <span class="role-code">{{ role.code }}</span>
          </div>
        </div>
        
        <p class="role-desc">{{ role.description || '暂无描述' }}</p>
        
        <div class="role-footer">
          <div class="role-users">
            <el-icon><User /></el-icon>
            <span>{{ role.userCount || role.user_count || 0 }} 人</span>
          </div>
          
          <div class="role-actions">
            <el-button type="primary" size="small" text @click="openEditModal(role)">
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
            <el-button type="danger" size="small" text @click="handleDelete(role)">
              <el-icon><Delete /></el-icon>
              删除
            </el-button>
          </div>
        </div>
      </el-card>

      <!-- 空状态 -->
      <el-empty v-if="!loading && filteredRoles.length === 0" description="暂无角色数据" />
    </div>

    <!-- 新增/编辑弹窗 -->
    <el-dialog 
      v-model="showModal" 
      :title="isEditing ? '编辑角色' : '新增角色'"
      width="480px"
      :close-on-click-modal="false"
    >
      <el-form 
        ref="formRef"
        :model="formData" 
        :rules="rules"
        label-width="90px"
        class="role-form"
      >
        <el-form-item label="角色编码" prop="code">
          <el-input v-model="formData.code" placeholder="例如: ADMIN" />
        </el-form-item>
        
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入角色名称" />
        </el-form-item>
        
        <el-form-item label="角色描述">
          <el-input 
            v-model="formData.description" 
            type="textarea" 
            :rows="3"
            placeholder="请输入角色描述" 
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="closeModal">取消</el-button>
        <el-button type="primary" :loading="loading" @click="handleSubmit">
          {{ isEditing ? '保存' : '创建' }}
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { roleApi } from '../api'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Key, Plus, Search, User, UserFilled, Edit, Delete } from '@element-plus/icons-vue'

const roles = ref([])
const searchQuery = ref('')
const showModal = ref(false)
const isEditing = ref(false)
const loading = ref(false)
const formRef = ref(null)

const formData = ref({
  code: '',
  name: '',
  description: ''
})

const rules = {
  code: [
    { required: true, message: '请输入角色编码', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入角色名称', trigger: 'blur' }
  ]
}

const filteredRoles = computed(() => {
  if (!searchQuery.value) return roles.value
  const query = searchQuery.value.toLowerCase()
  return roles.value.filter(role => 
    role.name?.toLowerCase().includes(query) ||
    role.code?.toLowerCase().includes(query) ||
    role.description?.toLowerCase().includes(query)
  )
})

const loadRoles = async () => {
  try {
    loading.value = true
    const res = await roleApi.getAll()
    if (res.code === 200) {
      roles.value = res.data || []
    }
  } catch (error) {
    console.error('加载角色失败:', error)
    ElMessage.error('加载角色失败')
  } finally {
    loading.value = false
  }
}

const openAddModal = () => {
  isEditing.value = false
  formData.value = { code: '', name: '', description: '' }
  showModal.value = true
}

const openEditModal = (role) => {
  isEditing.value = true
  formData.value = {
    code: role.code || '',
    name: role.name || '',
    description: role.description || ''
  }
  showModal.value = true
}

const closeModal = () => {
  showModal.value = false
  formData.value = { code: '', name: '', description: '' }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    
    try {
      loading.value = true
      
      if (isEditing.value) {
        await roleApi.update(formData.value.code, formData.value)
        ElMessage.success('角色更新成功')
      } else {
        await roleApi.create(formData.value)
        ElMessage.success('角色创建成功')
      }
      
      closeModal()
      await loadRoles()
    } catch (error) {
      console.error('保存角色失败:', error)
      ElMessage.error('保存失败')
    } finally {
      loading.value = false
    }
  })
}

const handleDelete = async (role) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除角色 "${role.name}" 吗？此操作不可恢复。`,
      '删除确认',
      { confirmButtonText: '删除', cancelButtonText: '取消', type: 'warning' }
    )
    
    await roleApi.delete(role.id)
    ElMessage.success('删除成功')
    await loadRoles()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除角色失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

onMounted(() => {
  loadRoles()
})
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
  color: #E6A23C;
}

.page-desc {
  font-size: 14px;
  color: #909399;
  margin: 6px 0 0 0;
}

.search-bar {
  margin-bottom: 24px;
}

.search-input {
  max-width: 320px;
}

.roles-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.role-card {
  border-radius: 12px;
  transition: all 0.3s ease;
}

.role-card:hover {
  transform: translateY(-4px);
}

.role-header {
  display: flex;
  align-items: center;
  gap: 14px;
  margin-bottom: 12px;
}

.role-icon-wrap {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  background: linear-gradient(135deg, #409EFF 0%, #67C23A 100%);
  display: flex;
  align-items: center;
  justify-content: center;
}

.role-icon {
  font-size: 24px;
  color: #fff;
}

.role-info {
  flex: 1;
}

.role-name {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 4px 0;
}

.role-code {
  font-size: 12px;
  color: #909399;
  font-family: monospace;
}

.role-desc {
  font-size: 14px;
  color: #606266;
  margin: 0 0 16px 0;
  line-height: 1.5;
}

.role-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 12px;
  border-top: 1px solid #ebeef5;
}

.role-users {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  color: #909399;
}

.role-actions {
  display: flex;
  gap: 8px;
}

.role-form :deep(.el-form-item__label) {
  font-weight: 500;
}
</style>
