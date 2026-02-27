<template>
  <div class="page-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <h2 class="page-title">
          <el-icon><Message /></el-icon>
          站内消息
        </h2>
        <p class="page-desc">个人消息中心</p>
      </div>
      <el-button type="primary" text @click="markAllRead">
        <el-icon><Check /></el-icon>
        全部标为已读
      </el-button>
    </div>

    <!-- 标签页 -->
    <el-tabs v-model="activeTab" class="message-tabs">
      <el-tab-pane label="全部消息" name="all">
        <template #label>
          <span class="tab-label">
            <el-icon><Message /></el-icon>
            全部消息
            <el-badge :value="unreadCount" v-if="unreadCount > 0" />
          </span>
        </template>
      </el-tab-pane>
      <el-tab-pane label="未读消息" name="unread">
        <template #label>
          <span class="tab-label">
            <el-icon><Bell /></el-icon>
            未读消息
            <el-badge :value="unreadCount" v-if="unreadCount > 0" />
          </span>
        </template>
      </el-tab-pane>
      <el-tab-pane label="已读消息" name="read">
        <template #label>
          <span class="tab-label">
            <el-icon><CircleCheck /></el-icon>
            已读消息
          </span>
        </template>
      </el-tab-pane>
    </el-tabs>

    <!-- 消息列表 -->
    <div class="message-list" v-loading="loading">
      <div 
        v-for="msg in filteredMessages" 
        :key="msg.id" 
        class="message-item"
        :class="{ unread: !msg.read }"
        @click="viewMessage(msg)"
      >
        <div class="message-icon">
          <el-avatar :size="44" :style="getIconStyle(msg.type)">
            <el-icon><component :is="getIcon(msg.type)" /></el-icon>
          </el-avatar>
        </div>
        <div class="message-content">
          <div class="message-header">
            <span class="sender">{{ msg.sender }}</span>
            <span class="time">{{ msg.createTime }}</span>
          </div>
          <div class="message-title">{{ msg.title }}</div>
          <div class="message-text">{{ msg.content }}</div>
        </div>
        <div class="message-status" v-if="!msg.read">
          <el-badge is-dot />
        </div>
      </div>

      <!-- 空状态 -->
      <el-empty v-if="!loading && filteredMessages.length === 0" description="暂无消息" />
    </div>

    <!-- 消息详情弹窗 -->
    <el-dialog 
      v-model="detailVisible" 
      :title="currentMessage?.title"
      width="550px"
    >
      <div class="message-detail" v-if="currentMessage">
        <div class="detail-header">
          <el-avatar :size="40" :style="getIconStyle(currentMessage.type)">
            <el-icon><component :is="getIcon(currentMessage.type)" /></el-icon>
          </el-avatar>
          <div class="sender-info">
            <span class="sender">{{ currentMessage.sender }}</span>
            <span class="time">{{ currentMessage.createTime }}</span>
          </div>
        </div>
        <div class="detail-content">{{ currentMessage.content }}</div>
      </div>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
        <el-button type="primary" @click="replyMessage" v-if="currentMessage?.type === 'task'">
          <el-icon><Position /></el-icon>
          立即处理
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  Message, Bell, CircleCheck, Check, User, Position,
  Document, BellFilled, ChatDotRound, Warning, SuccessFilled
} from '@element-plus/icons-vue'

