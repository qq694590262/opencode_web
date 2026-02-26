<template>
  <div class="page-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <h2 class="page-title">
          <el-icon><BellFilled /></el-icon>
          系统公告
        </h2>
        <p class="page-desc">查看系统公告和通知</p>
      </div>
    </div>

    <!-- 公告列表 -->
    <div class="notices-list" v-loading="loading">
      <el-card 
        v-for="notice in notices" 
        :key="notice.id" 
        class="notice-card"
        shadow="hover"
        @click="viewNotice(notice)"
      >
        <div class="notice-header">
          <div class="notice-title">
            <el-tag :type="getType(notice.type)" size="small">{{ getTypeText(notice.type) }}</el-tag>
            <span class="title">{{ notice.title }}</span>
          </div>
          <span class="notice-time">{{ formatTime(notice.createTime) }}</span>
        </div>
        <p class="notice-content">{{ notice.content }}</p>
        <div class="notice-footer">
          <span class="publisher">
            <el-icon><User /></el-icon>
            {{ notice.publisher }}
          </span>
          <span class="views">
            <el-icon><View /></el-icon>
            {{ notice.views }} 阅读
          </span>
        </div>
      </el-card>

      <!-- 空状态 -->
      <el-empty v-if="!loading && notices.length === 0" description="暂无公告" />
    </div>

    <!-- 公告详情弹窗 -->
    <el-dialog 
      v-model="detailVisible" 
      :title="currentNotice?.title"
      width="600px"
    >
      <div class="notice-detail" v-if="currentNotice">
        <div class="detail-header">
          <el-tag :type="getType(currentNotice.type)" size="small">{{ getTypeText(currentNotice.type) }}</el-tag>
          <span class="time">{{ formatTime(currentNotice.createTime) }}</span>
        </div>
        <div class="detail-content">{{ currentNotice.content }}</div>
        <div class="detail-footer">
          <span>
            <el-icon><User /></el-icon>
            发布人：{{ currentNotice.publisher }}
          </span>
          <span>
            <el-icon><View /></el-icon>
            阅读量：{{ currentNotice.views }}
          </span>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { BellFilled, User, View } from '@element-plus/icons-vue'
import { dashboardApi } from '../api'
import { getUserInfo } from '../auth'

export default {
  name: 'Notices',
  components: { BellFilled, User, View },
  setup() {
    const loading = ref(false)
    const notices = ref([])
    const detailVisible = ref(false)
    const currentNotice = ref(null)
    
    const getCurrentUserId = () => {
      const user = getUserInfo()
      return user?.id || 1
    }
    
    const getType = (type) => {
      const types = { warning: 'warning', success: 'success', danger: 'danger', info: 'info' }
      return types[type] || 'info'
    }
    
    const getTypeText = (type) => {
      const texts = { warning: '重要', success: '新功能', danger: '紧急', info: '通知' }
      return texts[type] || '通知'
    }
    
    const formatTime = (time) => {
      if (!time) return ''
      const date = new Date(time)
      return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
    }
    
    const loadNotices = async () => {
      loading.value = true
      try {
        const userId = getCurrentUserId()
        const res = await dashboardApi.getNotices(userId)
        if (res.code === 200) {
          notices.value = res.data || []
        }
      } catch (error) {
        console.error('获取公告失败:', error)
      } finally {
        loading.value = false
      }
    }
    
    const viewNotice = async (notice) => {
      currentNotice.value = notice
      // 标记为已读
      try {
        await dashboardApi.markNoticeAsRead(notice.id)
        notice.views++
      } catch (error) {
        console.error('标记已读失败:', error)
      }
      detailVisible.value = true
    }
    
    onMounted(loadNotices)
    
    return {
      loading,
      notices,
      detailVisible,
      currentNotice,
      getType,
      getTypeText,
      formatTime,
      viewNotice
    }
  }
}
</script>

<style scoped>
.page-container {
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

.page-header {
  margin-bottom: 24px;
}

.header-content {
  display: flex;
  flex-direction: column;
}

.page-title {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 22px;
  font-weight: 600;
  color: #303133;
  margin: 0;
}

.page-title .el-icon {
  color: #E6A23C;
}

.page-desc {
  font-size: 14px;
  color: #909399;
  margin: 6px 0 0 0;
}

.notices-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.notice-card {
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.notice-card:hover {
  transform: translateY(-2px);
}

.notice-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.notice-title {
  display: flex;
  align-items: center;
  gap: 10px;
}

.title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.notice-time {
  font-size: 13px;
  color: #909399;
}

.notice-content {
  font-size: 14px;
  color: #606266;
  line-height: 1.6;
  margin: 0 0 12px 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.notice-footer {
  display: flex;
  gap: 20px;
  font-size: 13px;
  color: #909399;
}

.notice-footer span {
  display: flex;
  align-items: center;
  gap: 4px;
}

/* 详情弹窗 */
.notice-detail .detail-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 20px;
  padding-bottom: 12px;
  border-bottom: 1px solid #ebeef5;
}

.notice-detail .time {
  font-size: 14px;
  color: #909399;
}

.notice-detail .detail-content {
  font-size: 15px;
  color: #303133;
  line-height: 1.8;
  margin-bottom: 20px;
}

.notice-detail .detail-footer {
  display: flex;
  justify-content: space-between;
  padding-top: 16px;
  border-top: 1px solid #ebeef5;
  font-size: 14px;
  color: #909399;
}

.notice-detail .detail-footer span {
  display: flex;
  align-items: center;
  gap: 6px;
}
</style>
