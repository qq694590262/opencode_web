<template>
  <div class="page-content">
    <div class="page-header">
      <h2>📅 日历日程</h2>
      <p>查看和安排日程</p>
    </div>

    <div class="calendar">
      <div class="calendar-header">
        <button class="nav-btn">◀</button>
        <h3>2024年3月</h3>
        <button class="nav-btn">▶</button>
      </div>
      <div class="calendar-grid">
        <div class="day-name" v-for="d in ['日','一','二','三','四','五','六']" :key="d">{{ d }}</div>
        <div class="day" v-for="(day, i) in calendarDays" :key="i" :class="{ 'other-month': day.other, 'today': day.today, 'has-event': day.event }">
          <span class="day-num">{{ day.date }}</span>
          <div class="event-dot" v-if="day.event"></div>
        </div>
      </div>
    </div>

    <div class="events-section">
      <h4>今日日程</h4>
      <div class="event-list">
        <div class="event-item" v-for="e in todayEvents" :key="e.id">
          <div class="event-time">{{ e.time }}</div>
          <div class="event-content">
            <h5>{{ e.title }}</h5>
            <p>{{ e.desc }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Calendar',
  setup() {
    const calendarDays = Array.from({length: 35}, (_, i) => {
      const d = i - 3
      return { date: d > 0 && d <= 31 ? d : 31 + d, other: d <= 0 || d > 31, today: d === 15, event: [5, 12, 18, 25].includes(d) }
    })
    const todayEvents = [
      { id: 1, time: '09:00', title: '晨会', desc: '部门周例会' },
      { id: 2, time: '14:00', title: '项目评审', desc: '企业管理系统评审' },
      { id: 3, time: '16:30', title: '技术分享', desc: 'Vue3 最佳实践' }
    ]
    return { calendarDays, todayEvents }
  }
}
</script>

<style scoped>
.page-content { animation: fadeIn 0.3s ease; }
@keyframes fadeIn { from { opacity: 0; transform: translateY(10px); } to { opacity: 1; transform: translateY(0); } }

.page-header { margin-bottom: 24px; }
.page-header h2 { font-size: 24px; font-weight: 700; color: #0c4a6e; margin: 0 0 8px 0; }
.page-header p { font-size: 14px; color: #64748b; margin: 0; }

.calendar { background: #fff; border-radius: 16px; padding: 24px; box-shadow: 0 4px 15px rgba(0,0,0,0.05); margin-bottom: 24px; }

.calendar-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.calendar-header h3 { font-size: 20px; font-weight: 600; color: #1e293b; margin: 0; }
.nav-btn { width: 36px; height: 36px; border: none; border-radius: 10px; background: #f1f5f9; cursor: pointer; transition: all 0.2s; }
.nav-btn:hover { background: #e0f2fe; }

.calendar-grid { display: grid; grid-template-columns: repeat(7, 1fr); gap: 8px; }
.day-name { text-align: center; font-size: 13px; font-weight: 600; color: #64748b; padding: 8px; }

.day { aspect-ratio: 1; display: flex; flex-direction: column; align-items: center; justify-content: center; border-radius: 10px; position: relative; cursor: pointer; transition: all 0.2s; }
.day:hover { background: #f1f5f9; }
.day.other { color: #cbd5e1; }
.day.today { background: #e0f2fe; }
.day.today .day-num { color: #0ea5e9; font-weight: 600; }
.day-num { font-size: 14px; color: #475569; }

.event-dot { width: 6px; height: 6px; border-radius: 50%; background: #0ea5e9; position: absolute; bottom: 6px; }

.events-section { background: #fff; border-radius: 16px; padding: 24px; box-shadow: 0 4px 15px rgba(0,0,0,0.05); }
.events-section h4 { font-size: 16px; font-weight: 600; color: #1e293b; margin: 0 0 16px 0; }

.event-item { display: flex; gap: 16px; padding: 12px 0; border-bottom: 1px solid #f1f5f9; }
.event-item:last-child { border-bottom: none; }
.event-time { font-size: 13px; font-weight: 600; color: #0ea5e9; width: 50px; }
.event-content h5 { font-size: 14px; font-weight: 600; color: #1e293b; margin: 0 0 4px 0; }
.event-content p { font-size: 12px; color: #64748b; margin: 0; }
</style>
