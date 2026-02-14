<template>
  <div class="dashboard">
    <aside class="sidebar">
      <div class="brand">
        <span class="brand-icon">💙</span>
        <span>Opencode</span>
      </div>
      
      <!-- 用户信息卡片 -->
      <div class="user-card">
        <div class="user-avatar">
          {{ userInfo.name ? userInfo.name.charAt(0).toUpperCase() : 'U' }}
        </div>
        <div class="user-details">
          <span class="user-name">{{ userInfo.name || userInfo.username || '用户' }}</span>
          <span class="user-role">{{ getRoleText(userInfo.role) }}</span>
        </div>
        <div class="user-status" :class="{ online: true }">
          <span class="status-dot"></span>
          <span class="status-text">在线</span>
        </div>
      </div>
      
      <SideMenu :menu="MENU" />
      
      <div class="sidebar-footer">
        <div class="quick-stats">
          <div class="stat-item">
            <span class="stat-value">{{ stats.projects }}</span>
            <span class="stat-label">项目</span>
          </div>
          <div class="stat-item">
            <span class="stat-value">{{ stats.tasks }}</span>
            <span class="stat-label">任务</span>
          </div>
          <div class="stat-item">
            <span class="stat-value">{{ stats.messages }}</span>
            <span class="stat-label">消息</span>
          </div>
        </div>
        <button class="logout-btn" @click="handleLogout">
          <span class="logout-icon">🚪</span>
          <span>退出登录</span>
        </button>
      </div>
    </aside>
    
    <section class="main-content">
      <header class="topbar">
        <div class="breadcrumb">
          <span class="home-icon">🏠</span>
          <span class="home" @click="goHome">首页</span>
          <span class="sep">/</span>
          <span class="current">{{ currentPageTitle }}</span>
        </div>
        
        <div class="topbar-right">
          <!-- 搜索框 -->
          <div class="search-wrapper" ref="searchWrapper">
            <div class="search-box" :class="{ active: searchFocused }">
              <span class="search-icon">🔍</span>
              <input 
                type="text" 
                v-model="searchQuery"
                placeholder="搜索菜单、功能..."
                @focus="searchFocused = true"
                @blur="handleSearchBlur"
                @keyup.enter="handleSearch"
                @keyup.down="moveSearchSelection(1)"
                @keyup.up="moveSearchSelection(-1)"
              />
              <span v-if="searchQuery" class="clear-icon" @click="clearSearch">✕</span>
            </div>
            
            <!-- 搜索结果下拉框 -->
            <transition name="search-dropdown">
              <div v-if="searchFocused && filteredSearchResults.length > 0" class="search-dropdown">
                <div class="search-results">
                  <div 
                    v-for="(item, index) in filteredSearchResults" 
                    :key="item.route"
                    class="search-result-item"
                    :class="{ active: index === selectedSearchIndex }"
                    @click="navigateTo(item.route)"
                    @mouseenter="selectedSearchIndex = index"
                  >
                    <span class="result-icon">{{ item.icon }}</span>
                    <div class="result-info">
                      <span class="result-title">{{ item.label }}</span>
                      <span class="result-path">{{ item.path }}</span>
                    </div>
                    <span class="result-category">{{ item.category }}</span>
                  </div>
                </div>
              </div>
            </transition>
          </div>
          
          <!-- 顶部图标 -->
          <div class="topbar-actions">
            <button class="icon-btn notification" :class="{ hasUnread: stats.messages > 0 }">
              <span class="icon">🔔</span>
              <span v-if="stats.messages > 0" class="badge">{{ stats.messages > 99 ? '99+' : stats.messages }}</span>
            </button>
            <button class="icon-btn" @click="goToSettings">
              <span class="icon">⚙️</span>
            </button>
            <button class="icon-btn avatar-btn" @click="goToProfile">
              <span class="mini-avatar">{{ userInfo.name ? userInfo.name.charAt(0).toUpperCase() : 'U' }}</span>
            </button>
          </div>
        </div>
      </header>
      
      <main class="content">
        <router-view v-slot="{ Component }">
          <transition name="page-fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </main>
    </section>
  </div>
</template>

<script>
import SideMenu from '../components/SideMenu.vue'
import { MENU } from '../data/menu'
import { logout, getUserInfo } from '../auth'
import { useRoute, useRouter } from 'vue-router'
import { computed, ref, onMounted, onBeforeUnmount } from 'vue'
import { dashboardApi } from '../api'
import { showToast } from '../components/Toast.vue'
import { showConfirm } from '../components/ConfirmDialog.vue'
import { searchWithPinyin, getPinyinInitials } from '../utils/pinyin.js'

