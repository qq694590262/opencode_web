<template>
  <div class="page-content">
    <div class="page-header">
      <h2>📄 文档列表</h2>
      <p>管理和共享文档</p>
    </div>

    <div class="toolbar">
      <input type="text" placeholder="搜索文档..." class="search-input" />
      <button class="btn-primary">+ 上传文档</button>
    </div>

    <div class="docs-grid">
      <div class="doc-card" v-for="doc in docs" :key="doc.id">
        <div class="doc-icon">{{ doc.icon }}</div>
        <div class="doc-info">
          <h4>{{ doc.name }}</h4>
          <p>{{ doc.desc }}</p>
        </div>
        <div class="doc-meta">
          <span>{{ doc.size }}</span>
          <span>{{ doc.date }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Documents',
  setup() {
    const docs = [
      { id: 1, icon: '📕', name: '项目需求文档.docx', desc: '企业管理系统需求说明', size: '2.3 MB', date: '2024-03-15' },
      { id: 2, icon: '📗', name: '技术方案.pdf', desc: '系统架构设计文档', size: '5.1 MB', date: '2024-03-12' },
      { id: 3, icon: '📘', name: 'API接口文档.md', desc: '接口定义与说明', size: '1.2 MB', date: '2024-03-10' },
      { id: 4, icon: '📙', name: '测试用例.xlsx', desc: '功能测试用例', size: '856 KB', date: '2024-03-08' },
      { id: 5, icon: '📓', name: '用户手册.pdf', desc: '操作手册', size: '3.4 MB', date: '2024-03-05' },
      { id: 6, icon: '📒', name: '部署文档.md', desc: '部署与运维指南', size: '980 KB', date: '2024-03-01' }
    ]
    return { docs }
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
.search-input { flex: 1; padding: 12px 16px; border: 2px solid #e2e8f0; border-radius: 10px; font-size: 14px; outline: none; }
.search-input:focus { border-color: #0ea5e9; }

.btn-primary { padding: 12px 24px; border: none; border-radius: 10px; background: linear-gradient(135deg, #0ea5e9, #0284c7); color: #fff; font-size: 14px; font-weight: 500; cursor: pointer; }

.docs-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 20px; }

.doc-card {
  background: #fff; border-radius: 16px; padding: 20px;
  display: flex; flex-direction: column; box-shadow: 0 4px 15px rgba(0,0,0,0.05); transition: all 0.3s;
}
.doc-card:hover { transform: translateY(-4px); box-shadow: 0 8px 25px rgba(0,0,0,0.1); }

.doc-icon { font-size: 40px; margin-bottom: 12px; }
.doc-info h4 { font-size: 15px; font-weight: 600; color: #1e293b; margin: 0 0 6px 0; }
.doc-info p { font-size: 13px; color: #64748b; margin: 0 0 12px 0; }
.doc-meta { display: flex; justify-content: space-between; font-size: 12px; color: #94a3b8; margin-top: auto; }

@media (max-width: 1024px) { .docs-grid { grid-template-columns: repeat(2, 1fr); } }
@media (max-width: 640px) { .docs-grid { grid-template-columns: 1fr; } }
</style>
