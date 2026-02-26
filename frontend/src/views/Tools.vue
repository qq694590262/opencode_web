<template>
  <div class="tools-page">
    <div class="page-header">
      <h2>📋 工具箱</h2>
      <p>便签和待办事项管理</p>
    </div>
    
    <div class="widgets-container">
      <div class="widget-section">
        <NoteWidget />
      </div>
      <div class="widget-section">
        <TodoWidget />
      </div>
    </div>
  </div>
</template>

<script>
import NoteWidget from '../components/NoteWidget.vue'
import TodoWidget from '../components/TodoWidget.vue'

export default {
  name: 'Tools',
  components: { NoteWidget, TodoWidget }
}
</script>

<style scoped>
.tools-page {
  padding: 24px;
  background: #f5f7fa;
  min-height: 100vh;
}

.page-header {
  margin-bottom: 24px;
}

.page-header h2 {
  font-size: 24px;
  color: #303133;
  margin: 0 0 8px 0;
}

.page-header p {
  color: #909399;
  margin: 0;
}

.widgets-container {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 24px;
}

.widget-section {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
}

@media (max-width: 1200px) {
  .widgets-container {
    grid-template-columns: 1fr;
  }
}
</style>