export default {
  name: 'Dashboard',
  components: { SideMenu },
  data() {
    return { MENU }
  },
  setup() {
    const route = useRoute()
    const router = useRouter()
    const searchWrapper = ref(null)
    
    // 用户信息
    const userInfo = ref({})
    
    // 统计数据
    const stats = ref({
      projects: 0,
      tasks: 0,
      messages: 0
    })
    
    // 搜索相关
    const searchQuery = ref('')
    const searchFocused = ref(false)
    const selectedSearchIndex = ref(0)
    
    // 生成搜索索引
    const searchIndex = computed(() => {
      const items = []
      MENU.forEach(category => {
        if (category.children && category.children.length > 0) {
          category.children.forEach(item => {
            items.push({
              label: item.label,
              route: item.route,
              icon: item.icon,
              category: category.label,
              path: `${category.label} > ${item.label}`
            })
          })
        } else if (category.route) {
          items.push({
            label: category.label,
            route: category.route,
            icon: category.icon,
            category: '主菜单',
            path: category.label
          })
        }
      })
      return items
    })
    
    // 过滤搜索结果（支持拼音首字母搜索）
    const filteredSearchResults = computed(() => {
      if (!searchQuery.value.trim()) return []
      const query = searchQuery.value.toLowerCase()
      
      return searchIndex.value.filter(item => {
        // 直接匹配标签和分类
        if (item.label.toLowerCase().includes(query) ||
            item.category.toLowerCase().includes(query)) {
          return true
        }
        
        // 拼音首字母匹配
        const labelPinyin = getPinyinInitials(item.label).toLowerCase()
        const categoryPinyin = getPinyinInitials(item.category).toLowerCase()
        const pathPinyin = getPinyinInitials(item.path).toLowerCase()
        
        return labelPinyin.includes(query) ||
               categoryPinyin.includes(query) ||
               pathPinyin.includes(query)
      }).slice(0, 8)
    })
    
    // 当前页面标题
    const currentPageTitle = computed(() => {
      const path = route.path
      for (const item of MENU) {
        if (item.route === path) return item.label
        if (item.children) {
          for (const child of item.children) {
            if (child.route === path) return child.label
          }
        }
      }
      return '仪表盘'
    })
    
    // 获取用户信息
    const loadUserInfo = async () => {
      try {
        const info = await getUserInfo()
        if (info) {
          userInfo.value = info
        }
      } catch (error) {
        console.error('获取用户信息失败:', error)
      }
    }
    
    // 获取统计数据
    const loadStats = async () => {
      try {
        const res = await dashboardApi.getOverview()
        if (res.code === 200 && res.data) {
          stats.value = {
            projects: res.data.totalProjects || 0,
            tasks: res.data.pendingTasks || 0,
            messages: res.data.unreadMessages || 0
          }
        }
      } catch (error) {
        console.error('获取统计数据失败:', error)
      }
    }
    
    // 角色文本转换
    const getRoleText = (role) => {
      const roleMap = {
        'ADMIN': '超级管理员',
        'USER': '普通用户',
        'EDITOR': '编辑',
        'MANAGER': '经理'
      }
      return roleMap[role] || role || '用户'
    }
    
    // 搜索功能
    const handleSearch = () => {
      if (filteredSearchResults.value.length > 0) {
        const item = filteredSearchResults.value[selectedSearchIndex.value]
        navigateTo(item.route)
      }
    }
    
    const navigateTo = (route) => {
      router.push(route)
      clearSearch()
    }
    
    const clearSearch = () => {
      searchQuery.value = ''
      searchFocused.value = false
      selectedSearchIndex.value = 0
    }
    
    const handleSearchBlur = () => {
      setTimeout(() => {
        searchFocused.value = false
      }, 200)
    }
    
    const moveSearchSelection = (direction) => {
      const max = filteredSearchResults.value.length - 1
      selectedSearchIndex.value += direction
      if (selectedSearchIndex.value < 0) selectedSearchIndex.value = max
      if (selectedSearchIndex.value > max) selectedSearchIndex.value = 0
    }
    
    // 导航功能
    const goHome = () => {
      router.push('/dashboard/overview')
    }
    
    const goToSettings = () => {
      router.push('/dashboard/system')
    }
    
    const goToProfile = () => {
      router.push('/dashboard/profile')
    }
    
    const handleLogout = async () => {
      const confirmed = await showConfirm({
        title: '退出确认',
        message: '确定要退出登录吗？',
        type: 'warning',
        confirmText: '退出',
        cancelText: '取消'
      })
      
      if (confirmed) {
        await logout()
        window.location.href = '/login'
      }
    }
    
    // 点击外部关闭搜索
    const handleClickOutside = (e) => {
      if (searchWrapper.value && !searchWrapper.value.contains(e.target)) {
        searchFocused.value = false
      }
    }
    
    onMounted(() => {
      loadUserInfo()
      loadStats()
      document.addEventListener('click', handleClickOutside)
    })
    
    onBeforeUnmount(() => {
      document.removeEventListener('click', handleClickOutside)
    })
    
    return {
      userInfo,
      stats,
      searchQuery,
      searchFocused,
      selectedSearchIndex,
      searchWrapper,
      filteredSearchResults,
      currentPageTitle,
      getRoleText,
      handleSearch,
      navigateTo,
      clearSearch,
      handleSearchBlur,
      moveSearchSelection,
      goHome,
      goToSettings,
      goToProfile,
      handleLogout
    }
  }
}
</script>

