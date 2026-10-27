<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { orderApi } from '../api/client.js'

const bags = ref([])
const error = ref('')
const placing = ref(false)
const router = useRouter()

async function loadBags() {
  error.value = ''
  try {
    const { data } = await orderApi.get('/bags')
    bags.value = data
  } catch (e) {
    error.value = 'Не удалось загрузить корзину'
  }
}

async function updateQuantity(bag, quantity) {
  if (quantity < 1) return
  try {
    await orderApi.patch(`/bags/${bag.id}/edit`, { id: bag.id, quantity })
    await loadBags()
  } catch (e) {
    error.value = 'Не удалось изменить количество'
  }
}

async function removeBag(bag) {
  try {
    await orderApi.delete(`/bags/${bag.id}`)
    await loadBags()
  } catch (e) {
    error.value = 'Не удалось удалить товар'
  }
}

const rejected = ref(null)

async function checkout() {
  placing.value = true
  error.value = ''
  rejected.value = null
  try {
    await orderApi.post('/orders/send')
    router.push('/orders')
  } catch (e) {
    const data = e.response?.data
    if (e.response?.status === 402 && data?.status === 'REJECTED') {
      rejected.value = data
    } else {
      error.value = 'Не удалось оформить заказ'
    }
  } finally {
    placing.value = false
  }
}

const total = () =>
  bags.value.reduce((sum, b) => sum + b.foodPrice * b.quantity, 0).toFixed(2)

onMounted(loadBags)
</script>

<template>
  <h2>Корзина</h2>
  <p v-if="error" class="error-text">{{ error }}</p>

  <div v-if="rejected" class="card" style="border: 1px solid #e34848; margin-bottom: 16px;">
    <p style="margin: 0 0 6px;"><strong>Статус заказа: Отклонён</strong></p>
    <p style="margin: 0 0 6px; color: #5b6271;">{{ rejected.message }}</p>
    <p style="margin: 0;">
      Сумма заказа: {{ rejected.totalPrice }} ₽, доступно на балансе: {{ rejected.balance }} ₽.
      <RouterLink to="/wallet">Пополнить кошелёк</RouterLink>
    </p>
  </div>

  <p v-if="bags.length === 0">Корзина пуста. <RouterLink to="/">Перейти в меню</RouterLink></p>

  <div v-else class="card">
    <div v-for="bag in bags" :key="bag.id" class="bag-row">
      <div class="bag-info">
        <strong>{{ bag.foodName }}</strong>
        <span style="color: #5b6271;">{{ bag.foodPrice }} ₽ / шт</span>
      </div>
      <div class="bag-actions">
        <button class="secondary" @click="updateQuantity(bag, bag.quantity - 1)">−</button>
        <span>{{ bag.quantity }}</span>
        <button class="secondary" @click="updateQuantity(bag, bag.quantity + 1)">+</button>
        <button class="danger" @click="removeBag(bag)">Удалить</button>
      </div>
    </div>

    <div class="bag-total">
      <strong>Итого: {{ total() }} ₽</strong>
      <button :disabled="placing" @click="checkout">
        {{ placing ? 'Оформляем...' : 'Оформить заказ' }}
      </button>
    </div>
  </div>
</template>

<style scoped>
.bag-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #eef0f3;
}

.bag-info {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.bag-actions {
  display: flex;
  align-items: center;
  gap: 10px;
}

.bag-total {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 16px;
  padding-top: 16px;
}
</style>
