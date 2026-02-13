<template>
  <teleport to="body">
    <transition name="toast-fade">
      <div v-if="visible" class="toast-overlay" @click="hide">
        <div class="toast-container" :class="type" @click.stop>
          <div class="toast-icon">{{ icon }}</div>
          <div class="toast-content">
            <div class="toast-title">{{ title }}</div>
            <div class="toast-message">{{ message }}</div>
          </div>
          <button class="toast-close" @click="hide">×</button>
        </div>
      </div>
    </transition>
  </teleport>
</template>

<script>
import { ref, computed } from 'vue'

const visible = ref(false)
const message = ref('')
const type = ref('info')
const title = ref('')
const duration = ref(3000)
let timer = null

const iconMap = {
  success: '✓',
  error: '✕',
  warning: '⚠',
  info: 'ℹ'
}

const titleMap = {
  success: '成功',
  error: '错误',
  warning: '警告',
  info: '提示'
}

export const showToast = (options) => {
  if (typeof options === 'string') {
    options = { message: options }
  }
  
  message.value = options.message || ''
  type.value = options.type || 'info'
  title.value = options.title || titleMap[type.value] || '提示'
  duration.value = options.duration || 3000
  visible.value = true
  
  if (timer) clearTimeout(timer)
  timer = setTimeout(() => {
    visible.value = false
  }, duration.value)
}

export default {
  name: 'Toast',
  setup() {
    const icon = computed(() => iconMap[type.value] || iconMap.info)
    
    const hide = () => {
      visible.value = false
      if (timer) clearTimeout(timer)
    }
    
    return {
      visible,
      message,
      type,
      title,
      icon,
      hide
    }
  }
}
</script>

<style scoped>
.toast-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.4);
  backdrop-filter: blur(4px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}

.toast-container {
  background: white;
  border-radius: 16px;
  padding: 24px 32px;
  min-width: 320px;
  max-width: 480px;
  display: flex;
  align-items: flex-start;
  gap: 16px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  border-left: 4px solid;
  animation: toastSlideIn 0.3s ease;
}

.toast-container.success {
  border-left-color: #22c55e;
}

.toast-container.error {
  border-left-color: #ef4444;
}

.toast-container.warning {
  border-left-color: #f59e0b;
}

.toast-container.info {
  border-left-color: #0ea5e9;
}

.toast-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  flex-shrink: 0;
}

.toast-container.success .toast-icon {
  background: #dcfce7;
  color: #16a34a;
}

.toast-container.error .toast-icon {
  background: #fee2e2;
  color: #dc2626;
}

.toast-container.warning .toast-icon {
  background: #fef3c7;
  color: #d97706;
}

.toast-container.info .toast-icon {
  background: #e0f2fe;
  color: #0284c7;
}

.toast-content {
  flex: 1;
}

.toast-title {
  font-size: 16px;
  font-weight: 600;
  color: #1e293b;
  margin-bottom: 4px;
}

.toast-message {
  font-size: 14px;
  color: #64748b;
  line-height: 1.5;
}

.toast-close {
  width: 28px;
  height: 28px;
  border: none;
  background: #f1f5f9;
  border-radius: 8px;
  cursor: pointer;
  font-size: 18px;
  color: #64748b;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s;
}

.toast-close:hover {
  background: #e2e8f0;
  color: #475569;
}

@keyframes toastSlideIn {
  from {
    opacity: 0;
    transform: translateY(-20px) scale(0.95);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

.toast-fade-enter-active,
.toast-fade-leave-active {
  transition: all 0.3s ease;
}

.toast-fade-enter-from,
.toast-fade-leave-to {
  opacity: 0;
}

.toast-fade-enter-from .toast-container,
.toast-fade-leave-to .toast-container {
  transform: translateY(-20px) scale(0.95);
}
</style>