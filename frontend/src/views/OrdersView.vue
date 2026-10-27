<script setup>
import { ref, onMounted } from 'vue'
import { notificationApi } from '../api/client.js'
import { useAuthStore } from '../store/auth.js'

const auth = useAuthStore()
const order = ref(null)
const error = ref('')
const loading = ref(true)

async function loadOrder() {
  loading.value = true
  error.value = ''
  try {
    const { data } = await notificationApi.get(`/notifications/${auth.userId}`)
    order.value = data
  } catch (e) {
    if (e.response?.status === 404) {
      order.value = null
    } else {
      error.value = 'Не удалось загрузить статус заказа'
    }
  } finally {
    loading.value = false
  }
}

onMounted(loadOrder)
</script>

<template>
  <h2>Мои заказы</h2>
  <p v-if="error" class="error-text">{{ error }}</p>

  <div v-if="loading">Загрузка...</div>
  <div v-else-if="!order" class="card">
    У вас пока нет оформленных заказов.
  </div>
  <div v-else class="card">
    <p>Последний заказ подтверждён уведомлением от сервиса доставки.</p>
    <p><strong>Сумма: {{ order.totalPrice }} ₽</strong></p>
    <button class="secondary" @click="loadOrder">Обновить</button>
  </div>
</template>
