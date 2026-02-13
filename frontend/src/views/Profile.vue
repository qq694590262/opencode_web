<template>
  <div class="page-content">
    <div class="page-header">
      <h2>👤 个人信息</h2>
      <p>查看和编辑个人资料</p>
    </div>

    <div class="profile-card">
      <div class="profile-header">
        <div class="avatar-large">A</div>
        <div class="profile-info">
          <h3>Admin</h3>
          <p>管理员</p>
        </div>
        <button class="btn-edit">编辑资料</button>
      </div>

      <div class="profile-form">
        <div class="form-row">
          <div class="form-group">
            <label>用户名</label>
            <input type="text" value="admin" />
          </div>
          <div class="form-group">
            <label>邮箱</label>
            <input type="email" value="admin@company.com" />
          </div>
        </div>
        <div class="form-row">
          <div class="form-group">
            <label>手机号</label>
            <input type="tel" value="138****8888" />
          </div>
          <div class="form-group">
            <label>部门</label>
            <input type="text" value="技术部" />
          </div>
        </div>
        <div class="form-group full">
          <label>个人简介</label>
          <textarea>热爱技术，关注用户体验</textarea>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Profile'
}
</script>

<style scoped>
.page-content { animation: fadeIn 0.3s ease; }
@keyframes fadeIn { from { opacity: 0; transform: translateY(10px); } to { opacity: 1; transform: translateY(0); } }

.page-header { margin-bottom: 24px; }
.page-header h2 { font-size: 24px; font-weight: 700; color: #0c4a6e; margin: 0 0 8px 0; }
.page-header p { font-size: 14px; color: #64748b; margin: 0; }

.profile-card { background: #fff; border-radius: 16px; padding: 32px; box-shadow: 0 4px 15px rgba(0,0,0,0.05); }

.profile-header { display: flex; align-items: center; gap: 20px; margin-bottom: 32px; padding-bottom: 24px; border-bottom: 1px solid #f1f5f9; }
.avatar-large { width: 80px; height: 80px; border-radius: 20px; background: linear-gradient(135deg, #38bdf8, #0ea5e9); display: flex; align-items: center; justify-content: center; font-size: 32px; font-weight: 600; color: #fff; }
.profile-info { flex: 1; }
.profile-info h3 { font-size: 22px; font-weight: 600; color: #1e293b; margin: 0 0 4px 0; }
.profile-info p { font-size: 14px; color: #64748b; margin: 0; }

.btn-edit { padding: 10px 20px; border: none; border-radius: 10px; background: linear-gradient(135deg, #0ea5e9, #0284c7); color: #fff; font-size: 14px; font-weight: 500; cursor: pointer; }

.form-row { display: flex; gap: 20px; margin-bottom: 20px; }
.form-group { flex: 1; }
.form-group.full { width: 100%; }

.form-group label { display: block; font-size: 13px; font-weight: 500; color: #475569; margin-bottom: 8px; }
.form-group input, .form-group textarea { width: 100%; padding: 12px 16px; border: 2px solid #e2e8f0; border-radius: 10px; font-size: 14px; outline: none; transition: border-color 0.3s; }
.form-group input:focus, .form-group textarea:focus { border-color: #0ea5e9; }
.form-group textarea { min-height: 100px; resize: vertical; }
</style>
