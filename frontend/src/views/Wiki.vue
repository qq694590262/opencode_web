<template>
  <div class="page-content">
    <div class="page-header">
      <div class="header-title">
        <h2>📚 知识库</h2>
        <p>团队知识共享与学习</p>
      </div>
      <div class="header-actions">
        <div class="search-box">
          <span class="search-icon">🔍</span>
          <input 
            v-model="searchQuery" 
            placeholder="搜索文章..." 
            @input="handleSearch"
          />
        </div>
        <button class="btn-primary" @click="openAddModal">
          <span>✏️</span> 写文章
        </button>
      </div>
    </div>

    <!-- 分类标签 -->
    <div class="category-tabs">
      <div 
        class="tab-item" 
        :class="{ active: selectedCategory === null }"
        @click="selectCategory(null)"
      >
        <span class="tab-icon">📑</span>
        <span class="tab-label">全部</span>
        <span class="tab-count">{{ totalArticles }}</span>
      </div>
      <div 
        v-for="cat in categories" 
        :key="cat.id"
        class="tab-item"
        :class="{ active: selectedCategory === cat.id }"
        @click="selectCategory(cat.id)"
      >
        <span class="tab-icon">{{ cat.icon || '📄' }}</span>
        <span class="tab-label">{{ cat.name }}</span>
        <span class="tab-count">{{ cat.articleCount || 0 }}</span>
      </div>
    </div>

    <!-- 文章列表 -->
    <div class="articles-list" v-if="filteredArticles.length > 0">
      <div 
        class="article-card" 
        v-for="article in filteredArticles" 
        :key="article.id"
        @click="viewArticle(article)"
      >
        <div class="article-header">
          <div class="article-category">
            <span class="cat-icon">{{ getCategoryIcon(article.categoryId) }}</span>
            <span class="cat-name">{{ getCategoryName(article.categoryId) }}</span>
          </div>
          <div class="article-actions" @click.stop>
            <button class="btn-icon" @click="editArticle(article)">✏️</button>
            <button class="btn-icon delete" @click="deleteArticle(article)">🗑️</button>
          </div>
        </div>
        <h3 class="article-title">{{ article.title }}</h3>
        <p class="article-summary">{{ article.summary || article.content?.substring(0, 100) + '...' }}</p>
        <div class="article-footer">
          <div class="article-author">
            <span class="author-avatar">{{ article.author?.charAt(0) || 'U' }}</span>
            <span class="author-name">{{ article.author || '匿名' }}</span>
          </div>
          <div class="article-meta">
            <span class="meta-item">👁️ {{ article.viewCount || 0 }}</span>
            <span class="meta-item">❤️ {{ article.likeCount || 0 }}</span>
            <span class="meta-item">📅 {{ formatDate(article.createTime) }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 空状态 -->
    <div class="empty-state" v-else>
      <div class="empty-icon">📚</div>
      <h3>暂无文章</h3>
      <p>该分类下还没有文章，点击右上角写文章按钮创建第一篇吧！</p>
    </div>

    <!-- 添加/编辑文章弹窗 -->
    <div v-if="showModal" class="modal-overlay" @click.self="closeModal">
      <div class="modal-content">
        <div class="modal-header">
          <h3>{{ isEditing ? '编辑文章' : '新建文章' }}</h3>
          <button class="btn-close" @click="closeModal">✕</button>
        </div>
        <form @submit.prevent="saveArticle">
          <div class="form-group">
            <label>文章标题 *</label>
            <input v-model="formData.title" required placeholder="请输入文章标题" />
          </div>
          <div class="form-row">
            <div class="form-group">
              <label>所属分类 *</label>
              <select v-model="formData.categoryId" required>
                <option v-for="cat in categories" :key="cat.id" :value="cat.id">
                  {{ cat.name }}
                </option>
              </select>
            </div>
            <div class="form-group">
              <label>作者</label>
              <input v-model="formData.author" placeholder="请输入作者" />
            </div>
          </div>
          <div class="form-group">
            <label>文章摘要</label>
            <input v-model="formData.summary" placeholder="简要描述文章内容..." />
          </div>
          <div class="form-group">
            <label>文章内容 *</label>
            <textarea 
              v-model="formData.content" 
              required 
              rows="10"
              placeholder="请输入文章内容，支持Markdown格式..."
            ></textarea>
          </div>
          <div class="modal-actions">
            <button type="button" class="btn-cancel" @click="closeModal">取消</button>
            <button type="submit" class="btn-confirm">{{ isEditing ? '保存' : '发布' }}</button>
          </div>
        </form>
      </div>
    </div>

    <!-- 查看文章弹窗 -->
    <div v-if="showViewModal" class="modal-overlay" @click.self="closeViewModal">
      <div class="modal-content view-mode">
        <div class="modal-header">
          <div class="view-header-info">
            <span class="view-category">{{ getCategoryIcon(currentArticle.categoryId) }} {{ getCategoryName(currentArticle.categoryId) }}</span>
            <h3>{{ currentArticle.title }}</h3>
          </div>
          <button class="btn-close" @click="closeViewModal">✕</button>
        </div>
        <div class="view-content">
          <div class="view-meta">
            <span class="view-author">
              <span class="author-avatar">{{ currentArticle.author?.charAt(0) || 'U' }}</span>
              {{ currentArticle.author || '匿名' }}
            </span>
            <span class="view-date">📅 {{ formatDate(currentArticle.createTime) }}</span>
            <span class="view-views">👁️ {{ currentArticle.viewCount || 0 }} 阅读</span>
          </div>
          <div class="article-body">
            {{ currentArticle.content }}
          </div>
        </div>
        <div class="modal-actions">
          <button class="btn-cancel" @click="closeViewModal">关闭</button>
          <button class="btn-confirm" @click="editFromView">编辑</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { wikiApi } from '../api'
import { showToast } from '../components/Toast.vue'
import { showConfirm } from '../components/ConfirmDialog.vue'

export default {
  name: 'Wiki',
  setup() {
    const categories = ref([])
    const articles = ref([])
    const searchQuery = ref('')
    const selectedCategory = ref(null)
    const showModal = ref(false)
    const showViewModal = ref(false)
    const isEditing = ref(false)
    const currentArticle = ref({})
    const formData = ref({
      title: '',
      categoryId: null,
      author: '',
      summary: '',
      content: ''
    })

    const totalArticles = computed(() => articles.value.length)

    const filteredArticles = computed(() => {
      let result = articles.value
      
      // 按分类筛选
      if (selectedCategory.value !== null) {
        result = result.filter(a => a.categoryId === selectedCategory.value)
      }
      
      // 按搜索词筛选
      if (searchQuery.value) {
        const query = searchQuery.value.toLowerCase()
        result = result.filter(a => 
          a.title?.toLowerCase().includes(query) ||
          a.content?.toLowerCase().includes(query) ||
          a.summary?.toLowerCase().includes(query)
        )
      }
      
      return result.sort((a, b) => new Date(b.createTime) - new Date(a.createTime))
    })

    const loadCategories = async () => {
      try {
        const res = await wikiApi.getCategories()
        if (res.code === 200) {
          categories.value = res.data || []
        }
      } catch (error) {
        console.error('获取分类失败:', error)
        showToast({ type: 'error', message: '获取分类失败' })
      }
    }

    const loadArticles = async () => {
      try {
        const res = await wikiApi.getArticles()
        if (res.code === 200) {
          articles.value = res.data || []
        }
      } catch (error) {
        console.error('获取文章失败:', error)
        showToast({ type: 'error', message: '获取文章失败' })
      }
    }

    const getCategoryName = (categoryId) => {
      const cat = categories.value.find(c => c.id === categoryId)
      return cat?.name || '未分类'
    }

    const getCategoryIcon = (categoryId) => {
      const cat = categories.value.find(c => c.id === categoryId)
      return cat?.icon || '📄'
    }

    const formatDate = (dateStr) => {
      if (!dateStr) return ''
      const date = new Date(dateStr)
      return date.toLocaleDateString('zh-CN')
    }

    const selectCategory = (id) => {
      selectedCategory.value = id
    }

    const handleSearch = () => {
      // 搜索通过computed自动处理
    }

    const openAddModal = () => {
      isEditing.value = false
      formData.value = {
        title: '',
        categoryId: categories.value[0]?.id || null,
        author: '',
        summary: '',
        content: ''
      }
      showModal.value = true
    }

    const editArticle = (article) => {
      isEditing.value = true
      currentArticle.value = article
      formData.value = { ...article }
      showModal.value = true
    }

    const viewArticle = (article) => {
      currentArticle.value = article
      showViewModal.value = true
      // 增加浏览量
      article.viewCount = (article.viewCount || 0) + 1
    }

    const closeViewModal = () => {
      showViewModal.value = false
      currentArticle.value = {}
    }

    const editFromView = () => {
      closeViewModal()
      editArticle(currentArticle.value)
    }

    const saveArticle = async () => {
      try {
        let res
        if (isEditing.value) {
          res = await wikiApi.updateArticle(currentArticle.value.id, formData.value)
        } else {
          res = await wikiApi.createArticle(formData.value)
        }
        
        if (res.code === 200) {
          showToast({ 
            type: 'success', 
            message: isEditing.value ? '文章更新成功' : '文章发布成功' 
          })
          closeModal()
          await loadArticles()
          await loadCategories() // 刷新分类文章数
        }
      } catch (error) {
        console.error('保存文章失败:', error)
        showToast({ type: 'error', message: '保存失败' })
      }
    }

    const deleteArticle = async (article) => {
      const confirmed = await showConfirm({
        title: '删除确认',
        message: `确定要删除文章 "${article.title}" 吗？此操作不可恢复。`,
        type: 'danger',
        confirmText: '删除',
        cancelText: '取消'
      })
      
      if (!confirmed) return
      
      try {
        const res = await wikiApi.deleteArticle(article.id)
        if (res.code === 200) {
          showToast({ type: 'success', message: '删除成功' })
          await loadArticles()
          await loadCategories()
        }
      } catch (error) {
        console.error('删除文章失败:', error)
        showToast({ type: 'error', message: '删除失败' })
      }
    }

    const closeModal = () => {
      showModal.value = false
      formData.value = {
        title: '',
        categoryId: null,
        author: '',
        summary: '',
        content: ''
      }
      currentArticle.value = {}
    }

    onMounted(() => {
      loadCategories()
      loadArticles()
    })

    return {
      categories,
      articles,
      searchQuery,
      selectedCategory,
      showModal,
      showViewModal,
      isEditing,
      currentArticle,
      formData,
      totalArticles,
      filteredArticles,
      getCategoryName,
      getCategoryIcon,
      formatDate,
      selectCategory,
      handleSearch,
      openAddModal,
      editArticle,
      viewArticle,
      closeViewModal,
      editFromView,
      saveArticle,
      deleteArticle,
      closeModal
    }
  }
}
</script>

<style scoped>
.page-content { 
  animation: fadeIn 0.3s ease; 
}

@keyframes fadeIn { 
  from { opacity: 0; transform: translateY(10px); } 
  to { opacity: 1; transform: translateY(0); } 
}

.page-header { 
  margin-bottom: 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-title h2 { 
  font-size: 24px; 
  font-weight: 700; 
  color: #0c4a6e; 
  margin: 0 0 8px 0; 
}

.header-title p { 
  font-size: 14px; 
  color: #64748b; 
  margin: 0; 
}

.header-actions {
  display: flex;
  gap: 16px;
  align-items: center;
}

.search-box {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 16px;
  background: #f1f5f9;
  border-radius: 10px;
  border: 2px solid transparent;
  transition: all 0.3s;
}

.search-box:focus-within {
  background: #fff;
  border-color: #0ea5e9;
}

.search-box input {
  border: none;
  background: transparent;
  outline: none;
  font-size: 14px;
  width: 200px;
  color: #334155;
}

.btn-primary {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  background: linear-gradient(135deg, #0ea5e9, #0284c7);
  color: white;
  border: none;
  border-radius: 10px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(14, 165, 233, 0.4);
}

/* 分类标签 */
.category-tabs {
  display: flex;
  gap: 12px;
  margin-bottom: 24px;
  flex-wrap: wrap;
}

.tab-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 16px;
  background: #fff;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.3s;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
  border: 2px solid transparent;
}

.tab-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.tab-item.active {
  background: linear-gradient(135deg, #0ea5e9, #0284c7);
  color: white;
  border-color: #0ea5e9;
}

.tab-icon {
  font-size: 18px;
}

.tab-label {
  font-size: 14px;
  font-weight: 500;
}

.tab-count {
  font-size: 12px;
  padding: 2px 8px;
  background: rgba(0,0,0,0.1);
  border-radius: 10px;
}

.tab-item.active .tab-count {
  background: rgba(255,255,255,0.2);
}

/* 文章列表 */
.articles-list {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.article-card {
  background: #fff;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.05);
  transition: all 0.3s;
  cursor: pointer;
}

.article-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 25px rgba(0,0,0,0.1);
}

.article-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.article-category {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 12px;
  background: #f0f9ff;
  border-radius: 20px;
}

.cat-icon {
  font-size: 14px;
}

.cat-name {
  font-size: 12px;
  color: #0284c7;
  font-weight: 500;
}

.article-actions {
  display: flex;
  gap: 8px;
  opacity: 0;
  transition: opacity 0.3s;
}

.article-card:hover .article-actions {
  opacity: 1;
}

.btn-icon {
  padding: 6px 10px;
  background: #f1f5f9;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 12px;
  transition: all 0.2s;
}

.btn-icon:hover {
  background: #e2e8f0;
}

.btn-icon.delete:hover {
  background: #fee2e2;
}

.article-title {
  font-size: 18px;
  font-weight: 600;
  color: #1e293b;
  margin: 0 0 12px 0;
  line-height: 1.4;
}

.article-summary {
  font-size: 14px;
  color: #64748b;
  line-height: 1.6;
  margin: 0 0 16px 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.article-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 16px;
  border-top: 1px solid #f1f5f9;
}

.article-author {
  display: flex;
  align-items: center;
  gap: 8px;
}

.author-avatar {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  background: linear-gradient(135deg, #38bdf8, #0ea5e9);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: 600;
  color: #fff;
}

.author-name {
  font-size: 13px;
  color: #475569;
}

.article-meta {
  display: flex;
  gap: 16px;
}

.meta-item {
  font-size: 12px;
  color: #94a3b8;
}

/* 空状态 */
.empty-state {
  text-align: center;
  padding: 80px 20px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.05);
}

.empty-icon {
  font-size: 64px;
  margin-bottom: 20px;
}

.empty-state h3 {
  font-size: 20px;
  color: #1e293b;
  margin: 0 0 12px 0;
}

.empty-state p {
  font-size: 14px;
  color: #64748b;
  margin: 0;
}

/* 弹窗样式 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.5);
  backdrop-filter: blur(4px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  padding: 20px;
}

.modal-content {
  background: #fff;
  border-radius: 16px;
  width: 100%;
  max-width: 700px;
  max-height: 90vh;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  animation: modalSlideIn 0.3s ease;
}

.modal-content.view-mode {
  max-width: 800px;
}

@keyframes modalSlideIn {
  from {
    opacity: 0;
    transform: translateY(-20px) scale(0.95);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  border-bottom: 1px solid #f1f5f9;
}

.modal-header h3 {
  margin: 0;
  font-size: 18px;
  color: #1e293b;
}

.view-header-info {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.view-category {
  font-size: 13px;
  color: #0284c7;
  background: #f0f9ff;
  padding: 4px 12px;
  border-radius: 20px;
  display: inline-block;
  width: fit-content;
}

.view-header-info h3 {
  font-size: 22px;
  margin: 0;
}

.btn-close {
  width: 32px;
  height: 32px;
  border: none;
  background: #f1f5f9;
  border-radius: 8px;
  cursor: pointer;
  font-size: 14px;
  color: #64748b;
  transition: all 0.2s;
}

.btn-close:hover {
  background: #e2e8f0;
  color: #475569;
}

.modal-content form {
  padding: 24px;
  overflow-y: auto;
}

.form-group {
  margin-bottom: 20px;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.form-group label {
  display: block;
  font-size: 13px;
  font-weight: 500;
  color: #64748b;
  margin-bottom: 8px;
}

.form-group input,
.form-group select,
.form-group textarea {
  width: 100%;
  padding: 12px 16px;
  border: 2px solid #e2e8f0;
  border-radius: 10px;
  font-size: 14px;
  color: #334155;
  outline: none;
  transition: all 0.3s;
  box-sizing: border-box;
}

.form-group input:focus,
.form-group select:focus,
.form-group textarea:focus {
  border-color: #0ea5e9;
  background: #f8fafc;
}

.form-group textarea {
  resize: vertical;
  font-family: inherit;
  line-height: 1.6;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding: 20px 24px;
  border-top: 1px solid #f1f5f9;
  background: #fafafa;
}

.btn-cancel {
  padding: 10px 20px;
  border: none;
  background: #f1f5f9;
  color: #64748b;
  border-radius: 8px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-cancel:hover {
  background: #e2e8f0;
}

.btn-confirm {
  padding: 10px 24px;
  border: none;
  background: linear-gradient(135deg, #0ea5e9, #0284c7);
  color: white;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-confirm:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(14, 165, 233, 0.4);
}

/* 查看内容 */
.view-content {
  padding: 24px;
  overflow-y: auto;
  max-height: 60vh;
}

.view-meta {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 24px;
  padding-bottom: 20px;
  border-bottom: 1px solid #f1f5f9;
}

.view-author {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: #475569;
}

.view-date,
.view-views {
  font-size: 13px;
  color: #94a3b8;
}

.article-body {
  font-size: 15px;
  line-height: 1.8;
  color: #334155;
  white-space: pre-wrap;
}

@media (max-width: 1024px) {
  .articles-list {
    grid-template-columns: 1fr;
  }
  
  .form-row {
    grid-template-columns: 1fr;
  }
}
</style>