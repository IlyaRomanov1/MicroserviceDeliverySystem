import axios from 'axios'

const AUTH_BASE = import.meta.env.VITE_AUTH_API || 'http://localhost:8082'
const ORDER_BASE = import.meta.env.VITE_ORDER_API || 'http://localhost:8080'
const NOTIFICATION_BASE = import.meta.env.VITE_NOTIFICATION_API || 'http://localhost:8081'

export const authApi = axios.create({ baseURL: AUTH_BASE })
export const orderApi = axios.create({ baseURL: ORDER_BASE })
export const notificationApi = axios.create({ baseURL: NOTIFICATION_BASE })

function attachToken(instance) {
  instance.interceptors.request.use((config) => {
    const token = localStorage.getItem('jwt-token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  })
}

attachToken(orderApi)
attachToken(notificationApi)
