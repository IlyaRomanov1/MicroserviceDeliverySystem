<script setup>
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '../store/auth.js'

const email = ref('')
const password = ref('')
const error = ref('')
const loading = ref(false)

const auth = useAuthStore()
const router = useRouter()
const route = useRoute()

async function submit() {
  error.value = ''
  loading.value = true
  try {
    await auth.login(email.value, password.value)
    router.push(route.query.redirect || '/')
  } catch (e) {
    error.value = e.response?.data?.error || 'Неверный email или пароль'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="card" style="max-width: 360px; margin: 40px auto;">
    <h2>Вход</h2>
    <form @submit.prevent="submit">
      <div class="form-field">
        <label>Email</label>
        <input v-model="email" type="email" required autocomplete="email" />
      </div>
      <div class="form-field">
        <label>Пароль</label>
        <input v-model="password" type="password" required autocomplete="current-password" />
      </div>
      <p v-if="error" class="error-text">{{ error }}</p>
      <button type="submit" :disabled="loading" style="width: 100%;">
        {{ loading ? 'Входим...' : 'Войти' }}
      </button>
    </form>
    <p style="margin-top: 14px; font-size: 14px;">
      Нет аккаунта? <RouterLink to="/register">Зарегистрироваться</RouterLink>
    </p>
  </div>
</template>
