<template>
  <teleport to="body">
    <transition name="confirm-fade">
      <div v-if="visible" class="confirm-overlay" @click="handleCancel">
        <div class="confirm-container" @click.stop>
          <div class="confirm-header">
            <div class="confirm-icon" :class="type">
              {{ icon }}
            </div>
            <h3 class="confirm-title">{{ title }}</h3>
          </div>
          <div class="confirm-content">
            <p class="confirm-message">{{ message }}</p>
          </div>
          <div class="confirm-actions">
            <button 
              v-if="showCancel" 
              class="confirm-btn cancel" 
              @click="handleCancel"
            >
              {{ cancelText }}
            </button>
            <button 
              class="confirm-btn confirm" 
              :class="type"
              @click="handleConfirm"
            >
              {{ confirmText }}
            </button>
          </div>
        </div>
      </div>
    </transition>
  </teleport>
</template>

<script>
import { ref, computed } from 'vue'

const visible = ref(false)
const title = ref('确认')
const message = ref('')
const type = ref('warning')
const confirmText = ref('确定')
const cancelText = ref('取消')
const showCancel = ref(true)
let resolvePromise = null

const iconMap = {
  warning: '⚠',
  danger: '✕',
  info: 'ℹ',
  success: '✓'
}

export const showConfirm = (options) => {
  if (typeof options === 'string') {
    options = { message: options }
  }
  
  title.value = options.title || '确认'
  message.value = options.message || ''
  type.value = options.type || 'warning'
  confirmText.value = options.confirmText || '确定'
  cancelText.value = options.cancelText || '取消'
  showCancel.value = options.showCancel !== false
  visible.value = true
  
  return new Promise((resolve) => {
    resolvePromise = resolve
  })
}

export default {
  name: 'ConfirmDialog',
  setup() {
    const icon = computed(() => iconMap[type.value] || iconMap.warning)
    
    const handleConfirm = () => {
      visible.value = false
      if (resolvePromise) {
        resolvePromise(true)
        resolvePromise = null
      }
    }
    
    const handleCancel = () => {
      visible.value = false
      if (resolvePromise) {
        resolvePromise(false)
        resolvePromise = null
      }
    }
    
    return {
      visible,
      title,
      message,
      type,
      confirmText,
      cancelText,
      showCancel,
      icon,
      handleConfirm,
      handleCancel
    }
  }
}
</script>

<style scoped>
.confirm-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(4px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 10000;
}

.confirm-container {
  background: white;
  border-radius: 16px;
  padding: 28px 32px;
  min-width: 360px;
  max-width: 480px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  animation: confirmSlideIn 0.3s ease;
}

@keyframes confirmSlideIn {
  from {
    opacity: 0;
    transform: translateY(-30px) scale(0.95);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

.confirm-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

.confirm-icon {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 22px;
  flex-shrink: 0;
}

.confirm-icon.warning {
  background: #fef3c7;
  color: #d97706;
}

.confirm-icon.danger {
  background: #fee2e2;
  color: #dc2626;
}

.confirm-icon.info {
  background: #e0f2fe;
  color: #0284c7;
}

.confirm-icon.success {
  background: #dcfce7;
  color: #16a34a;
}

.confirm-title {
  font-size: 18px;
  font-weight: 600;
  color: #1e293b;
  margin: 0;
}

.confirm-content {
  margin-bottom: 24px;
  padding-left: 56px;
}

.confirm-message {
  font-size: 15px;
  color: #64748b;
  line-height: 1.6;
  margin: 0;
}

.confirm-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding-left: 56px;
}

.confirm-btn {
  padding: 10px 20px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
  border: none;
}

.confirm-btn.cancel {
  background: #f1f5f9;
  color: #64748b;
}

.confirm-btn.cancel:hover {
  background: #e2e8f0;
}

.confirm-btn.confirm {
  color: white;
}

.confirm-btn.confirm.warning {
  background: linear-gradient(135deg, #f59e0b, #d97706);
}

.confirm-btn.confirm.danger {
  background: linear-gradient(135deg, #ef4444, #dc2626);
}

.confirm-btn.confirm.info {
  background: linear-gradient(135deg, #0ea5e9, #0284c7);
}

.confirm-btn.confirm.success {
  background: linear-gradient(135deg, #22c55e, #16a34a);
}

.confirm-btn.confirm:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.confirm-fade-enter-active,
.confirm-fade-leave-active {
  transition: all 0.3s ease;
}

.confirm-fade-enter-from,
.confirm-fade-leave-to {
  opacity: 0;
}

.confirm-fade-enter-from .confirm-container,
.confirm-fade-leave-to .confirm-container {
  transform: translateY(-30px) scale(0.95);
}
</style>