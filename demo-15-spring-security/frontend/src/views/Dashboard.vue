<template>
  <div class="dashboard-wrapper">
    <!-- Navbar -->
    <header class="navbar">
      <div class="nav-container">
        <div class="brand">
          <div class="brand-icon">
            <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M12 2L3 7V12C3 17.52 6.84 22.74 12 24C17.16 22.74 21 17.52 21 12V7L12 2Z" fill="url(#shield-grad)"/>
              <path d="M9 12L11 14L15 10" stroke="#fff" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              <defs>
                <linearGradient id="shield-grad" x1="3" y1="2" x2="21" y2="24" gradientUnits="userSpaceOnUse">
                  <stop stop-color="#6366f1"/>
                  <stop offset="1" stop-color="#a855f7"/>
                </linearGradient>
              </defs>
            </svg>
          </div>
          <div class="brand-text">
            <span class="title">Spring Security 6</span>
            <span class="badge">JDK 25 + JWT</span>
          </div>
        </div>

        <div class="nav-actions">
          <div class="status-indicator">
            <span class="pulse-dot"></span>
            <span class="status-label">JWT Authenticated</span>
          </div>
          <div class="user-chip" v-if="userInfo">
            <img :src="userInfo.avatar" alt="Avatar" class="chip-avatar" />
            <span class="chip-name">{{ userInfo.username }}</span>
          </div>
          <button @click="logout" class="logout-btn" title="Sign out of session">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"></path>
              <polyline points="16 17 21 12 16 7"></polyline>
              <line x1="21" y1="12" x2="9" y2="12"></line>
            </svg>
            <span>Log Out</span>
          </button>
        </div>
      </div>
    </header>

    <!-- Main Content -->
    <main class="main-container">
      <!-- Loading State -->
      <div v-if="loading" class="state-card loading-card">
        <div class="spinner"></div>
        <p>Loading security context and profile...</p>
      </div>

      <div v-else-if="userInfo" class="content-grid">
        <!-- Profile Banner -->
        <section class="card profile-card">
          <div class="avatar-box">
            <img :src="userInfo.avatar" alt="Avatar" class="profile-avatar" />
            <span class="online-badge">✓</span>
          </div>
          <div class="profile-details">
            <div class="profile-header">
              <h1>Welcome, <span class="highlight">{{ userInfo.username }}</span></h1>
              <div class="role-list">
                <span class="role-pill" v-for="role in userInfo.roles" :key="role">
                  {{ role }}
                </span>
                <span class="badge-sub">Stateless Session</span>
              </div>
            </div>
            <p class="profile-desc">
              Your request was authenticated by <code>JwtAuthenticationFilter</code> and authorized by Spring Security 6.
            </p>
          </div>
        </section>

        <!-- JWT Token Details Card -->
        <section class="card token-card">
          <div class="card-header">
            <div class="header-left">
              <span class="header-icon">🔑</span>
              <div>
                <h2>Active JWT Token</h2>
                <p class="subtitle">Bearer token stored in localStorage and attached to request headers</p>
              </div>
            </div>
            <button @click="copyToken" class="action-btn">
              <span v-if="copied">✓ Copied!</span>
              <span v-else>📋 Copy Token</span>
            </button>
          </div>

          <div class="token-preview">
            <code class="token-text">{{ showFullToken ? token : tokenPreview }}</code>
            <button class="toggle-btn" @click="showFullToken = !showFullToken">
              {{ showFullToken ? 'Collapse' : 'Show Full' }}
            </button>
          </div>

          <div class="token-meta-grid">
            <div class="meta-item">
              <span class="meta-key">Algorithm</span>
              <span class="meta-val">HMAC-SHA256 (HS256)</span>
            </div>
            <div class="meta-item">
              <span class="meta-key">Subject (sub)</span>
              <span class="meta-val highlight">{{ userInfo.username }}</span>
            </div>
            <div class="meta-item">
              <span class="meta-key">Session Policy</span>
              <span class="meta-val">SessionCreationPolicy.STATELESS</span>
            </div>
            <div class="meta-item">
              <span class="meta-key">Token Expiry</span>
              <span class="meta-val">24 Hours (86,400s)</span>
            </div>
          </div>
        </section>

        <!-- API Testing & Live Console -->
        <section class="card console-card">
          <div class="card-header">
            <div class="header-left">
              <span class="header-icon">⚡</span>
              <div>
                <h2>Interactive Security API Console</h2>
                <p class="subtitle">Test Spring Security filter chain & 401 interception in real time</p>
              </div>
            </div>
          </div>

          <div class="api-btn-row">
            <button @click="testProtectedApi" :disabled="apiLoading" class="btn btn-primary">
              <span class="btn-icon">✓</span>
              GET /api/user/info (Valid Token)
            </button>
            <button @click="testTamperedToken" :disabled="apiLoading" class="btn btn-danger">
              <span class="btn-icon">⚠</span>
              Simulate 401 (Tampered Token)
            </button>
            <button @click="fetchUserInfo" :disabled="apiLoading" class="btn btn-secondary">
              <span class="btn-icon">🔄</span>
              Refresh Profile
            </button>
          </div>

          <!-- Console Output Box -->
          <div class="console-box">
            <div class="console-header">
              <div class="console-title">
                <span class="console-dot red"></span>
                <span class="console-dot yellow"></span>
                <span class="console-dot green"></span>
                <span class="log-title">HTTP Response Inspector</span>
              </div>
              <span v-if="apiResponse" :class="['status-badge', apiResponse.ok ? 'status-200' : 'status-401']">
                Status: {{ apiResponse.status }} {{ apiResponse.statusText }} ({{ apiResponse.latency }}ms)
              </span>
            </div>
            <pre class="console-content"><code>{{ apiResponse ? apiResponse.body : '// Click one of the test buttons above to trigger live API requests...' }}</code></pre>
          </div>
        </section>

        <!-- Architecture Overview Grid -->
        <section class="card tech-card">
          <div class="card-header">
            <div class="header-left">
              <span class="header-icon">🛠️</span>
              <div>
                <h2>Architecture & Security Pipeline</h2>
                <p class="subtitle">Key components in this modern Spring Boot 3.5 + JDK 25 architecture</p>
              </div>
            </div>
          </div>

          <div class="feature-grid">
            <div class="feature-item">
              <div class="feature-tag">Backend</div>
              <h3>Spring Security 6</h3>
              <p>Bean-based <code>SecurityFilterChain</code> with stateless session management & CORS enabled.</p>
            </div>
            <div class="feature-item">
              <div class="feature-tag">Auth</div>
              <h3>JJWT 0.12.3</h3>
              <p>Modern JSON Web Token generation & validation utilizing <code>verifyWith()</code> and <code>getPayload()</code>.</p>
            </div>
            <div class="feature-item">
              <div class="feature-tag">Filter</div>
              <h3>JwtAuthenticationFilter</h3>
              <p>Custom <code>OncePerRequestFilter</code> parsing <code>Authorization: Bearer &lt;token&gt;</code>.</p>
            </div>
            <div class="feature-item">
              <div class="feature-tag">Frontend</div>
              <h3>Vue 3 + Vite SPA</h3>
              <p>Axios request/response interceptors with automatic token injection and 401 redirection.</p>
            </div>
          </div>
        </section>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '../utils/request'
import axios from 'axios'

const userInfo = ref(null)
const loading = ref(true)
const apiLoading = ref(false)
const copied = ref(false)
const showFullToken = ref(false)
const apiResponse = ref(null)
const router = useRouter()

const token = computed(() => localStorage.getItem('token') || '')
const tokenPreview = computed(() => {
  if (!token.value) return 'No token found'
  if (token.value.length <= 40) return token.value
  return `${token.value.slice(0, 24)} •••••••••••••••••••••••••••••• ${token.value.slice(-16)}`
})

const fetchUserInfo = async () => {
  loading.value = true
  try {
    const startTime = performance.now()
    const res = await request.get('/user/info')
    const endTime = performance.now()
    if (res.code === 200) {
      userInfo.value = res.data
      apiResponse.value = {
        ok: true,
        status: 200,
        statusText: 'OK',
        latency: Math.round(endTime - startTime),
        body: JSON.stringify(res, null, 2)
      }
    }
  } catch (error) {
    console.error('Error fetching user info:', error)
  } finally {
    loading.value = false
  }
}

const testProtectedApi = async () => {
  apiLoading.value = true
  const startTime = performance.now()
  try {
    const res = await request.get('/user/info')
    const endTime = performance.now()
    apiResponse.value = {
      ok: true,
      status: 200,
      statusText: 'OK',
      latency: Math.round(endTime - startTime),
      body: JSON.stringify(res, null, 2)
    }
  } catch (err) {
    const endTime = performance.now()
    apiResponse.value = {
      ok: false,
      status: err.response?.status || 500,
      statusText: err.response?.statusText || 'Error',
      latency: Math.round(endTime - startTime),
      body: JSON.stringify(err.response?.data || { message: err.message }, null, 2)
    }
  } finally {
    apiLoading.value = false
  }
}

const testTamperedToken = async () => {
  apiLoading.value = true
  const startTime = performance.now()
  try {
    // Send request with an invalid/forged token directly
    await axios.get('http://localhost:8080/api/user/info', {
      headers: {
        Authorization: 'Bearer invalid_tampered_jwt_token_payload_123456'
      }
    })
  } catch (err) {
    const endTime = performance.now()
    apiResponse.value = {
      ok: false,
      status: err.response?.status || 401,
      statusText: 'Unauthorized (Blocked by Spring Security)',
      latency: Math.round(endTime - startTime),
      body: JSON.stringify({
        status: err.response?.status || 401,
        error: "Unauthorized",
        message: "Full authentication is required to access this resource",
        explanation: "Spring Security 6 rejected the request because the JWT signature failed verification in JwtAuthenticationFilter."
      }, null, 2)
    }
  } finally {
    apiLoading.value = false
  }
}

const copyToken = async () => {
  if (!token.value) return
  try {
    await navigator.clipboard.writeText(token.value)
    copied.value = true
    setTimeout(() => {
      copied.value = false
    }, 2000)
  } catch (e) {
    console.error('Failed to copy', e)
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
.dashboard-wrapper {
  width: 100%;
  min-height: 100vh;
  background: radial-gradient(circle at 10% 20%, #1e1b4b 0%, #0f172a 60%, #090d16 100%);
  color: #f8fafc;
  display: flex;
  flex-direction: column;
}

/* Navbar */
.navbar {
  width: 100%;
  background: rgba(15, 23, 42, 0.75);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
  position: sticky;
  top: 0;
  z-index: 100;
}

.nav-container {
  max-width: 1280px;
  margin: 0 auto;
  padding: 0.85rem 1.5rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.brand {
  display: flex;
  align-items: center;
  gap: 0.85rem;
}

.brand-icon {
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.brand-icon svg {
  width: 100%;
  height: 100%;
}

.brand-text {
  display: flex;
  align-items: center;
  gap: 0.6rem;
}

.brand-text .title {
  font-size: 1.15rem;
  font-weight: 700;
  color: #fff;
  letter-spacing: -0.02em;
}

.brand-text .badge {
  background: rgba(99, 102, 241, 0.15);
  border: 1px solid rgba(99, 102, 241, 0.35);
  color: #a5b4fc;
  font-size: 0.75rem;
  padding: 0.15rem 0.5rem;
  border-radius: 9999px;
  font-weight: 600;
}

.nav-actions {
  display: flex;
  align-items: center;
  gap: 1.25rem;
}

.status-indicator {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  background: rgba(34, 197, 94, 0.1);
  border: 1px solid rgba(34, 197, 94, 0.25);
  padding: 0.35rem 0.75rem;
  border-radius: 9999px;
  font-size: 0.8rem;
  color: #86efac;
  font-weight: 500;
}

.pulse-dot {
  width: 8px;
  height: 8px;
  background: #22c55e;
  border-radius: 50%;
  box-shadow: 0 0 10px #22c55e;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%, 100% { opacity: 1; transform: scale(1); }
  50% { opacity: 0.4; transform: scale(0.85); }
}

.user-chip {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  background: rgba(255, 255, 255, 0.06);
  padding: 0.25rem 0.75rem 0.25rem 0.35rem;
  border-radius: 9999px;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.chip-avatar {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  background: #334155;
}

.chip-name {
  font-size: 0.875rem;
  font-weight: 600;
  color: #e2e8f0;
}

.logout-btn {
  display: flex;
  align-items: center;
  gap: 0.4rem;
  background: rgba(239, 68, 68, 0.12);
  color: #fca5a5;
  border: 1px solid rgba(239, 68, 68, 0.3);
  padding: 0.45rem 0.9rem;
  border-radius: 10px;
  font-size: 0.85rem;
  font-weight: 600;
  transition: all 0.2s ease;
}

.logout-btn svg {
  width: 16px;
  height: 16px;
}

.logout-btn:hover {
  background: rgba(239, 68, 68, 0.25);
  color: #fff;
  border-color: rgba(239, 68, 68, 0.5);
  transform: translateY(-1px);
}

/* Main Container */
.main-container {
  flex: 1;
  max-width: 1280px;
  width: 100%;
  margin: 0 auto;
  padding: 2rem 1.5rem 4rem;
}

.content-grid {
  display: flex;
  flex-direction: column;
  gap: 1.75rem;
}

/* Cards Base */
.card {
  background: rgba(255, 255, 255, 0.03);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 20px;
  padding: 1.75rem;
  box-shadow: 0 10px 30px -10px rgba(0, 0, 0, 0.5);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 1.25rem;
}

.header-left {
  display: flex;
  align-items: flex-start;
  gap: 0.85rem;
}

.header-icon {
  font-size: 1.5rem;
  line-height: 1;
  background: rgba(255, 255, 255, 0.05);
  padding: 0.5rem;
  border-radius: 12px;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.card-header h2 {
  font-size: 1.25rem;
  font-weight: 700;
  color: #fff;
  margin-bottom: 0.25rem;
}

.subtitle {
  font-size: 0.85rem;
  color: #94a3b8;
}

/* Profile Card */
.profile-card {
  display: flex;
  align-items: center;
  gap: 2rem;
  background: linear-gradient(135deg, rgba(99, 102, 241, 0.12) 0%, rgba(168, 85, 247, 0.08) 100%);
  border: 1px solid rgba(99, 102, 241, 0.25);
}

.avatar-box {
  position: relative;
  flex-shrink: 0;
}

.profile-avatar {
  width: 96px;
  height: 96px;
  border-radius: 24px;
  background: #1e293b;
  border: 3px solid rgba(255, 255, 255, 0.2);
  box-shadow: 0 10px 25px -5px rgba(99, 102, 241, 0.4);
}

.online-badge {
  position: absolute;
  bottom: -4px;
  right: -4px;
  width: 24px;
  height: 24px;
  background: #22c55e;
  border: 3px solid #0f172a;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.75rem;
  font-weight: bold;
  color: #fff;
}

.profile-details {
  flex: 1;
}

.profile-header h1 {
  font-size: 1.85rem;
  font-weight: 800;
  color: #fff;
  letter-spacing: -0.025em;
  margin-bottom: 0.5rem;
}

.highlight {
  color: #818cf8;
}

.role-list {
  display: flex;
  align-items: center;
  gap: 0.6rem;
  margin-bottom: 0.75rem;
}

.role-pill {
  background: #4f46e5;
  color: #fff;
  padding: 0.2rem 0.75rem;
  border-radius: 9999px;
  font-size: 0.75rem;
  font-weight: 700;
  letter-spacing: 0.05em;
}

.badge-sub {
  background: rgba(255, 255, 255, 0.08);
  color: #cbd5e1;
  padding: 0.2rem 0.65rem;
  border-radius: 9999px;
  font-size: 0.75rem;
  font-weight: 500;
}

.profile-desc {
  font-size: 0.95rem;
  color: #94a3b8;
}

.profile-desc code {
  background: rgba(0, 0, 0, 0.3);
  color: #c084fc;
  padding: 0.15rem 0.45rem;
  border-radius: 6px;
  font-family: 'JetBrains Mono', monospace;
  font-size: 0.85rem;
}

/* Token Card */
.token-preview {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1rem;
  background: rgba(0, 0, 0, 0.35);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 12px;
  padding: 0.85rem 1.25rem;
  margin-bottom: 1.25rem;
}

.token-text {
  font-family: 'JetBrains Mono', monospace;
  font-size: 0.85rem;
  color: #38bdf8;
  word-break: break-all;
  flex: 1;
}

.toggle-btn {
  background: rgba(255, 255, 255, 0.08);
  color: #cbd5e1;
  border: none;
  padding: 0.35rem 0.75rem;
  border-radius: 8px;
  font-size: 0.75rem;
  font-weight: 600;
  transition: all 0.2s;
  flex-shrink: 0;
}

.toggle-btn:hover {
  background: rgba(255, 255, 255, 0.15);
  color: #fff;
}

.action-btn {
  background: rgba(99, 102, 241, 0.15);
  border: 1px solid rgba(99, 102, 241, 0.35);
  color: #a5b4fc;
  padding: 0.45rem 0.9rem;
  border-radius: 10px;
  font-size: 0.85rem;
  font-weight: 600;
  transition: all 0.2s;
}

.action-btn:hover {
  background: #6366f1;
  color: #fff;
}

.token-meta-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 1rem;
}

.meta-item {
  background: rgba(255, 255, 255, 0.02);
  border: 1px solid rgba(255, 255, 255, 0.05);
  padding: 0.75rem 1rem;
  border-radius: 10px;
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.meta-key {
  font-size: 0.75rem;
  color: #64748b;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  font-weight: 600;
}

.meta-val {
  font-size: 0.9rem;
  font-weight: 600;
  color: #e2e8f0;
}

/* Console Card */
.api-btn-row {
  display: flex;
  flex-wrap: wrap;
  gap: 0.85rem;
  margin-bottom: 1.25rem;
}

.btn {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.65rem 1.25rem;
  border-radius: 12px;
  font-size: 0.9rem;
  font-weight: 600;
  border: none;
  transition: all 0.2s ease;
}

.btn-primary {
  background: #4f46e5;
  color: #fff;
  box-shadow: 0 4px 14px 0 rgba(79, 70, 229, 0.35);
}

.btn-primary:hover:not(:disabled) {
  background: #4338ca;
  transform: translateY(-1px);
}

.btn-danger {
  background: rgba(239, 68, 68, 0.15);
  color: #fca5a5;
  border: 1px solid rgba(239, 68, 68, 0.3);
}

.btn-danger:hover:not(:disabled) {
  background: #ef4444;
  color: #fff;
  transform: translateY(-1px);
}

.btn-secondary {
  background: rgba(255, 255, 255, 0.08);
  color: #e2e8f0;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.btn-secondary:hover:not(:disabled) {
  background: rgba(255, 255, 255, 0.15);
  transform: translateY(-1px);
}

.btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.console-box {
  background: #090d16;
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 14px;
  overflow: hidden;
}

.console-header {
  background: rgba(255, 255, 255, 0.03);
  padding: 0.6rem 1rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid rgba(255, 255, 255, 0.06);
}

.console-title {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.console-dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
}

.console-dot.red { background: #ef4444; }
.console-dot.yellow { background: #f59e0b; }
.console-dot.green { background: #22c55e; }

.log-title {
  font-size: 0.8rem;
  color: #94a3b8;
  font-weight: 500;
  margin-left: 0.35rem;
}

.status-badge {
  font-size: 0.75rem;
  font-weight: 700;
  padding: 0.2rem 0.6rem;
  border-radius: 6px;
  font-family: 'JetBrains Mono', monospace;
}

.status-200 {
  background: rgba(34, 197, 94, 0.15);
  color: #4ade80;
  border: 1px solid rgba(34, 197, 94, 0.3);
}

.status-401 {
  background: rgba(239, 68, 68, 0.15);
  color: #f87171;
  border: 1px solid rgba(239, 68, 68, 0.3);
}

.console-content {
  padding: 1.25rem;
  margin: 0;
  font-family: 'JetBrains Mono', monospace;
  font-size: 0.85rem;
  line-height: 1.6;
  color: #a5f3fc;
  overflow-x: auto;
  max-height: 280px;
}

/* Feature Grid */
.feature-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 1.25rem;
}

.feature-item {
  background: rgba(255, 255, 255, 0.02);
  border: 1px solid rgba(255, 255, 255, 0.06);
  padding: 1.25rem;
  border-radius: 14px;
  transition: all 0.25s ease;
}

.feature-item:hover {
  background: rgba(255, 255, 255, 0.04);
  border-color: rgba(99, 102, 241, 0.3);
  transform: translateY(-2px);
}

.feature-tag {
  font-size: 0.7rem;
  font-weight: 700;
  color: #818cf8;
  text-transform: uppercase;
  letter-spacing: 0.06em;
  margin-bottom: 0.4rem;
}

.feature-item h3 {
  font-size: 1.05rem;
  font-weight: 700;
  color: #fff;
  margin-bottom: 0.4rem;
}

.feature-item p {
  font-size: 0.85rem;
  color: #94a3b8;
  line-height: 1.5;
}

.feature-item code {
  color: #c084fc;
  font-family: 'JetBrains Mono', monospace;
  font-size: 0.8rem;
}

/* States */
.state-card {
  text-align: center;
  padding: 5rem 2rem;
  background: rgba(255, 255, 255, 0.03);
  border-radius: 20px;
  border: 1px solid rgba(255, 255, 255, 0.08);
}

.spinner {
  width: 44px;
  height: 44px;
  border: 3px solid rgba(255, 255, 255, 0.1);
  border-top-color: #6366f1;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
  margin: 0 auto 1.5rem;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

@media (max-width: 768px) {
  .profile-card {
    flex-direction: column;
    text-align: center;
  }
  .role-list {
    justify-content: center;
  }
  .nav-container {
    flex-direction: column;
    gap: 1rem;
  }
}
</style>