<style scoped>
/* 整体布局 */
.dashboard {
  display: flex;
  height: 100vh;
  overflow: hidden;
  background: linear-gradient(135deg, #f0f9ff 0%, #e0f2fe 50%, #dbeafe 100%);
}

/* 侧边栏 */
.sidebar {
  width: 280px;
  min-width: 280px;
  background: linear-gradient(180deg, #0c4a6e 0%, #075985 50%, #0c4a6e 100%);
  padding: 20px 16px;
  display: flex;
  flex-direction: column;
  box-shadow: 4px 0 30px rgba(0, 0, 0, 0.15);
  overflow-y: auto;
  position: relative;
}

.sidebar::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: url("data:image/svg+xml,%3Csvg width='60' height='60' viewBox='0 0 60 60' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg fill='%23ffffff' fill-opacity='0.03'%3E%3Cpath d='M36 34v-4h-2v4h-4v2h4v4h2v-4h4v-2h-4zm0-30V0h-2v4h-4v2h4v4h2V6h4V4h-4zM6 34v-4H4v4H0v2h4v4h2v-4h4v-2H6zM6 4V0H4v4H0v2h4v4h2V6h4V4H6z'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E");
  pointer-events: none;
}

/* 品牌标识 */
.brand {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 22px;
  font-weight: 700;
  color: #fff;
  padding: 12px;
  margin-bottom: 24px;
  position: relative;
  z-index: 1;
}

.brand-icon {
  font-size: 28px;
  filter: drop-shadow(0 2px 4px rgba(0,0,0,0.2));
}

/* 用户卡片 */
.user-card {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 16px;
  margin-bottom: 24px;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  position: relative;
  z-index: 1;
  transition: all 0.3s ease;
}

.user-card:hover {
  background: rgba(255, 255, 255, 0.15);
  transform: translateY(-2px);
}

.user-avatar {
  width: 48px;
  height: 48px;
  border-radius: 14px;
  background: linear-gradient(135deg, #38bdf8 0%, #0ea5e9 50%, #0284c7 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  font-weight: 700;
  color: #fff;
  box-shadow: 0 4px 12px rgba(14, 165, 233, 0.4);
}

.user-details {
  display: flex;
  flex-direction: column;
  flex: 1;
}

.user-name {
  font-size: 15px;
  font-weight: 600;
  color: #fff;
}

.user-role {
  font-size: 12px;
  color: #7dd3fc;
  margin-top: 2px;
}

.user-status {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 4px 8px;
  background: rgba(34, 197, 94, 0.2);
  border-radius: 20px;
}

.status-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: #22c55e;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.5; }
}

.status-text {
  font-size: 11px;
  color: #86efac;
}

/* 侧边栏底部 */
.sidebar-footer {
  margin-top: auto;
  padding-top: 20px;
  position: relative;
  z-index: 1;
}

.quick-stats {
  display: flex;
  justify-content: space-around;
  padding: 16px 12px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 12px;
  margin-bottom: 16px;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
}

.stat-value {
  font-size: 18px;
  font-weight: 700;
  color: #38bdf8;
}

.stat-label {
  font-size: 11px;
  color: #7dd3fc;
}

.logout-btn {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 12px;
  background: linear-gradient(135deg, rgba(239, 68, 68, 0.2), rgba(239, 68, 68, 0.1));
  border: 1px solid rgba(239, 68, 68, 0.3);
  border-radius: 12px;
  color: #fca5a5;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.logout-btn:hover {
  background: linear-gradient(135deg, rgba(239, 68, 68, 0.3), rgba(239, 68, 68, 0.2));
  color: #fff;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(239, 68, 68, 0.3);
}

.logout-icon {
  font-size: 16px;
}

/* 主内容区 */
.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  background: rgba(255, 255, 255, 0.6);
  backdrop-filter: blur(20px);
}

