<template>
  <div class="page-content">
    <div class="page-header">
      <h2>📚 知识库</h2>
      <p>团队知识共享与学习</p>
    </div>

    <div class="wiki-categories">
      <div class="category-card" v-for="cat in categories" :key="cat.id" @click="editCategory(cat)">
        <div class="cat-icon">{{ cat.icon || '📄' }}</div>
        <h4>{{ cat.name }}</h4>
        <p>{{ cat.description }}</p>
        <span class="cat-count">{{ cat.articleCount || 0 }} 篇文章</span>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { wikiApi } from '../api'

export default {
  name: 'Wiki',
  setup() {
    const categories = ref([])

    const loadCategories = async () => {
      try {
        const res = await wikiApi.getCategories()
        if (res.code === 200) {
          categories.value = res.data || []
        }
      } catch (error) {
        console.error('获取知识库分类失败:', error)
      }
    }

    const editCategory = (cat) => {
      console.log('编辑分类:', cat)
      // 可以打开编辑弹窗或跳转到详情页
    }

    onMounted(loadCategories)

    return {
      categories,
      editCategory
    }
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
  cursor: pointer;
}
.category-card:hover { transform: translateY(-4px); box-shadow: 0 8px 25px rgba(0,0,0,0.1); }

.cat-icon { font-size: 40px; margin-bottom: 12px; }
.category-card h4 { font-size: 16px; font-weight: 600; color: #1e293b; margin: 0 0 8px 0; }
.category-card p { font-size: 13px; color: #64748b; margin: 0 0 12px 0; }
.cat-count { font-size: 12px; color: #0ea5e9; font-weight: 500; }

@media (max-width: 1024px) { .wiki-categories { grid-template-columns: repeat(2, 1fr); } }
@media (max-width: 640px) { .wiki-categories { grid-template-columns: 1fr; } }
</style>
