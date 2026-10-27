<script setup>
import { ref, reactive, onMounted } from 'vue'
import { orderApi } from '../api/client.js'
import { useAuthStore } from '../store/auth.js'

const auth = useAuthStore()
const foods = ref([])
const error = ref('')
const message = ref('')
const quantities = reactive({})

const newFood = reactive({ name: '', price: '' })
const editing = reactive({})

async function loadFoods() {
  error.value = ''
  try {
    const { data } = await orderApi.get('/foods')
    foods.value = data
    data.forEach((food) => {
      if (!(food.id in quantities)) quantities[food.id] = 1
    })
  } catch (e) {
    error.value = 'Не удалось загрузить меню'
  }
}

async function addToCart(food) {
  message.value = ''
  error.value = ''
  try {
    await orderApi.post('/bags', {
      foodId: food.id,
      quantity: Number(quantities[food.id] || 1),
    })
    message.value = `«${food.name}» добавлено в корзину`
  } catch (e) {
    error.value = e.response?.data?.message || 'Не удалось добавить в корзину'
  }
}

async function createFood() {
  if (!newFood.name || !newFood.price) return
  try {
    await orderApi.post('/foods', { name: newFood.name, price: Number(newFood.price) })
    newFood.name = ''
    newFood.price = ''
    await loadFoods()
  } catch (e) {
    error.value = e.response?.data?.message || 'Не удалось добавить блюдо'
  }
}

function startEdit(food) {
  editing[food.id] = { name: food.name, price: food.price }
}

async function saveEdit(food) {
  try {
    await orderApi.patch(`/foods/${food.id}/edit`, editing[food.id])
    delete editing[food.id]
    await loadFoods()
  } catch (e) {
    error.value = e.response?.data?.message || 'Не удалось изменить блюдо'
  }
}

function cancelEdit(food) {
  delete editing[food.id]
}

async function deleteFood(food) {
  try {
    await orderApi.delete(`/foods/${food.id}`)
    await loadFoods()
  } catch (e) {
    error.value = e.response?.data?.message || 'Не удалось удалить блюдо'
  }
}

onMounted(loadFoods)
</script>

<template>
  <h2>Меню</h2>

  <div v-if="auth.isAdmin" class="card" style="margin-bottom: 20px;">
    <h3 style="margin-top: 0;">Добавить блюдо</h3>
    <form @submit.prevent="createFood" style="display: flex; gap: 10px; align-items: flex-end; flex-wrap: wrap;">
      <div class="form-field" style="flex: 2; margin-bottom: 0;">
        <label>Название</label>
        <input v-model="newFood.name" type="text" required />
      </div>
      <div class="form-field" style="flex: 1; margin-bottom: 0;">
        <label>Цена</label>
        <input v-model="newFood.price" type="number" min="0.01" step="0.01" required />
      </div>
      <button type="submit">Добавить</button>
    </form>
  </div>

  <p v-if="error" class="error-text">{{ error }}</p>
  <p v-if="message" class="success-text">{{ message }}</p>

  <div class="food-grid">
    <div v-for="food in foods" :key="food.id" class="card food-card">
      <template v-if="editing[food.id]">
        <input v-model="editing[food.id].name" type="text" style="margin-bottom: 8px;" />
        <input v-model="editing[food.id].price" type="number" step="0.01" style="margin-bottom: 8px;" />
        <div class="food-actions">
          <button @click="saveEdit(food)">Сохранить</button>
          <button class="secondary" @click="cancelEdit(food)">Отмена</button>
        </div>
      </template>
      <template v-else>
        <h3 style="margin: 0 0 6px;">{{ food.name }}</h3>
        <p style="margin: 0 0 12px; color: #5b6271;">{{ food.price }} ₽</p>

        <div v-if="auth.isAuthenticated && !auth.isAdmin" class="food-actions">
          <input v-model="quantities[food.id]" type="number" min="1" style="width: 70px;" />
          <button @click="addToCart(food)">В корзину</button>
        </div>

        <div v-if="auth.isAdmin" class="food-actions">
          <button class="secondary" @click="startEdit(food)">Изменить</button>
          <button class="danger" @click="deleteFood(food)">Удалить</button>
        </div>

        <p v-if="!auth.isAuthenticated" style="font-size: 13px; color: #5b6271;">
          <RouterLink to="/login">Войдите</RouterLink>, чтобы заказать
        </p>
      </template>
    </div>
  </div>
</template>

<style scoped>
.food-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 16px;
}

.food-actions {
  display: flex;
  gap: 8px;
  align-items: center;
}
</style>
