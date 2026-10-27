<script setup>
import { useRouter } from 'vue-router'
import { useAuthStore } from '../store/auth.js'

const auth = useAuthStore()
const router = useRouter()

function logout() {
  auth.logout()
  router.push({ name: 'login' })
}
</script>

<template>
  <nav class="navbar">
    <div class="navbar-inner container">
      <RouterLink to="/" class="brand">🍜 Delivery</RouterLink>
      <div class="links">
        <RouterLink to="/">Меню</RouterLink>
        <RouterLink v-if="auth.isAuthenticated" to="/cart">Корзина</RouterLink>
        <RouterLink v-if="auth.isAuthenticated" to="/orders">Мои заказы</RouterLink>
        <RouterLink v-if="auth.isAuthenticated" to="/wallet">Кошелёк</RouterLink>
      </div>
      <div class="auth-actions">
        <template v-if="auth.isAuthenticated">
          <span class="role-badge" :class="{ admin: auth.isAdmin }">
            {{ auth.isAdmin ? 'Админ' : 'Пользователь' }}
          </span>
          <button class="secondary" @click="logout">Выйти</button>
        </template>
        <template v-else>
          <RouterLink to="/login"><button class="secondary">Войти</button></RouterLink>
          <RouterLink to="/register"><button>Регистрация</button></RouterLink>
        </template>
      </div>
    </div>
  </nav>
</template>

<style scoped>
.navbar {
  background: #fff;
  border-bottom: 1px solid #e4e7ec;
  position: sticky;
  top: 0;
  z-index: 10;
}

.navbar-inner {
  display: flex;
  align-items: center;
  gap: 24px;
  padding-top: 14px;
  padding-bottom: 14px;
}

.brand {
  font-weight: 700;
  font-size: 18px;
  white-space: nowrap;
}

.links {
  display: flex;
  gap: 16px;
  flex: 1;
  flex-wrap: wrap;
}

.links a {
  font-size: 14px;
  color: #5b6271;
}

.links a.router-link-exact-active {
  color: #2f6feb;
  font-weight: 600;
}

.auth-actions {
  display: flex;
  align-items: center;
  gap: 10px;
  white-space: nowrap;
}

.role-badge {
  font-size: 12px;
  padding: 4px 8px;
  border-radius: 12px;
  background: #e4e7ec;
}

.role-badge.admin {
  background: #ffe6b3;
  color: #8a5a00;
}
</style>
