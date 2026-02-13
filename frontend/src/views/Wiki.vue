<template>
  <div class="page-content">
    <div class="page-header">
      <h2>📚 知识库</h2>
      <p>团队知识共享与学习</p>
    </div>

    <div class="wiki-categories">
      <div class="category-card" v-for="cat in categories" :key="cat.id">
        <div class="cat-icon">{{ cat.icon }}</div>
        <h4>{{ cat.name }}</h4>
        <p>{{ cat.desc }}</p>
        <span class="cat-count">{{ cat.articles }} 篇文章</span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Wiki',
  setup() {
    const categories = [
      { id: 1, icon: '🚀', name: '新手入门', desc: '快速上手指南', articles: 12 },
      { id: 2, icon: '💻', name: '技术文档', desc: '开发技术文档', articles: 28 },
      { id: 3, icon: '📖', name: '产品知识', desc: '产品功能介绍', articles: 15 },
      { id: 4, icon: '🔧', name: '运维指南', desc: '系统运维手册', articles: 8 },
      { id: 5, icon: '📋', name: '常见问题', desc: 'FAQ 汇总', articles: 20 },
      { id: 6, icon: '💡', name: '最佳实践', desc: '经验与技巧', articles: 16 }
    ]
    return { categories }
  }
}
</script>

<style scoped>
.page-content { animation: fadeIn 0.3s ease; }
@keyframes fadeIn { from { opacity: 0; transform: translateY(10px); } to { opacity: 1; transform: translateY(0); } }

.page-header { margin-bottom: 24px; }
.page-header h2 { font-size: 24px; font-weight: 700; color: #0c4a6e; margin: 0 0 8px 0; }
.page-header p { font-size: 14px; color: #64748b; margin: 0; }

.wiki-categories { display: grid; grid-template-columns: repeat(3, 1fr); gap: 20px; }

.category-card {
  background: #fff; border-radius: 16px; padding: 24px;
  text-align: center; box-shadow: 0 4px 15px rgba(0,0,0,0.05); transition: all 0.3s;
}
.category-card:hover { transform: translateY(-4px); box-shadow: 0 8px 25px rgba(0,0,0,0.1); }

.cat-icon { font-size: 40px; margin-bottom: 12px; }
.category-card h4 { font-size: 16px; font-weight: 600; color: #1e293b; margin: 0 0 8px 0; }
.category-card p { font-size: 13px; color: #64748b; margin: 0 0 12px 0; }
.cat-count { font-size: 12px; color: #0ea5e9; font-weight: 500; }

@media (max-width: 1024px) { .wiki-categories { grid-template-columns: repeat(2, 1fr); } }
@media (max-width: 640px) { .wiki-categories { grid-template-columns: 1fr; } }
</style>
