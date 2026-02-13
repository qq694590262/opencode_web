<template>
  <nav class="side-menu">
    <ul class="menu-list">
      <li v-for="item in menu" :key="item.id" class="menu-item">
        <div 
          class="menu-entry" 
          :class="{ active: isActive(item) }"
          @click="handleClick(item)"
        >
          <img v-if="item.icon" :src="item.icon" class="icon" />
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
              <img v-if="child.icon" :src="child.icon" class="icon" />
              <span class="label">{{ child.label }}</span>
              <span v-if="child.children && child.children.length" class="caret" :class="{ expanded: isExpanded(child) }">▶</span>
            </div>
            
            <ul v-if="child.children && child.children.length" class="sub-menu" v-show="isExpanded(child)">
              <li v-for="grand in child.children" :key="grand.id" class="menu-item sub-item">
                <div 
                  class="menu-entry sub-entry" 
                  :class="{ active: isActive(grand) }"
                  @click="handleClick(grand)"
                >
                  <img v-if="grand.icon" :src="grand.icon" class="icon" />
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
  padding: 0;
  margin: 0;
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
  gap: 10px;
  padding: 10px 14px;
  cursor: pointer;
  border-radius: 10px;
  color: rgba(255, 255, 255, 0.75);
  font-size: 14px;
  transition: all 0.2s ease;
}

.menu-entry:hover {
  background: rgba(255, 255, 255, 0.1);
  color: #fff;
}

.menu-entry.active {
  background: rgba(56, 189, 248, 0.25);
  color: #fff;
}

.icon {
  width: 18px;
  height: 18px;
  object-fit: contain;
}

.label {
  flex: 1;
}

.caret {
  font-size: 10px;
  transition: transform 0.2s ease;
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
  padding: 8px 14px 8px 30px;
  font-size: 13px;
}

.sub-item .sub-entry {
  padding: 8px 14px 8px 46px;
  font-size: 13px;
}
</style>
