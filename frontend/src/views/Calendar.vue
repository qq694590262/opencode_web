<template>
  <div class="page-content">
    <div class="page-header">
      <h2>📅 日历日程</h2>
      <p>查看和安排日程</p>
    </div>

    <div class="calendar">
      <div class="calendar-header">
        <button class="nav-btn" @click="prevMonth">◀</button>
        <h3>{{ currentYear }}年{{ currentMonth }}月</h3>
        <button class="nav-btn" @click="nextMonth">▶</button>
      </div>
      <div class="calendar-grid">
        <div class="day-name" v-for="d in ['日','一','二','三','四','五','六']" :key="d">{{ d }}</div>
        <div class="day" v-for="(day, i) in calendarDays" :key="i" 
             :class="{ 'other-month': day.other, 'today': day.today, 'has-event': hasEvent(day.date) }"
             @click="selectDate(day)">
          <span class="day-num">{{ day.date }}</span>
          <div class="event-dot" v-if="hasEvent(day.date)"></div>
        </div>
      </div>
    </div>

    <div class="events-section">
      <div class="section-header">
        <h4>日程列表</h4>
        <button class="btn-add" @click="showAddModal = true">+ 新建日程</button>
      </div>
      <div class="event-list">
        <div class="event-item" v-for="e in events" :key="e.id" @click="editEvent(e)">
          <div class="event-time">{{ formatTime(e.startTime) }}</div>
          <div class="event-content">
            <h5>{{ e.title }}</h5>
            <p>{{ e.description }}</p>
          </div>
          <div class="event-actions">
            <button class="btn-delete" @click.stop="deleteEvent(e.id)">删除</button>
          </div>
        </div>
      </div>
    </div>

    <!-- 添加/编辑日程弹窗 -->
    <div v-if="showAddModal || showEditModal" class="modal-overlay" @click.self="closeModal">
      <div class="modal">
        <h3>{{ showEditModal ? '编辑日程' : '新建日程' }}</h3>
        <form @submit.prevent="saveEvent">
          <div class="form-group">
            <label>标题</label>
            <input v-model="formData.title" required />
          </div>
          <div class="form-group">
            <label>描述</label>
            <input v-model="formData.description" />
          </div>
          <div class="form-group">
            <label>开始时间</label>
            <input type="datetime-local" v-model="formData.startTime" required />
          </div>
          <div class="form-group">
            <label>结束时间</label>
            <input type="datetime-local" v-model="formData.endTime" />
          </div>
          <div class="form-group">
            <label>类型</label>
            <select v-model="formData.type">
              <option value="meeting">会议</option>
              <option value="event">活动</option>
              <option value="task">任务</option>
              <option value="reminder">提醒</option>
            </select>
          </div>
          <div class="modal-actions">
            <button type="button" class="btn-cancel" @click="closeModal">取消</button>
            <button type="submit" class="btn-confirm">保存</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { calendarApi } from '../api'