/* 顶部栏 */
.topbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 32px;
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(20px);
  border-bottom: 1px solid rgba(14, 165, 233, 0.1);
  box-shadow: 0 2px 20px rgba(0, 0, 0, 0.05);
}

/* 面包屑 */
.breadcrumb {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 15px;
}

.home-icon {
  font-size: 16px;
}

.home {
  color: #64748b;
  cursor: pointer;
  transition: all 0.2s;
  display: flex;
  align-items: center;
  gap: 6px;
}

.home:hover {
  color: #0ea5e9;
}

.sep {
  color: #cbd5e1;
  font-weight: 300;
}

.current {
  color: #0ea5e9;
  font-weight: 600;
}

/* 顶部右侧 */
.topbar-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

/* 搜索框 */
.search-wrapper {
  position: relative;
}

.search-box {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 16px;
  background: #f1f5f9;
  border-radius: 12px;
  border: 2px solid transparent;
  transition: all 0.3s ease;
  min-width: 280px;
}

.search-box.active,
.search-box:focus-within {
  background: #fff;
  border-color: #0ea5e9;
  box-shadow: 0 0 0 4px rgba(14, 165, 233, 0.1);
}

.search-icon {
  font-size: 16px;
  color: #94a3b8;
}

.search-box input {
  border: none;
  background: transparent;
  outline: none;
  font-size: 14px;
  flex: 1;
  color: #334155;
}

.search-box input::placeholder {
  color: #94a3b8;
}

.clear-icon {
  font-size: 12px;
  color: #94a3b8;
  cursor: pointer;
  padding: 4px;
  border-radius: 50%;
  transition: all 0.2s;
}

.clear-icon:hover {
  background: #e2e8f0;
  color: #64748b;
}

/* 搜索下拉框 */
.search-dropdown {
  position: absolute;
  top: calc(100% + 8px);
  left: 0;
  right: 0;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.15);
  border: 1px solid #e2e8f0;
  overflow: hidden;
  z-index: 1000;
}

.search-results {
  max-height: 320px;
  overflow-y: auto;
}

.search-result-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  cursor: pointer;
  transition: all 0.2s;
  border-bottom: 1px solid #f1f5f9;
}

.search-result-item:last-child {
  border-bottom: none;
}

.search-result-item:hover,
.search-result-item.active {
  background: #f0f9ff;
}

.result-icon {
  font-size: 20px;
}

.result-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.result-title {
  font-size: 14px;
  font-weight: 500;
  color: #1e293b;
}

.result-path {
  font-size: 12px;
  color: #94a3b8;
}

.result-category {
  font-size: 11px;
  padding: 2px 8px;
  background: #e0f2fe;
  color: #0284c7;
  border-radius: 20px;
}

/* 下拉框动画 */
.search-dropdown-enter-active,
.search-dropdown-leave-active {
  transition: all 0.2s ease;
}

.search-dropdown-enter-from,
.search-dropdown-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

/* 顶部操作按钮 */
.topbar-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.icon-btn {
  position: relative;
  width: 42px;
  height: 42px;
  border: none;
  background: #f1f5f9;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s ease;
}

.icon-btn:hover {
  background: #e0f2fe;
  transform: scale(1.05);
}

.icon-btn .icon {
  font-size: 18px;
}

.icon-btn.notification.hasUnread {
  background: #fef3c7;
}

.icon-btn.notification.hasUnread:hover {
  background: #fde68a;
}

.badge {
  position: absolute;
  top: -4px;
  right: -4px;
  min-width: 18px;
  height: 18px;
  padding: 0 5px;
  background: #ef4444;
  color: #fff;
  font-size: 10px;
  font-weight: 600;
  border-radius: 9px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 2px solid #fff;
}

.avatar-btn {
  padding: 0;
  overflow: hidden;
}

.mini-avatar {
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #38bdf8, #0ea5e9);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  font-weight: 600;
  color: #fff;
}

/* 内容区 */
.content {
  flex: 1;
  padding: 28px 32px;
  overflow-y: auto;
}

/* 页面切换动画 */
.page-fade-enter-active,
.page-fade-leave-active {
  transition: all 0.3s ease;
}

.page-fade-enter-from {
  opacity: 0;
  transform: translateX(20px);
}

.page-fade-leave-to {
  opacity: 0;
  transform: translateX(-20px);
}
</style>