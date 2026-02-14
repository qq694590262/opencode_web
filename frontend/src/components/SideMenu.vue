<template>
  <nav class="side-menu">
    <ul class="menu-list">
      <li v-for="item in menu" :key="item.id" class="menu-item">
        <div 
          class="menu-entry" 
          :class="{ active: isActive(item) }"
          @click="handleClick(item)"
        >
          <span class="icon">{{ item.icon }}</span>
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
              <span class="icon">{{ child.icon }}</span>
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
                  <span class="icon">{{ grand.icon }}</span>
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
.side-menu {
  padding: 8px 12px;
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
  margin: 4px 0;
}

.menu-entry {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  cursor: pointer;
  border-radius: 8px;
  color: rgba(255, 255, 255, 0.7);
  font-size: 14px;
  font-weight: 500;
  transition: all 0.2s ease;
}

.menu-entry:hover {
  background: rgba(255, 255, 255, 0.12);
  color: #fff;
}

.menu-entry.active {
  background: linear-gradient(135deg, #3b82f6, #2563eb);
  color: #fff;
  box-shadow: 0 2px 8px rgba(37, 99, 235, 0.4);
}

.icon {
  font-size: 16px;
  width: 22px;
  text-align: center;
  flex-shrink: 0;
}

.label {
  flex: 1;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.caret {
  font-size: 9px;
  transition: transform 0.2s ease;
  color: rgba(255, 255, 255, 0.5);
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
  padding: 10px 16px 10px 48px;
  font-size: 13px;
  color: rgba(255, 255, 255, 0.6);
}

.sub-item .menu-entry:hover {
  background: rgba(255, 255, 255, 0.08);
  color: #fff;
}

.sub-item .menu-entry.active {
  background: rgba(59, 130, 246, 0.3);
  color: #fff;
}

.sub-item .sub-entry {
  padding: 10px 16px 10px 72px;
  font-size: 13px;
}
</style>
