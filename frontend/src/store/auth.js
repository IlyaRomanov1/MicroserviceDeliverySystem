import { defineStore } from 'pinia'
import { authApi } from '../api/client.js'

function decodeToken(token) {
  try {
    const payload = token.split('.')[1]
    const json = atob(payload.replace(/-/g, '+').replace(/_/g, '/'))
    return JSON.parse(json)
  } catch {
    return null
  }
}

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('jwt-token') || null,
    userId: null,
    role: null,
  }),

  getters: {
    isAuthenticated: (state) => !!state.token,
    isAdmin: (state) => state.role === 'ROLE_ADMIN',
  },

  actions: {
    init() {
      if (this.token) {
        const claims = decodeToken(this.token)
        if (claims) {
          this.userId = claims.userId
          this.role = claims.role
        }
      }
    },

    async login(email, password) {
      const { data } = await authApi.post('/auth/login', { email, password })
      this.setToken(data['jwt-token'])
    },

    async register({ email, name, age, password }) {
      await authApi.post('/auth/register', { email, name, age, password })
    },

    setToken(token) {
      this.token = token
      localStorage.setItem('jwt-token', token)
      const claims = decodeToken(token)
      if (claims) {
        this.userId = claims.userId
        this.role = claims.role
      }
    },

    logout() {
      this.token = null
      this.userId = null
      this.role = null
      localStorage.removeItem('jwt-token')
    },
  },
})
