<template>
  <div class="dashboard">
    <!-- 侧边栏 -->
    <aside class="sidebar" :class="{ collapsed: sidebarCollapsed }">
      <div class="brand">
        <span class="brand-icon">🏢</span>
        <span v-if="!sidebarCollapsed" class="brand-text">企业管理系统</span>
      </div>
      
      <SideMenu v-if="!sidebarCollapsed" :menu="MENU" />
      
      <div class="sidebar-footer">
        <button class="logout-btn" @click="handleLogout">
          <span class="logout-icon">🚪</span>
          <span v-if="!sidebarCollapsed">退出登录</span>
        </button>
      </div>
    </aside>

    <!-- 折叠按钮 - 始终显示，在侧边栏外右侧紧贴 -->
    <button class="sidebar-toggle" @click="toggleSidebar">
      <!-- 展开时显示左箭头 -->
      <svg v-if="!sidebarCollapsed" class="toggle-arrow" width="10" height="16" viewBox="0 0 10 16" fill="none">
        <path d="M8 12L4 8L8 4" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
      </svg>
      <!-- 折叠时显示右箭头 -->
      <svg v-else class="toggle-arrow" width="10" height="16" viewBox="0 0 10 16" fill="none">
        <path d="M2 4L6 8L2 12" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
      </svg>
    </button>
    
    <section class="main-content" :class="{ 'sidebar-collapsed': sidebarCollapsed }">
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
                placeholder="搜索菜单，功能..."
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
          
          <!-- 顶部操作区 -->
          <div class="topbar-actions">
            <!-- 设置下拉菜单 -->
            <div class="settings-dropdown" ref="settingsDropdown">
              <button class="icon-btn settings-btn" @click="toggleSettingsMenu">
                <span class="icon">⚙️</span>
              </button>
              <transition name="dropdown-fade">
                <div v-if="showSettingsMenu" class="dropdown-menu">
                  <div class="dropdown-item" @click="goToSettings">
                    <span class="dropdown-icon">🔧</span>
                    <span>系统设置</span>
                  </div>
                  <div class="dropdown-item" @click="goToProfile">
                    <span class="dropdown-icon">👤</span>
                    <span>个人信息</span>
                  </div>
                  <div class="dropdown-divider"></div>
                  <div class="dropdown-item danger" @click="handleLogout">
                    <span class="dropdown-icon">🚪</span>
                    <span>退出登录</span>
                  </div>
                </div>
              </transition>
            </div>
            
            <!-- 用户名称 - 纯文本 -->
            <span class="user-name-text">{{ userInfo.name || userInfo.username || '用户' }}</span>
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
import { getPinyinInitials, getFullPinyin } from '../utils/pinyin.js'

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
    const settingsDropdown = ref(null)
    
    // 侧边栏折叠状态
    const sidebarCollapsed = ref(false)
    
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
    const showSettingsMenu = ref(false)
    
    // 切换侧边栏
    const toggleSidebar = () => {
      sidebarCollapsed.value = !sidebarCollapsed.value
    }
    
    // 切换设置菜单
    const toggleSettingsMenu = () => {
      showSettingsMenu.value = !showSettingsMenu.value
    }
    
    // 关闭设置菜单
    const closeSettingsMenu = () => {
      showSettingsMenu.value = false
    }
    
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
    
    // 过滤搜索结果（支持中文、拼音首字母、全拼）
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
        const labelInitials = getPinyinInitials(item.label).toLowerCase()
        const categoryInitials = getPinyinInitials(item.category).toLowerCase()
        const pathInitials = getPinyinInitials(item.path).toLowerCase()
        
        if (labelInitials.includes(query) ||
            categoryInitials.includes(query) ||
            pathInitials.includes(query)) {
          return true
        }
        
        // 全拼匹配
        const labelFull = getFullPinyin(item.label).toLowerCase()
        const categoryFull = getFullPinyin(item.category).toLowerCase()
        const pathFull = getFullPinyin(item.path).toLowerCase()
        
        return labelFull.includes(query) ||
               categoryFull.includes(query) ||
               pathFull.includes(query)
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
      closeSettingsMenu()
      router.push('/dashboard/system')
    }
    
    const goToProfile = () => {
      closeSettingsMenu()
      router.push('/dashboard/profile')
    }
    
    const handleLogout = async () => {
      closeSettingsMenu()
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
    
    // 点击外部关闭设置菜单
    const handleClickOutside = (e) => {
      if (settingsDropdown.value && !settingsDropdown.value.contains(e.target)) {
        closeSettingsMenu()
      }
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
      sidebarCollapsed,
      userInfo,
      stats,
      searchQuery,
      searchFocused,
      selectedSearchIndex,
      searchWrapper,
      settingsDropdown,
      showSettingsMenu,
      filteredSearchResults,
      currentPageTitle,
      toggleSidebar,
      toggleSettingsMenu,
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
  background: #f5f7fa;
  position: relative;
}

/* 侧边栏 */
.sidebar {
  width: 220px;
  min-width: 220px;
  background: #ffffff;
  display: flex;
  flex-direction: column;
  box-shadow: 2px 0 12px rgba(0, 0, 0, 0.08);
  overflow-y: auto;
  transition: all 0.25s ease;
  position: relative;
  z-index: 10;
  border-right: 1px solid #e4e7ed;
}

.sidebar.collapsed {
  width: 0;
  min-width: 0;
  overflow: hidden;
  border-right: none;
}

/* 折叠按钮 - 在侧边栏外右侧紧贴 */
.sidebar-toggle {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  left: 220px;
  width: 22px;
  height: 52px;
  background: #ffffff;
  border: none;
  border-left: 1px solid #e4e7ed;
  border-right: 1px solid #e4e7ed;
  color: #909399;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.25s ease;
  z-index: 20;
}

.sidebar-toggle .toggle-arrow {
  transition: transform 0.2s ease;
}

.sidebar.collapsed ~ .sidebar-toggle {
  left: 0;
  border-left: 1px solid #e4e7ed;
  border-right: none;
}

.sidebar-toggle:hover {
  background: #409eff;
  color: #fff;
  border-color: #409eff;
}

/* 品牌标识 */
.brand {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 17px;
  font-weight: 600;
  color: #303133;
  padding: 18px 16px;
  margin-bottom: 8px;
  position: relative;
  z-index: 1;
  border-bottom: 1px solid #ebeef5;
  flex-shrink: 0;
}

.brand-icon {
  font-size: 24px;
  flex-shrink: 0;
}

.brand-text {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  flex: 1;
}

/* 侧边栏底部 */
.sidebar-footer {
  margin-top: auto;
  padding: 16px;
  position: relative;
  z-index: 1;
  border-top: 1px solid #ebeef5;
}

.logout-btn {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 10px 16px;
  background: #fff;
  border: 1px solid #ebeef5;
  border-radius: 8px;
  color: #606266;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.logout-btn:hover {
  background: #fef0f0;
  border-color: #fdecea;
  color: #f56c6c;
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
  background: #f5f7fa;
  margin-left: 0;
  transition: all 0.25s ease;
}

.main-content.sidebar-collapsed {
  margin-left: 0;
}

/* 顶部栏 */
.topbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
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
  gap: 12px;
  padding-right: 8px;
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

/* 顶部操作区 */
.topbar-actions {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-left: auto;
}

/* 用户名称 - 纯文本 */
.user-name-text {
  font-size: 14px;
  font-weight: 500;
  color: #334155;
  padding: 8px 12px;
  cursor: pointer;
  transition: color 0.2s;
}

.user-name-text:hover {
  color: #0ea5e9;
}

/* 设置下拉菜单 */
.settings-dropdown {
  position: relative;
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

/* 下拉菜单 */
.dropdown-menu {
  position: absolute;
  top: calc(100% + 8px);
  right: 0;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.15);
  border: 1px solid #e2e8f0;
  overflow: hidden;
  z-index: 1000;
  min-width: 160px;
}

.dropdown-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px 16px;
  cursor: pointer;
  transition: all 0.2s;
  font-size: 14px;
  color: #334155;
}

.dropdown-item:hover {
  background: #f0f9ff;
}

.dropdown-item.danger {
  color: #dc2626;
}

.dropdown-item.danger:hover {
  background: #fef2f2;
}

.dropdown-icon {
  font-size: 16px;
}

.dropdown-divider {
  height: 1px;
  background: #f1f5f9;
  margin: 4px 0;
}

.dropdown-fade-enter-active,
.dropdown-fade-leave-active {
  transition: all 0.2s ease;
}

.dropdown-fade-enter-from,
.dropdown-fade-leave-to {
  opacity: 0;
  transform: translateY(-10px);
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