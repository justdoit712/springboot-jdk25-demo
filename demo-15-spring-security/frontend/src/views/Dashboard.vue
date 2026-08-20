<template>
  <div class="dashboard-container">
    <nav class="navbar">
      <div class="nav-content">
        <div class="brand">
          <span class="logo-icon">✨</span>
          <span class="brand-name">Security Demo</span>
        </div>
        <button @click="logout" class="logout-btn">
          Log Out
        </button>
      </div>
    </nav>

    <main class="main-content">
      <div class="welcome-card" v-if="userInfo">
        <div class="avatar-section">
          <img :src="userInfo.avatar" alt="Avatar" class="avatar" />
        </div>
        <div class="info-section">
          <h1>Welcome, {{ userInfo.username }}!</h1>
          <p class="role-badge" v-for="role in userInfo.roles" :key="role">{{ role }}</p>
          <p class="status">You have successfully authenticated via JWT.</p>
        </div>
      </div>
      <div v-else-if="loading" class="loading-state">
        <div class="spinner"></div>
        <p>Loading secure data...</p>
      </div>
      <div v-else class="error-state">
        <p>Failed to load user info. Token might be invalid.</p>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '../utils/request'

const userInfo = ref(null)
const loading = ref(true)
const router = useRouter()

const fetchUserInfo = async () => {
  try {
    const res = await request.get('/user/info')
    if (res.code === 200) {
      userInfo.value = res.data
    }
  } catch (error) {
    console.error('Error fetching user info:', error)
  } finally {
    loading.value = false
  }
}

const logout = () => {
  localStorage.removeItem('token')
  router.push('/login')
}

onMounted(() => {
  fetchUserInfo()
})
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap');

.dashboard-container {
  min-height: 100vh;
  background: #f8fafc;
  font-family: 'Inter', sans-serif;
}

.navbar {
  background: white;
  border-bottom: 1px solid #e2e8f0;
  padding: 1rem 2rem;
  position: sticky;
  top: 0;
  z-index: 10;
}

.nav-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.brand {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.logo-icon {
  font-size: 1.5rem;
}

.brand-name {
  font-size: 1.25rem;
  font-weight: 700;
  color: #0f172a;
  letter-spacing: -0.025em;
}

.logout-btn {
  background: white;
  border: 1px solid #e2e8f0;
  color: #64748b;
  padding: 0.5rem 1rem;
  border-radius: 8px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.logout-btn:hover {
  background: #f1f5f9;
  color: #0f172a;
}

.main-content {
  max-width: 800px;
  margin: 4rem auto;
  padding: 0 2rem;
}

.welcome-card {
  background: white;
  border-radius: 24px;
  padding: 3rem;
  box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.05), 0 8px 10px -6px rgba(0, 0, 0, 0.01);
  display: flex;
  gap: 2.5rem;
  align-items: center;
  border: 1px solid #f1f5f9;
}

.avatar-section {
  flex-shrink: 0;
}

.avatar {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  background: #f1f5f9;
  border: 4px solid white;
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
}

.info-section h1 {
  margin: 0 0 0.5rem;
  color: #0f172a;
  font-size: 2rem;
  font-weight: 700;
  letter-spacing: -0.025em;
}

.role-badge {
  display: inline-block;
  background: #eef2ff;
  color: #4f46e5;
  padding: 0.25rem 0.75rem;
  border-radius: 9999px;
  font-size: 0.875rem;
  font-weight: 600;
  margin-bottom: 1rem;
}

.status {
  color: #64748b;
  font-size: 1.125rem;
  line-height: 1.5;
}

.loading-state, .error-state {
  text-align: center;
  padding: 4rem;
  color: #64748b;
  background: white;
  border-radius: 24px;
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.05);
}

.spinner {
  width: 40px;
  height: 40px;
  border: 3px solid #f1f5f9;
  border-radius: 50%;
  border-top-color: #6366f1;
  animation: spin 1s linear infinite;
  margin: 0 auto 1.5rem;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}
</style>
