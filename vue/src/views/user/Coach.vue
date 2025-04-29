<template>
  <div>
    <!--置顶大图-->
    <div>
      <img style="width: 100%; height: 400px; padding-bottom: 20px" src="@/assets/coach.jpg" alt="">
    </div>

    <div style="font-size: 28px; text-align: center; color: #737578">预约教练</div>

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
        <div class="btn-container">
          <button class="reserve-btn">我要预约</button>
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
  coachList:[]
})

const load = () => {
  request.get('/coach/selectAll').then(res => {
    console.log(res)
    data.coachList = res.data
    console.log(data.coachList)
  })
}

load()

const goToDetail = (id) => {
  router.push({
    name: 'coachDetails',
    params: {
      id:id
    }
  })
}
</script>

<style>
.coach-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr); /* 四列布局 */
  gap: 15px; /* 列和行之间的间距 */
  max-width: 90%; /* 限制整体宽度 */
  margin: 0 auto; /* 居中显示 */
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
  height: 280px; /* 固定高度 */
  overflow: hidden;
  border-radius: 8px;
  background-color: #f5f5f5; /* 默认背景色 */
}

.coach-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover; /* 保持图片比例并填充容器 */
  display: block;
}

.coach-info {
  margin-top: 15px;
  font-size: 14px;
  color: #666;
  display: flex; /* 使用 Flexbox 布局 */
  justify-content: space-between; /* 在两端对齐 */
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

.btn-container {
  margin-top: auto; /* 将按钮推到卡片底部 */
  width: 100%;
  padding-top: 10px;
}

.reserve-btn {
  width: 100%; /* 按钮宽度填满容器 */
  padding: 10px 0;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s;
}

.reserve-btn:hover {
  background-color: #45a049;
}



</style>