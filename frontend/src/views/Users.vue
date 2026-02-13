<template>
  <div class="page-content">
    <div class="page-header">
      <h2>👥 用户列表</h2>
      <p>管理系统用户账号</p>
    </div>

    <div class="toolbar">
      <input type="text" placeholder="搜索用户..." class="search-input" />
      <button class="btn-primary">+ 新增用户</button>
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
          <tr v-for="user in users" :key="user.id">
            <td>{{ user.id }}</td>
            <td>
              <div class="user-cell">
                <span class="avatar">{{ user.avatar }}</span>
                {{ user.name }}
              </div>
            </td>
            <td>{{ user.email }}</td>
            <td><span class="role-tag">{{ user.role }}</span></td>
            <td><span class="status-tag" :class="user.status">{{ user.statusText }}</span></td>
            <td>
              <button class="action-btn">编辑</button>
              <button class="action-btn danger">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Users',
  setup() {
    const users = [
      { id: 1, avatar: '👤', name: '张三', email: 'zhangsan@company.com', role: '管理员', status: 'active', statusText: '正常' },
      { id: 2, avatar: '👤', name: '李四', email: 'lisi@company.com', role: '编辑', status: 'active', statusText: '正常' },
      { id: 3, avatar: '👤', name: '王五', email: 'wangwu@company.com', role: '用户', status: 'inactive', statusText: '禁用' },
      { id: 4, avatar: '👤', name: '赵六', email: 'zhaoliu@company.com', role: '用户', status: 'active', statusText: '正常' },
      { id: 5, avatar: '👤', name: '钱七', email: 'qianqi@company.com', role: '编辑', status: 'active', statusText: '正常' }
    ]
    return { users }
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
</style>
