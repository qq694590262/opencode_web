<template>
  <div class="login-wrap">
    <div class="login-left">
      <div class="image-stack">
        <img src="/images/login/2.png" alt="img2" class="stack-img img-1" />
        <img src="/images/login/3.png" alt="img3" class="stack-img img-2" />
        <img src="/images/login/login.png" alt="login" class="stack-img img-3" />
      </div>
    </div>
    <div class="login-right">
      <div class="login-card">
        <div class="login-header">
          <h1>欢迎回来</h1>
          <p>登录您的账户</p>
        </div>
        <form class="login-form" @submit.prevent="doLogin">
          <div class="field">
            <label>用户名</label>
            <div class="input-wrap">
              <span class="input-icon">👤</span>
              <input v-model="username" placeholder="请输入用户名" required />
            </div>
          </div>
          <div class="field">
            <label>密码</label>
            <div class="input-wrap">
              <span class="input-icon">🔒</span>
              <input type="password" v-model="password" placeholder="请输入密码" required />
            </div>
          </div>
          <div class="options">
            <label class="remember">
              <input type="checkbox" />
              <span>记住我</span>
            </label>
            <a href="#" class="forgot">忘记密码？</a>
          </div>
          <button type="submit" class="login-btn" :disabled="loading">{{ loading ? '登录中...' : '登 录' }}</button>
          <div class="error" v-if="error">{{ error }}</div>
        </form>
        <div class="login-footer">
          <span>还没有账号？</span>
          <a href="#">立即注册</a>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue'
import { login as authLogin } from '../auth'
import { useRouter } from 'vue-router'
export default {
  name: 'Login',
  setup() {
    const username = ref('admin')
    const password = ref('password')
    const error = ref(null)
    const loading = ref(false)
    const router = useRouter()
    async function doLogin(){
      loading.value = true
      error.value = null
      const success = await authLogin(username.value, password.value)
      loading.value = false
      if (success) {
        router.push('/dashboard')
      } else {
        error.value = '用户名或密码错误'
      }
    }
    return { username, password, error, loading, doLogin }
  }
}
</script>

<style scoped>
.login-wrap {
  display: flex;
  height: 100vh;
  background: linear-gradient(135deg, #e0f2fe 0%, #bae6fd 50%, #7dd3fc 100%);
}

.login-left {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(180deg, #0ea5e9 0%, #0284c7 100%);
  padding: 40px;
  position: relative;
  overflow: hidden;
}

.image-stack {
  position: relative;
  width: 400px;
  height: 400px;
}

.stack-img {
  position: absolute;
  border-radius: 16px;
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
  transition: transform 0.3s ease;
}

.img-1 {
  width: 180px;
  top: 0;
  left: 0;
  transform: rotate(-8deg);
  z-index: 1;
}

.img-2 {
  width: 180px;
  top: 20px;
  right: 0;
  transform: rotate(8deg);
  z-index: 2;
}

.img-3 {
  width: 220px;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%) rotate(-2deg);
  z-index: 3;
  border: 4px solid rgba(255,255,255,0.3);
}

.stack-img:hover {
  transform: scale(1.05) rotate(0deg) !important;
  z-index: 10;
}

.login-right {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
}

.login-card {
  width: 100%;
  max-width: 420px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 24px;
  padding: 48px 40px;
  box-shadow: 0 20px 40px rgba(14, 165, 233, 0.15);
}

.login-header {
  text-align: center;
  margin-bottom: 36px;
}

.login-header h1 {
  font-size: 28px;
  font-weight: 700;
  color: #0c4a6e;
  margin: 0 0 8px 0;
}

.login-header p {
  font-size: 14px;
  color: #64748b;
  margin: 0;
}

.field {
  margin-bottom: 24px;
}

.field label {
  display: block;
  font-size: 13px;
  font-weight: 600;
  color: #334155;
  margin-bottom: 8px;
}

.input-wrap {
  position: relative;
  display: flex;
  align-items: center;
}

.input-icon {
  position: absolute;
  left: 14px;
  font-size: 16px;
  z-index: 1;
}

.field input {
  width: 100%;
  padding: 14px 14px 14px 44px;
  border-radius: 12px;
  border: 2px solid #e2e8f0;
  background: #f8fafc;
  font-size: 14px;
  color: #1e293b;
  transition: all 0.3s ease;
}

.field input:focus {
  outline: none;
  border-color: #0ea5e9;
  background: #fff;
  box-shadow: 0 0 0 4px rgba(14, 165, 233, 0.15);
}

.field input::placeholder {
  color: #94a3b8;
}

.options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 28px;
}

.remember {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  color: #64748b;
  cursor: pointer;
}

.remember input {
  width: 16px;
  height: 16px;
  accent-color: #0ea5e9;
}

.forgot {
  font-size: 13px;
  color: #0ea5e9;
  text-decoration: none;
  font-weight: 500;
}

.forgot:hover {
  text-decoration: underline;
}

.login-btn {
  width: 100%;
  padding: 16px;
  border-radius: 12px;
  border: none;
  background: linear-gradient(135deg, #0ea5e9 0%, #0284c7 100%);
  color: #fff;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 14px rgba(14, 165, 233, 0.4);
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(14, 165, 233, 0.5);
}

.login-btn:active {
  transform: translateY(0);
}

.error {
  color: #ef4444;
  font-size: 13px;
  text-align: center;
  margin-top: 16px;
  padding: 10px;
  background: #fef2f2;
  border-radius: 8px;
}

.login-footer {
  text-align: center;
  margin-top: 28px;
  padding-top: 24px;
  border-top: 1px solid #e2e8f0;
  font-size: 14px;
  color: #64748b;
}

.login-footer a {
  color: #0ea5e9;
  text-decoration: none;
  font-weight: 600;
  margin-left: 6px;
}

.login-footer a:hover {
  text-decoration: underline;
}

@media (max-width: 768px) {
  .login-wrap {
    flex-direction: column;
  }
  
  .login-left {
    flex: 0;
    padding: 30px;
  }
  
  .image-stack {
    width: 280px;
    height: 280px;
  }
  
  .img-1, .img-2 {
    width: 120px;
  }
  
  .img-3 {
    width: 160px;
  }
  
  .login-right {
    flex: 1;
    padding: 24px;
  }
  
  .login-card {
    padding: 32px 24px;
  }
}
</style>
