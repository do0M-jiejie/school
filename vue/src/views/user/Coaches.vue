<template>
  <div>
    <!--置顶大图-->
    <div>
      <img style="width: 100%; height: 400px; padding-bottom: 20px" src="@/assets/coach.jpg" alt="">
    </div>

    <div style="font-size: 28px; text-align: center; color: #737578">教练列表</div>

    <div style="font-size: 22px; text-align: center; color: #737578; margin: 15px;padding-bottom: 20px">专业负责的私人教练，一对一定制化教学</div>

    <div class="coach-grid">
      <div v-for="(coach, index) in data.coachList" :key="index" class="coach-card">
        <div class="coach-avatar" @click="goToDetail(coach.coachId)">
          <img :src="coach.avatar" alt="教练头像">
        </div>
        <div class="coach-info">
          <div class="coach-name">姓名：{{ coach.coachName }}</div>
          <div class="coach-phone">电话：{{ coach.phone }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import request from '@/utils/request.js'
import { reactive } from 'vue'
import router from '@/router/index.js'

const data = reactive({
  coachList: []
})

const load = () => {
  request.get('/coach/selectAll').then(res => {
    data.coachList = res.data
  })
}

const goToDetail = (id) => {
  router.push({
    name:'coachDetails',
    params:{
      id: id
    }
  })
}

load()
</script>

<style>
.coach-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 15px;
  max-width: 90%;
  margin: 0 auto;
}

.coach-card {
  border-radius: 8px;
  padding: 10px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  transition: transform 0.3s ease;
}

.coach-card:hover {
  transform: translateY(-5px);
}

.coach-avatar {
  width: 100%;
  height: 280px;
  overflow: hidden;
  border-radius: 8px;
  background-color: #f5f5f5;
  cursor: pointer;
}

.coach-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.coach-info {
  margin-top: 15px;
  font-size: 14px;
  color: #666;
  display: flex;
  justify-content: space-between;
  width: 100%;
}

.coach-name, .coach-phone {
  font-size: 14px;
  color: #666;
}

.coach-name {
  font-weight: bold;
  color: #333;
}

.coach-phone {
  color: #333;
}
</style>