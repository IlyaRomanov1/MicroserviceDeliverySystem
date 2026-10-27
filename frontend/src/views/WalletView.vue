<script setup>
import { ref, onMounted } from 'vue'
import { orderApi } from '../api/client.js'

const balance = ref(null)
const amount = ref(500)
const error = ref('')
const message = ref('')
const loading = ref(false)

async function loadBalance() {
  error.value = ''
  try {
    const { data } = await orderApi.get('/wallet')
    balance.value = data.balance
  } catch (e) {
    error.value = 'Не удалось загрузить баланс'
  }
}

async function topUp() {
  if (!amount.value || amount.value <= 0) return
  error.value = ''
  message.value = ''
  loading.value = true
  try {
    const { data } = await orderApi.post('/wallet/topup', { amount: Number(amount.value) })
    balance.value = data.balance
    message.value = `Баланс пополнен на ${amount.value} ₽`
  } catch (e) {
    error.value = 'Не удалось пополнить баланс'
  } finally {
    loading.value = false
  }
}

onMounted(loadBalance)
</script>

<template>
  <h2>Кошелёк</h2>

  <div class="card" style="max-width: 400px;">
    <p style="color: #5b6271; margin: 0 0 4px;">Текущий баланс</p>
    <p style="font-size: 32px; margin: 0 0 20px;"><strong>{{ balance ?? '...' }} ₽</strong></p>

    <form @submit.prevent="topUp" style="display: flex; gap: 10px; align-items: flex-end;">
      <div class="form-field" style="flex: 1; margin-bottom: 0;">
        <label>Сумма пополнения</label>
        <input v-model="amount" type="number" min="1" step="1" required />
      </div>
      <button type="submit" :disabled="loading">
        {{ loading ? 'Пополняем...' : 'Пополнить' }}
      </button>
    </form>

    <p v-if="error" class="error-text">{{ error }}</p>
    <p v-if="message" class="success-text">{{ message }}</p>
  </div>
</template>
