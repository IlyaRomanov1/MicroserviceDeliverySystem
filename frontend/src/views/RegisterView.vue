<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../store/auth.js'

const email = ref('')
const name = ref('')
const age = ref(18)
const password = ref('')
const error = ref('')
const success = ref(false)
const loading = ref(false)

const auth = useAuthStore()
const router = useRouter()

async function submit() {
  error.value = ''
  loading.value = true
  try {
    await auth.register({
      email: email.value,
      name: name.value,
      age: Number(age.value),
      password: password.value,
    })
    success.value = true
    setTimeout(() => router.push('/login'), 1000)
  } catch (e) {
    error.value = e.response?.data?.error || 'Не удалось зарегистрироваться'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="card" style="max-width: 360px; margin: 40px auto;">
    <h2>Регистрация</h2>
    <form @submit.prevent="submit">
      <div class="form-field">
        <label>Имя</label>
        <input v-model="name" type="text" required />
      </div>
      <div class="form-field">
        <label>Email</label>
        <input v-model="email" type="email" required autocomplete="email" />
      </div>
      <div class="form-field">
        <label>Возраст</label>
        <input v-model="age" type="number" min="1" max="149" required />
      </div>
      <div class="form-field">
        <label>Пароль</label>
        <input v-model="password" type="password" required autocomplete="new-password" />
      </div>
      <p v-if="error" class="error-text">{{ error }}</p>
      <p v-if="success" class="success-text">Готово! Переходим на страницу входа...</p>
      <button type="submit" :disabled="loading" style="width: 100%;">
        {{ loading ? 'Регистрируем...' : 'Зарегистрироваться' }}
      </button>
    </form>
    <p style="margin-top: 14px; font-size: 14px;">
      Уже есть аккаунт? <RouterLink to="/login">Войти</RouterLink>
    </p>
  </div>
</template>
