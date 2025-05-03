<template>
  <div class="data-container">
    <div class="data-grid">
      <div class="data-card">
        <div class="data-title">普通用户</div>
        <div class="data-value">{{ data.userCount }}</div>
      </div>
      <div class="data-card">
        <div class="data-title">教练数量</div>
        <div class="data-value">{{ data.coachCount }}</div>
      </div>
      <div class="data-card">
        <div class="data-title">课程数量</div>
        <div class="data-value">{{ data.courseCount }}</div>
      </div>
      <div class="data-card">
        <div class="data-title">器材数量</div>
        <div class="data-value">{{ data.equipmentCount }}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, onMounted } from 'vue'
import request from '@/utils/request.js'

const data = reactive({
  userCount: 0,
  coachCount: 0,
  courseCount: 0,
  equipmentCount: 0
})

const loadData = () => {
  // 获取普通用户数量
  request.get('/users/count').then(res => {
    data.userCount = res.data
  })

  // 获取教练数量
  request.get('/coach/count').then(res => {
    data.coachCount = res.data
  })

  // 获取课程数量
  request.get('/course/count').then(res => {
    data.courseCount = res.data
  })

  // 获取器材数量
  request.get('/equipment/count').then(res => {
    data.equipmentCount = res.data
  })
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.data-container {
  padding: 20px;
}

.data-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.data-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  text-align: center;
  transition: transform 0.3s ease;
}

.data-card:hover {
  transform: translateY(-5px);
}

.data-title {
  font-size: 18px;
  color: #666;
  margin-bottom: 10px;
}

.data-value {
  font-size: 36px;
  font-weight: bold;
  color: #409EFF;
}
</style>