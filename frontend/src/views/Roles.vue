<template>
  <div class="page-content">
    <div class="page-header">
      <h2>🛡️ 角色权限</h2>
      <p>管理系统角色与权限配置</p>
    </div>

    <div class="roles-grid">
      <div class="role-card" v-for="role in roles" :key="role.id">
        <div class="role-header">
          <span class="role-icon">{{ role.icon }}</span>
          <h4>{{ role.name }}</h4>
        </div>
        <p class="role-desc">{{ role.desc }}</p>
        <div class="role-users">{{ role.count }} 人</div>
        <div class="role-actions">
          <button class="btn-edit">编辑</button>
          <button class="btn-view">查看</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Roles',
  setup() {
    const roles = [
      { id: 1, icon: '👑', name: '超级管理员', desc: '拥有系统所有权限', count: 2 },
      { id: 2, icon: '🛡️', name: '管理员', desc: '管理所有模块和用户', count: 5 },
      { id: 3, icon: '✏️', name: '编辑', desc: '内容编辑和发布权限', count: 15 },
      { id: 4, icon: '👁️', name: '访客', desc: '只读访问权限', count: 128 }
    ]
    return { roles }
  }
}
</script>

<style scoped>
.page-content { animation: fadeIn 0.3s ease; }
@keyframes fadeIn { from { opacity: 0; transform: translateY(10px); } to { opacity: 1; transform: translateY(0); } }

.page-header { margin-bottom: 24px; }
.page-header h2 { font-size: 24px; font-weight: 700; color: #0c4a6e; margin: 0 0 8px 0; }
.page-header p { font-size: 14px; color: #64748b; margin: 0; }

.roles-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 20px; }

.role-card {
  background: #fff; border-radius: 16px; padding: 24px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.05); transition: all 0.3s;
}
.role-card:hover { transform: translateY(-4px); box-shadow: 0 8px 25px rgba(0,0,0,0.1); }

.role-header { display: flex; align-items: center; gap: 12px; margin-bottom: 12px; }
.role-icon { font-size: 28px; }
.role-header h4 { font-size: 16px; font-weight: 600; color: #1e293b; margin: 0; }

.role-desc { font-size: 13px; color: #64748b; margin: 0 0 16px 0; }
.role-users { font-size: 14px; color: #0ea5e9; font-weight: 500; margin-bottom: 16px; }

.role-actions { display: flex; gap: 10px; }
.btn-edit, .btn-view { flex: 1; padding: 8px; border: none; border-radius: 8px; font-size: 13px; cursor: pointer; transition: all 0.2s; }
.btn-edit { background: #e0f2fe; color: #0284c7; }
.btn-edit:hover { background: #bae6fd; }
.btn-view { background: #f1f5f9; color: #475569; }
.btn-view:hover { background: #e2e8f0; }

@media (max-width: 1200px) { .roles-grid { grid-template-columns: repeat(2, 1fr); } }
@media (max-width: 640px) { .roles-grid { grid-template-columns: 1fr; } }
</style>