export default {
  name: 'Messages',
  setup() {
    const loading = ref(false)
    const messages = ref([])
    const activeTab = ref('all')
    const detailVisible = ref(false)
    const currentMessage = ref(null)
    
    const mockMessages = [
      {
        id: 1,
        title: '新任务通知',
        content: '您有一个新的任务分配：请在本周完成用户管理模块的开发工作，详细需求请查看任务详情。',
        sender: '系统管理员',
        type: 'task',
        read: false,
        createTime: '2024-01-12 14:30'
      },
      {
        id: 2,
        title: '项目进度更新',
        content: '项目"企业管理系统"已进入测试阶段，请测试人员及时开展测试工作。',
        sender: '项目经理',
        type: 'project',
        read: false,
        createTime: '2024-01-12 10:15'
      },
      {
        id: 3,
        title: '系统公告',
        content: '系统将于今晚22:00进行例行维护，预计持续30分钟，请提前保存工作内容。',
        sender: '系统通知',
        type: 'notice',
        read: false,
        createTime: '2024-01-11 16:00'
      },
      {
        id: 4,
        title: '待办事项提醒',
        content: '您有3条待办事项即将到期，请及时处理。',
        sender: '待办中心',
        type: 'todo',
        read: false,
        createTime: '2024-01-11 09:00'
      },
      {
        id: 5,
        title: '审批通过通知',
        content: '您的请假申请已通过审批，请知悉。',
        sender: '审批系统',
        type: 'approve',
        read: true,
        createTime: '2024-01-10 15:30'
      },
      {
        id: 6,
        title: '文件共享通知',
        content: '张三分享了一个文件给您：项目需求文档V2.0.docx',
        sender: '张三',
        type: 'share',
        read: true,
        createTime: '2024-01-10 11:20'
      }
    ]
    
    const unreadCount = computed(() => messages.value.filter(m => !m.read).length)
    
    const filteredMessages = computed(() => {
      if (activeTab.value === 'unread') {
        return messages.value.filter(m => !m.read)
      } else if (activeTab.value === 'read') {
        return messages.value.filter(m => m.read)
      }
      return messages.value
    })
    
    const getIcon = (type) => {
      const icons = { 
        task: 'Document', 
        project: 'Folder', 
        notice: 'BellFilled',
        todo: 'Check',
        approve: 'SuccessFilled',
        share: 'ChatDotRound'
      }
      return icons[type] || 'Message'
    }
    
    const getIconStyle = (type) => {
      const styles = {
        task: { backgroundColor: '#409EFF', color: '#fff' },
        project: { backgroundColor: '#67C23A', color: '#fff' },
        notice: { backgroundColor: '#E6A23C', color: '#fff' },
        todo: { backgroundColor: '#909399', color: '#fff' },
        approve: { backgroundColor: '#67C23A', color: '#fff' },
        share: { backgroundColor: '#409EFF', color: '#fff' }
      }
      return styles[type] || { backgroundColor: '#909399', color: '#fff' }
    }
    
    const loadMessages = async () => {
      loading.value = true
      try {
        await new Promise(resolve => setTimeout(resolve, 300))
        messages.value = mockMessages
      } catch (error) {
        console.error('获取消息失败:', error)
      } finally {
        loading.value = false
      }
    }
    
    const viewMessage = (msg) => {
      currentMessage.value = msg
      if (!msg.read) {
        msg.read = true
      }
      detailVisible.value = true
    }
    
    const markAllRead = async () => {
      try {
        await ElMessageBox.confirm('确定将所有消息标为已读吗？', '确认', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'info'
        })
        messages.value.forEach(m => m.read = true)
        ElMessage.success('操作成功')
      } catch {}
    }
    
    const replyMessage = () => {
      ElMessage.info('跳转到任务处理页面')
      detailVisible.value = false
    }
    
    onMounted(loadMessages)
    
    return {
      loading,
      messages,
      activeTab,
      unreadCount,
      filteredMessages,
      detailVisible,
      currentMessage,
      getIcon,
      getIconStyle,
      viewMessage,
      markAllRead,
      replyMessage,
      Message, Bell, CircleCheck, Check, User, Position,
      Document, BellFilled, ChatDotRound, Warning, SuccessFilled
    }
  }
}
</script>

<style scoped>
/* Minimal overrides - most styles now in global.css */
.message-tabs { margin-bottom: 20px; }
.tab-label { display: flex; align-items: center; gap: 6px; }
.message-list { display: flex; flex-direction: column; gap: 12px; }
.message-item { display: flex; align-items: flex-start; gap: 14px; padding: 16px; background: #fff; border-radius: 12px; cursor: pointer; transition: all 0.2s ease; position: relative; }
.message-item:hover { background: #f5f7fa; }
.message-item.unread { background: #f0f9ff; }
.message-item.unread::before { content: ''; position: absolute; left: 0; top: 50%; transform: translateY(-50%); width: 4px; height: 60%; background: #409EFF; border-radius: 0 4px 4px 0; }
.message-icon { flex-shrink: 0; }
.message-content { flex: 1; min-width: 0; }
.message-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 6px; }
.sender { font-size: 14px; font-weight: 600; color: #303133; }
.time { font-size: 12px; color: #909399; }
.message-title { font-size: 15px; font-weight: 500; color: #303133; margin-bottom: 4px; }
.message-text { font-size: 13px; color: #606266; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.message-status { flex-shrink: 0; }
.message-detail .detail-header { display: flex; align-items: center; gap: 14px; margin-bottom: 20px; padding-bottom: 16px; border-bottom: 1px solid #ebeef5; }
.sender-info { display: flex; flex-direction: column; }
.sender-info .sender { font-size: 15px; }
.sender-info .time { font-size: 13px; }
.message-detail .detail-content { font-size: 15px; color: #303133; line-height: 1.8; }
</style>
