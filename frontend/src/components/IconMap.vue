<script setup>
import { computed } from 'vue'
import { Setting, Folder, Odometer, OfficeBuilding, School, UserFilled, Key, FolderOpened, Checked, Calendar, Notebook, Document, Reading, Bell, BellFilled, Message, TrendCharts, DataAnalysis, List, Tools } from '@element-plus/icons-vue'

const props = defineProps({
  name: {
    type: String,
    required: true
  }
})

// 手动映射，确保图标一定存在
const iconMap = {
  Odometer, OfficeBuilding, School, UserFilled, Key, FolderOpened, Checked, Calendar,
  Notebook, Document, Reading, Bell, BellFilled, Message, TrendCharts, DataAnalysis,
  Setting, Folder, List, Tools
}

const iconComponent = computed(() => {
  const iconName = props.name
  if (!iconName) return Setting || Folder
  
  // 尝试从映射中获取图标
  if (iconMap[iconName]) return iconMap[iconName]
  
  // 如果找不到，返回默认图标
  return Setting || Folder
})
</script>

<template>
  <component :is="iconComponent" v-if="iconComponent" />
</template>
