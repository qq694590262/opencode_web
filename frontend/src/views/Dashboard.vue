<template>
  <div class="dashboard">
    <!-- 侧边栏 -->
    <aside class="sidebar" :class="{ collapsed: sidebarCollapsed }">
      <!-- 品牌区 -->
      <div class="brand">
        <el-icon class="brand-icon"><DataBoard /></el-icon>
        <span v-if="!sidebarCollapsed" class="brand-text">企业管理系统</span>
      </div>
      
      <!-- 菜单 -->
      <el-menu
        :default-active="activeMenu"
        class="sidebar-menu"
        :collapse="sidebarCollapsed"
        :collapse-transition="false"
        :popper-append-to-body="false"
        router
      >

        <template v-for="item in MENU" :key="item.route || item.label">
          <el-menu-item v-if="!item.children" :index="item.route">
            <el-icon><IconMap :name="item.icon" /></el-icon>
            <template #title>{{ item.label }}</template>
          </el-menu-item>
          
          <el-sub-menu v-else :index="item.label">
            <template #title>
              <el-icon><IconMap :name="item.icon" /></el-icon>
              <span>{{ item.label }}</span>
            </template>
            <el-menu-item 
              v-for="child in item.children" 
              :key="child.route" 
              :index="child.route"
            >
              <el-icon><IconMap :name="child.icon" /></el-icon>
              <span>{{ child.label }}</span>
            </el-menu-item>
          </el-sub-menu>
        </template>
      </el-menu>
      
      <!-- 侧边栏底部 -->
      <div class="sidebar-footer">
        <button 
          class="logout-btn" 
          @click="handleLogout"
        >
          <el-icon><SwitchButton /></el-icon>
          <span v-if="!sidebarCollapsed">退出登录</span>
        </button>
      </div>
    </aside>

    <!-- 折叠按钮 -->
    <button class="sidebar-toggle" @click="toggleSidebar">
      <el-icon v-if="sidebarCollapsed"><ArrowRight /></el-icon>
      <el-icon v-else><ArrowLeft /></el-icon>
    </button>
    
    <!-- 主内容区 -->
    <section class="main-content" :class="{ 'sidebar-collapsed': sidebarCollapsed }">
      <!-- 顶部栏 -->
      <header class="topbar">
        <div class="breadcrumb">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/dashboard/overview' }">
              <el-icon><HomeFilled /></el-icon>
            </el-breadcrumb-item>
            <el-breadcrumb-item>{{ currentPageTitle }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        
        <div class="topbar-right">
          <!-- 搜索框 -->
          <el-autocomplete
            v-model="searchQuery"
            :fetch-suggestions="querySearch"
            placeholder="搜索菜单..."
            class="search-input"
            @select="handleSelect"
            @keyup.enter="handleEnter"
            :trigger-on-focus="false"
            clearable
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
            <template #default="{ item }">
              <div class="search-item">
                <el-icon><IconMap :name="item.icon" /></el-icon>
                <span class="search-label">{{ item.label }}</span>
                <span class="search-category">{{ item.category }}</span>
              </div>
            </template>
          </el-autocomplete>
          
          <!-- 顶部操作区 -->
          <div class="topbar-actions">
            <!-- 通知图标 -->
            <el-dropdown @command="handleNotify" trigger="click">
              <el-badge :value="notifyCount" :hidden="notifyCount === 0" class="notification-badge">
                <el-button :icon="Bell" circle />
              </el-badge>
              <template #dropdown>
                <el-dropdown-menu class="notify-dropdown">
                  <div class="notify-header">
                    <span>通知中心</span>
                    <el-button type="primary" link size="small" @click.stop="goToMessages">查看全部</el-button>
                  </div>
                  <el-dropdown-item v-for="notify in notifies" :key="notify.id" :command="'view:' + notify.id">
                    <div class="notify-item">
                      <el-icon :class="notify.type"><component :is="getNotifyIcon(notify.type)" /></el-icon>
                      <div class="notify-content">
                        <div class="notify-title">{{ notify.title }}</div>
                        <div class="notify-time">{{ notify.time }}</div>
                      </div>
                    </div>
                  </el-dropdown-item>
                  <div class="notify-empty" v-if="notifies.length === 0">暂无新通知</div>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
            
            <!-- 用户下拉菜单 -->
            <el-dropdown @command="handleCommand">
              <div class="user-info">
                <el-avatar :size="36" :style="avatarStyle">
                  {{ userInfo.name?.[0] || userInfo.username?.[0] || '用户' }}
                </el-avatar>
                <span class="user-name">{{ userInfo.name || userInfo.username || '用户' }}</span>
                <el-icon><ArrowDown /></el-icon>
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">
                    <el-icon><User /></el-icon>个人信息
                  </el-dropdown-item>
                  <el-dropdown-item command="system">
                    <el-icon><Setting /></el-icon>系统设置
                  </el-dropdown-item>
                  <el-dropdown-item divided command="logout">
                    <el-icon><SwitchButton /></el-icon>退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </header>
      
      <!-- 内容区 -->
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
import IconMap from '../components/IconMap.vue'
import { MENU } from '../data/menu'
import { logout, getUserInfo } from '../auth'
import { useRoute, useRouter } from 'vue-router'
import { computed, ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  DataBoard, HomeFilled, Search, Bell, ArrowDown, ArrowLeft, ArrowRight,
  User, Setting, SwitchButton, 
  Odometer, TrendCharts, DataAnalysis, User as UserIcon, 
  Document, Folder, Calendar, Notebook, Grid, Setting as SystemIcon,
  Avatar, Collection, List,
  OfficeBuilding, School, BellFilled, Message, Checked, Reading, Cpu
} from '@element-plus/icons-vue'

export default {
  name: 'Dashboard',
  components: {
    IconMap
  },
  data() {
    return { 
      MENU,
      avatarColors: [
        '#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399',
        '#C71585', '#409EFF', '#20B2AA', '#FF8C00', '#9400D3'
      ]
    }
  },
  computed: {
    activeMenu() {
      return this.$route.path
    },
    avatarStyle() {
      const colors = this.avatarColors
      const index = (this.userInfo.username?.charCodeAt(0) || 0) % colors.length
      return {
        backgroundColor: colors[index],
        color: '#fff'
      }
    }
  },
  setup() {
    const route = useRoute()
    const router = useRouter()
    
    // 侧边栏折叠状态
    const sidebarCollapsed = ref(false)
    
    // 用户信息
    const userInfo = ref({})
    
    // 搜索相关
    const searchQuery = ref('')
    
    // 切换侧边栏
    const toggleSidebar = () => {
      sidebarCollapsed.value = !sidebarCollapsed.value
    }
    
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
    
    // 构建搜索建议
    const searchIndex = computed(() => {
      const items = []
      MENU.forEach(category => {
        if (category.children && category.children.length > 0) {
          category.children.forEach(item => {
            items.push({
              value: item.label,
              label: item.label,
              route: item.route,
              icon: item.icon || 'Folder',
              category: category.label
            })
          })
        } else if (category.route) {
          items.push({
            value: category.label,
            label: category.label,
            route: category.route,
            icon: category.icon || 'Folder',
            category: '主菜单'
          })
        }
      })
      return items
    })
    
    // 搜索建议
    const querySearch = (queryString, cb) => {
      const results = queryString
        ? searchIndex.value.filter(item => 
            item.label.toLowerCase().includes(queryString.toLowerCase())
          )
        : searchIndex.value
      cb(results.slice(0, 10))
    }
    
    // 选择搜索结果
    const handleSelect = (item) => {
      router.push(item.route)
      searchQuery.value = ''
    }
    
    const handleEnter = (item) => {
      if (item) {
        handleSelect(item)
      }
    }
    
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
    
    // 处理下拉菜单命令
    const handleCommand = (command) => {
      switch (command) {
        case 'profile':
          router.push('/dashboard/profile')
          break
        case 'system':
          router.push('/dashboard/system')
          break
        case 'logout':
          handleLogout()
          break
      }
    }
    
    // 通知相关
    const notifyCount = ref(3)
    const notifies = ref([
      { id: 1, title: '新任务通知', time: '10分钟前', type: 'task' },
      { id: 2, title: '系统公告', time: '1小时前', type: 'notice' },
      { id: 3, title: '审批提醒', time: '2小时前', type: 'approve' }
    ])
    
    const getNotifyIcon = (type) => {
      const icons = { task: 'Document', notice: 'BellFilled', approve: 'CircleCheck' }
      return icons[type] || 'Bell'
    }
    
    const handleNotify = (command) => {
      if (command.startsWith('view:')) {
        router.push('/dashboard/messages')
      }
    }
    
    const goToMessages = () => {
      router.push('/dashboard/messages')
    }
    
    // 退出登录
    const handleLogout = async () => {
      try {
        await ElMessageBox.confirm('确定要退出登录吗？', '退出确认', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        await logout()
        window.location.href = '/login'
      } catch (e) {
        // 用户取消
      }
    }
    
    onMounted(() => {
      loadUserInfo()
    })
    
    return {
      sidebarCollapsed,
      userInfo,
      searchQuery,
      currentPageTitle,
      toggleSidebar,
      querySearch,
      handleSelect,
      handleEnter,
      handleCommand,
      handleLogout,
      notifyCount,
      notifies,
      getNotifyIcon,
      handleNotify,
      goToMessages,
      // Icons
      DataBoard, HomeFilled, Search, Bell, ArrowDown, ArrowLeft, ArrowRight,
      User, Setting, SwitchButton,
      OfficeBuilding, School, BellFilled, Message, Checked, Reading, Cpu
    }
  }
}
</script>

<style scoped>
/* 禁用菜单所有动画 - 解决卡顿问题 */
:deep(.el-menu-item),
:deep(.el-sub-menu__title),
:deep(.el-sub-menu .el-menu) {
  transition: none !important;
  animation: none !important;
}

/* 整体布局 */

/* 整体布局 */


/* 整体布局 */
/* 整体布局 */
.dashboard {
  display: flex;
  height: 100vh;
  overflow: hidden;
  background: #f5f7fa;
}

/* 侧边栏 */
.sidebar {
  width: 220px;
  min-width: 220px;
  background: #fff;
  display: flex;
  flex-direction: column;
  box-shadow: 2px 0 12px rgba(0, 0, 0, 0.08);
  position: relative;
  z-index: 10;
  transition: all 0.3s ease;
}

.sidebar.collapsed {
  width: 64px;
  min-width: 64px;
}

/* 品牌标识 - 与右侧topbar对齐 */
.brand {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  height: 56px;
  box-sizing: border-box;
  border-bottom: 1px solid #ebeef5;
  flex-shrink: 0;
}

.brand-icon {
  font-size: 22px;
  color: #409EFF;
  flex-shrink: 0;
}

.brand-text {
  font-size: 15px;
  font-weight: 600;
  color: #303133;
  white-space: nowrap;
  overflow: hidden;
}

/* 菜单样式 */
.sidebar-menu {
  border-right: none;
  flex: 1;
  overflow-y: auto;
}

.sidebar-menu:not(.el-menu--collapse) {
  width: 220px;
}

/* 侧边栏底部 */
.sidebar-footer {
  padding: 12px 16px;
  border-top: 1px solid #ebeef5;
  flex-shrink: 0;
}

.logout-btn {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 8px 12px;
  background: transparent;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  color: #606266;
  cursor: pointer;
  transition: all 0.2s;
}

.logout-btn:hover {
  color: #409EFF;
  border-color: #409EFF;
}

/* 折叠按钮 */
.sidebar-toggle {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  left: 220px;
  width: 20px;
  height: 48px;
  background: #fff;
  border: 1px solid #e4e7ed;
  border-left: none;
  color: #909399;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  z-index: 20;
  border-radius: 0 8px 8px 0;
}

.sidebar.collapsed ~ .sidebar-toggle {
  left: 64px;
}

.sidebar-toggle:hover {
  background: #409EFF;
  color: #fff;
  border-color: #409EFF;
}

/* 主内容区 */
.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  background: #f5f7fa;
  margin-left: 0;
  transition: all 0.3s ease;
}

/* 顶部栏 */
.topbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 24px;
  height: 56px;
  box-sizing: border-box;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
  flex-shrink: 0;
}

