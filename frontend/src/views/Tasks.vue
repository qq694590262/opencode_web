<template>
  <div class="page-content">
    <div class="page-header">
      <h2>✅ 任务管理</h2>
      <p>查看和跟踪任务进度</p>
    </div>

    <div class="tasks-board">
      <div class="task-column" v-for="col in columns" :key="col.id">
        <div class="column-header">
          <h4>{{ col.title }}</h4>
          <span class="count">{{ col.tasks.length }}</span>
        </div>
        <div class="task-list">
          <div class="task-card" v-for="task in col.tasks" :key="task.id">
            <div class="task-priority" :class="task.priority"></div>
            <h5>{{ task.title }}</h5>
            <p>{{ task.desc }}</p>
            <div class="task-footer">
              <span class="task-user">👤 {{ task.user }}</span>
              <span class="task-date">{{ task.date }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Tasks',
  setup() {
    const columns = [
      { id: 1, title: '待办', tasks: [
        { id: 1, title: '完成首页设计', desc: '完成登录页和仪表盘设计稿', user: '张三', date: '03-20', priority: 'high' },
        { id: 2, title: 'API 接口对接', desc: '对接用户管理模块接口', user: '李四', date: '03-21', priority: 'medium' }
      ]},
      { id: 2, title: '进行中', tasks: [
        { id: 3, title: '数据库优化', desc: '优化查询性能', user: '王五', date: '03-18', priority: 'high' },
        { id: 4, title: '单元测试', desc: '编写核心模块测试用例', user: '赵六', date: '03-19', priority: 'low' }
      ]},
      { id: 3, title: '已完成', tasks: [
        { id: 5, title: '环境搭建', desc: '配置开发环境', user: '钱七', date: '03-15', priority: 'medium' },
        { id: 6, title: '需求分析', desc: '完成需求文档', user: '张三', date: '03-10', priority: 'low' }
      ]}
    ]
    return { columns }
  }
}
</script>

<style scoped>
.page-content { animation: fadeIn 0.3s ease; }
@keyframes fadeIn { from { opacity: 0; transform: translateY(10px); } to { opacity: 1; transform: translateY(0); } }

.page-header { margin-bottom: 24px; }
.page-header h2 { font-size: 24px; font-weight: 700; color: #0c4a6e; margin: 0 0 8px 0; }
.page-header p { font-size: 14px; color: #64748b; margin: 0; }

.tasks-board { display: flex; gap: 20px; overflow-x: auto; padding-bottom: 20px; }

.task-column { min-width: 300px; background: #f8fafc; border-radius: 16px; padding: 16px; }

.column-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.column-header h4 { font-size: 16px; font-weight: 600; color: #1e293b; margin: 0; }
.count { padding: 2px 10px; background: #e2e8f0; border-radius: 10px; font-size: 12px; color: #64748b; }

.task-card { background: #fff; border-radius: 12px; padding: 16px; margin-bottom: 12px; box-shadow: 0 2px 8px rgba(0,0,0,0.05); position: relative; overflow: hidden; }
.task-card:hover { box-shadow: 0 4px 12px rgba(0,0,0,0.1); }

.task-priority { position: absolute; left: 0; top: 0; bottom: 0; width: 4px; }
.task-priority.high { background: #ef4444; }
.task-priority.medium { background: #f59e0b; }
.task-priority.low { background: #22c55e; }

.task-card h5 { font-size: 14px; font-weight: 600; color: #1e293b; margin: 0 0 8px 0; padding-left: 8px; }
.task-card p { font-size: 12px; color: #64748b; margin: 0 0 12px 0; padding-left: 8px; }

.task-footer { display: flex; justify-content: space-between; font-size: 12px; color: #94a3b8; padding-left: 8px; }
</style>
