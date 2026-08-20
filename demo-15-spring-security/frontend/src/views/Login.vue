<template>
  <div class="login-wrapper">
    <div class="glow-bg"></div>
    <div class="glass-panel">
      <!-- Brand / Logo -->
      <div class="logo-box">
        <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path d="M12 2L3 7V12C3 17.52 6.84 22.74 12 24C17.16 22.74 21 17.52 21 12V7L12 2Z" fill="url(#login-shield-grad)"/>
          <path d="M9 12L11 14L15 10" stroke="#fff" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          <defs>
            <linearGradient id="login-shield-grad" x1="3" y1="2" x2="21" y2="24" gradientUnits="userSpaceOnUse">
              <stop stop-color="#6366f1"/>
              <stop offset="1" stop-color="#a855f7"/>
            </linearGradient>
          </defs>
        </svg>
      </div>

      <div class="header-text">
        <h2>Spring Security 6</h2>
        <p class="subtitle">Stateless JWT Authentication Demo (JDK 25)</p>
      </div>

      <!-- Quick Fill Credentials Badge -->
      <div class="preset-banner" @click="fillAdminCredentials" title="Click to fill test credentials">
        <span class="preset-icon">💡</span>
        <span class="preset-text">Click to fill default account: <strong>admin / 123456</strong></span>
      </div>

      <form @submit.prevent="handleLogin" class="login-form">
        <div class="input-group">
          <label>Username</label>
          <div class="input-wrapper">
            <input 
              type="text" 
              v-model="username" 
              placeholder="Username (admin)" 
              required 
              autocomplete="off"
            />
          </div>
        </div>

        <div class="input-group">
          <label>Password</label>
          <div class="input-wrapper">
            <input 
              type="password" 
              v-model="password" 
              placeholder="Password (123456)" 
              required 
              autocomplete="off"
            />
          </div>
        </div>

        <div v-if="errorMsg" class="error-msg">
          <span>⚠ {{ errorMsg }}</span>
        </div>

        <button type="submit" :disabled="loading" class="login-btn">
          <span v-if="!loading">Sign In to Console →</span>
          <div v-else class="spinner"></div>
        </button>
      </form>

      <div class="footer-note">
        <span>Protected by Spring Security 6 <code>SecurityFilterChain</code></span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import request from '../utils/request'

const username = ref('admin')
const password = ref('123456')
const errorMsg = ref('')
const loading = ref(false)
const router = useRouter()

const fillAdminCredentials = () => {
  username.value = 'admin'
  password.value = '123456'
  errorMsg.value = ''
}

const handleLogin = async () => {
  loading.value = true
  errorMsg.value = ''
  try {
    const res = await request.post('/auth/login', {
      username: username.value,
      password: password.value
    })

    if (res.code === 200 && res.data?.token) {
      localStorage.setItem('token', res.data.token)
      router.push('/dashboard')
    } else {
      errorMsg.value = res.message || 'Authentication failed'
    }
  } catch (err) {
    errorMsg.value = err.response?.data?.message || 'Network error or backend is not reachable (Check port 8080)'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-wrapper {
  width: 100%;
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: radial-gradient(circle at 50% 30%, #1e1b4b 0%, #0f172a 60%, #080c14 100%);
  color: #fff;
  padding: 1.5rem;
  position: relative;
  overflow: hidden;
}

.glow-bg {
  position: absolute;
  width: 500px;
  height: 500px;
  background: radial-gradient(circle, rgba(99, 102, 241, 0.15) 0%, transparent 70%);
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  pointer-events: none;
}

.glass-panel {
  position: relative;
  z-index: 1;
  background: rgba(255, 255, 255, 0.04);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 24px;
  padding: 2.75rem 2.5rem;
  width: 100%;
  max-width: 440px;
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.6);
  transition: all 0.3s ease;
}

.logo-box {
  width: 56px;
  height: 56px;
  margin: 0 auto 1.25rem;
}

.logo-box svg {
  width: 100%;
  height: 100%;
  filter: drop-shadow(0 8px 16px rgba(99, 102, 241, 0.4));
}

.header-text {
  text-align: center;
  margin-bottom: 1.5rem;
}

.header-text h2 {
  font-size: 1.75rem;
  font-weight: 800;
  letter-spacing: -0.025em;
  color: #fff;
  margin-bottom: 0.35rem;
}

.subtitle {
  color: #94a3b8;
  font-size: 0.875rem;
}

.preset-banner {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  background: rgba(99, 102, 241, 0.12);
  border: 1px dashed rgba(99, 102, 241, 0.35);
  padding: 0.6rem 0.85rem;
  border-radius: 12px;
  font-size: 0.8rem;
  color: #c7d2fe;
  margin-bottom: 1.5rem;
  cursor: pointer;
  transition: all 0.2s;
}

.preset-banner:hover {
  background: rgba(99, 102, 241, 0.2);
  border-color: rgba(99, 102, 241, 0.6);
  transform: translateY(-1px);
}

.preset-banner strong {
  color: #fff;
}

.input-group {
  margin-bottom: 1.25rem;
}

label {
  display: block;
  font-size: 0.825rem;
  font-weight: 600;
  margin-bottom: 0.45rem;
  color: #cbd5e1;
  text-transform: uppercase;
  letter-spacing: 0.04em;
}

.input-wrapper input {
  width: 100%;
  padding: 0.8rem 1rem;
  background: rgba(0, 0, 0, 0.3);
  border: 1px solid rgba(255, 255, 255, 0.12);
  border-radius: 12px;
  color: #fff;
  font-size: 0.95rem;
  transition: all 0.2s;
}

.input-wrapper input:focus {
  outline: none;
  border-color: #6366f1;
  background: rgba(0, 0, 0, 0.45);
  box-shadow: 0 0 0 4px rgba(99, 102, 241, 0.15);
}

.login-btn {
  width: 100%;
  padding: 0.9rem;
  background: linear-gradient(135deg, #6366f1 0%, #4f46e5 100%);
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 0.95rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 48px;
  margin-top: 1.5rem;
  box-shadow: 0 10px 20px -5px rgba(99, 102, 241, 0.4);
}

.login-btn:hover:not(:disabled) {
  background: linear-gradient(135deg, #4f46e5 0%, #4338ca 100%);
  transform: translateY(-2px);
  box-shadow: 0 12px 25px -5px rgba(99, 102, 241, 0.5);
}

.login-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.error-msg {
  color: #fca5a5;
  font-size: 0.825rem;
  text-align: center;
  margin-top: 0.75rem;
  background: rgba(239, 68, 68, 0.12);
  padding: 0.6rem 0.8rem;
  border-radius: 10px;
  border: 1px solid rgba(239, 68, 68, 0.25);
}

.spinner {
  width: 20px;
  height: 20px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  border-top-color: #fff;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.footer-note {
  margin-top: 2rem;
  text-align: center;
  font-size: 0.75rem;
  color: #64748b;
}

.footer-note code {
  color: #94a3b8;
  font-family: 'JetBrains Mono', monospace;
}
</style>