.breadcrumb {
  display: flex;
  align-items: center;
}

.breadcrumb :deep(.el-breadcrumb__item) {
  font-size: 14px;
}

/* 顶部右侧 */
.topbar-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

/* 搜索框 */
.search-input {
  width: 240px;
}

.search-input :deep(.el-input__wrapper) {
  padding: 4px 12px;
}

.search-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.search-label {
  flex: 1;
}

.search-category {
  font-size: 12px;
  color: #909399;
}

/* 顶部操作区 */
.topbar-actions {
  display: flex;
  align-items: center;
  gap: 8px;
}

.notification-badge :deep(.el-badge__content) {
  background: #F56C6C;
}

/* 用户信息 */
.user-info {
  display: flex;
  align-items: center;
  gap: 6px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 6px;
  transition: background 0.2s;
}

.user-info:hover {
  background: #f5f7fa;
}

.user-name {
  font-size: 13px;
  color: #303133;
  font-weight: 500;
  white-space: nowrap;
}

/* 内容区 */
.content {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  overflow-x: hidden;
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

/* 通知下拉菜单 */
.notify-dropdown {
  padding: 0 !important;
  min-width: 280px;
}

.notify-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  border-bottom: 1px solid #ebeef5;
  font-weight: 600;
  color: #303133;
}

.notify-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 4px 0;
}

.notify-item .el-icon {
  font-size: 18px;
}

.notify-item .el-icon.task {
  color: #409EFF;
}

.notify-item .el-icon.notice {
  color: #E6A23C;
}

.notify-item .el-icon.approve {
  color: #67C23A;
}

.notify-content {
  flex: 1;
}

.notify-title {
  font-size: 13px;
  color: #303133;
}

.notify-time {
  font-size: 12px;
  color: #909399;
}

.notify-empty {
  padding: 20px;
  text-align: center;
  color: #909399;
  font-size: 13px;
}
</style>
