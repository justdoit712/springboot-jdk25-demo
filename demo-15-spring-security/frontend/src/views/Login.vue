<template>
  <div class="login-container">
    <div class="glass-panel">
      <div class="logo">
        <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path d="M12 2L2 7L12 12L22 7L12 2Z" fill="currentColor"/>
          <path d="M2 17L12 22L22 17" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          <path d="M2 12L12 17L22 12" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
      </div>
      <h2>Welcome Back</h2>
      <p class="subtitle">Enter your credentials to access your account</p>
      
      <form @submit.prevent="handleLogin">
        <div class="input-group">
          <label>Username</label>
          <input type="text" v-model="username" placeholder="Enter 'admin'" required autocomplete="off"/>
        </div>
        
        <div class="input-group">
          <label>Password</label>
          <input type="password" v-model="password" placeholder="Enter '123456'" required autocomplete="off"/>
        </div>

        <div v-if="errorMsg" class="error-msg">{{ errorMsg }}</div>
        
        <button type="submit" :disabled="loading" class="login-btn">
          <span v-if="!loading">Sign In</span>
          <div v-else class="spinner"></div>
        </button>
      </form>
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

const handleLogin = async () => {
  loading.value = true
  errorMsg.value = ''
  try {
    const res = await request.post('/auth/login', {
      username: username.value,
      password: password.value
    })
    
    if (res.code === 200 && res.data.token) {
      localStorage.setItem('token', res.data.token)
      router.push('/dashboard')
    } else {
      errorMsg.value = res.message || 'Login failed'
    }
  } catch (err) {
    errorMsg.value = err.response?.data?.message || 'Network error or server is down'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap');

.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #0f172a 0%, #1e1b4b 100%);
  font-family: 'Inter', sans-serif;
  color: #fff;
}

.glass-panel {
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 24px;
  padding: 3rem;
  width: 100%;
  max-width: 420px;
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.5);
  transform: translateY(0);
  transition: all 0.3s ease;
}

.glass-panel:hover {
  transform: translateY(-5px);
  box-shadow: 0 30px 60px -12px rgba(0, 0, 0, 0.6);
  border-color: rgba(255, 255, 255, 0.15);
}

.logo {
  width: 48px;
  height: 48px;
  margin: 0 auto 1.5rem;
  color: #6366f1;
}

h2 {
  text-align: center;
  margin: 0 0 0.5rem;
  font-size: 1.75rem;
  font-weight: 700;
  letter-spacing: -0.025em;
}

.subtitle {
  text-align: center;
  color: #94a3b8;
  font-size: 0.875rem;
  margin-bottom: 2rem;
}

.input-group {
  margin-bottom: 1.5rem;
}

label {
  display: block;
  font-size: 0.875rem;
  font-weight: 500;
  margin-bottom: 0.5rem;
  color: #cbd5e1;
}

input {
  width: 100%;
  padding: 0.75rem 1rem;
  background: rgba(0, 0, 0, 0.2);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  color: #fff;
  font-size: 1rem;
  transition: all 0.2s;
  box-sizing: border-box;
}

input:focus {
  outline: none;
  border-color: #6366f1;
  background: rgba(0, 0, 0, 0.3);
  box-shadow: 0 0 0 4px rgba(99, 102, 241, 0.1);
}

.login-btn {
  width: 100%;
  padding: 0.875rem;
  background: #6366f1;
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 48px;
  margin-top: 1rem;
}

.login-btn:hover:not(:disabled) {
  background: #4f46e5;
  transform: translateY(-2px);
  box-shadow: 0 10px 15px -3px rgba(99, 102, 241, 0.4);
}

.login-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.error-msg {
  color: #ef4444;
  font-size: 0.875rem;
  text-align: center;
  margin-top: 0.5rem;
  background: rgba(239, 68, 68, 0.1);
  padding: 0.5rem;
  border-radius: 8px;
  border: 1px solid rgba(239, 68, 68, 0.2);
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
</style>
