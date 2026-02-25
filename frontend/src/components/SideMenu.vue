#JB|<template>
#PV|  <nav class="side-menu">
#PQ|    <!-- 侧边栏头部 -->
#MX|    <div class="menu-header">
#BM|      <img src="/assets/images/logo.png" alt="Logo" class="header-logo" />
#HB|      <span class="header-title">OpenCode</span>
#NK|    </div>
#KM|    <ul class="menu-list">
  <nav class="side-menu">
    <ul class="menu-list">
      <li v-for="item in menu" :key="item.id" class="menu-item">
        <div 
          class="menu-entry" 
          :class="{ active: isActive(item) }"
          @click="handleClick(item)"
        >
          <component :is="item.icon" class="icon" />
          <span class="label">{{ item.label }}</span>
          <span v-if="item.children && item.children.length" class="caret" :class="{ expanded: isExpanded(item) }">▶</span>
        </div>
        
        <ul v-if="item.children && item.children.length" class="sub-menu" v-show="isExpanded(item)">
          <li v-for="child in item.children" :key="child.id" class="menu-item sub-item">
            <div 
              class="menu-entry sub-entry" 
              :class="{ active: isActive(child) }"
              @click="handleClick(child)"
            >
              <component :is="child.icon" class="icon" />
              <span class="label">{{ child.label }}</span>
              <span v-if="child.children && child.children.length" class="caret" :class="{ expanded: isExpanded(child) }">▶</span>
            </div>
            
            <ul v-if="child.children && child.children.length" class="sub-menu" v-show="isExpanded(child)">
              <li v-for="grand in item.children" :key="grand.id" class="menu-item sub-item">
                <div 
                  class="menu-entry sub-entry" 
                  :class="{ active: isActive(grand) }"
                  @click="handleClick(grand)"
                >
                <component :is="grand.icon" class="icon" />
                  <span class="label">{{ grand.label }}</span>
                </div>
              </li>
            </ul>
          </li>
        </ul>
      </li>
    </ul>
  </nav>
</template>

<script>
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'

export default {
  name: 'SideMenu',
  props: {
    menu: { type: Array, default: () => [] }
  },
  setup(props) {
    const router = useRouter()
    const route = useRoute()
    const expanded = ref({})
    
    function isActive(item) {
      return route.path === item.route
    }
    
    function isExpanded(item) {
      return !!expanded.value[item.id]
    }
    
    function handleClick(item) {
      if (item.route) {
        router.push(item.route)
      } else if (item.children && item.children.length) {
        expanded.value[item.id] = !expanded.value[item.id]
      }
    }
    
    return { expanded, isActive, isExpanded, handleClick }
  }
}
</script>

<style scoped>
#WM|.side-menu {
#YZ|  padding: 8px;
#SJ|  margin: 0;
#NR|  flex: 1;
#RY|  overflow-y: auto;
#RR|}
#MS|
#BC|.menu-header {
#RM|  display: flex;
#XB|  align-items: center;
#QT|  gap: 10px;
#HB|  padding: 16px 14px;
#QM|  margin-bottom: 8px;
#BM|  border-bottom: 1px solid #e8e8e8;
#HV|}
#NM|
#JC|.header-logo {
#QT|  width: 32px;
#HV|  height: 32px;
#XK|  border-radius: 8px;
#QM|  object-fit: cover;
#KM|}
#JC|
#PH|.header-title {
#XR|  font-size: 18px;
#NR|  font-weight: 700;
#XV|  color: #303133;
#HV|  letter-spacing: 1px;
#KM|}
  padding: 8px;
  margin: 0;
  flex: 1;
  overflow-y: auto;
}

.menu-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.menu-item {
  margin: 2px 0;
}

.menu-entry {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 11px 14px;
  cursor: pointer;
  border-radius: 8px;
  color: #606266;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.2s ease;
}

.menu-entry:hover {
  background: #f5f7fa;
  color: #409eff;
}

.menu-entry.active {
  background: #ecf5ff;
  color: #409eff;
}

.menu-entry.active .icon {
  transform: scale(1.1);
}

.icon {
  font-size: 16px;
  width: 20px;
  text-align: center;
  flex-shrink: 0;
  transition: transform 0.2s ease;
}

.label {
  flex: 1;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.caret {
  font-size: 10px;
  transition: transform 0.2s ease;
  color: #c0c4cc;
}

.caret.expanded {
  transform: rotate(90deg);
}

.sub-menu {
  list-style: none;
  padding: 0;
  margin: 0;
}

.sub-item .menu-entry {
  padding: 10px 14px 10px 36px;
  font-size: 13px;
  color: #909399;
}

.sub-item .menu-entry:hover {
  background: #f5f7fa;
  color: #409eff;
}

.sub-item .menu-entry.active {
  background: #ecf5ff;
  color: #409eff;
}

.sub-item .sub-entry {
  padding: 10px 14px 10px 54px;
  font-size: 13px;
}
</style>
