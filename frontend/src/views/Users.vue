<template>
  <div class="page-content">
    <div class="page-header">
      <h2>👥 用户列表</h2>
      <p>管理系统用户账号</p>
    </div>

    <div class="toolbar">
      <input type="text" v-model="searchQuery" placeholder="搜索用户..." class="search-input" @input="handleSearch" />
      <button class="btn-primary" @click="showAddModal = true">+ 新增用户</button>
    </div>

    <div class="table-card">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>用户名</th>
            <th>邮箱</th>
            <th>角色</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in filteredUsers" :key="user.id">
            <td>{{ user.id }}</td>
            <td>
              <div class="user-cell">
                <span class="avatar">👤</span>
                {{ user.name || user.username }}
              </div>
            </td>
            <td>{{ user.email }}</td>
            <td><span class="role-tag">{{ user.role || '用户' }}</span></td>
            <td>
              <span class="status-tag" :class="user.status === 1 ? 'active' : 'inactive'">
                {{ user.status === 1 ? '正常' : '禁用' }}
              </span>
            </td>
            <td>
              <button class="action-btn" @click="editUser(user)">编辑</button>
              <button class="action-btn danger" @click="deleteUser(user)">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 添加/编辑用户弹窗 -->
    <div v-if="showAddModal || showEditModal" class="modal-overlay" @click.self="closeModal">
      <div class="modal">
        <h3>{{ showEditModal ? '编辑用户' : '新增用户' }}</h3>
        <form @submit.prevent="saveUser">
          <div class="form-group">
            <label>用户名</label>
            <input v-model="formData.username" required />
          </div>
          <div class="form-group">
            <label>姓名</label>
            <input v-model="formData.name" />
          </div>
          <div class="form-group">
            <label>邮箱</label>
            <input v-model="formData.email" type="email" />
          </div>
          <div class="form-group">
            <label>角色</label>
            <select v-model="formData.role">
              <option value="USER">用户</option>
              <option value="ADMIN">管理员</option>
              <option value="EDITOR">编辑</option>
            </select>
          </div>
          <div class="form-group">
            <label>状态</label>
            <select v-model="formData.status">
              <option :value="1">正常</option>
              <option :value="0">禁用</option>
            </select>
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
import { userApi } from '../api'
import { showToast } from '../components/Toast.vue'
import { showConfirm } from '../components/ConfirmDialog.vue'

export default {
  name: 'Users',
  setup() {
    const users = ref([])
    const searchQuery = ref('')
    const showAddModal = ref(false)
    const showEditModal = ref(false)
    const formData = ref({
      username: '',
      name: '',
      email: '',
      role: 'USER',
      status: 1
    })

    const filteredUsers = computed(() => {
      if (!searchQuery.value) return users.value
      const query = searchQuery.value.toLowerCase()
      return users.value.filter(user => 
        (user.username && user.username.toLowerCase().includes(query)) ||
        (user.name && user.name.toLowerCase().includes(query)) ||
        (user.email && user.email.toLowerCase().includes(query))
      )
    })

    const loadUsers = async () => {
      try {
        const res = await userApi.getAll()
        if (res.code === 200) {
          users.value = res.data || []
        }
      } catch (error) {
        console.error('获取用户列表失败:', error)
        showToast({ type: 'error', message: '获取用户列表失败: ' + (error.message || '未知错误') })
      }
    }

    const handleSearch = () => {
      // 搜索已通过计算属性实现
    }

    const editUser = (user) => {
      formData.value = { ...user }
      showEditModal.value = true
    }

    const deleteUser = async (user) => {
      const confirmed = await showConfirm({
        title: '删除确认',
        message: `确定要删除用户 "${user.name || user.username}" 吗？此操作不可恢复。`,
        type: 'danger',
        confirmText: '删除',
        cancelText: '取消'
      })
      
      if (!confirmed) return
      
      try {
        const res = await userApi.delete(user.id)
        if (res.code === 200) {
          showToast({ type: 'success', message: '删除成功' })
          await loadUsers()
        }
      } catch (error) {
        console.error('删除用户失败:', error)
        showToast({ type: 'error', message: '删除失败: ' + (error.message || '未知错误') })
      }
    }

    const saveUser = async () => {
      try {
        let res
        if (showEditModal.value) {
          res = await userApi.update(formData.value.id, formData.value)
        } else {
          res = await userApi.create(formData.value)
        }
        if (res.code === 200) {
          showToast({ type: 'success', message: showEditModal.value ? '用户更新成功' : '用户创建成功' })
          closeModal()
          await loadUsers()
        }
      } catch (error) {
        console.error('保存用户失败:', error)
        showToast({ type: 'error', message: '保存失败: ' + (error.message || '未知错误') })
      }
    }

    const closeModal = () => {
      showAddModal.value = false
      showEditModal.value = false
      formData.value = {
        username: '',
        name: '',
        email: '',
        role: 'USER',
        status: 1
      }
    }

    onMounted(loadUsers)

    return {
      users,
      filteredUsers,
      searchQuery,
      showAddModal,
      showEditModal,
      formData,
      handleSearch,
      editUser,
      deleteUser,
      saveUser,
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
.search-input { flex: 1; padding: 12px 16px; border: 2px solid #e2e8f0; border-radius: 10px; font-size: 14px; outline: none; transition: border-color 0.3s; }
.search-input:focus { border-color: #0ea5e9; }

.btn-primary { padding: 12px 24px; border: none; border-radius: 10px; background: linear-gradient(135deg, #0ea5e9, #0284c7); color: #fff; font-size: 14px; font-weight: 500; cursor: pointer; transition: all 0.3s; }
.btn-primary:hover { transform: translateY(-2px); box-shadow: 0 4px 15px rgba(14, 165, 233, 0.4); }

.table-card { background: #fff; border-radius: 16px; padding: 20px; box-shadow: 0 4px 15px rgba(0,0,0,0.05); overflow-x: auto; }

.data-table { width: 100%; border-collapse: collapse; }
.data-table th { text-align: left; padding: 14px 16px; font-size: 13px; font-weight: 600; color: #64748b; border-bottom: 2px solid #f1f5f9; }
.data-table td { padding: 16px; font-size: 14px; color: #1e293b; border-bottom: 1px solid #f1f5f9; }
.data-table tr:hover { background: #f8fafc; }

.user-cell { display: flex; align-items: center; gap: 10px; }
.avatar { width: 36px; height: 36px; border-radius: 10px; background: #e0f2fe; display: flex; align-items: center; justify-content: center; }

.role-tag { padding: 4px 12px; border-radius: 20px; background: #e0f2fe; color: #0284c7; font-size: 12px; font-weight: 500; }
.status-tag { padding: 4px 12px; border-radius: 20px; font-size: 12px; font-weight: 500; }
.status-tag.active { background: #dcfce7; color: #16a34a; }
.status-tag.inactive { background: #fee2e2; color: #dc2626; }

.action-btn { padding: 6px 12px; margin-right: 8px; border: none; border-radius: 6px; background: #f1f5f9; color: #475569; font-size: 12px; cursor: pointer; transition: all 0.2s; }
.action-btn:hover { background: #e0f2fe; color: #0ea5e9; }
.action-btn.danger:hover { background: #fee2e2; color: #dc2626; }

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
</style>