export default {
  name: 'Calendar',
  setup() {
    const events = ref([])
    const currentDate = ref(new Date())
    const showAddModal = ref(false)
    const showEditModal = ref(false)
    const formData = ref({
      title: '',
      description: '',
      startTime: '',
      endTime: '',
      type: 'event'
    })

    const currentYear = computed(() => currentDate.value.getFullYear())
    const currentMonth = computed(() => currentDate.value.getMonth() + 1)

    const calendarDays = computed(() => {
      const year = currentDate.value.getFullYear()
      const month = currentDate.value.getMonth()
      const firstDay = new Date(year, month, 1)
      const lastDay = new Date(year, month + 1, 0)
      const startPadding = firstDay.getDay()
      const daysInMonth = lastDay.getDate()
      
      const days = []
      
      // 上月填充
      const prevMonthLastDay = new Date(year, month, 0).getDate()
      for (let i = startPadding - 1; i >= 0; i--) {
        days.push({ date: prevMonthLastDay - i, other: true, today: false })
      }
      
      // 当月
      const today = new Date()
      for (let i = 1; i <= daysInMonth; i++) {
        days.push({
          date: i,
          other: false,
          today: year === today.getFullYear() && month === today.getMonth() && i === today.getDate()
        })
      }
      
      // 下月填充
      const remaining = 35 - days.length
      for (let i = 1; i <= remaining; i++) {
        days.push({ date: i, other: true, today: false })
      }
      
      return days
    })

    const hasEvent = (date) => {
      if (!date || date > 31) return false
      return events.value.some(e => {
        const eventDate = new Date(e.startTime)
        return eventDate.getDate() === date && 
               eventDate.getMonth() === currentDate.value.getMonth() &&
               eventDate.getFullYear() === currentDate.value.getFullYear()
      })
    }

    const formatTime = (dateStr) => {
      if (!dateStr) return ''
      const date = new Date(dateStr)
      return date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
    }

    const loadEvents = async () => {
      try {
        const res = await calendarApi.getAll()
        if (res.code === 200) {
          events.value = res.data || []
        }
      } catch (error) {
        console.error('获取日程失败:', error)
      }
    }

    const prevMonth = () => {
      currentDate.value = new Date(currentDate.value.getFullYear(), currentDate.value.getMonth() - 1, 1)
    }

    const nextMonth = () => {
      currentDate.value = new Date(currentDate.value.getFullYear(), currentDate.value.getMonth() + 1, 1)
    }

    const selectDate = (day) => {
      if (!day.other) {
        console.log('选中日期:', day.date)
      }
    }

    const editEvent = (event) => {
      formData.value = { ...event }
      showEditModal.value = true
    }

    const deleteEvent = async (id) => {
      if (!confirm('确定要删除该日程吗？')) return
      try {
        const res = await calendarApi.delete(id)
        if (res.code === 200) {
          await loadEvents()
        }
      } catch (error) {
        console.error('删除日程失败:', error)
      }
    }

    const saveEvent = async () => {
      try {
        let res
        if (showEditModal.value) {
          res = await calendarApi.update(formData.value.id, formData.value)
        } else {
          res = await calendarApi.create(formData.value)
        }
        if (res.code === 200) {
          closeModal()
          await loadEvents()
        }
      } catch (error) {
        console.error('保存日程失败:', error)
      }
    }

    const closeModal = () => {
      showAddModal.value = false
      showEditModal.value = false
      formData.value = {
        title: '',
        description: '',
        startTime: '',
        endTime: '',
        type: 'event'
      }
    }

    onMounted(loadEvents)

    return {
      events,
      currentDate,
      currentYear,
      currentMonth,
      calendarDays,
      showAddModal,
      showEditModal,
      formData,
      hasEvent,
      formatTime,
      prevMonth,
      nextMonth,
      selectDate,
      editEvent,
      deleteEvent,
      saveEvent,
      closeModal
    }
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
.section-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.events-section h4 { font-size: 16px; font-weight: 600; color: #1e293b; margin: 0; }
.btn-add { padding: 8px 16px; border: none; border-radius: 8px; background: linear-gradient(135deg, #0ea5e9, #0284c7); color: #fff; font-size: 13px; cursor: pointer; }

.event-list { max-height: 300px; overflow-y: auto; }
.event-item { display: flex; gap: 16px; padding: 12px 0; border-bottom: 1px solid #f1f5f9; cursor: pointer; }
.event-item:hover { background: #f8fafc; }
.event-item:last-child { border-bottom: none; }
.event-time { font-size: 13px; font-weight: 600; color: #0ea5e9; width: 60px; }
.event-content { flex: 1; }
.event-content h5 { font-size: 14px; font-weight: 600; color: #1e293b; margin: 0 0 4px 0; }
.event-content p { font-size: 12px; color: #64748b; margin: 0; }
.event-actions { opacity: 0; transition: opacity 0.2s; }
.event-item:hover .event-actions { opacity: 1; }
.btn-delete { padding: 4px 8px; border: none; border-radius: 4px; background: #fee2e2; color: #dc2626; font-size: 11px; cursor: pointer; }

.modal-overlay { position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0,0,0,0.5); display: flex; align-items: center; justify-content: center; z-index: 1000; }
.modal { background: #fff; border-radius: 16px; padding: 24px; width: 500px; max-width: 90%; max-height: 85vh; overflow-y: auto; }
.modal h3 { margin: 0 0 20px 0; font-size: 18px; color: #1e293b; }
.form-group { margin-bottom: 16px; }
.form-group label { display: block; font-size: 13px; color: #64748b; margin-bottom: 6px; }
.form-group input, .form-group select { width: 100%; box-sizing: border-box; padding: 10px 12px; border: 2px solid #e2e8f0; border-radius: 8px; font-size: 14px; outline: none; }
.form-group input:focus, .form-group select:focus { border-color: #0ea5e9; }
.modal-actions { display: flex; gap: 12px; justify-content: flex-end; margin-top: 20px; }
.btn-cancel { padding: 10px 20px; border: none; border-radius: 8px; background: #f1f5f9; color: #475569; font-size: 14px; cursor: pointer; }
.btn-confirm { padding: 10px 20px; border: none; border-radius: 8px; background: linear-gradient(135deg, #0ea5e9, #0284c7); color: #fff; font-size: 14px; cursor: pointer; }
</style>
