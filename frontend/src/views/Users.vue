<template>
  <div class="page-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <h2 class="page-title">
          <el-icon><User /></el-icon>
          用户管理
        </h2>
        <p class="page-desc">管理系统用户账号</p>
      </div>
      <el-button type="primary" :icon="Plus" @click="openAddDialog">新增用户</el-button>
    </div>

    <!-- 搜索栏 -->
    <div class="search-bar">
      <el-input
        v-model="searchQuery"
        placeholder="搜索用户名、邮箱..."
        :prefix-icon="Search"
        clearable
        class="search-input"
        @input="handleSearch"
      />
    </div>

    <!-- 用户表格 -->
    <el-table 
      :data="filteredUsers" 
      stripe 
      border
      class="user-table"
      v-loading="loading"
    >
      <el-table-column prop="id" label="ID" width="80" align="center" />
      
      <el-table-column label="用户" min-width="150">
        <template #default="{ row }">
          <div class="user-info">
            <el-avatar :size="36" :style="getAvatarStyle(row)">
              {{ (row.name || row.username || 'U')[0].toUpperCase() }}
            </el-avatar>
            <div class="user-detail">
              <span class="user-name">{{ row.name || row.username }}</span>
              <span class="user-username">@{{ row.username }}</span>
            </div>
          </div>
        </template>
      </el-table-column>
      
      <el-table-column prop="email" label="邮箱" min-width="180" />
      
      <el-table-column label="角色" width="120" align="center">
        <template #default="{ row }">
          <el-tag :type="getRoleType(row.role)" effect="light" round>
            {{ getRoleText(row.role) }}
          </el-tag>
        </template>
      </el-table-column>
      
      <el-table-column label="状态" width="100" align="center">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'" effect="dark" round>
            {{ row.status === 1 ? '正常' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      
      <el-table-column label="操作" width="160" align="center" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" size="small" text @click="editUser(row)">
            <el-icon><Edit /></el-icon>
            编辑
          </el-button>
          <el-button type="danger" size="small" text @click="deleteUser(row)">
            <el-icon><Delete /></el-icon>
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 用户表单弹窗 -->
    <el-dialog 
      v-model="dialogVisible" 
      :title="isEdit ? '编辑用户' : '新增用户'"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form 
        ref="formRef"
        :model="formData" 
        :rules="rules"
        label-width="80px"
        class="user-form"
      >
        <el-form-item label="用户名" prop="username">
          <el-input v-model="formData.username" placeholder="请输入用户名" />
        </el-form-item>
        
        <el-form-item label="姓名" prop="name">
          <el-input v-model="formData.name" placeholder="请输入姓名" />
        </el-form-item>
        
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="formData.email" placeholder="请输入邮箱" />
        </el-form-item>
        
        <el-form-item label="角色" prop="role">
          <el-select v-model="formData.role" placeholder="请选择角色" style="width: 100%">
            <el-option label="管理员" value="ADMIN" />
            <el-option label="编辑" value="EDITOR" />
            <el-option label="用户" value="USER" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="状态" prop="status">
          <el-switch
            v-model="formData.status"
            :active-value="1"
            :inactive-value="0"
            active-text="正常"
            inactive-text="禁用"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="closeDialog">取消</el-button>
        <el-button type="primary" :loading="saving" @click="saveUser">
          {{ saving ? '保存中...' : '保存' }}
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { userApi } from '../api'
import { ElMessage, ElMessageBox } from 'element-plus'
import { User, Plus, Search, Edit, Delete } from '@element-plus/icons-vue'

export default {
  name: 'Users',
  setup() {
    const loading = ref(false)
    const saving = ref(false)
    const users = ref([])
    const searchQuery = ref('')
    const dialogVisible = ref(false)
    const isEdit = ref(false)
    const formRef = ref(null)
    
    const formData = ref({
      id: null,
      username: '',
      name: '',
      email: '',
      role: 'USER',
      status: 1
    })
    
    const rules = {
      username: [
        { required: true, message: '请输入用户名', trigger: 'blur' }
      ],
      email: [
        { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
      ]
    }
    
    const avatarColors = ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399', '#C71585', '#20B2AA', '#FF8C00']
    
    const filteredUsers = computed(() => {
      if (!searchQuery.value) return users.value
      const query = searchQuery.value.toLowerCase()
      return users.value.filter(user => 
        (user.username && user.username.toLowerCase().includes(query)) ||
        (user.name && user.name.toLowerCase().includes(query)) ||
        (user.email && user.email.toLowerCase().includes(query))
      )
    })
    
    const getAvatarStyle = (row) => {
      const index = (row.username?.charCodeAt(0) || 0) % avatarColors.length
      return { backgroundColor: avatarColors[index], color: '#fff' }
    }
    
    const getRoleType = (role) => {
      const types = { 'ADMIN': 'danger', 'EDITOR': 'warning', 'USER': 'info' }
      return types[role] || 'info'
    }
    
    const getRoleText = (role) => {
      const texts = { 'ADMIN': '管理员', 'EDITOR': '编辑', 'USER': '用户' }
      return texts[role] || '用户'
    }
    
    const loadUsers = async () => {
      loading.value = true
      try {
        const res = await userApi.getAll()
        if (res.code === 200) {
          users.value = res.data || []
        }
      } catch (error) {
        console.error('获取用户列表失败:', error)
        ElMessage.error('获取用户列表失败')
      } finally {
        loading.value = false
      }
    }
    
    const handleSearch = () => {
      // 搜索通过计算属性实现
    }
    
    const openAddDialog = () => {
      isEdit.value = false
      formData.value = {
        id: null,
        username: '',
        name: '',
        email: '',
        role: 'USER',
        status: 1
      }
      dialogVisible.value = true
    }
    
    const editUser = (user) => {
      isEdit.value = true
      formData.value = { ...user }
      dialogVisible.value = true
    }
    
    const deleteUser = async (user) => {
      try {
        await ElMessageBox.confirm(
          `确定要删除用户 "${user.name || user.username}" 吗？此操作不可恢复。`,
          '删除确认',
          { confirmButtonText: '删除', cancelButtonText: '取消', type: 'warning' }
        )
        
        const res = await userApi.delete(user.id)
        if (res.code === 200) {
          ElMessage.success('删除成功')
          await loadUsers()
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除用户失败:', error)
          ElMessage.error('删除失败')
        }
      }
    }
    
    const saveUser = async () => {
      if (!formRef.value) return
      
      await formRef.value.validate(async (valid) => {
        if (!valid) return
        
        saving.value = true
        try {
          let res
          if (isEdit.value) {
            res = await userApi.update(formData.value.id, formData.value)
          } else {
            res = await userApi.create(formData.value)
          }
          
          if (res.code === 200) {
            ElMessage.success(isEdit.value ? '更新成功' : '创建成功')
            closeDialog()
            await loadUsers()
          }
        } catch (error) {
          console.error('保存用户失败:', error)
          ElMessage.error('保存失败')
        } finally {
          saving.value = false
        }
      })
    }
    
    const closeDialog = () => {
      dialogVisible.value = false
      formData.value = {
        id: null,
        username: '',
        name: '',
        email: '',
        role: 'USER',
        status: 1
      }
    }
    
    onMounted(loadUsers)
    
    return {
      loading,
      saving,
      users,
      searchQuery,
      filteredUsers,
      dialogVisible,
      isEdit,
      formRef,
      formData,
      rules,
      getAvatarStyle,
      getRoleType,
      getRoleText,
      handleSearch,
      openAddDialog,
      editUser,
      deleteUser,
      saveUser,
      closeDialog,
      User,
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

.user-table {
  border-radius: 12px;
  overflow: hidden;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-detail {
  display: flex;
  flex-direction: column;
}

.user-name {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
}

.user-username {
  font-size: 12px;
  color: #909399;
}

.user-form :deep(.el-form-item__label) {
  font-weight: 500;
}
</style>
