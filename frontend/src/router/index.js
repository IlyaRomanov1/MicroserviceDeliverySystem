import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../store/auth.js'

import MenuView from '../views/MenuView.vue'
import LoginView from '../views/LoginView.vue'
import RegisterView from '../views/RegisterView.vue'
import CartView from '../views/CartView.vue'
import OrdersView from '../views/OrdersView.vue'
import WalletView from '../views/WalletView.vue'

const routes = [
  { path: '/', name: 'menu', component: MenuView },
  { path: '/login', name: 'login', component: LoginView },
  { path: '/register', name: 'register', component: RegisterView },
  { path: '/cart', name: 'cart', component: CartView, meta: { requiresAuth: true } },
  { path: '/orders', name: 'orders', component: OrdersView, meta: { requiresAuth: true } },
  { path: '/wallet', name: 'wallet', component: WalletView, meta: { requiresAuth: true } },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to) => {
  const auth = useAuthStore()
  if (to.meta.requiresAuth && !auth.isAuthenticated) {
    return { name: 'login', query: { redirect: to.fullPath } }
  }
  return true
})

export default router
