<template>
  <div class="dashboard">
    <aside class="sidebar">
      <div class="brand">
        <span class="brand-icon">💙</span>
        <span>Opencode</span>
      </div>
      <div class="user-info">
        <div class="avatar">A</div>
        <div class="user-detail">
          <span class="name">Admin</span>
          <span class="role">管理员</span>
        </div>
      </div>
      <SideMenu :menu="MENU" />
      <div class="sidebar-footer">
        <button class="logout" @click="logout">
          <span>🚪</span> 退出登录
        </button>
      </div>
    </aside>
    <section class="main-content">
      <header class="topbar">
        <div class="breadcrumb">
          <span class="home">首页</span>
          <span class="sep">/</span>
          <span class="current">{{ currentPageTitle }}</span>
        </div>
        <div class="topbar-right">
          <div class="search-box">
            <span>🔍</span>
            <input type="text" placeholder="搜索..." />
          </div>
          <div class="topbar-icons">
            <button class="icon-btn">🔔</button>
            <button class="icon-btn">⚙️</button>
          </div>
        </div>
      </header>
      <main class="content">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
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
import { logout } from '../auth'
import { useRoute } from 'vue-router'
import { computed } from 'vue'

export default {
  name: 'Dashboard',
  components: { SideMenu },
  data() {
    return { MENU }
  },
  setup() {
    const route = useRoute()
    const currentPageTitle = computed(() => {
      const path = route.path
      for (const item of MENU) {
        if (item.route === path) return item.label
        if (item.children) {
          for (const child of item.children) {
            if (child.route === path) return child.label
            if (child.children) {
              for (const grand of child.children) {
                if (grand.route === path) return grand.label
              }
            }
          }
        }
      }
      return '仪表盘'
    })
    return { currentPageTitle }
  },
  methods: {
    logout() {
      logout()
      window.location.href = '/login'
    }
  }
}
</script>

<style scoped>
.dashboard {
  display: flex;
  height: 100vh;
  overflow: hidden;
  background: linear-gradient(180deg, #f0f9ff 0%, #e0f2fe 50%, #bae6fd 100%);
}

.sidebar {
  width: 260px;
  min-width: 260px;
  background: linear-gradient(180deg, #0c4a6e 0%, #075985 50%, #0c4a6e 100%);
  padding: 20px 12px;
  display: flex;
  flex-direction: column;
  box-shadow: 4px 0 20px rgba(0, 0, 0, 0.1);
  overflow-y: auto;
}

.brand {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 20px;
  font-weight: 700;
  color: #fff;
  padding: 8px 12px;
  margin-bottom: 20px;
}

.brand-icon {
  font-size: 24px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px 12px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  margin-bottom: 20px;
}

.avatar {
  width: 44px;
  height: 44px;
  border-radius: 12px;
  background: linear-gradient(135deg, #38bdf8 0%, #0ea5e9 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  font-weight: 600;
  color: #fff;
}

.user-detail {
  display: flex;
  flex-direction: column;
}

.user-detail .name {
  font-size: 14px;
  font-weight: 600;
  color: #fff;
}

.user-detail .role {
  font-size: 12px;
  color: #7dd3fc;
}

.sidebar-footer {
  margin-top: auto;
  padding-top: 16px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.logout {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 12px;
  background: rgba(239, 68, 68, 0.2);
  border: 1px solid rgba(239, 68, 68, 0.3);
  border-radius: 10px;
  color: #fca5a5;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.logout:hover {
  background: rgba(239, 68, 68, 0.3);
  color: #fff;
}

.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.topbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 28px;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid rgba(14, 165, 233, 0.1);
}

.breadcrumb {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
}

.breadcrumb .home {
  color: #64748b;
}

.breadcrumb .sep {
  color: #94a3b8;
}

.breadcrumb .current {
  color: #0ea5e9;
  font-weight: 500;
}

.topbar-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.search-box {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  background: #f1f5f9;
  border-radius: 10px;
}

.search-box input {
  border: none;
  background: transparent;
  outline: none;
  font-size: 14px;
  width: 180px;
  color: #334155;
}

.search-box input::placeholder {
  color: #94a3b8;
}

.topbar-icons {
  display: flex;
  gap: 8px;
}

.icon-btn {
  width: 40px;
  height: 40px;
  border: none;
  background: #f1f5f9;
  border-radius: 10px;
  font-size: 18px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.icon-btn:hover {
  background: #e0f2fe;
  transform: scale(1.05);
}

.content {
  flex: 1;
  padding: 24px 28px;
  overflow-y: auto;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
